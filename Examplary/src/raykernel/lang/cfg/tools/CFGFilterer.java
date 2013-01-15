package raykernel.lang.cfg.tools;

import java.util.List;

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
import raykernel.lang.dom.expression.UnknownExpressionException;
import raykernel.lang.dom.expression.Variable;
import raykernel.lang.dom.naming.MethodSignature;
import raykernel.lang.dom.statement.Statement;
import raykernel.lang.parse.ClassDeclaration;
import raykernel.lang.parse.EclipseCFGParser;

import com.google.common.base.Predicate;

public class CFGFilterer {

	Predicate<CFGNode> nodesToKeep;

	public CFGFilterer(Predicate<CFGNode> nodesToKeep) {
		this.nodesToKeep = nodesToKeep;
	}

	public void apply(CFG inputCFG) {
		inputCFG.setEntry(processNode(inputCFG.getEntry()));
		// maybe also fix node list?
	}

	CFGNode processNode(CFGNode node) {

		// cache results

		if (node == null)
			return null;

		if (node instanceof StatementNode) {
			StatementNode sn = (StatementNode) node;
			if (nodesToKeep.apply(sn)) {
				sn.setNext(processNode(sn.getSuccessor()));
				return sn;
			} else
				return processNode(sn.getSuccessor());
		} else if (node instanceof WhileNode) {
			WhileNode wn = (WhileNode) node;
			CFGNode processedBody = processNode(wn.getBodyNode());
			CFGNode processedSucessor = processNode(wn.getExitNode());
			if (processedBody instanceof ExitLoopNode && processedBody == processedSucessor)
				return ((ExitLoopNode) processedBody).getNext();
			wn.setBodyNode(processedBody);
			wn.setExitNode(processedSucessor);
			return wn;
		} else if (node instanceof ForNode) {
			ForNode fn = (ForNode) node;
			CFGNode processedBody = processNode(fn.getBodyNode());
			CFGNode processedSucessor = processNode(fn.getExitNode());
			if (processedBody instanceof ExitLoopNode && processedBody == processedSucessor)
				return ((ExitLoopNode) processedBody).getNext();
			fn.setBodyNode(processedBody);
			fn.setExitNode(processedSucessor);
			return fn;
		} else if (node instanceof EnhancedForNode) {
			EnhancedForNode fn = (EnhancedForNode) node;
			CFGNode processedBody = processNode(fn.getBodyNode());
			CFGNode processedSucessor = processNode(fn.getExitNode());
			if (processedBody instanceof ExitLoopNode && processedBody == processedSucessor)
				return ((ExitLoopNode) processedBody).getNext();
			fn.setBodyNode(processedBody);
			fn.setExitNode(processedSucessor);
			return fn;
		}
		else if (node instanceof IfNode) {
			IfNode in = (IfNode) node;
			ThenBranchNode processedThen = (ThenBranchNode) processNode(in.getThenNode());
			ElseBranchNode processedElse = (ElseBranchNode) processNode(in.getElseNode());
			CFGNode processedExit = processNode(in.getExitNode());
			if (processedThen == null || processedThen.getNext() == null)
				return processedExit;

			if (processedThen.getNext().equals(processedExit)
					&& (processedElse == null || processedElse.getNext().equals(processedExit)))
				return processedExit;
			in.setThenNode(processedThen);
			in.setElseNode(processedElse);
			in.setExitNode(processedExit);
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
		} else if (node instanceof ExitLoopNode) {
			ExitLoopNode eln = (ExitLoopNode) node;
			eln.setNext(processNode(eln.getNext()));
			return eln;
		}

		throw new IllegalStateException("Unknown node type: " + node + " " + node.getClass());
	}

	public static void main(String[] args) throws UnknownExpressionException {

		String testClassSrc =
				" public class Test { " +
						"   void fun() { " +
						"        Var theVar = new Var();          " +
						"      Object a = foo(); " +
						"       b = foo().bar(); " +
						"	   Object c = theVar.baz(); " +
						"      while (x > y) { " +
						"	     if( yada() ) { " +
						"         c = frm.bat(); " +
						"        } else { }" +
						"      }  " +
						"       while(yada.hasNext()) " +
						"         d = barg(theVar);  " +
						"    } " +
						"  } ";

		EclipseCFGParser parser = new EclipseCFGParser();
		List<ClassDeclaration> classes = parser.parse(testClassSrc);
		ClassDeclaration testClass = classes.get(0);
		MethodSignature method = testClass.getMethods().get(0);
		CFG fun = method.getCFG();
		System.out.println(fun);
		System.out.println("--------");

		final Variable thisSeed = new Variable("theVar");
		Predicate<CFGNode> relevantNodes = new Predicate<CFGNode>() {
			public boolean apply(CFGNode node) {
				if (node instanceof StatementNode) {
					Statement statement = ((StatementNode) node).getStatement();
					return statement.getAllSubExpressions().contains(thisSeed);
				}
				return false;
			}
		};

		// filter each method cfg
		// only statements relevant to var (
		CFGFilterer filter = new CFGFilterer(relevantNodes);
		filter.apply(fun);
		System.out.println(fun);
	}
}
