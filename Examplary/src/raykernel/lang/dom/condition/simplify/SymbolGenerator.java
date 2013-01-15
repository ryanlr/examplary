package raykernel.lang.dom.condition.simplify;

public class SymbolGenerator
{
	char next = 'a';
	
	public String getNext()
	{
		String ret = next + "";
		
		next++;
		
		return ret;
	}
}
