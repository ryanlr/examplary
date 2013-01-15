package raykernel.lang.cfg;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

public class CFGToDot
{
	public String toDot(CFG graph)
	{
		if (graph.getEntry() == null)
			return "Empty Method: " + graph.getMethodSig().getName();
		
		StringBuffer br = new StringBuffer();
		
		br.append("digraph " + graph.getMethodSig().getName() + " {\n");
		
		CFGNode current = graph.getEntry();
		
		br.append(printTransitions(current));
		
		br.append("\n}\n");
		
		return br.toString();
		
	}
	
	private final Set<CFGNode> printed = new HashSet<CFGNode>();
	
	private String printTransitions(CFGNode current)
	{
		printed.add(current);
		
		//System.out.println("Sucessors of " + current + " = " + current.getSuccessors());
		
		StringBuffer br = new StringBuffer();
		
		for (CFGNode child : current.getSuccessors())
		{
			if (child == null)
			{
				continue;
			}
			
			br.append(escapeNode(current) + " -> " + escapeNode(child) + ";\n");
			
			if (!printed.contains(child))
			{
				br.append(printTransitions(child));
			}
		}
		
		return br.toString();
	}
	
	private String escapeNode(CFGNode input)
	{
		return "\"" + StringEscapeUtils.escapeJava(input.toString()) + "\"";
	}
	
}
