package raykernel.lang.dom.statement;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import raykernel.lang.dom.expression.EclipseExpressionAdapter;
import raykernel.lang.dom.expression.Expression;
import raykernel.lang.dom.expression.UnknownExpressionException;
import raykernel.lang.dom.expression.Variable;
import raykernel.lang.dom.naming.Type;

public class EclipseStatementAdapter {
	public static List<Statement> translateSafe(org.eclipse.jdt.core.dom.Statement stmt) {
		List<Statement> ret;
		try {
			ret = translate(stmt);
			return ret;
		} catch (UnknownExpressionException e) {
			System.out.println("WARNING. IGNORING: " + e);
			return new LinkedList<Statement>();
		}
	}

	private static List<Statement> translate(org.eclipse.jdt.core.dom.Statement stmt)
			throws UnknownExpressionException {

		// System.out.println("Translating stmt: " + stmt + " -- " +
		// stmt.getClass());

		List<Statement> stmtList = new LinkedList<Statement>();

		if (stmt instanceof org.eclipse.jdt.core.dom.ReturnStatement) {
			org.eclipse.jdt.core.dom.ReturnStatement retstmt = (org.eclipse.jdt.core.dom.ReturnStatement) stmt;

			ReturnStatement rt = new ReturnStatement(EclipseExpressionAdapter.translate(retstmt
					.getExpression()));

			stmtList.add(rt);
		} else if (stmt instanceof org.eclipse.jdt.core.dom.SuperConstructorInvocation) {
			org.eclipse.jdt.core.dom.SuperConstructorInvocation sci = (org.eclipse.jdt.core.dom.SuperConstructorInvocation) stmt;

			// TODO: handle super constructor invocations
		} else if (stmt instanceof org.eclipse.jdt.core.dom.SynchronizedStatement) {
			org.eclipse.jdt.core.dom.SynchronizedStatement sync = (org.eclipse.jdt.core.dom.SynchronizedStatement) stmt;

			List<org.eclipse.jdt.core.dom.Statement> body = sync.getBody().statements();

			for (org.eclipse.jdt.core.dom.Statement s : body) {
				stmtList.addAll(translate(s));
			}
		} else if (stmt instanceof org.eclipse.jdt.core.dom.ThrowStatement) {
			org.eclipse.jdt.core.dom.ThrowStatement retstmt = (org.eclipse.jdt.core.dom.ThrowStatement) stmt;

			ThrowStatement rt = new ThrowStatement(EclipseExpressionAdapter.translate(retstmt
					.getExpression()));

			stmtList.add(rt);
		} else if (stmt instanceof org.eclipse.jdt.core.dom.ExpressionStatement) {

			Expression e = EclipseExpressionAdapter
					.translate(((org.eclipse.jdt.core.dom.ExpressionStatement) stmt)
							.getExpression());

			ExpressionStatement es = new ExpressionStatement(e);

			stmtList.add(es);
		} else if (stmt instanceof org.eclipse.jdt.core.dom.VariableDeclarationStatement) {
			org.eclipse.jdt.core.dom.VariableDeclarationStatement vdstmt = (org.eclipse.jdt.core.dom.VariableDeclarationStatement) stmt;

			Type type = EclipseExpressionAdapter.translateType(vdstmt.getType());

			List<VariableDeclarationFragment> frags = vdstmt.fragments();

			for (VariableDeclarationFragment frag : frags) {
				Variable var = new Variable(frag.getName().toString());

				VariableDeclarationStatement ret;

				if (frag.getInitializer() == null) {
					ret = new VariableDeclarationStatement(type, var);
				} else {
					ret = new VariableDeclarationStatement(type, var,
							EclipseExpressionAdapter.translate(frag.getInitializer()));
				}

				stmtList.add(ret);
			}
		} else if (stmt instanceof org.eclipse.jdt.core.dom.LabeledStatement) {
			org.eclipse.jdt.core.dom.LabeledStatement lbledstmt = (org.eclipse.jdt.core.dom.LabeledStatement) stmt;

			stmtList.addAll(translate(lbledstmt.getBody()));
		} else if (stmt instanceof org.eclipse.jdt.core.dom.EmptyStatement) {
			// ignore these
		} else
			throw new UnknownExpressionException(stmt);

		return stmtList;

	}

}
