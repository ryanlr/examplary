package raykernel.lang.cfg;

import java.util.LinkedList;
import java.util.List;

/**
 * Enumerates all paths though a cfg using depth-first search
 * @author buse
 *
 */
public class CFGPathEnumerator
{
	List<Path> pathsFound;
	
	public CFGPathEnumerator()
	{
		
	}
	
	public List<Path> getPaths(CFG cfg)
	{
		pathsFound = new LinkedList<Path>();
		
		if (cfg.getEntry() == null)
			return pathsFound;
		
		LinkedList<CFGNode> visited = new LinkedList<CFGNode>();
		visited.add(cfg.getEntry());
		depthFirst(visited);
		
		return pathsFound;
	}
	
	private void depthFirst(LinkedList<CFGNode> visited)
	{
		List<CFGNode> nodes = visited.getLast().getSuccessors();
		
		//System.out.println("Examining sucessors of: " + visited.getLast() + " : " + visited.getLast().getSuccessors());
		
		// examine adjacent nodes
		for (CFGNode node : nodes)
		{
			if (visited.contains(node) || node == null)
			{
				continue;
			}
			if (node.getSuccessors().isEmpty())
			{
				visited.add(node);
				pathsFound.add(new Path(visited));
				visited.removeLast();
				//break;
			}
		}
		// recursion needs to come after visiting adjacent nodes
		for (CFGNode node : nodes)
		{
			if (node == null || visited.contains(node) || node.getSuccessors().isEmpty())
			{
				continue;
			}
			visited.addLast(node);
			depthFirst(visited);
			visited.removeLast();
		}
	}
}
