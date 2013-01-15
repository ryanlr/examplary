package raykernel.lang.dom.expression;

import raykernel.lang.dom.condition.OneOpExpressionCondition;
import raykernel.lang.dom.naming.Type;

public class CastExpression extends OneOpExpressionCondition
{
	Type type;
	
	public CastExpression(Expression exp, Type type)
	{
		this.exp = exp;
		this.type = type;
	}
	
	@Override
	public CastExpression clone()
	{
		return new CastExpression(exp.clone(), type);
	}
	
	@Override
	public String toString()
	{
		String ret;
		
		if (!isTrue)
		{
			ret = "!((" + type + ") " + exp + ")";
		}
		else
		{
			ret = "(" + type + ") " + exp;
		}
		
		return ret;
	}
	
}