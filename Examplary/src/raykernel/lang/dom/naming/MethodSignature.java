package raykernel.lang.dom.naming;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import raykernel.lang.cfg.CFG;
import raykernel.lang.parse.ClassDeclaration;

public class MethodSignature implements Serializable
{
	ClassDeclaration classDec;

	Type returnType;
	String methodName;
	List<Declaration> parameters = new LinkedList<Declaration>();

	CFG cfg;

	public MethodSignature(Type returnType, String methodName, List<Declaration> parameters)
	{
		this.returnType = returnType;
		this.methodName = methodName;
		this.parameters = parameters;
	}

	public void setCFG(CFG cfg)
	{
		this.cfg = cfg;
	}

	public void setClass(ClassDeclaration classDec)
	{
		this.classDec = classDec;
	}

	public CFG getCFG()
	{
		return cfg;
	}

	@Override
	public String toString()
	{
		return methodName;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classDec == null) ? 0 : classDec.hashCode());
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
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
		MethodSignature other = (MethodSignature) obj;
		if (classDec == null)
		{
			if (other.classDec != null)
				return false;
		}
		else if (!classDec.equals(other.classDec))
			return false;
		if (methodName == null)
		{
			if (other.methodName != null)
				return false;
		}
		else if (!methodName.equals(other.methodName))
			return false;
		if (parameters == null)
		{
			if (other.parameters != null)
				return false;
		}
		else if (!parameters.equals(other.parameters))
			return false;
		if (returnType == null)
		{
			if (other.returnType != null)
				return false;
		}
		else if (!returnType.equals(other.returnType))
			return false;
		return true;
	}

	public List<Declaration> getParameters() {
		return parameters;
	}

}
