
import java.util.Hashtable;

import java.util.Enumeration;


public class SymbolTable 
{

	class Properties 
	{
		private String type;
		private String kind;
		private int index;

		public Properties(String type, String kind, int index) 
		{
			this.type = type;
			this.kind = kind;
			this.index = index;
		}

		public String getType() 		
		{return this.type;}

		public String getKind()			
		{return this.kind;}

		public int getIndex()			
		{return this.index;}
	}


	private Hashtable<String, Properties> classScope, subScope;
	private boolean inSubScope;
	private int staticIndex, fieldIndex, argIndex, varIndex;


	public SymbolTable() 
	{
		classScope = new Hashtable<String, Properties>();
		subScope = new Hashtable<String, Properties>();
		staticIndex = 0;
		fieldIndex = 0;
		argIndex = 0;
		varIndex = 0;
		inSubScope = false;
	}


	public void startSubroutine(String subType) 
	{
		subScope.clear();
		inSubScope = true;
		if (subType.equals("method") )
		{
			//the first argument (number 0) refers to the this object
			argIndex = 1;
		}
		else
		{
			argIndex = 0;
		}
		varIndex = 0;
	}


	public void Define(String name, String type, String kind) {

		Properties tmp = null;
		int i = 0;
		if(kind.equals("STATIC") || kind.equals("FIELD")) 
		{
			switch(kind) 
			{
			case "STATIC":				
				i = staticIndex++;
				break;
			case "FIELD":					
				i = fieldIndex++;
				break;
			}
			tmp = classScope.put(name, new Properties(type, kind, i));

		}
		else if(kind.equals("ARG") || kind.equals("VAR")) 
		{
			switch(kind) 
			{
			case "ARG":					
				i = argIndex++;
				break;
			case "VAR":					
				i = varIndex++;
				break;
			}
			tmp = subScope.put(name, new Properties(type, kind, i));
		}
	}


	


	public String KindOf(String name) 
	{

		if (inSubScope)
		{
			if (subScope.get(name) != null)
			{
				return subScope.get(name).getKind();
			}
		}

		if (classScope.get(name) != null)
			return classScope.get(name).getKind();

		return "NONE";
	}


	public String TypeOf(String name) 
	{
		if (inSubScope)
		{
			if (subScope.get(name) != null)
			{
				return subScope.get(name).getType();
			}
		}

		if (classScope.get(name) != null)
			return classScope.get(name).getType();

		return null;

	}

	public int IndexOf(String name) 
	{

		if (inSubScope)
		{
			if (subScope.get(name) != null)
			{
				return subScope.get(name).getIndex();
			}
		}
		if (classScope.get(name) != null)
			return classScope.get(name).getIndex();

		return -1;
	}
	
	
	public int VarCount(String kind) 
	{
		int count = 0;
		Hashtable<String, Properties> tmp = null;

		if(kind.equals("VAR") || kind.equals("ARG"))
			tmp = subScope;
		else if(kind.equals("FIELD") || kind.equals("STATIC"))
			tmp = classScope;

		Enumeration<String> e = tmp.keys();

		while(e != null & e.hasMoreElements()) 
		{
			String key = e.nextElement();
			if(tmp.get(key) != null && tmp.get(key).getKind().equals(kind))
				count++;
		}

		return count;
	}


}
