package raykernel.lang.dom.expression;

import java.util.Collection;
import java.util.LinkedList;

public class ConstantExpression extends Expression
{
	public ConstantExpression(String string)
	{
		str = string;
	}
	
	String str;
	
	@Override
	public Collection<Expression> getSubExpressions()
	{
		return new LinkedList<Expression>();
	}
	
	@Override
	public String toString()
	{
		return str;
	}
	
	@Override
	public void substitute(Expression oldExp, Expression newExp)
	{
		
	}
	
	@Override
	public Expression clone()
	{
		return new ConstantExpression(str);
	}
}
