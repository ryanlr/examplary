package raykernel.lang.cfg;

import java.util.List;

import raykernel.lang.dom.expression.Expression;
import raykernel.util.Tools;

public class SwtichCaseNode extends CFGNode
{
	Expression exp;
	CFGNode next;
	
	public SwtichCaseNode(Expression exp)
	{
		this.exp = exp;
	}
	
	@Override
	public List<CFGNode> getSuccessors()
	{
		return Tools.makeList(next);
	}
	
	public void setNext(CFGNode next)
	{
		this.next = next;
	}
	
	@Override
	public String toString()
	{
		if (exp != null)
			return "case: " + exp;
		return "default case";
	}
	
	public boolean isDeafult()
	{
		return exp == null;
	}
	
	public Expression getExpression()
	{
		return exp;
	}
	
}
