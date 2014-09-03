import java.io.*;
import java.util.ArrayList;


public class JackTokenizer {


	public static String currToken;

	private final String[] keywords = {"class", "constructor",  "function", "method", "field", "static", "var", "int", "char", "boolean", "void", 
			"true", "false", "null", "this", "let", "do", "if", "else", "while", "return"};

	private final char[] symbols = {'{', '}', '(', ')', '[', ']', '.', ',', ';', '+', '-', '*', '/', '&', '|', '<', '>', '=', '~'};

	private String[] currLineTokens;
	private BufferedReader br;

	private int tokIndex;

	public JackTokenizer(String inFile)  throws Exception
	{	
		removeComments(inFile);  //  create inFileTemp.jack, the no-comment edition of original jack file

		String tempFile = inFile.substring(0, inFile.lastIndexOf('.')) + "Temp.jack";

		br = new BufferedReader(new FileReader(tempFile));

		tokIndex = 0;
	}


	public void removeComments(String fileName) throws Exception
	{
		boolean firstSlashFound = false;
		boolean firstStarFound = false;		
		boolean slashCommentFound = false;
		boolean starCommentFound = false;	
		boolean closingStarFound = false;
		boolean startDoubleQuoteFound = false;
		int charRead;

		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		fileName = fileName.substring(0, fileName.lastIndexOf('.'));
		String tempFile = fileName + "Temp.jack";		
		BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

		while ((charRead = reader.read()) != -1) 
		{
			switch((char)charRead)
			{

			case '/':
				if (starCommentFound && !closingStarFound) //case:   /*   /
				{
					continue;
				}
				else if (closingStarFound && starCommentFound) //   case: /*  */
				{
					firstStarFound = false;
					starCommentFound = false;
					closingStarFound = false;
					continue;
				} 
				else if (firstSlashFound && !slashCommentFound	&& !starCommentFound)  //case:   //
				{
					slashCommentFound = true;
					firstSlashFound = false;
					continue;
				} 
				else if (!slashCommentFound && !starCommentFound && !startDoubleQuoteFound) // case: /
				{
					firstSlashFound = true;
					continue;
				}
				break;

			case '*':
				if (starCommentFound) //case:   /* *
				{
					closingStarFound = true;
					continue;
				}
				else if (firstSlashFound && !starCommentFound) //case: /* 
				{
					starCommentFound = true;
					firstSlashFound = false;
					continue;
				} 		
				break;

			case '\"':
				if (starCommentFound || slashCommentFound) 
				{
					continue;
				}
				startDoubleQuoteFound = !startDoubleQuoteFound; 
				break;

			case '\n': 
				if (slashCommentFound) // end of //*
				{
					System.out.print((char) charRead);
					bw.write(""+(char)charRead);
					starCommentFound = false;
					slashCommentFound = false;
					firstStarFound = false;
					firstSlashFound = false;
					continue;
				}
				break;

			default:	
				break;
			}

			if (starCommentFound && !closingStarFound) //case: /* 
				continue;

			if (startDoubleQuoteFound && charRead == ' ')
			{
				System.out.print("##SPACE##");
				bw.write("##SPACE##");
				continue;
			}

			if (charRead != '*'&& charRead != '/') 
			{
				if (firstSlashFound)
				{
					System.out.print("/");
					bw.write("/");
				}


				if (closingStarFound) //case:  /* *#
				{
					System.out.print((char) charRead);
					bw.write(""+(char)charRead);
				}		

				firstStarFound = false;
				closingStarFound = false;
				firstSlashFound = false;
			}

			if (!slashCommentFound && !starCommentFound) 	//case:  /* *#
			{
				System.out.print((char) charRead); 
				bw.write(""+(char)charRead);
			}			
		}
		reader.close();
		bw.close();
	}
	

	public boolean hasMoreTokens() throws IOException 
	{
		return br.ready();
	}

	public void advance() throws IOException 
	{
		if(!hasMoreTokens())
			return;

		if(tokIndex == 0)
		{
			String currLine;

			//get a new line while the new line is not empty
			do 
			{
				currLine = br.readLine();

				currLine = currLine.replaceAll("\\s+", " ");
				currLineTokens = currLine.split(" ");

			}while(currLine.isEmpty() || currLine.matches("\\s+"));

			ArrayList<String> tokenList = new ArrayList<String>();
			for(int i=0; i < currLineTokens.length; i++)
			{
				if(currLineTokens[i].matches("[\\S]*[\"][\\S]*[\"][\\S]*"))
				{
					currLineTokens[i] = currLineTokens[i].replaceAll("##SPACE##", " ");
				}

				if(currLineTokens[i].isEmpty())
					continue;

				boolean symbolsContained = false ; // to check if there are elements other than only one symbol in a single token

				//seperate symbols
				for(int j=0; j < symbols.length; j++)
				{
					if(currLineTokens[i].equals(symbols[j]+""))
						break;
					else if(currLineTokens[i].contains(symbols[j]+""))
					{
						String tmp = "";
						symbolsContained = true;
						int lastIdx = 0; //used to keep track of the break point of the current line tokens
						for(int k=0; k < currLineTokens[i].length(); k++)
						{
							for(int l=0; l < symbols.length; l++)
							{
								if(currLineTokens[i].charAt(k) == symbols[l])
								{
									if(lastIdx != k)
									{
										tokenList.add(currLineTokens[i].substring(lastIdx, k));
									}
									tokenList.add(symbols[l] + "");
									lastIdx = k+1;
								}
							}
							if(lastIdx < currLineTokens[i].length() && k == currLineTokens[i].length()-1)
								tokenList.add(currLineTokens[i].substring(lastIdx, currLineTokens[i].length()));
						}
						break;
					}
				}

				//add token to token list if it was not added 
				if(!symbolsContained)
					tokenList.add(currLineTokens[i]);
			}

			//update currLineTokens
			String[] tmp = new String[tokenList.size()];

			for(int i=0; i < tokenList.size(); i++)
				tmp[i] = tokenList.get(i);

			currLineTokens = tmp;
		}

		do 
		{
			currToken = currLineTokens[tokIndex++];
		}while(currToken.isEmpty());

		if(tokIndex == currLineTokens.length)
			tokIndex = 0;
	}

	public String tokenType() 
	{	
		
				
		for(int i=0; i < keywords.length; i++)
		{
			if(currToken.equals(keywords[i]))
				return "KEYWORD";
		}


		for(int i=0; i < symbols.length; i++)
		{
			if(currToken.charAt(0) == symbols[i])
				return "SYMBOL";
		}

		
		try {
			int i = Integer.parseInt(currToken);
			if(i >= 0 && i <= 32767)
				return "INT_CONST";
		}
		catch (NumberFormatException nfe) 
		{}
		
		if(currToken.matches("^[\"]([(\\S| )&&[^\"]])*[\"]"))
			return "STRING_CONST";

		if(currToken.matches("^[\\D][\\w]*"))
			return "IDENTIFIER";

		return null;
	}


	public String keyWord() 
	{
		return currToken;
	}


	public char symbol() 
	{
		return currToken.charAt(0);
	}

	public int intVal() 
	{
		return Integer.parseInt(currToken);
	}

	public String stringVal() 
	{	
		return currToken.substring(1, currToken.length()-1);
	}

	public String identifier() 
	{	
		return currToken;
	}


}