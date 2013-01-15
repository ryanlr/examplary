package raykernel.lang.cfg;

import java.util.List;

import raykernel.lang.dom.condition.Condition;
import raykernel.util.Tools;

public class WhileNode extends CFGNode
{
	
	Condition condition;
	CFGNode bodyNode;
	CFGNode exitNode;
	
	public WhileNode(Condition condition)
	{
		this.condition = condition;
	}
	
	public void setBodyNode(CFGNode bodyNode)
	{
		this.bodyNode = bodyNode;
	}
	
	public void setExitNode(CFGNode exitNode)
	{
		this.exitNode = exitNode;
	}
	
	public CFGNode getBodyNode()
	{
		return bodyNode;
	}
	
	public CFGNode getExitNode()
	{
		return exitNode;
	}
	
	@Override
	public List<CFGNode> getSuccessors()
	{
		return Tools.makeList(bodyNode, exitNode);
	}
	
	@Override
	public String toString()
	{
		return "while( " + condition + " )";
	}
	
	public Condition getCondition()
	{
		return condition;
	}
	
}
