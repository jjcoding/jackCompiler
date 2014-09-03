
import java.io.*;

public class JackCompiler {


	public static void main(String[] args) {	

		String fileName = args[0].substring(0, args[0].lastIndexOf('.'));
		//String xmlFile = inFile + ".xml";		

		BufferedWriter bw;
		CompilationEngine ce;
		try{
			ce = new CompilationEngine(args[0], fileName);	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		try
		{	 
    		File tempFile = new File(fileName + "Temp.jack");
    		if(tempFile.delete())
    		{
    			System.out.println(tempFile.getName() + " is deleted!");
    		}
    		else
    		{
    			System.out.println("Delete operation is failed.");
    		}
    	}
		catch(Exception e){
    		e.printStackTrace();
    	}
	}
}
