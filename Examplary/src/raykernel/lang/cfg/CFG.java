package raykernel.lang.cfg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import raykernel.lang.dom.condition.Condition;
import raykernel.lang.dom.expression.EclipseExpressionAdapter;
import raykernel.lang.dom.expression.Expression;
import raykernel.lang.dom.expression.UnknownExpressionException;
import raykernel.lang.dom.naming.Declaration;
import raykernel.lang.dom.statement.EclipseStatementAdapter;
import raykernel.lang.dom.statement.ReturnStatement;
import raykernel.lang.dom.statement.ThrowStatement;
import raykernel.util.Tools;

public class CFG implements Iterable<CFGNode> {
	CFGNode entry;
	MethodDeclaration methodSig;

	public static CFG buildCFG(MethodDeclaration repOk) throws UnknownExpressionException {
		CFG temp = new CFG();
		temp.createCFG(repOk);
		return temp;
	}

	private void createCFG(MethodDeclaration meth) throws UnknownExpressionException {
		methodSig = meth;
		Block body = meth.getBody();

		if (body == null)
			return;

		List<Statement> statements = body.statements();
		entry = makeNode(statements, null, null, null);
	}

	Map<Statement, CFGNode> stmtNodeMap = new HashMap<>();

	private CFGNode makeNode(List<Statement> statements, CFGNode returnTo, CFGNode loopExit,
			CFGNode switchHead) throws UnknownExpressionException {
		if (statements.isEmpty()) {
			if (returnTo != null)
				return returnTo;
			else
				return null;
		}

		Statement currentStmt = statements.get(0);

		// avoid duplicating
		CFGNode result = stmtNodeMap.get(currentStmt);
		if (result != null)
			return result;

		List<Statement> rest;

		if (statements.size() == 1) {
			rest = new LinkedList<Statement>();
		} else {
			rest = statements.subList(1, statements.size());
		}

		// System.out.println("Processing: " + currentStmt + " rest = " + rest);

		if (currentStmt.getNodeType() == ASTNode.LABELED_STATEMENT) {
			org.eclipse.jdt.core.dom.LabeledStatement lbledstmt = (org.eclipse.jdt.core.dom.LabeledStatement) currentStmt;

			currentStmt = lbledstmt.getBody();
		}

		if (currentStmt.getNodeType() == ASTNode.IF_STATEMENT) {
			IfStatement ifstmt = (IfStatement) currentStmt;

			IfNode ifnode = new IfNode(EclipseExpressionAdapter.translateCondition(ifstmt
					.getExpression()));

			CFGNode afterNode = makeNode(rest, returnTo, loopExit, switchHead);

			CFGNode thenBlockNode = makeNode(Tools.makeList(ifstmt.getThenStatement()), afterNode,
					loopExit, switchHead);
			ThenBranchNode thenNode = new ThenBranchNode();
			thenNode.setBranchHead(ifnode);
			thenNode.setNext(thenBlockNode);

			ElseBranchNode elseNode = new ElseBranchNode();
			elseNode.setBranchHead(ifnode);

			if (ifstmt.getElseStatement() != null) {
				CFGNode elseBlockNode = makeNode(Tools.makeList(ifstmt.getElseStatement()),
						afterNode, loopExit, switchHead);
				elseNode.setNext(elseBlockNode);
			} else {
				elseNode.setNext(afterNode);
			}

			ifnode.setExitNode(afterNode);
			ifnode.setThenNode(thenNode);
			ifnode.setElseNode(elseNode);

			stmtNodeMap.put(currentStmt, ifnode);
			return ifnode;
		}

		else if (currentStmt.getNodeType() == ASTNode.SWITCH_STATEMENT) {
			SwitchStatement switchStmt = (SwitchStatement) currentStmt;

			SwitchNode snode = new SwitchNode(EclipseExpressionAdapter.translate(switchStmt
					.getExpression()));

			CFGNode afterNode = makeNode(rest, returnTo, loopExit, switchHead);

			List<Statement> stmts = switchStmt.statements();

			makeNode(stmts, afterNode, afterNode, snode);

			stmtNodeMap.put(currentStmt, snode);
			return snode;
		}

		else if (currentStmt.getNodeType() == ASTNode.SWITCH_CASE) {
			SwitchCase switchCase = (SwitchCase) currentStmt;

			SwtichCaseNode cnode = new SwtichCaseNode(EclipseExpressionAdapter.translate(switchCase
					.getExpression()));

			SwitchNode parentNode = (SwitchNode) switchHead;

			SwitchToCaseNode stcn = new SwitchToCaseNode(parentNode, cnode);

			parentNode.addCase(stcn);

			CFGNode next = makeNode(rest, returnTo, loopExit, switchHead);
			cnode.setNext(next);

			stmtNodeMap.put(currentStmt, cnode);
			return cnode;
		}

		else if (currentStmt.getNodeType() == ASTNode.WHILE_STATEMENT) {
			WhileStatement whilestmt = (WhileStatement) currentStmt;

			WhileNode whilenode = new WhileNode(
					EclipseExpressionAdapter.translateCondition(whilestmt.getExpression()));

			CFGNode afterNode = makeNode(rest, returnTo, loopExit, switchHead);

			ExitLoopNode exitNode = new ExitLoopNode();
			exitNode.setLoopHead(whilenode);
			exitNode.setNext(afterNode);

			whilenode.setExitNode(exitNode);
			whilenode.setBodyNode(makeNode(Tools.makeList(whilestmt.getBody()), exitNode, exitNode,
					switchHead));

			stmtNodeMap.put(currentStmt, whilenode);
			return whilenode;
		}

		/**
		 * For our pruposes do and while can be treated the same
		 */
		else if (currentStmt.getNodeType() == ASTNode.DO_STATEMENT) {
			DoStatement dostmt = (DoStatement) currentStmt;

			WhileNode whilenode = new WhileNode(EclipseExpressionAdapter.translateCondition(dostmt
					.getExpression()));

			CFGNode afterNode = makeNode(rest, returnTo, loopExit, switchHead);

			ExitLoopNode exitNode = new ExitLoopNode();
			exitNode.setLoopHead(whilenode);
			exitNode.setNext(afterNode);

			whilenode.setExitNode(exitNode);
			whilenode.setBodyNode(makeNode(Tools.makeList(dostmt.getBody()), exitNode, exitNode,
					switchHead));

			stmtNodeMap.put(currentStmt, whilenode);
			return whilenode;
		}

		else if (currentStmt.getNodeType() == ASTNode.ENHANCED_FOR_STATEMENT) {
			org.eclipse.jdt.core.dom.EnhancedForStatement fstmt = (org.eclipse.jdt.core.dom.EnhancedForStatement) currentStmt;

			Expression exp = EclipseExpressionAdapter.translate(fstmt.getExpression());
			Declaration declar = EclipseExpressionAdapter
					.translateDeclaration(fstmt.getParameter());

			EnhancedForNode forNode = new EnhancedForNode(declar, exp);

			CFGNode afterNode = makeNode(rest, returnTo, loopExit, switchHead);

			ExitLoopNode exitNode = new ExitLoopNode();
			exitNode.setLoopHead(forNode);
			exitNode.setNext(afterNode);

			forNode.setExitNode(exitNode);
			forNode.setBodyNode(makeNode(Tools.makeList(fstmt.getBody()), exitNode, exitNode,
					switchHead));

			stmtNodeMap.put(currentStmt, forNode);
			return forNode;

		}

		else if (currentStmt.getNodeType() == ASTNode.FOR_STATEMENT) {
			org.eclipse.jdt.core.dom.ForStatement fstmt = (org.eclipse.jdt.core.dom.ForStatement) currentStmt;

			Condition c = EclipseExpressionAdapter.translateCondition(fstmt.getExpression());

			// TODO: ignoring these for now
			// fstmt.initializers();
			// fstmt.updaters();

			ForNode forNode = new ForNode(c);

			CFGNode afterNode = makeNode(rest, returnTo, loopExit, switchHead);

			ExitLoopNode exitNode = new ExitLoopNode();
			exitNode.setLoopHead(forNode);
			exitNode.setNext(afterNode);

			forNode.setExitNode(exitNode);
			forNode.setBodyNode(makeNode(Tools.makeList(fstmt.getBody()), exitNode, loopExit,
					switchHead));

			stmtNodeMap.put(currentStmt, forNode);
			return forNode;

		}

		// need to exit current if block or loop block
		else if (currentStmt.getNodeType() == ASTNode.BREAK_STATEMENT
				|| currentStmt.getNodeType() == ASTNode.CONTINUE_STATEMENT) {
			BreakNode breaknode = new BreakNode(loopExit);

			makeNode(rest, returnTo, loopExit, switchHead);

			stmtNodeMap.put(currentStmt, breaknode);
			return breaknode;
		} else if (currentStmt.getNodeType() == ASTNode.TRY_STATEMENT) {
			TryStatement trystmt = (TryStatement) currentStmt;

			List<Statement> trystatements = new LinkedList<Statement>();

			trystatements.addAll(trystmt.getBody().statements());

			if (trystmt.getFinally() != null) {
				trystatements.addAll(trystmt.getFinally().statements());
			}

			CFGNode afterNode = makeNode(rest, returnTo, loopExit, switchHead);
			CFGNode trybody = makeNode(trystatements, afterNode, loopExit, switchHead);

			stmtNodeMap.put(currentStmt, trybody);
			return trybody;
		}

		else if (currentStmt.getNodeType() == ASTNode.BLOCK) {
			Block block = (Block) currentStmt;
			CFGNode blockNode = makeNode(block.statements(), returnTo, loopExit, switchHead);

			stmtNodeMap.put(currentStmt, blockNode);
			return blockNode;
		}

		// some other statement type

		List<raykernel.lang.dom.statement.Statement> stmts = EclipseStatementAdapter
				.translateSafe(currentStmt);

		int charIndex = currentStmt.getStartPosition();

		// System.out.println("Start Position for " + currentStmt + " = " +
		// charIndex);

		if (stmts.isEmpty())
			return makeNode(rest, returnTo, loopExit, switchHead);

		StatementNode prevNode = null;
		StatementNode stmtnode = null;

		for (raykernel.lang.dom.statement.Statement stmt : stmts) {
			/**
			 * need something like
			 * 
			 * if stmt contains conditional expression, de sugar: add if stmt
			 * and each possibility below it
			 */

			stmt.setCharIndex(charIndex);
			stmtnode = new StatementNode(stmt);

			if (prevNode != null) {
				prevNode.setNext(stmtnode);
			}

			prevNode = stmtnode;
		}

		if (stmtnode.statement instanceof ReturnStatement
				|| stmtnode.statement instanceof ThrowStatement) {
			stmtnode.setNext(null);
		} else {
			stmtnode.setNext(makeNode(rest, returnTo, loopExit, switchHead));
		}

		stmtNodeMap.put(currentStmt, stmtnode);
		return stmtnode;
	}

	public CFGNode getEntry() {
		return entry;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();

		for (CFGNode node : this) {
			buf.append(node + "\n");
		}

		return buf.toString();
	}

	public MethodDeclaration getMethodSig() {
		return methodSig;
	}

	public void setEntry(CFGNode entry) {
		this.entry = entry;
	}

	public boolean isEmpty() {
		return entry == null;
	}

	@Override
	public Iterator<CFGNode> iterator() {
		Set<CFGNode> unvisited = new HashSet<CFGNode>();
		Set<CFGNode> visited = new HashSet<CFGNode>();

		unvisited.add(getEntry());

		while (!unvisited.isEmpty()) {
			CFGNode n = unvisited.iterator().next();

			if (n == null) {
				unvisited.remove(n);
				continue;
			}

			unvisited.remove(n);
			visited.add(n);

			for (CFGNode node : n.getSuccessors()) {
				if (node != null && !visited.contains(node)) {
					unvisited.add(node);
				}
			}

		}
		return visited.iterator();
	}

}