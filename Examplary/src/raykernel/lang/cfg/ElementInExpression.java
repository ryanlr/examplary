package raykernel.lang.cfg;

import raykernel.lang.dom.expression.Expression;
import raykernel.lang.dom.expression.OneOpExpression;

public class ElementInExpression extends OneOpExpression
{
	
	public ElementInExpression(Expression exp)
	{
		this.exp = exp;
	}
	
	@Override
	public Expression clone()
	{
		return new ElementInExpression(exp);
	}
	
	@Override
	public String toString()
	{
		return "(element in " + exp + ")";
	}
	
}
