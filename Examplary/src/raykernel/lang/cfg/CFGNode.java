package raykernel.lang.cfg;

import java.util.List;

public abstract class CFGNode
{
	public abstract List<CFGNode> getSuccessors();
	
	/*
	@Override
	public boolean equals(Object o)
	{
		return toString().equals(o.toString());
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	*/
}