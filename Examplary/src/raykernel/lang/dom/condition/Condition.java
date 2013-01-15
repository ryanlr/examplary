package raykernel.lang.dom.condition;

import raykernel.lang.dom.expression.Expression;

public abstract class Condition extends Expression
{
	public abstract Condition negated();
	
	public Condition or(Condition c)
	{
		return new OrCondition(this, c);
	}
	
	public Condition and(Condition c)
	{
		return new AndCondition(this, c);
	}
}
