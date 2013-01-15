package raykernel.lang.cfg;

import java.util.LinkedList;
import java.util.List;

import raykernel.lang.dom.condition.AndCondition;
import raykernel.lang.dom.condition.Condition;
import raykernel.lang.dom.expression.Expression;

public class SwitchNode extends CFGNode
{
	Expression exp;
	List<CFGNode> cases = new LinkedList<>();
	
	public SwitchNode(Expression exp)
	{
		this.exp = exp;
	}
	
	@Override
	public List<CFGNode> getSuccessors()
	{
		return cases;
	}
	
	public void addCase(SwitchToCaseNode stcn)
	{
		cases.add(stcn);
	}
	
	@Override
	public String toString()
	{
		return "switch( " + exp + ")";
	}
	
	public Expression getExpression()
	{
		return exp;
	}
	
	public Condition getDefaultCondition()
	{
		AndCondition ret = new AndCondition();
		
		for (CFGNode caseNode : cases)
		{
			SwitchToCaseNode stcn = (SwitchToCaseNode) caseNode;
			
			if (!stcn.isDefault())
			{
				ret.addCondition(stcn.getCondition().negated());
			}
		}
		
		return ret;
	}
	
}
