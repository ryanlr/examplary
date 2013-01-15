package raykernel.lang.parse;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodExtractor extends ASTVisitor
{
	List<MethodDeclaration> methods = new LinkedList<MethodDeclaration>();
	
	public static List<MethodDeclaration> getMethods(ASTNode node)
	{
		MethodExtractor me = new MethodExtractor();
		
		node.accept(me);
		
		return me.methods;
	}
	
	@Override
	public boolean visit(MethodDeclaration md)
	{
		methods.add(md);
		return false;
	}
	
}
