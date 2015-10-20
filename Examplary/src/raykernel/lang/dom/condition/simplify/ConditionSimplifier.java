package raykernel.lang.dom.condition.simplify;

import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import minimize.MinimizedTable;
import raykernel.lang.dom.condition.AndCondition;
import raykernel.lang.dom.condition.Condition;
import raykernel.lang.dom.condition.OrCondition;
import raykernel.lang.dom.expression.True;
import raykernel.lang.dom.naming.ToolBox;
import raykernel.util.Regex;

public class ConditionSimplifier
{
	static PrintStream ps = new NullPrintStream();
	
	public static Condition flatten(Condition c)
	{
		if (c instanceof OrCondition)
		{
			OrCondition or = (OrCondition) c;
			if (or.getConditions().size() == 1)
				return flatten(or.getConditions().iterator().next());
			else if (or.getConditions().size() == 0)
				return new True();
		}
		else if (c instanceof AndCondition)
		{
			AndCondition and = (AndCondition) c;
			if (and.getConditions().size() == 1)
				return flatten(and.getConditions().iterator().next());
			else if (and.getConditions().size() == 0)
				return new True();
		}
		
		return c;
	}
	
	public static Condition remove(Condition inputCond, Condition toRem)
	{
		if (!(inputCond instanceof AndCondition) && !(inputCond instanceof OrCondition))
			return inputCond;
		
		//System.out.println("  Executing: " + inputCond + " - " + toRem);
		
		SymbolTable symbolTable = new SymbolTable();
		
		symbolTable.setTrue(toRem);
		
		String encoded = encodeCondition(inputCond, symbolTable);
		
		//System.out.println("  Encoded: " + encoded);
		
		MinimizedTable m = new MinimizedTable(encoded, ps);
		String minimised = m.toString();
		
		//System.out.println("  minimised: " + minimised);
		
		Condition decoded = decodeCondition(minimised, symbolTable);
		
		//System.out.println("  decoded: " + decoded);
		
		Condition flattened = flatten(decoded);
		
		//System.out.println("  flattened: " + flattened);
		
		return flattened;
	}
	
	public static Condition simplify(Condition inputCond)
	{
		if (!(inputCond instanceof AndCondition) && !(inputCond instanceof OrCondition))
			return inputCond;
		
		SymbolTable symbolTable = new SymbolTable();
		
		String encoded = encodeCondition(inputCond, symbolTable);
		
		MinimizedTable m = new MinimizedTable(encoded, ps);
		String minimised = m.toString();
		
		Condition decoded = decodeCondition(minimised, symbolTable);
		
		return flatten(decoded);
	}
	
	/**
	 * Recursively constructs a condition tree
	 */
	private static String encodeCondition(Condition inputCond, SymbolTable symbolTable)
	{
		if (inputCond instanceof AndCondition)
		{
			List<String> encodedList = encodeList(((AndCondition) inputCond).getConditions(), symbolTable);
			
			return "( " + ToolBox.stringifyList(encodedList, "*") + " )";
		}
		else if (inputCond instanceof OrCondition)
		{
			List<String> encodedList = encodeList(((OrCondition) inputCond).getConditions(), symbolTable);
			
			return "( " + ToolBox.stringifyList(encodedList, "+") + " )";
		}
		else
			return symbolTable.getSymbol(inputCond);
	}
	
	/**
	 * Helper function
	 */
	private static List<String> encodeList(Collection<Condition> collection, SymbolTable symbolTable)
	{
		List<String> ret = new LinkedList<String>();
		
		for (Condition c : collection)
		{
			ret.add(encodeCondition(c, symbolTable));
		}
		
		return ret;
	}
	
	/**
	 * This parses the minimized expression, and reconstructs a Condition
	 * Assumes Disjunctive normal form  (e.g., ab + ab' + abc ... )
	 */
	private static Condition decodeCondition(String minimised, SymbolTable symbolTable)
	{
		OrCondition ret = new OrCondition();
		
		for (String clause : minimised.split("(\\+|\\s)+"))
		{
			AndCondition and = new AndCondition();
			
			for (String symbol : getSymbols(clause))
			{
				Condition c = symbolTable.getCondition(symbol);
				and.addCondition(c);
				//System.out.println("     added to and: " + c);
			}
			
			ret.addCondition(and);
			//System.out.println("     added to or: " + and);
		}
		
		return ret;
	}
	
	/**
	 * Split abcd'ef' => a, b, c, d', e, f'
	 */
	private static List<String> getSymbols(String clause)
	{
		String regex = "[01a-zA-Z][']?";
		return Regex.getMatches(clause, regex);
	}
	
	public static void main(String[] args)
	{
		String in = "d + c'f' + ab0 + abcd'ef'";
		
		String[] tokens = in.split("(\\+|\\s)+");
		
		for (String token : tokens)
		{
			System.out.println(token);
			
			for (String s : getSymbols(token))
			{
				System.out.println("  > " + s);
			}
		}
		
	}
}
