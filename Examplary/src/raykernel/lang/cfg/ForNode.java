package raykernel.lang.cfg;

import java.util.List;

import raykernel.lang.dom.condition.Condition;
import raykernel.util.Tools;

public class ForNode extends CFGNode {
	Condition condition;
	CFGNode bodyNode;
	CFGNode exitNode;

	public ForNode(Condition c) {
		condition = c;
	}

	public void setBodyNode(CFGNode bodyNode) {
		this.bodyNode = bodyNode;
	}

	public void setExitNode(CFGNode exitNode) {
		this.exitNode = exitNode;
	}

	public CFGNode getBodyNode() {
		return bodyNode;
	}

	public CFGNode getExitNode() {
		return exitNode;
	}

	@Override
	public List<CFGNode> getSuccessors() {
		return Tools.makeList(bodyNode, exitNode);
	}

	public Condition getCondition() {
		return condition;
	}

	@Override
	public String toString() {
		return "for( [inits]; " + condition + "; [updaters] )";
	}

}
