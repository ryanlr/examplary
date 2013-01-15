package raykernel.lang.cfg;

import java.util.List;

import raykernel.util.Tools;

public class ThenBranchNode extends CFGNode
{
	CFGNode next;
	CFGNode branch;
	
	public ThenBranchNode()
	{
		
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
	
	public CFGNode getBranchHead()
	{
		return branch;
	}
	
	public void setBranchHead(CFGNode loopHead)
	{
		branch = loopHead;
	}
	
	@Override
	public String toString()
	{
		return "Then Branch of: " + branch;
	}
	
}
