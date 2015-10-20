package raykernel.lang.dom.expression;

import java.util.Collection;
import java.util.LinkedList;

public class NullExpression extends Expression
{
	@Override
	public Collection<Expression> getSubExpressions()
	{
		return new LinkedList<Expression>();
	}
	
	@Override
	public String toString()
	{
		return "null";
	}
	
	@Override
	public void substitute(Expression oldExp, Expression newExp)
	{
	}
	
	@Override
	public Expression clone()
	{
		return new NullExpression();
	}
}
