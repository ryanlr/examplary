package raykernel.lang.parse;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeDecExtractor extends ASTVisitor
{
	List<TypeDeclaration> classes = new LinkedList<TypeDeclaration>();
	
	public static List<TypeDeclaration> getTypeDefs(ASTNode node)
	{
		TypeDecExtractor me = new TypeDecExtractor();
		
		node.accept(me);
		
		return me.classes;
	}
	
	@Override
	public boolean visit(TypeDeclaration td)
	{
		classes.add(td);
		return false;
	}
	
}
