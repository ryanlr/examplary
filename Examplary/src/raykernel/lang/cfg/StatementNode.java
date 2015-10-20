package raykernel.lang.cfg;

import java.util.LinkedList;
import java.util.List;

import raykernel.lang.dom.statement.Statement;
import raykernel.util.Tools;

public class StatementNode extends CFGNode {
	Statement statement;
	CFGNode next;

	public StatementNode(Statement statement) {
		this.statement = statement;
	}

	@Override
	public List<CFGNode> getSuccessors() {
		if (next != null)
			return Tools.makeList(next);
		return new LinkedList<CFGNode>();
	}

	public void setNext(CFGNode next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return statement.toString();
	}

	public Statement getStatement() {
		return statement;
	}

	public CFGNode getSuccessor() {
		return next;
	}

	/*
	 * public boolean includesVariable(String varName) { for (int i = 0; i <
	 * statements.size(); i++) { Statement s = statements.get(i); if
	 * (s.getNodeType() == ASTNode.EXPRESSION_STATEMENT) { ExpressionStatement
	 * es = (ExpressionStatement) s; if (es.getExpression().getNodeType() ==
	 * Expression.ASSIGNMENT) { Assignment as = (Assignment) es.getExpression();
	 * if (as.getLeftHandSide().toString().trim().equals(varName)) return true;
	 * } } } return false; }
	 */

}
