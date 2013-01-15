package raykernel.lang.cfg;

import java.util.List;

import raykernel.lang.dom.condition.Condition;
import raykernel.lang.dom.expression.ComparasonExpression;
import raykernel.util.Tools;

public class SwitchToCaseNode extends CFGNode
{
	SwitchNode switchNode;
	SwtichCaseNode caseNode;
	
	public SwitchToCaseNode(SwitchNode switchNode, SwtichCaseNode caseNode)
	{
		this.switchNode = switchNode;
		this.caseNode = caseNode;
	}
	
	@Override
	public List<CFGNode> getSuccessors()
	{
		return Tools.makeList((CFGNode) caseNode);
	}
	
	public Condition getCondition()
	{
		if (this.isDefault())
			return switchNode.getDefaultCondition();
		return new ComparasonExpression(switchNode.getExpression(), caseNode.getExpression(), "==");
	}
	
	@Override
	public String toString()
	{
		if (caseNode.isDeafult())
			return "Jump to case: " + switchNode.getDefaultCondition();
		
		return "Jump to case: " + getCondition();
	}
	
	public boolean isDefault()
	{
		return caseNode.isDeafult();
	}
	
}
