package raykernel.lang.dom.expression;

import java.util.Collection;
import java.util.LinkedList;

import raykernel.lang.dom.condition.Condition;
import raykernel.lang.dom.condition.ExpressionCondition;

public class False extends ExpressionCondition
{
	
	@Override
	public Expression clone()
	{
		return new False();
	}
	
	@Override
	public boolean equals(Object o)
	{
		return o instanceof False;
	}
	
	@Override
	public Condition negated()
	{
		return new True();
	}
	
	@Override
	public String toString()
	{
		return "FALSE";
	}
	
	@Override
	public void substitute(Expression oldExp, Expression newExp)
	{
		
	}
	
	@Override
	public Collection<Expression> getSubExpressions()
	{
		return new LinkedList<Expression>();
	}
	
}
