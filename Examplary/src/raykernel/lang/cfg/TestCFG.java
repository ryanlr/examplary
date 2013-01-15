package raykernel.lang.cfg;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import raykernel.io.FileReader;
import raykernel.lang.dom.expression.UnknownExpressionException;

public class TestCFG
{
	public static void main(String[] args) throws UnknownExpressionException
	{
		String fName = args[0];
		
		String source = FileReader.readFile(new File(fName));
		
		CFGBuilder builder = new CFGBuilder();
		List<CFG> cfgs = builder.buildAll(source.toCharArray());
		
		for (CFG cfg : cfgs)
		{
			Set<CFGNode> unvisited = new HashSet<CFGNode>();
			Set<CFGNode> visited = new HashSet<CFGNode>();
			
			unvisited.add(cfg.getEntry());
			
			while (!unvisited.isEmpty())
			{
				CFGNode n = unvisited.iterator().next();
				unvisited.remove(n);
				visited.add(n);
				
				System.out.println("\nNew Node\n");
				
				System.out.println(n);
				
				for (CFGNode node : n.getSuccessors())
				{
					if (!visited.contains(node))
					{
						unvisited.add(node);
					}
				}
				
			}
		}
	}
}
