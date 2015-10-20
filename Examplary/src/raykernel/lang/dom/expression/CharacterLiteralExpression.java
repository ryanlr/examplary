package raykernel.lang.dom.expression;

import java.util.Collection;
import java.util.LinkedList;

public class CharacterLiteralExpression extends Expression
{
	String escapedChar;
	
	public CharacterLiteralExpression(String escapedChar)
	{
		this.escapedChar = escapedChar;
	}
	
	@Override
	public Expression clone()
	{
		return new CharacterLiteralExpression(escapedChar);
	}
	
	@Override
	public Collection<Expression> getSubExpressions()
	{
		return new LinkedList<Expression>();
	}
	
	@Override
	public String toString()
	{
		return "'" + escapedChar + "'";
	}
	
	@Override
	public void substitute(Expression oldExp, Expression newExp)
	{
		
	}
	
}
