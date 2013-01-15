package raykernel.apps.apidoc2.model;

import static java.util.Collections.EMPTY_LIST;

import java.util.List;
import java.util.Set;

import raykernel.lang.cfg.CFG;
import raykernel.lang.cfg.CFGNode;
import raykernel.lang.cfg.StatementNode;
import raykernel.lang.cfg.tools.CFGFilterer;
import raykernel.lang.cfg.tools.SingleInvocationPerStatementTransformer;
import raykernel.lang.dom.expression.Variable;
import raykernel.lang.dom.naming.Declaration;
import raykernel.lang.dom.naming.MethodSignature;
import raykernel.lang.dom.naming.Type;
import raykernel.lang.dom.statement.Statement;
import raykernel.lang.dom.statement.VariableDeclarationStatement;
import raykernel.lang.parse.ClassDeclaration;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ConcreteUse {

	public static final Predicate<Object> NOTNULL = new Predicate<Object>() {
		public boolean apply(Object input) {
			return input != null;
		}
	};

	Declaration seed;
	List<CFG> cfgs = Lists.newArrayList();
	Set<Declaration> auxVars = Sets.newHashSet();

	public ConcreteUse(Declaration seed) {
		this.seed = seed;
	}

	public static List<ConcreteUse> extractUses(final Type type, ClassDeclaration classDec) {
		List<ConcreteUse> ret = Lists.newArrayList();

		// Get a list of use seeds
		// TODO: use ITypeBindings if they exist to get true types
		Set<Declaration> auxVarToType = getDeclaredVariables(classDec);

		// filter for use seeds (declarations matching type)
		Set<Declaration> seeds = Sets.filter(auxVarToType, new Predicate<Declaration>() {
			public boolean apply(Declaration input) {
				return input.getType().equals(type);
			}
		});

		if (seeds.isEmpty())
			return EMPTY_LIST;

		// For each seed, Collect all the information that goes with each use
		for (Declaration seed : seeds) {
			ConcreteUse use = new ConcreteUse(seed);
			use.addAuxVariables(auxVarToType);
			ret.add(use);

			// TODO: add ast for field inits

			for (MethodSignature sig : classDec.getMethods()) {
				CFG cfg = sig.getCFG();

				final Variable thisSeed = seed.getVariable();
				Predicate<CFGNode> relevantNodes = new Predicate<CFGNode>() {
					public boolean apply(CFGNode node) {
						if (node instanceof StatementNode) {
							Statement statement = ((StatementNode) node).getStatement();

							if (statement instanceof VariableDeclarationStatement) {
								if (((VariableDeclarationStatement) statement).getVariable()
										.equals(thisSeed))
									return true;
							}

							return statement.getAllSubExpressions().contains(thisSeed);
						}
						return false;
					}
				};

				// filter each method cfg
				// only statements relevant to var (
				CFGFilterer cfgFilterer = new CFGFilterer(relevantNodes);
				cfgFilterer.apply(cfg);

				// transform cfg so that each statement contains only one method
				// call
				SingleInvocationPerStatementTransformer singleInvokeTransformer = new SingleInvocationPerStatementTransformer();
				singleInvokeTransformer.apply(cfg);

				if (!cfg.isEmpty()) {
					use.addCFG(cfg);
				}
			}
		}
		return ret;
	}

	private static Set<Declaration> getDeclaredVariables(ClassDeclaration classDec) {
		Set<Declaration> ret = Sets.newHashSet();

		// Fields
		for (VariableDeclarationStatement field : classDec.getFields()) {
			ret.add(new Declaration(field.getType(), field.getVariable()));
		}

		// parameters and stuff in methods
		for (MethodSignature method : classDec.getMethods()) {
			ret.addAll(method.getParameters());

			Iterable<Declaration> decs = Iterables.filter(
					Iterables.transform(method.getCFG(),
							new Function<CFGNode, Declaration>() {
								public Declaration apply(CFGNode node) {
									if (node instanceof StatementNode) {
										Statement statement = ((StatementNode) node).getStatement();
										// Assuming decs cant be nested inside
										// some other stmt
										if (statement instanceof VariableDeclarationStatement) {
											VariableDeclarationStatement vds =
													((VariableDeclarationStatement) statement);
											return new Declaration(vds.getType(), vds.getVariable());
										}
									}
									return null;
								}
							}), NOTNULL);

			ret.addAll(Lists.newArrayList(decs));
		}
		return ret;
	}

	private void addCFG(CFG cfg) {
		cfgs.add(cfg);
	}

	private void addAuxVariables(Set<Declaration> auxVarToType) {
		auxVars.addAll(auxVarToType);
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Concrete Use of: " + seed + "\n");
		for (CFG cfg : cfgs) {
			buf.append(cfg.toString());
		}
		return buf.toString();
	}
}
