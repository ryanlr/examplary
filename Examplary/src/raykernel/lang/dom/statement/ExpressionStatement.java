package raykernel.lang.dom.statement;

import raykernel.lang.dom.expression.Expression;

/**
 * This is a statement consisting entirely of an Expression
 * For example:  <code> System.out.println("Hello"); </code>
 * @author buse
 *
 */
public class ExpressionStatement extends OneExpressionStatement
{
	public ExpressionStatement(Expression exp)
	{
		this.exp = exp;
	}
	
	@Override
	public String toString()
	{
		return exp.toString();
	}
	
	@Override
	public Statement clone()
	{
		ExpressionStatement newEs = new ExpressionStatement(exp.clone());
		newEs.charIndex = charIndex;
		return newEs;
	}
	
}
