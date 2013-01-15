package raykernel.apps.apidoc2.miner;

import java.util.Set;

import raykernel.lang.cfg.CFG;
import raykernel.lang.cfg.CFGNode;
import raykernel.lang.cfg.StatementNode;
import raykernel.lang.dom.naming.MethodSignature;
import raykernel.lang.dom.naming.Type;
import raykernel.lang.dom.statement.VariableDeclarationStatement;
import raykernel.lang.parse.ClassDeclaration;

import com.google.common.collect.Sets;

public class Util {

	static Set<Type> getTypesUsed(ClassDeclaration dec) {
		Set<Type> ret = Sets.newHashSet();

		for (MethodSignature method : dec.getMethods()) {
			ret.addAll(Util.getTypesUsed(method.getCFG()));
		}

		return ret;
	}

	static Set<Type> getTypesUsed(CFG cfg) {
		Set<Type> ret = Sets.newHashSet();

		for (CFGNode node : cfg) {
			if (node instanceof StatementNode) {
				raykernel.lang.dom.statement.Statement stmt = ((StatementNode) node)
						.getStatement();

				if (stmt instanceof VariableDeclarationStatement) {
					VariableDeclarationStatement vds = (VariableDeclarationStatement) stmt;
					ret.add(vds.getType());
				}
			}
		}

		return ret;
	}

}
