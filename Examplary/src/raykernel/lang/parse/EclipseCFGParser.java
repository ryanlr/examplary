package raykernel.lang.parse;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import raykernel.lang.cfg.CFG;
import raykernel.lang.dom.expression.EclipseExpressionAdapter;
import raykernel.lang.dom.expression.Expression;
import raykernel.lang.dom.expression.UnknownExpressionException;
import raykernel.lang.dom.expression.Variable;
import raykernel.lang.dom.naming.Declaration;
import raykernel.lang.dom.naming.MethodSignature;
import raykernel.lang.dom.naming.Type;
import raykernel.lang.dom.statement.VariableDeclarationStatement;

public class EclipseCFGParser {
	public List<ClassDeclaration> parse(String source)
			throws UnknownExpressionException {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(source.toCharArray());
		CompilationUnit unit = (CompilationUnit) parser.createAST(null);
		List<TypeDeclaration> types = TypeDecExtractor.getTypeDefs(unit);

		List<ClassDeclaration> ret = new LinkedList<ClassDeclaration>();

		for (TypeDeclaration type : types) {
			ClassDeclaration cd = translateTypeDec(type);

			for (MethodDeclaration method : MethodExtractor.getMethods(type)) {
				MethodSignature methodSig = translateMethodDec(method);
				CFG cfg = CFG.buildCFG(method);

				// **** Printing DOT ****
				// CFGToDot toDot = new CFGToDot();
				// String dot = toDot.toDot(cfg);
				// System.out.println(dot);

				methodSig.setCFG(cfg);
				cd.addMethod(methodSig);
			}

			for (FieldDeclaration fd : type.getFields()) {
				Type t = new Type(fd.getType().toString());

				for (Object o : fd.fragments()) {
					VariableDeclarationFragment frag = (VariableDeclarationFragment) o;
					Variable var = new Variable(frag.getName().toString());

					Expression init = EclipseExpressionAdapter.translate(frag
							.getInitializer());
					VariableDeclarationStatement vs = new VariableDeclarationStatement(
							t, var, init);
					cd.addFieldDec(vs);
				}
			}
			ret.add(cd);
		}

		return ret;
	}

	private MethodSignature translateMethodDec(MethodDeclaration method) {
		String name = method.getName().toString();
		Type retType = null;
		if (method.getReturnType2() != null) {
			retType = new Type(method.getReturnType2().toString());
		}
		List<Declaration> parameterList = new LinkedList<Declaration>();

		for (Object o : method.parameters()) {
			SingleVariableDeclaration dec = (SingleVariableDeclaration) o;
			Variable param = new Variable(dec.getName().toString());
			Type paramType = new Type(dec.getType().toString());
			parameterList.add(new Declaration(paramType, param));
		}

		return new MethodSignature(retType, name, parameterList);
	}

	private ClassDeclaration translateTypeDec(TypeDeclaration td) {
		Type t = new Type(td.getName().toString());
		ClassDeclaration cd = new ClassDeclaration(t);

		return cd;
	}
}
