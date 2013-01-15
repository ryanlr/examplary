package raykernel.lang.dom.statement;

import java.util.Collection;
import java.util.LinkedList;

import raykernel.lang.dom.expression.Expression;
import raykernel.util.Tools;

public abstract class OneExpressionStatement extends Statement
{
	Expression exp;
	
	public Expression getExpression()
	{
		return exp;
	}
	
	@Override
	public Collection<Expression> getSubExpressions()
	{
		if (exp == null)
			return new LinkedList<Expression>();
		
		return Tools.makeCollection(exp);
	}
	
	@Override
	public void substitute(Expression oldExp, Expression newExp)
	{
		if (exp == null)
			return;
		
		if (exp.equals(oldExp))
		{
			exp = newExp;
		}
		else
		{
			exp.substitute(oldExp, newExp);
		}
	}
	
	@Override
	public boolean equals(Object o)
	{
		boolean result = false;
		
		if (this.getClass().equals(o.getClass()))
			if (exp.equals(((OneExpressionStatement) o).exp))
			{
				result = true;
			}
		
		//System.out.println(this + " ?= " + o + " : " + result);
		
		return result;
	}
	
	@Override
	public int hashCode()
	{
		return 13 * exp.hashCode() + 7;
	}
}
