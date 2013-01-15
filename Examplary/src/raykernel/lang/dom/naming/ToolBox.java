package raykernel.lang.dom.naming;

import java.util.Collection;

public class ToolBox
{
	public static <T> String stringifyList(Collection<T> things, String seperator)
	{
		StringBuffer br = new StringBuffer();
		
		boolean first = true;
		
		for (T o : things)
		{
			if (!first)
			{
				br.append(" " + seperator + " " + o);
			}
			else
			{
				br.append(o.toString());
				first = false;
			}
		}
		
		return br.toString();
	}
	
}
