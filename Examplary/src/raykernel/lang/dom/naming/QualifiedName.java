package raykernel.lang.dom.naming;

import java.util.Collection;
import java.util.LinkedList;

import raykernel.lang.dom.expression.Expression;

public class QualifiedName extends Expression
{
	String name;
	
	public QualifiedName(String name)
	{
		this.name = name;
	}
	
	@Override
	public Collection<Expression> getSubExpressions()
	{
		return new LinkedList<Expression>();
	}
	
	@Override
	public void substitute(Expression oldExp, Expression newExp)
	{
		
	}
	
	@Override
	public Expression clone()
	{
		return this;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
}
