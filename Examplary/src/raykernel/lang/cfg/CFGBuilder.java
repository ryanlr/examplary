package raykernel.lang.cfg;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import raykernel.lang.dom.expression.UnknownExpressionException;
import raykernel.lang.parse.MethodExtractor;

public class CFGBuilder
{
	
	private CompilationUnit unit;
	
	public List<CFG> buildAll(char[] source) throws UnknownExpressionException
	{
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(source);
		unit = (CompilationUnit) parser.createAST(null);
		
		// get each method
		List<CFG> cfgs = new LinkedList<CFG>();
		for (MethodDeclaration method : MethodExtractor.getMethods(unit))
		{
			cfgs.add(build(method));
		}
		
		return cfgs;
	}
	
	private CFG build(MethodDeclaration method) throws UnknownExpressionException
	{
		CFG cfg = CFG.buildCFG(method);
		return cfg;
	}
	
}