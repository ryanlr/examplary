package raykernel.lang.dom.expression;

import java.util.Collection;

import raykernel.lang.dom.condition.ExpressionCondition;
import raykernel.util.Tools;

public abstract class OneOpExpressionCondition extends ExpressionCondition
{
	Expression exp;
	
	public Expression getExpression()
	{
		return exp;
	}
	
	@Override
	public Collection<Expression> getSubExpressions()
	{
		return Tools.makeCollection(exp);
	}
	
	@Override
	public void substitute(Expression oldExp, Expression newExp)
	{
		if (exp.equals(oldExp))
		{
			exp = newExp;
		}
		else
		{
			exp.substitute(oldExp, newExp);
		}
	}
}
