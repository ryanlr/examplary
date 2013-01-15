package raykernel.lang.cfg;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Path implements Iterable<CFGNode>
{
	List<CFGNode> nodes;
	
	public Path(LinkedList<CFGNode> nodes)
	{
		this.nodes = (List<CFGNode>) nodes.clone();
	}
	
	public Path()
	{
		nodes = new LinkedList<CFGNode>();
	}
	
	@Override
	public String toString()
	{
		StringBuffer buff = new StringBuffer();
		
		buff.append("Path: \n");
		
		for (CFGNode node : nodes)
		{
			buff.append(node.toString() + "\n");
		}
		
		return buff.toString();
	}
	
	public boolean contains(CFGNode node)
	{
		return nodes.contains(node);
	}
	
	public Iterator<CFGNode> iterator()
	{
		return nodes.iterator();
	}
	
}
