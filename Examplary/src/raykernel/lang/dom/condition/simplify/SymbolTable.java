package raykernel.lang.dom.condition.simplify;

import java.util.HashMap;
import java.util.Map;

import raykernel.lang.dom.condition.Condition;
import raykernel.lang.dom.expression.False;
import raykernel.lang.dom.expression.True;

public class SymbolTable
{
	Map<Condition, String> table = new HashMap<Condition, String>();
	Map<String, Condition> revTable = new HashMap<String, Condition>();
	
	SymbolGenerator gen = new SymbolGenerator();
	
	public String getSymbol(Condition c)
	{
		if (c.equals(new True()))
			return "1";
		
		if (c.equals(new False()))
			return "0";
		
		String ret = table.get(c);
		
		if (ret != null)
			return ret;
		
		ret = gen.getNext();
		
		Condition cneg = c.negated();
		
		table.put(c, ret);
		table.put(cneg, ret + "'");
		
		revTable.put(ret, c);
		revTable.put(ret + "'", cneg);
		
		return ret;
	}
	
	public Condition getCondition(String symbol)
	{
		if (symbol.equals("1"))
			return new True();
		
		if (symbol.equals("0"))
			return new False();
		
		return revTable.get(symbol);
	}
	
	public void setTrue(Condition c)
	{
		Condition cneg = c.negated();
		
		table.put(c, "1");
		table.put(cneg, "0");
		
		revTable.put("1", c);
		revTable.put("0", cneg);
		
	}
	
}
