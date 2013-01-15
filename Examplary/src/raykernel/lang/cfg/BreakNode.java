package raykernel.lang.cfg;

import java.util.List;

import raykernel.util.Tools;

public class BreakNode extends CFGNode
{
	
	CFGNode next;
	
	public BreakNode(CFGNode next)
	{
		this.next = next;
	}
	
	@Override
	public List<CFGNode> getSuccessors()
	{
		return Tools.makeList(next);
	}
	
	public CFGNode getNext()
	{
		return next;
	}
	
	public void setNext(CFGNode next)
	{
		this.next = next;
	}
	
	@Override
	public String toString()
	{
		return "break to " + next;
	}
}
