package raykernel.lang.cfg;

import java.util.List;

import raykernel.lang.dom.expression.Expression;
import raykernel.lang.dom.naming.Declaration;
import raykernel.util.Tools;

public class EnhancedForNode extends CFGNode
{
	
	Declaration dec;
	Expression exp;
	CFGNode bodyNode;
	CFGNode exitNode;
	
	public EnhancedForNode(Declaration dec, Expression exp)
	{
		this.dec = dec;
		this.exp = exp;
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
		return "for( " + dec + " : " + exp + " )";
	}
	
	public Declaration getDeclaration()
	{
		return dec;
	}
	
	public Expression getExpression()
	{
		return exp;
	}
	
}
