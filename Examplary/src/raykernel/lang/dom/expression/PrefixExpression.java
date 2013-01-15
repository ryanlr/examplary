package raykernel.lang.dom.expression;

public class PrefixExpression extends OneOpExpressionCondition
{
	
	String op;
	
	PrefixExpression(Expression exp, String op)
	{
		this.exp = exp;
		this.op = op;
	}
	
	@Override
	public String toString()
	{
		return op + exp;
	}
	
	@Override
	public Expression clone()
	{
		return new PrefixExpression(exp.clone(), op);
	}
	
}
