import java.io.*;

public class VMWriter 
{
	private BufferedWriter bw;

	public VMWriter(String out) throws IOException 
	{
		File outFile = new File(out);
		bw = new BufferedWriter(new FileWriter(outFile.getAbsoluteFile()));
	}

	public void writePush(String segment, int index) throws IOException 
	{
		String segm;
		switch(segment) 
		{
		case "CONST":			
			segm = "constant";
			break;
		case "ARG":		
			segm = "argument";
			break;
		case "LOCAL":			
			segm = "local";
			break;
		case "STATIC":	
			segm = "static";
			break;
		case "THIS":			
			segm = "this";
			break;
		case "THAT":			
			segm = "that";
			break;
		case "POINTER":			
			segm = "pointer";
			break;
		case "TEMP":			
			segm = "temp";
			break;
		case "FIELD":	 	
			segm = "this";
			break;
		case "VAR":		
			segm = "local";
			break;
		default: 
			segm = null;
			break;
		}
		if(segm != null)
			bw.write("push " + segm + " " + index + "\n");
	}


	public void writePop(String segment, int index) throws IOException 
	{
		String segm;
		switch(segment) 
		{
		case "CONST":			
			segm = "constant";
			break;
		case "ARG":		
			segm = "argument";
			break;
		case "LOCAL":			
			segm = "local";
			break;

		case "STATIC":	
			segm = "static";
			break;
		case "THIS":			
			segm = "this";
			break;
		case "THAT":			
			segm = "that";
			break;
		case "POINTER":			
			segm = "pointer";
			break;
		case "TEMP":			
			segm = "temp";
			break;
		case "FIELD":	 	
			segm = "this";
			break;
		case "VAR":		
			segm = "local";
			break;
		default:
			segm = null;
			break;
		}

		if (segm != null)
			bw.write("pop " + segm + " " + index + "\n");
	}


	public void WriteArithmetic(String command) throws IOException 
	{
		command = command.toLowerCase(); 
		bw.write(command + "\n");
	}


	public void WriteLabel(String label) throws IOException 
	{
		bw.write("label " + label + "\n");
	}

	public void WriteGoto(String label) throws IOException 
	{
		bw.write("goto " + label + "\n");
	}

	public void WriteIf(String label) throws IOException 
	{
		bw.write("if-goto " + label + "\n");
	}

	public void writeCall(String name, int nArgs) throws IOException 
	{
		bw.write("call " + name + " " + nArgs + "\n");
	}

	public void writeFunction(String name, int nLocals) throws IOException 
	{
		bw.write("function " + name + " " + nLocals + "\n");
	}

	public void writeReturn() throws IOException 
	{
		bw.write("return\n");
	}

	public void close() throws IOException 
	{
		bw.close();
	}
}
