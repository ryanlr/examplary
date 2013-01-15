package raykernel.lang.dom.expression;

public class ParenthesizedExpression extends OneOpExpressionCondition
{
	
	public ParenthesizedExpression(Expression exp)
	{
		this.exp = exp;
	}
	
	@Override
	public String toString()
	{
		return "(" + exp + ")";
	}
	
	@Override
	public Expression clone()
	{
		return new ParenthesizedExpression(exp.clone());
	}
	
}
