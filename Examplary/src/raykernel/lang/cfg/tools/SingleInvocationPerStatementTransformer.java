package raykernel.lang.cfg.tools;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import raykernel.lang.cfg.CFG;
import raykernel.lang.cfg.CFGNode;
import raykernel.lang.cfg.ElseBranchNode;
import raykernel.lang.cfg.EnhancedForNode;
import raykernel.lang.cfg.ExitLoopNode;
import raykernel.lang.cfg.ForNode;
import raykernel.lang.cfg.IfNode;
import raykernel.lang.cfg.StatementNode;
import raykernel.lang.cfg.ThenBranchNode;
import raykernel.lang.cfg.WhileNode;
import raykernel.lang.dom.expression.AssignmentExpression;
import raykernel.lang.dom.expression.Expression;
import raykernel.lang.dom.expression.InvocationExpression;
import raykernel.lang.dom.expression.NewExpression;
import raykernel.lang.dom.expression.UnknownExpressionException;
import raykernel.lang.dom.expression.Variable;
import raykernel.lang.dom.naming.MethodSignature;
import raykernel.lang.dom.naming.Type;
import raykernel.lang.dom.statement.Statement;
import raykernel.lang.dom.statement.VariableDeclarationStatement;
import raykernel.lang.parse.ClassDeclaration;
import raykernel.lang.parse.EclipseCFGParser;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;

public class SingleInvocationPerStatementTransformer {

	int counter = 0;

	private Variable makeVar() {
		return new Variable("var" + counter++);
	}

	public void apply(CFG inputCFG) {
		inputCFG.setEntry(processNode(inputCFG.getEntry()));
		// maybe also fix node list?
	}

	// only node that can have an invocation expression is a StatementNode
	// that has an expression statement containing an either an
	// invocationExpression
	// directly or and invocation on the right side of an assignment statement
	CFGNode processNode(CFGNode node) {
		if (node == null)
			return null;

		if (node instanceof StatementNode) {
			StatementNode sn = (StatementNode) node;
			List<VariableDeclarationStatement> newDecs = extractInvocations(sn.getStatement());

			if (newDecs.isEmpty()) {
				sn.setNext(processNode(sn.getSuccessor()));
				return sn;
			}

			StatementNode firstNode = null, prevNode = null;
			for (VariableDeclarationStatement vds : newDecs) {
				StatementNode newNode = new StatementNode(vds);
				if (firstNode == null) {
					firstNode = newNode;
				}
				else {
					if (prevNode != null) {
						prevNode.setNext(newNode);
					}
				}
				prevNode = newNode;
			}

			prevNode.setNext(sn);
			sn.setNext(processNode(sn.getSuccessor()));
			return firstNode;
		}
		else if (node instanceof IfNode) {
			IfNode in = (IfNode) node;
			in.setThenNode(processNode(in.getThenNode()));
			in.setElseNode(processNode(in.getElseNode()));
			in.setExitNode(processNode(in.getExitNode()));
			return in;
		}
		else if (node instanceof ThenBranchNode) {
			ThenBranchNode tbn = (ThenBranchNode) node;
			tbn.setNext(processNode(tbn.getNext()));
			return tbn;
		}
		else if (node instanceof ElseBranchNode) {
			ElseBranchNode ebn = (ElseBranchNode) node;
			ebn.setNext(processNode(ebn.getNext()));
			return ebn;
		}
		else if (node instanceof WhileNode) {
			WhileNode wn = (WhileNode) node;
			wn.setBodyNode(processNode(wn.getBodyNode()));
			wn.setExitNode(processNode(wn.getExitNode()));
			return wn;
		}
		else if (node instanceof ForNode) {
			ForNode fn = (ForNode) node;
			fn.setBodyNode(processNode(fn.getBodyNode()));
			fn.setExitNode(processNode(fn.getExitNode()));
			return fn;
		}
		else if (node instanceof EnhancedForNode) {
			EnhancedForNode fn = (EnhancedForNode) node;
			fn.setBodyNode(processNode(fn.getBodyNode()));
			fn.setExitNode(processNode(fn.getExitNode()));
			return fn;
		}
		else if (node instanceof ExitLoopNode) {
			ExitLoopNode eln = (ExitLoopNode) node;
			eln.setNext(processNode(eln.getNext()));
			return eln;
		}

		throw new IllegalStateException("Unknown node type: " + node + " " + node.getClass());
	}

	private List<VariableDeclarationStatement> extractInvocations(Statement stmt) {
		return Lists.newArrayList(Iterables.concat(Iterables.transform(stmt.getSubExpressions(),
				new Function<Expression, List<VariableDeclarationStatement>>() {
					public List<VariableDeclarationStatement> apply(Expression e) {
						return extractInvocations(e);
					}
				})));
	}

	/**
	 * Removes invocation expressions from the given expression and returns
	 * statements which declare the new variables
	 */
	private List<VariableDeclarationStatement> extractInvocations(Expression expr) {

		// System.out.println("Extracting invocations from: " + expr + " -- " +
		// expr.getClass());

		Set<Expression> invokes = getInvocationExpressionsIn(expr);

		// System.out.println("Invokes: " + invokes);

		if (invokes.isEmpty())
			return Collections.emptyList();

		// try to match with {InvokeExpr} or {Assign(Var, InvokeExpr)}
		if (invokes.size() == 1) {
			if (expr instanceof InvocationExpression || expr instanceof AssignmentExpression)
				return Collections.emptyList();
		}

		// otherwise substitute
		Variable newVar = makeVar();
		// get smallest
		Ordering<Expression> smallestToLargest = new Ordering<Expression>() {
			@Override
			public int compare(Expression e1, Expression e2) {
				return e1.toString().length() - e2.toString().length();
			}
		};
		Expression subExp = smallestToLargest.leastOf(invokes, 1).get(0);
		expr.substitute(subExp, newVar);

		// statement to declare the new variable
		List<VariableDeclarationStatement> ret = Lists.newArrayList();
		ret.add(new VariableDeclarationStatement(new Type("Object"), newVar, subExp));

		// recursive call to get any more
		// ret.addAll(extractInvocations(subExp));
		ret.addAll(extractInvocations(expr));

		return ret;
	}

	private Set<Expression> getInvocationExpressionsIn(final Expression expr) {
		return Sets.newHashSet(Iterables.filter(Expression.getAllSubExpressions(expr),
				new Predicate<Expression>() {
					public boolean apply(Expression e) {
						return (e instanceof InvocationExpression || e instanceof NewExpression)
								&& !e.equals(expr);
					}
				}));
	}

	//
	public static void main(String[] args) throws UnknownExpressionException {

		String testClassSrc =
				" public class Test { " +
						"   void fun() { " +
						"      Object a = foo(p.poof(new Something())); " +
						"       b = foo().bar(); " +
						"	   Object c = foo().bar().baz(); " +
						"      while (x > y) { " +
						"	     if( yada() ) { " +
						"         c = bin().bat(); " +
						"        } else { }" +
						"      }  " +
						"      d = arg().barg(new Something(fooberger()));  " +
						"    } " +
						"  } ";

		EclipseCFGParser parser = new EclipseCFGParser();
		List<ClassDeclaration> classes = parser.parse(testClassSrc);
		ClassDeclaration testClass = classes.get(0);
		MethodSignature method = testClass.getMethods().get(0);
		CFG fun = method.getCFG();
		System.out.println(fun);
		System.out.println("--------");
		SingleInvocationPerStatementTransformer trans =
				new SingleInvocationPerStatementTransformer();
		trans.apply(fun);
		System.out.println(fun);
	}
}
