package raykernel.lang.dom.statement;

import raykernel.lang.dom.expression.Expression;

public class ThrowStatement extends OneExpressionStatement
{
	
	public ThrowStatement(Expression expression)
	{
		exp = expression;
	}
	
	@Override
	public String toString()
	{
		return "throw " + exp.toString();
	}
	
	@Override
	public Statement clone()
	{
		ThrowStatement newEs = new ThrowStatement(exp.clone());
		newEs.charIndex = charIndex;
		return newEs;
	}
	
}
