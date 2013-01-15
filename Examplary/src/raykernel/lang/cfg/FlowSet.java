package raykernel.lang.cfg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import raykernel.lang.dom.condition.AndCondition;
import raykernel.lang.dom.condition.Condition;
import raykernel.lang.dom.expression.ArrayCreationExpression;
import raykernel.lang.dom.expression.Expression;
import raykernel.lang.dom.expression.NewExpression;
import raykernel.lang.dom.expression.Variable;
import raykernel.lang.dom.naming.Type;
import raykernel.lang.dom.statement.Statement;

/**
 * The result of symbolically executing a path to a specific point.
 * A set of conditions that must
 * be true and also a register file (and seperate parameter reg set?)
 * 
 * @author buse
 * 
 */
public class FlowSet
{
	HashMap<Variable, Expression> regFile = new HashMap<Variable, Expression>();
	
	HashMap<Variable, Type> typeMap = new HashMap<Variable, Type>();
	
	LinkedList<Condition> conditions = new LinkedList<Condition>();
	
	public Condition getPredicate()
	{
		AndCondition ret = new AndCondition();
		
		for (Condition c : conditions)
		{
			ret.addCondition(c);
		}
		
		return ret;
	}
	
	@Override
	public String toString()
	{
		StringBuffer br = new StringBuffer();
		
		br.append("Predicate: " + conditions);
		br.append("RegFile: " + regFile);
		
		return br.toString();
	}
	
	@Override
	public FlowSet clone()
	{
		FlowSet f = new FlowSet();
		f.regFile = (HashMap<Variable, Expression>) regFile.clone();
		f.typeMap = (HashMap<Variable, Type>) typeMap.clone();
		f.conditions = (LinkedList<Condition>) conditions.clone();
		return f;
	}
	
	public void addAssignment(Variable variable, Expression expression)
	{
		Expression e = computeSymbolicExpression(expression);
		regFile.put(variable, e);
	}
	
	public void clearVariable(Variable var)
	{
		regFile.remove(var);
	}
	
	public void addDeclairation(Type type, Variable variable)
	{
		typeMap.put(variable, type);
	}
	
	public void addCondition(Condition c)
	{
		conditions.add((Condition) computeSymbolicExpression(c));
	}
	
	public void removeCondition(Condition c)
	{
		conditions.remove(computeSymbolicExpression(c));
	}
	
	/**
	 * After writing to a variable, we have to invalidate old conditions 
	 * @param e
	 */
	public void removeConditionsWith(Expression e)
	{
		List<Condition> toRem = new LinkedList<Condition>();
		
		for (Condition c : conditions)
		{
			if (c.getSubExpressions().contains(e))
			{
				toRem.add(c);
			}
		}
		
		conditions.removeAll(toRem);
	}
	
	public Statement computeSymbolicStmt(Statement stmt)
	{
		Statement ret = stmt.clone();
		
		//System.out.println("Computing symbolic stmt for: " + ret);
		
		for (Variable v : regFile.keySet())
		{
			Expression sub = regFile.get(v);
			
			if (Expression.getAllSubExpressions(sub).contains(v))
			{
				//System.out.println("will not use [" + v + "->" + sub + "]");
				continue;
			}
			
			if (sub instanceof NewExpression || sub instanceof ArrayCreationExpression)
			{
				continue;
			}
			
			ret.substitute(v, sub); //resursive call here?
			
			//System.out.println("[" + v + "->" + sub + "]: " + ret);
		}
		
		return ret;
	}
	
	public Expression computeSymbolicExpression(Expression e)
	{
		Expression ret = e.clone();
		
		for (Variable v : regFile.keySet())
		{
			Expression sub = regFile.get(v);
			
			if (Expression.getAllSubExpressions(sub).contains(v))
			{
				continue;
			}
			
			ret.substitute(v, sub); //resursive call here?
		}
		
		return ret;
	}
	
	public Condition computeSymbolicPredicate()
	{
		AndCondition ret = new AndCondition();
		
		for (Condition c : conditions)
		{
			ret.addCondition(c);
		}
		/*
		for (Variable v : regFile.keySet())
		{
			Expression sub = regFile.get(v);
			
			if (Expression.getAllSubExpressions(sub).contains(v))
			{
				continue;
			}
			
			ret.substitute(v, sub); //resursive call here?
		}*/
		
		return ret;
		
	}
	
}
