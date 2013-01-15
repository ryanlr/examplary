package raykernel.lang.dom.expression;

import raykernel.lang.dom.condition.OneOpExpressionCondition;
import raykernel.lang.dom.naming.Type;

public class InstanceOfExpression extends OneOpExpressionCondition
{
	Type type;
	
	public InstanceOfExpression(Expression exp, Type type)
	{
		this.exp = exp;
		this.type = type;
	}
	
	@Override
	public InstanceOfExpression clone()
	{
		return new InstanceOfExpression(exp.clone(), type);
	}
	
	@Override
	public String toString()
	{
		String ret;
		
		if (!isTrue)
		{
			ret = exp + " not instanceof " + type;
		}
		else
		{
			ret = exp + " instanceof " + type;
		}
		
		return ret;
	}
	
}
