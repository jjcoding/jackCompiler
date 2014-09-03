
import java.io.*;


public class CompilationEngine 
{
	private JackTokenizer jt;
	private BufferedWriter bw;
	private SymbolTable st;
	private VMWriter vmw;
	private int nArgs, ifNum, whileNum;
	private String className;


	public CompilationEngine(String in, String out) throws Exception 
	{
		nArgs = 0;
		ifNum = 0;
		whileNum = 0;
		className = "";

		st = new SymbolTable();
		jt = new JackTokenizer(in);

		File xmlFile = new File(out + ".xml");
		bw = new BufferedWriter(new FileWriter(xmlFile.getAbsoluteFile()));

		vmw = new VMWriter(out + ".vm");

		CompileClass();
		bw.close();
		vmw.close();
	}


	public void CompileClass() throws IOException 
	{
		jt.advance();
		bw.write("<class>\n");
		System.out.println("<class>\n");

		if(jt.tokenType().equals("KEYWORD") && jt.keyWord().equals("class")) 
		{
			bw.write("<keyword> class </keyword>\n");
			System.out.println("<keyword> class </keyword>\n");
		}


		jt.advance();
		if(jt.tokenType().equals("IDENTIFIER")) 
		{
			className = jt.identifier();
			bw.write("<identifier> " + jt.identifier() + " </identifier>\n");
			System.out.println("<identifier> " + jt.identifier() + " </identifier>\n");
		}


		jt.advance();
		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == '{') 
		{
			bw.write("<symbol> { </symbol>\n");
			System.out.println("<symbol> { </symbol>\n");
		}

		jt.advance();
		while(jt.tokenType().equals("KEYWORD") && (jt.keyWord().equals("static") || jt.keyWord().equals("field")))
		{
			CompileClassVarDec();
		}

