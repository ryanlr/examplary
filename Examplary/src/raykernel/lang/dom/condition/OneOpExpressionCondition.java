package raykernel.lang.dom.condition;

import java.util.Collection;

import raykernel.lang.dom.expression.Expression;

public abstract class OneOpExpressionCondition extends ExpressionCondition
{
	public Expression exp;
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	@Override
	public Collection<Expression> getSubExpressions()
	{
		return raykernel.util.Tools.makeCollection(exp);
	}
	
	@Override
	public void substitute(Expression oldExp, Expression newExp)
	{
		if (exp.equals(oldExp))
		{
			exp = newExp;
		}
		
		exp.substitute(oldExp, newExp);
	}
	
}
