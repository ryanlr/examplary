package raykernel.lang.cfg;

import java.util.List;

import raykernel.util.Tools;

public class ElseBranchNode extends CFGNode
{
	CFGNode next;
	CFGNode branch;
	
	public ElseBranchNode()
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
		return "Else Branch of: " + branch;
	}
	
}
