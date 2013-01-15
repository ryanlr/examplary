package raykernel.lang.dom.expression;

public class UnknownExpressionException extends Exception
{
	Object exp;
	
	public UnknownExpressionException(Object exp)
	{
		this.exp = exp;
	}
	
	@Override
	public String toString()
	{
		return "Unknown Expression: " + exp + " Class: " + exp.getClass().getName();
	}
}
