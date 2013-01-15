package raykernel.lang.dom.expression;

import java.util.Collection;
import java.util.LinkedList;

public class UnknownExpression extends Expression
{
	String info = null;
	
	public UnknownExpression()
	{
	}
	
	public UnknownExpression(String string)
	{
		info = string;
	}
	
	@Override
	public Collection<Expression> getSubExpressions()
	{
		return new LinkedList<Expression>();
	}
	
	@Override
	public String toString()
	{
		
		return info == null ? "[Unknown]" : "[Expr " + info + "]";
	}
	
	@Override
	public void substitute(Expression oldExp, Expression newExp)
	{
		
	}
	
	@Override
	public Expression clone()
	{
		return new UnknownExpression(info);
	}
}
