package raykernel.lang.dom.condition;


/**
 * An expression which can be treated as a boolean
 * @author buse
 *
 */
public abstract class ExpressionCondition extends Condition
{
	protected boolean isTrue = true;
	
	@Override
	public Condition negated()
	{
		ExpressionCondition c = (ExpressionCondition) clone();
		c.isTrue = !c.isTrue;
		return c;
	}
	
	@Override
	public boolean equals(Object o)
	{
		return toString().equals(o.toString());
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
}