		while( jt.tokenType().equals("KEYWORD") && ((jt.keyWord().equals("constructor") || jt.keyWord().equals("function") || jt.keyWord().equals("method"))))
		{
			st.startSubroutine(jt.keyWord());
			CompileSubroutine();
			jt.advance();	
		}

		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == '}') 
		{
			bw.write("<symbol> } </symbol>\n");
			System.out.println("<symbol> } </symbol>\n");
		}

		bw.write("</class>\n");
		System.out.println("</class>\n");
	}

	public void CompileClassVarDec() throws IOException 
	{
		String name, type, kind;
		name = "";
		type = "";
		kind = "";

		bw.write("<classVarDec>\n");
		System.out.println("<classVarDec>\n");

		if(jt.tokenType().equals("KEYWORD") && (jt.keyWord().equals("static") || jt.keyWord().equals("field"))) 
		{
			if(jt.keyWord().equals("static"))
				kind = "STATIC";
			else
				kind = "FIELD";

			bw.write("<keyword> " + jt.keyWord() + " </keyword>\n");	
			System.out.println("<keyword> " + jt.keyWord() + " </keyword>\n");
		}

		jt.advance();
		if(jt.tokenType().equals("KEYWORD") && (jt.keyWord().equals("int") | jt.keyWord().equals("char") | jt.keyWord().equals("boolean"))) 
		{
			type = jt.keyWord();
			bw.write("<keyword> " + jt.keyWord() + " </keyword>\n");
			System.out.println("<keyword> " + jt.keyWord() + " </keyword>\n");
		}
		else if(jt.tokenType().equals("IDENTIFIER")) 
		{
			type = jt.identifier();
			bw.write("<identifier> " + jt.identifier() + " </identifier>\n");
			System.out.println("<identifier> " + jt.identifier() + " </identifier>\n");
		}

		jt.advance();
		if(jt.tokenType().equals("IDENTIFIER")) 
		{
			name = jt.identifier();

			if(st.IndexOf(name) == -1)
				st.Define(name, type, kind);

			bw.write("<identifier> " + jt.identifier() + " </identifier>\n");
			System.out.println("<identifier> " + jt.identifier() + " </identifier>\n");
		}

		jt.advance();
		while(jt.tokenType().equals("SYMBOL") && jt.symbol() == ',') 
		{
			bw.write("<symbol> , </symbol>\n");
			System.out.println("<symbol> , </symbol>\n");

			jt.advance();
			if(jt.tokenType().equals("IDENTIFIER")) 
			{
				name = jt.identifier();
				if(st.IndexOf(name) == -1)	
					st.Define(name, type, kind);

				bw.write("<identifier> " + jt.identifier()  + " </identifier>\n");
				System.out.println("<identifier> " + jt.identifier() + " </identifier>\n");
			}

			jt.advance();
		}

		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == ';') 
		{
			bw.write("<symbol> ; </symbol>\n");
			System.out.println("<symbol> ; </symbol>\n");
		}


		jt.advance();
		bw.write("</classVarDec>\n");
		System.out.println("</classVarDec>\n");
	}

	public void CompileSubroutine() throws IOException 
	{
		String name, kind, funcName, subType;
		name = "";
		kind = "";
		funcName = "";
		subType = "";
		
		bw.write("<subroutineDec>\n");
		System.out.println("<subroutineDec>\n");

		if(jt.tokenType().equals("KEYWORD") && (jt.keyWord().equals("constructor") || jt.keyWord().equals("function") || jt.keyWord().equals("method"))) 
		{
			subType = jt.keyWord();
			bw.write("<keyword> " + jt.keyWord() + " </keyword>\n");
			System.out.println("<keyword> " + jt.keyWord() + " </keyword>\n");
		}

		jt.advance();
		if(jt.tokenType().equals("KEYWORD")  && (jt.keyWord().equals("void") || jt.keyWord().equals("int") || jt.keyWord().equals("char") || jt.keyWord().equals("boolean"))) 
		{
			bw.write("<keyword> " +  jt.keyWord() + " </keyword>\n");
			System.out.println("<keyword> " +  jt.keyWord() + " </keyword>\n");
		}
		else if(jt.tokenType().equals("IDENTIFIER")) 
		{

			bw.write("<identifier> " + jt.identifier() + " </identifier>\n");
			System.out.println("<identifier> " + jt.identifier() + " </identifier>\n");
		}

		jt.advance();
		if(jt.tokenType().equals("IDENTIFIER"))
		{
			funcName = jt.identifier();
			bw.write("<identifier> " + jt.identifier() + " </identifier>\n");
			System.out.println("<identifier> " + jt.identifier() + " </identifier>\n");
		}

		jt.advance();
		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == '(') 
		{
			bw.write("<symbol> ( </symbol>\n");
			System.out.println("<symbol> ( </symbol>\n");
		}

		jt.advance();
		compileParameterList();

		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == ')') 
		{
			bw.write("<symbol> ) </symbol>\n");
			System.out.println("<symbol> ) </symbol>\n");
		}

		bw.write("<subroutineBody>\n");
		System.out.println("<subroutineBody>\n");


		jt.advance();
		if(jt.tokenType().equals("SYMBOL")  && jt.symbol() == '{') 
		{
			bw.write("<symbol> { </symbol>\n");
			System.out.println("<symbol> { </symbol>\n");
		}

		jt.advance();
		while(jt.tokenType().equals("KEYWORD")  && jt.keyWord().equals("var")) 
		{
			compileVarDec();
			jt.advance();
		}

		vmw.writeFunction(className + "." + funcName, st.VarCount("VAR"));
		if(subType.equals("constructor")) 
		{
			vmw.writePush("CONST", st.VarCount("FIELD"));
			vmw.writeCall("Memory.alloc", 1);
			vmw.writePop("POINTER", 0);
		}
		else if(subType.equals("method")) 
		{
			vmw.writePush("ARG", 0);
			vmw.writePop("POINTER", 0);
		}

		compileStatements();

		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == '}')
		{
			bw.write("<symbol> } </symbol>\n");
			System.out.println("<symbol> } </symbol>\n");
		}	

		bw.write("</subroutineBody>\n");
		System.out.println("</subroutineBody>\n");

		bw.write("</subroutineDec>\n");
		System.out.println("</subroutineDec>\n");
	}


	public void compileParameterList() throws IOException 
	{
		bw.write("<parameterList>\n");
		System.out.println("<parameterList>\n");


		String name, type, kind;
		kind = "ARG";
		name = "";
		type = "";

		if(jt.tokenType().equals("KEYWORD") || jt.tokenType().equals("IDENTIFIER")) 
		{
			if(jt.tokenType().equals("KEYWORD") 
					&& (jt.keyWord().equals("int") || jt.keyWord().equals("char") || jt.keyWord().equals("boolean")))
			{
				type = jt.keyWord();
				bw.write("<keyword> " + jt.keyWord() + "</keyword>\n");
				System.out.println("<keyword> " + jt.keyWord() + "</keyword>\n");
			}
			else if(jt.tokenType().equals("IDENTIFIER")) 
			{
				type = jt.identifier();
				bw.write("<identifier> " + jt.identifier() + " </identifier>\n");
				System.out.println("<identifier> " + jt.identifier() +" </identifier>\n");
			}


			jt.advance();
			if(jt.tokenType().equals("IDENTIFIER")) 
			{
				name = jt.identifier();
				if(st.IndexOf(name) == -1)
					st.Define(name, type, kind);

				bw.write("<identifier> " + jt.identifier() + " </identifier>\n");
				System.out.println("<identifier> " + jt.identifier() + " </identifier>\n");
			}

			jt.advance();
			while(jt.tokenType().equals("SYMBOL") && jt.symbol() == ',')
			{
				bw.write("<symbol> , </symbol>\n");
				System.out.println("<symbol> , </symbol>\n");

				jt.advance();

				if(jt.tokenType().equals("KEYWORD") && (jt.keyWord().equals("int") || jt.keyWord().equals("char") || jt.keyWord().equals("boolean"))) 
				{
					type = jt.keyWord();;
					bw.write("<keyword> " + jt.keyWord() + "</keyword>\n");
					System.out.println("<keyword> " + jt.keyWord() + "</keyword>\n");
				}
				else if(jt.tokenType().equals("IDENTIFIER")) 
				{
					type = jt.identifier();
					bw.write("<identifier> " + jt.identifier() + " </identifier>\n");
					System.out.println("<identifier> " + jt.identifier() + " </identifier>\n");
				}

				jt.advance();
				if(jt.tokenType().equals("IDENTIFIER")) 
				{
					name = jt.identifier();
					if(st.IndexOf(name) == -1)
					{
						st.Define(name, type, kind);
					}

					bw.write("<identifier> " + jt.identifier() + " </identifier>\n");
					System.out.println("<identifier> " + jt.identifier() + " </identifier>\n");
				}
				jt.advance();
			}
		}

		bw.write("</parameterList>\n");
		System.out.println("</parameterList>\n");
	}


	public void compileVarDec() throws IOException 
	{
		String name, type, kind;
		name = "";
		type = "";
		kind = "VAR";

		bw.write("<varDec>\n");
		System.out.println("<varDec>\n");

		if(jt.tokenType().equals("KEYWORD") && jt.keyWord().equals("var")) 
		{
			bw.write("<keyword> var </keyword>\n");
			System.out.println("<keyword> var </keyword>\n");
		}

		jt.advance(); 

		if(jt.tokenType().equals("KEYWORD") && ((jt.keyWord().equals("int") || jt.keyWord().equals("char") || jt.keyWord().equals("boolean")))) 
		{
			type = jt.keyWord();
			bw.write("<keyword> " + jt.keyWord() + " </keyword>\n");
			System.out.println("<keyword> " + jt.keyWord() + " </keyword>\n");
		}
		else if(jt.tokenType().equals("IDENTIFIER")) 
		{
			type = jt.identifier();
			bw.write("<identifier> " + jt.identifier() + " </identifier>\n");
			System.out.println("<identifier> " + jt.identifier() + " </identifier>\n");
		}

		jt.advance();
		if(jt.tokenType().equals("IDENTIFIER")) 
		{
			name = jt.identifier();
			if(st.IndexOf(name) == -1)
			{
				st.Define(name, type, kind);
			}

			bw.write("<identifier> " + jt.identifier() +  " </identifier>\n");
			System.out.println("<identifier> " + jt.identifier() +  " </identifier>\n");
		}

		jt.advance();
		while(jt.tokenType().equals("SYMBOL")  && jt.symbol() == ',') 
		{
			bw.write( "<symbol> , </symbol>\n");
			System.out.println("<symbol> , </symbol>\n");

			jt.advance();
			if(jt.tokenType().equals("IDENTIFIER")) 
			{
				name = jt.identifier();
				if(st.IndexOf(name) == -1)
				{
					st.Define(name, type, kind);
				}

				bw.write("<identifier> " + jt.identifier() + " </identifier>\n");
				System.out.println("<identifier> " + jt.identifier() + " </identifier>\n");
			}
			jt.advance();
		}

		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == ';') 
		{
			bw.write("<symbol> ; </symbol>\n");
			System.out.println("<symbol> ; </symbol>\n");
		}

		bw.write("</varDec>\n");
		System.out.println("</varDec>\n");
	}


	public void compileStatements() throws IOException 
	{
		bw.write("<statements>\n");
		System.out.println("<statements>\n");

		while(jt.tokenType().equals("KEYWORD") && ((jt.keyWord().equals("let") || jt.keyWord().equals("if") || jt.keyWord().equals("while") 
				|| jt.keyWord().equals("do") || jt.keyWord().equals("return")))) 
		{
			if(jt.keyWord().equals("let")) 
			{
				compileLet();
				jt.advance();		
			}
			else if(jt.keyWord().equals("if")) 
			{
				compileIf();
			}
			else if(jt.keyWord().equals("while")) 
			{
				compileWhile();
				jt.advance();
			}
			else if(jt.keyWord().equals("do")) 
			{
				compileDo();
				jt.advance();
			}
			else if(jt.keyWord().equals("return")) 
			{
				compileReturn();
				jt.advance();
			}
		}

		bw.write("</statements>\n");
		System.out.println("</statements>\n");
	}

	public void compileDo() throws IOException 
	{
		String subName = "";

		bw.write("<doStatement>\n");
		System.out.println("<doStatement>\n");

		if(jt.tokenType().equals("KEYWORD") && jt.keyWord().equals("do")) 
		{
			bw.write("<keyword> do </keyword>\n");
			System.out.println("<keyword> do </keyword>\n");
		}

		jt.advance();
		if(jt.tokenType().equals("IDENTIFIER")) 
		{	
			subName = jt.identifier();
			bw.write("<identifier> " + jt.identifier() + " </identifier>\n");
			System.out.println("<identifier> " + jt.identifier() + " </identifier>\n");
		}

		jt.advance();
		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == '(') 
		{
			bw.write("<symbol> ( </symbol>\n");
			System.out.println("<symbol> ( </symbol>\n");
		}

		else if(jt.tokenType().equals("SYMBOL") && jt.symbol() == '.') 
		{
			String obj = subName;
			subName += ".";
			bw.write("<symbol> . </symbol>\n");
			System.out.println("<symbol> . </symbol>\n");

			if(st.IndexOf(obj) != -1) 
			{
				vmw.writePush(st.KindOf(obj), st.IndexOf(obj));
				nArgs++;
				subName = st.TypeOf(obj) + ".";
			}

			jt.advance();
			if(jt.tokenType().equals("IDENTIFIER")) 
			{
				subName += jt.identifier();
				bw.write("<identifier> " + jt.identifier() + " </identifier>\n");
				System.out.println("<identifier> " + jt.identifier() + " </identifier>\n");
			}

			jt.advance();
			if(jt.tokenType().equals("SYMBOL") && jt.symbol() == '(') 
			{
				bw.write("<symbol> ( </symbol>\n");
				System.out.println("<symbol> ( </symbol>\n");
			}
		}

		if(subName.indexOf('.') == -1) 
		{
			vmw.writePush("POINTER", 0);
			subName = className + "." + subName;
			nArgs++;
		}

		jt.advance();
		CompileExpressionList();	

		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == ')') 
		{
			bw.write("<symbol> ) </symbol>\n");
			System.out.println("<symbol> ) </symbol>\n");
		}

		vmw.writeCall(subName, nArgs);
		vmw.writePop("TEMP", 0);
		nArgs = 0;

		jt.advance();
		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == ';') 
		{
			bw.write("<symbol> ; </symbol>\n");
			System.out.println("<symbol> ; </symbol>\n");
		}

		bw.write("</doStatement>\n");
		System.out.println("</doStatement>\n");
	}

	public void compileLet() throws IOException 
	{
		boolean isArray = false;
		String varName = "";
		bw.write("<letStatement>\n");
		System.out.println("<letStatement>\n");
		if(jt.tokenType().equals("KEYWORD") && jt.keyWord().equals("let")) 
		{
			bw.write("<keyword> let </keyword>\n");
			System.out.println("<keyword> let </keyword>\n");
		}

		jt.advance();
		if(jt.tokenType().equals("IDENTIFIER")) 
		{
			varName = jt.identifier();
			bw.write("<identifier> " + jt.identifier() + " </identifier>\n");
			System.out.println("<identifier> " + jt.identifier() + " </identifier>\n");
		}

		jt.advance();
		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == '[') 
		{
			isArray = true;
			bw.write("<symbol> [ </symbol>\n");
			System.out.println("<symbol> [ </symbol>\n");
			vmw.writePush(st.KindOf(varName), st.IndexOf(varName));			

			jt.advance();
			CompileExpression();

			vmw.WriteArithmetic("ADD");

			if(jt.tokenType().equals("SYMBOL")  && jt.symbol() == ']') 
			{
				bw.write("<symbol> ] </symbol>\n");
				System.out.println("<symbol> ] </symbol>\n");
			}

			jt.advance();
		}

		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == '=') 
		{

			if(isArray) 
			{
				jt.advance();
				CompileExpression();
				vmw.writePop("TEMP", 0);
				vmw.writePop("POINTER", 1);
				vmw.writePush("TEMP", 0);
			}
			else 
			{
				jt.advance();
				CompileExpression();
			}

			bw.write("<symbol> = </symbol>\n");
			System.out.println("<symbol> = </symbol>\n");
		}

		if(!st.KindOf(varName).equals("NONE")) 
		{
			if(isArray) 
			{
				vmw.writePop("THAT", 0);
			}
			else 
			{
				vmw.writePop(st.KindOf(varName), st.IndexOf(varName));
			}
		}

		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == ';') 
		{
			bw.write("<symbol> ; </symbol>\n");
			System.out.println("<symbol> ; </symbol>\n");
		}

		bw.write("</letStatement>\n");
		System.out.println("</letStatement>\n");
	}


	public void compileWhile() throws IOException 
	{
		bw.write("<whileStatement>\n");
		System.out.println("<whileStatement>\n");

		if(jt.tokenType().equals("KEYWORD") && jt.keyWord().equals("while")) 
		{
			bw.write("<keyword> while </keyword>\n");
			System.out.println("<keyword> while </keyword>\n");
		}

		vmw.WriteLabel("WHILE_BEGIN" + whileNum);

		jt.advance();
		if(jt.tokenType().equals("SYMBOL")  && jt.symbol() == '(') 
		{
			bw.write("<symbol> ( </symbol>\n");
			System.out.println("<symbol> ( </symbol>\n");
		}

		jt.advance();
		CompileExpression();

		if(jt.tokenType().equals("SYMBOL")  && jt.symbol() == ')') 
		{
			bw.write("<symbol> ) </symbol>\n");
			System.out.println("<symbol> ) </symbol>\n");
		}


		vmw.WriteArithmetic("NOT"); 

		jt.advance();
		if(jt.tokenType().equals("SYMBOL")  && jt.symbol() == '{') 
		{
			bw.write("<symbol> { </symbol>\n");
			System.out.println("<symbol> { </symbol>\n");
		}


		vmw.WriteIf("WHILE_END" + whileNum);
		int whileNum2 = whileNum++;

		jt.advance();
		compileStatements();

		vmw.WriteGoto("WHILE_BEGIN" + whileNum2);

		if(jt.tokenType().equals("SYMBOL")  && jt.symbol() == '}') 
		{
			bw.write("<symbol> } </symbol>\n");
			System.out.println("<symbol> } </symbol>\n");
		}

		vmw.WriteLabel("WHILE_END" + whileNum2);

		bw.write("</whileStatement>\n");
		System.out.println("</whileStatement>\n");
	}

	public void compileReturn() throws IOException 
	{
		bw.write("<returnStatement>\n");
		System.out.println("<returnStatement>\n");

		if(jt.tokenType().equals("KEYWORD")  && jt.keyWord().equals("return")) 
		{
			bw.write("<keyword> return </keyword>\n");
			System.out.println("<keyword> return </keyword>\n");
		}


		jt.advance();
		String t = jt.tokenType();

		if((jt.tokenType().equals("INT_CONST") || jt.tokenType().equals("STRING_CONST") || jt.tokenType().equals("KEYWORD") || 
				jt.tokenType().equals("IDENTIFIER")) || 
				(jt.tokenType().equals("SYMBOL") && (jt.symbol() == '(' || jt.symbol() == '-' || jt.symbol() == '~')))
		{

			CompileExpression();
			vmw.writeReturn();
		}
		else 
		{
			vmw.writePush("CONST", 0);
			vmw.writeReturn();
		}

		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == ';') 
		{
			bw.write("<symbol> ; </symbol>\n");
			System.out.println("<symbol> ; </symbol>\n");
		}

		bw.write("</returnStatement>\n");
		System.out.println("</returnStatement>\n");
	}


	public void compileIf() throws IOException {
		bw.write("<ifStatement>\n");
		System.out.println("<ifStatement>\n");

		if(jt.tokenType().equals("KEYWORD") && jt.keyWord().equals("if")) 
		{
			bw.write("<keyword> if </keyword>\n");
			System.out.println("<keyword> if </keyword>\n");
		}

		jt.advance();
		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == '(') 
		{
			bw.write("<symbol> ( </symbol>\n");
			System.out.println("<symbol> ( </symbol>\n");
		}

		jt.advance();
		CompileExpression();
		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == ')') 
		{
			bw.write("<symbol> ) </symbol>\n");
			System.out.println("<symbol> ) </symbol>\n");
		}

		vmw.WriteIf("IF_TRUE" + ifNum);
		vmw.WriteGoto("IF_FALSE" + ifNum);
		int ifNum2 = ifNum++;		

		jt.advance();
		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == '{') 
		{
			bw.write("<symbol> { </symbol>\n");
			System.out.println("<symbol> { </symbol>\n");
		}

		vmw.WriteLabel("IF_TRUE" + ifNum2);

		jt.advance();
		compileStatements();
		vmw.WriteGoto("IF_END" + ifNum2);
		vmw.WriteLabel("IF_FALSE" + ifNum2);

		if(jt.tokenType().equals("SYMBOL") && jt.symbol() == '}') 
		{
			bw.write("<symbol> } </symbol>\n");
			System.out.println("<symbol> } </symbol>\n");
		}


		jt.advance(); 
		if(jt.tokenType().equals("KEYWORD") && jt.keyWord().equals("else")) 
		{
			bw.write("<keyword> else </keyword>\n");
			System.out.println("<keyword> else </keyword>\n");

			jt.advance();
			if(jt.tokenType().equals("SYMBOL") && jt.symbol() == '{') 
			{
				bw.write("<symbol> { </symbol>\n");
				System.out.println("<symbol> { </symbol>\n");
			}

			jt.advance();
			compileStatements();

			if(jt.tokenType().equals("SYMBOL") && jt.symbol() == '}') 
			{
				bw.write("<symbol> } </symbol>\n");
				System.out.println("<symbol> } </symbol>\n");
			}

			jt.advance(); 
		}

		vmw.WriteLabel("IF_END" + ifNum2);
		bw.write("</ifStatement>\n");
		System.out.println("</ifStatement>\n");
	}


	public void CompileExpression() throws IOException 
	{
		bw.write("<expression>\n");
		System.out.println("<expression>\n");

		CompileTerm();

		char operator = jt.symbol();
		while(operator == '+' || operator == '-' || operator == '*' || operator == '/' 
				|| operator == '&' || operator == '|' || operator == '<' 
				|| operator == '>' || operator == '=')
		{
			String s = "";

			if(operator == '<')
				s = "&lt;";
			else if(operator == '>')
				s = "&gt;";
			else if(operator == '&')
				s = "&amp;";
			else
				s = operator + "";

			bw.write("<symbol> " + s + " </symbol>\n");
			System.out.println("<symbol> " + s + " </symbol>\n");

			jt.advance();
			CompileTerm();

			switch(operator) {
			case '+':		
				vmw.WriteArithmetic("ADD");
				break;
			case '-':		
				vmw.WriteArithmetic("SUB");
				break;
			case '&':		
				vmw.WriteArithmetic("AND");
				break;
			case '|':		
				vmw.WriteArithmetic("OR");
				break;
			case '<':		
				vmw.WriteArithmetic("LT");
				break;
			case '>':		
				vmw.WriteArithmetic("GT");
				break;
			case '=':		
				vmw.WriteArithmetic("EQ");
				break;
			case '*':		
				vmw.writeCall("Math.multiply", 2);
				break;
			case '/':		
				vmw.writeCall("Math.divide", 2);
				break;
			}
			operator = jt.symbol();
		}

		bw.write("</expression>\n");
		System.out.println("</expression>\n");

	}

	public void CompileTerm() throws IOException 
	{
		bw.write("<term>\n");
		System.out.println("<term>\n");

		if(jt.tokenType().equals("INT_CONST")) 
		{
			bw.write("<integerConstant> " + jt.intVal() + " </integerConstant>\n");
			System.out.println("<integerConstant> " + jt.intVal() + " </integerConstant>\n");
			vmw.writePush("CONST", jt.intVal());
			jt.advance();
		}
		else if(jt.tokenType().equals("STRING_CONST")) 
		{
			String str = jt.stringVal();
			bw.write("<stringConstant> " + jt.stringVal() + " </stringConstant>\n");
			System.out.println("<stringConstant> " + jt.stringVal() + " </stringConstant>\n");
			jt.advance();

			vmw.writePush("CONST", str.length());
			vmw.writeCall("String.new", 1);
			for(int i=0; i < str.length(); i++) 
			{
				vmw.writePush("CONST", (int)str.charAt(i));
				vmw.writeCall("String.appendChar", 2);
			}
		}
		else if(jt.tokenType().equals("KEYWORD")) 
		{
			bw.write("<keyword> " + jt.keyWord() + " </keyword>\n");
			System.out.println("<keyword> " + jt.keyWord() + " </keyword>\n");

			if(jt.keyWord().equals("true")) 
			{
				vmw.writePush("CONST", 0);
				vmw.WriteArithmetic("NOT");
			} 
			else if(jt.keyWord().equals("false"))
			{
				vmw.writePush("CONST", 0);
			}
			else if(jt.keyWord().equals("this"))
			{
				vmw.writePush("POINTER", 0);
			}
			else if(jt.keyWord().equals("null"))
			{
				vmw.writePush("CONST", 0);
			}


			jt.advance();
		}
		else if(jt.tokenType().equals("IDENTIFIER")) 
		{
			boolean isArray = false;
			String subName = "";
			String obj = jt.identifier();

			bw.write("<identifier> " + jt.identifier() + " </identifier>\n");
			System.out.println("<identifier> " + jt.identifier() + " </identifier>\n");
			vmw.writePush(st.KindOf(jt.identifier()), st.IndexOf(jt.identifier()));
			subName = jt.identifier();

			jt.advance(); 
			if( jt.tokenType().equals("SYMBOL") && jt.symbol() == '[') 
			{
				isArray = true;
				bw.write("<symbol> [ </symbol>\n");
				System.out.println("<symbol> [ </symbol>\n");

				jt.advance();
				CompileExpression();

				vmw.WriteArithmetic("ADD");

				if(jt.tokenType().equals("SYMBOL") && jt.symbol() == ']') 
				{
					bw.write("<symbol> ] </symbol>\n");
					System.out.println("<symbol> ] </symbol>\n");
				}
				jt.advance();
				vmw.writePop("POINTER", 1);
				vmw.writePush("THAT", 0);
			}
			else if(jt.tokenType().equals("SYMBOL") && (jt.symbol() == '(' || jt.symbol() == '.')) 
			{
				if(jt.symbol() == '(') 
				{
					bw.write("<symbol> ( </symbol>\n");
					System.out.println("<symbol> ( </symbol>\n");
				}
				else if(jt.symbol() == '.') 
				{
					if(st.IndexOf(subName) != -1) 
					{
						nArgs++;
						subName = st.TypeOf(subName);
					}

					subName += ".";
					bw.write("<symbol> . </symbol>\n");
					System.out.println("<symbol> . </symbol>\n");

					jt.advance();
					if(jt.tokenType().equals("IDENTIFIER")) 
					{
						subName += jt.identifier();
						bw.write("<identifier> " + jt.identifier() + " </identifier>\n");
						System.out.println("<identifier> " + jt.identifier() + " </identifier>\n");
					}

					jt.advance();
					if(jt.tokenType().equals("SYMBOL") && jt.symbol() == '(') 
					{
						bw.write("<symbol> ( </symbol>\n");
						System.out.println("<symbol> ( </symbol>\n");
					}

				}	

				jt.advance();
				CompileExpressionList();	
				if(jt.tokenType().equals("SYMBOL") && jt.symbol() == ')') 
				{
					bw.write("<symbol> ) </symbol>\n");
					System.out.println("<symbol> ) </symbol>\n");
				}

				vmw.writeCall(subName, nArgs);
				nArgs = 0;

				jt.advance();
			}

		}
		else if(jt.tokenType().equals("SYMBOL") && jt.symbol() == '(') 
		{
			bw.write("<symbol> ( </symbol>\n");
			System.out.println("<symbol> ( </symbol>\n");

			jt.advance();
			CompileExpression();

			if(jt.tokenType().equals("SYMBOL") && jt.symbol() == ')') 
			{
				bw.write("<symbol> ) </symbol>\n");
				System.out.println("<symbol> ) </symbol>\n");
			}
			jt.advance();
		}

		else if(jt.tokenType().equals("SYMBOL") && (jt.symbol() == '-' || jt.symbol() == '~')) 
		{
			bw.write("<symbol> " + jt.symbol() + " </symbol>\n");
			System.out.println("<symbol> " + jt.symbol() + " </symbol>\n");

			String cmd = "";
			if(jt.symbol() == '-')
			{
				cmd = "NEG";
			}
			else
			{
				cmd = "NOT";
			}

			jt.advance();
			CompileTerm();

			vmw.WriteArithmetic(cmd);
		}

		bw.write("</term>\n");
		System.out.println("</term>\n");
	}

	public void CompileExpressionList() throws IOException 
	{
		bw.write("<expressionList>\n");
		System.out.println("<expressionList>\n");

		if((jt.tokenType().equals("INT_CONST") || jt.tokenType().equals("STRING_CONST") || jt.tokenType().equals("KEYWORD") || 
				jt.tokenType().equals("IDENTIFIER")) 
				|| ((jt.tokenType().equals("SYMBOL")) && (jt.symbol() == '(' || jt.symbol() == '-' || jt.symbol() == '~' ))) 
		{

			CompileExpression();
			nArgs++;

			while(jt.tokenType().equals("SYMBOL") && jt.symbol() == ',') 
			{
				bw.write("<symbol> , </symbol>\n");
				System.out.println("<symbol> , </symbol>\n");

				jt.advance();
				CompileExpression();
				nArgs++;
			}
		}
		bw.write("</expressionList>\n");
		System.out.println("</expressionList>\n");
	}
}
