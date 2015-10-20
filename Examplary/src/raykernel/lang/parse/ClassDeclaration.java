package raykernel.lang.parse;

import java.util.LinkedList;
import java.util.List;

import raykernel.lang.dom.naming.MethodSignature;
import raykernel.lang.dom.naming.Type;
import raykernel.lang.dom.statement.VariableDeclarationStatement;

public class ClassDeclaration
{
	Type classname;
	
	List<VariableDeclarationStatement> fields = new LinkedList<VariableDeclarationStatement>();
	
	List<MethodSignature> methods = new LinkedList<MethodSignature>();
	
	public ClassDeclaration(Type name)
	{
		classname = name;
	}
	
	public void addMethod(MethodSignature methodSig)
	{
		methodSig.setClass(this);
		methods.add(methodSig);
	}
	
	public void addFieldDec(VariableDeclarationStatement dec)
	{
		fields.add(dec);
	}
	
	public List<MethodSignature> getMethods()
	{
		return methods;
	}
	
	public Type getType()
	{
		return classname;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classname == null) ? 0 : classname.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassDeclaration other = (ClassDeclaration) obj;
		if (classname == null)
		{
			if (other.classname != null)
				return false;
		}
		else if (!classname.equals(other.classname))
			return false;
		return true;
	}
	
	public List<VariableDeclarationStatement> getFields()
	{
		return fields;
	}
	
}
