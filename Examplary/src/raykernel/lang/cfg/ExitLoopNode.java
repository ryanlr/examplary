package raykernel.lang.cfg;

import java.util.List;

import raykernel.util.Tools;

public class ExitLoopNode extends CFGNode {
	CFGNode next;
	CFGNode loopHead;

	public ExitLoopNode() {

	}

	@Override
	public List<CFGNode> getSuccessors() {
		return Tools.makeList(next);
	}

	public CFGNode getNext() {
		return next;
	}

	public void setNext(CFGNode next) {
		this.next = next;
	}

	public CFGNode getLoopHead() {
		return loopHead;
	}

	public void setLoopHead(CFGNode loopHead) {
		this.loopHead = loopHead;
	}

	@Override
	public String toString() {
		return "Exit Loop: " + loopHead;
	}

	@Override
	public CFGNode clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
