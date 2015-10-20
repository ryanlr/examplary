package raykernel.lang.dom.expression;

import java.util.Collection;
import java.util.LinkedList;

public class StringLiteralExpression extends Expression
{
	public StringLiteralExpression(String string)
	{
		exp = string;
	}
	
	String exp;
	
	@Override
	public Collection<Expression> getSubExpressions()
	{
		return new LinkedList<Expression>();
	}
	
	@Override
	public String toString()
	{
		return "\"" + exp + "\"";
	}
	
	@Override
	public void substitute(Expression oldExp, Expression newExp)
	{
		
	}
	
	@Override
	public Expression clone()
	{
		return new StringLiteralExpression(exp);
	}
}
