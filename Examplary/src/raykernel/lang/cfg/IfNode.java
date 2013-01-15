package raykernel.lang.cfg;

import java.util.List;

import raykernel.lang.dom.condition.Condition;
import raykernel.util.Tools;

public class IfNode extends CFGNode
{
	Condition condition;
	CFGNode thenNode;
	CFGNode elseNode;
	CFGNode exitNode;
	
	public IfNode(Condition condition)
	{
		this.condition = condition;
	}
	
	public void setThenNode(CFGNode thenNode)
	{
		this.thenNode = thenNode;
	}
	
	public void setElseNode(CFGNode elseNode)
	{
		this.elseNode = elseNode;
	}
	
	public void setExitNode(CFGNode exitNode)
	{
		this.exitNode = exitNode;
	}
	
	public CFGNode getThenNode()
	{
		return thenNode;
	}
	
	public CFGNode getElseNode()
	{
		return elseNode;
	}
	
	public CFGNode getExitNode()
	{
		return exitNode;
	}
	
	@Override
	public String toString()
	{
		return "if( " + condition + " )"; // { \n" + thenNode.toString() + "}\n" + "else { \n" + elseNode.toString() + "}\n";
	}
	
	@Override
	public List<CFGNode> getSuccessors()
	{
		return Tools.makeList(thenNode, elseNode);
	}
	
	public Condition getCondition()
	{
		return condition;
	}
	
}
