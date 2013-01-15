package raykernel.lang.dom.expression;

public class PostfixExpression extends OneOpExpressionCondition
{
	String op;
	
	PostfixExpression(Expression exp, String op)
	{
		this.exp = exp;
		this.op = op;
	}
	
	@Override
	public String toString()
	{
		return exp + op;
	}
	
	@Override
	public Expression clone()
	{
		return new PostfixExpression(exp.clone(), op);
	}
	
}
