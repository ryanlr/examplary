package raykernel.lang.dom.expression;

import java.util.Collection;

import raykernel.util.Tools;

public class AssignmentExpression extends Expression
{
	Variable leftHandSide;
	Expression rightHandSide;
	
	public AssignmentExpression(Variable var, Expression init)
	{
		leftHandSide = var;
		rightHandSide = init;
	}
	
	@Override
	public Collection<Expression> getSubExpressions()
	{
		return Tools.makeCollection(rightHandSide);
	}
	
	@Override
	public void substitute(Expression oldExp, Expression newExp)
	{
		if (leftHandSide.equals(oldExp) && newExp instanceof Variable)
		{
			leftHandSide = (Variable) newExp;
		}
		
		if (rightHandSide.equals(oldExp))
		{
			rightHandSide = newExp;
		}
		
		leftHandSide.substitute(oldExp, newExp);
		rightHandSide.substitute(oldExp, newExp);
	}
	
	public Expression getExpression()
	{
		return rightHandSide;
	}
	
	public Variable getVariable()
	{
		return leftHandSide;
	}
	
	@Override
	public String toString()
	{
		return leftHandSide + " = " + rightHandSide;
	}
	
	@Override
	public Expression clone()
	{
		return new AssignmentExpression(leftHandSide, rightHandSide.clone());
	}
}
