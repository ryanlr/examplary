package raykernel.lang.cfg;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import raykernel.lang.dom.condition.Condition;
import raykernel.lang.dom.expression.AssignmentExpression;
import raykernel.lang.dom.expression.Expression;
import raykernel.lang.dom.expression.PostfixExpression;
import raykernel.lang.dom.expression.PrefixExpression;
import raykernel.lang.dom.naming.Declaration;
import raykernel.lang.dom.statement.ExpressionStatement;
import raykernel.lang.dom.statement.Statement;
import raykernel.lang.dom.statement.VariableDeclarationStatement;
import raykernel.util.Tuple;

public class Symbexe
{
	CFGPathEnumerator pathEnum = new CFGPathEnumerator();
	
	//Paths we have executed
	List<Path> pathsExecuted = new LinkedList<Path>();
	
	//Primary result we are looking for
	//State of the reg file just after the current node on the current path
	Map<Tuple<Path, CFGNode>, FlowSet> flowSetMap = new HashMap<Tuple<Path, CFGNode>, FlowSet>();
	
	/**
	 * Enumerate all paths through the graph, then execute each one (associate with each a symbolic flow set and a path predicate)
	 * @param cfg
	 */
	public void execute(CFG cfg)
	{
		List<Path> paths = pathEnum.getPaths(cfg);
		
		for (Path p : paths)
		{
			//System.out.println(p);
			
			executePath(p);
		}
	}
	
	private void executePath(Path path)
	{
		//create empty flow set
		FlowSet flowset = new FlowSet();
		
		//execute each statment getting a new flowset for each
		//and passing forward the last flow set
		for (CFGNode node : path)
		{
			//record for each stmt the flowset for just BEFORE the node is executed
			flowSetMap.put(new Tuple<Path, CFGNode>(path, node), flowset);
			
			flowset = executeNode(node, path, flowset);
		}
		
		pathsExecuted.add(path);
	}
	
	/**
	 * This is where the symbolic execution really happens
	 * @param node
	 * @param path 
	 * @param in_flowset
	 * @return
	 */
	private FlowSet executeNode(CFGNode node, Path path, FlowSet in_flowset)
	{
		//System.out.println("Executing Node: " + node);
		//System.out.println("in flowset : " + in_flowset);
		
		FlowSet out_flowset = in_flowset.clone();
		
		// contains an assignment? update symbolic reg file
		
		if (node instanceof StatementNode)
		{
			Statement stmt = ((StatementNode) node).getStatement();
			
			if (stmt instanceof ExpressionStatement)
			{
				Expression e = ((ExpressionStatement) stmt).getExpression();
				
				if (e instanceof AssignmentExpression)
				{
					out_flowset.addAssignment(((AssignmentExpression) e).getVariable(),
							((AssignmentExpression) e).getExpression());
					
					out_flowset.removeConditionsWith(((AssignmentExpression) e).getVariable());
				}
				else if (e instanceof PostfixExpression)
				{
					//					Expression var = ((PostfixExpression) e).getExpression();
					//					
					//					System.out.println("Write to : " + var + " type= " + var.getClass());
					//					
					//					if (var instanceof Variable)
					//					{
					//						out_flowset.clearVariable((Variable) var);
					//					}
					//					
					//out_flowset.removeConditionsWith(((PostfixExpression) e).getExpression());
				}
				else if (e instanceof PrefixExpression)
				{
					//					Expression var = ((PrefixExpression) e).getExpression();
					//					
					//					if (var instanceof Variable)
					//					{
					//						out_flowset.clearVariable((Variable) var);
					//					}
					//					
					//out_flowset.removeConditionsWith(((PrefixExpression) e).getExpression());
				}
			}
			else if (stmt instanceof VariableDeclarationStatement)
			{
				VariableDeclarationStatement dec = (VariableDeclarationStatement) stmt;
				
				out_flowset.addDeclairation(dec.getType(), dec.getVariable());
				if (((VariableDeclarationStatement) stmt).hasInit())
				{
					AssignmentExpression assign = dec.getInitExpression();
					
					if (assign.getExpression() != null)
					{
						out_flowset.addAssignment(dec.getVariable(), assign.getExpression());
					}
				}
			}
		}
		
		// branch stmt? add condition
		else if (node instanceof ThenBranchNode)
		{
			ThenBranchNode ifnode = (ThenBranchNode) node;
			
			Condition c = ((IfNode) ifnode.getBranchHead()).getCondition();
			
			out_flowset.addCondition(c);
		}
		else if (node instanceof ElseBranchNode)
		{
			ElseBranchNode ifnode = (ElseBranchNode) node;
			
			Condition c = ((IfNode) ifnode.getBranchHead()).getCondition();
			
			out_flowset.addCondition(c.negated());
		}
		
		//switch
		else if (node instanceof SwitchToCaseNode)
		{
			SwitchToCaseNode stcn = (SwitchToCaseNode) node;
			
			Condition c = stcn.getCondition();
			
			out_flowset.addCondition(c);
		}
		
		// entering loop body? add loop condition
		else if (node instanceof WhileNode)
		{
			WhileNode wnode = (WhileNode) node;
			
			Condition c = wnode.getCondition();
			
			out_flowset.addCondition(c);
		}
		
		//entering a for?  add codition  TODO: updaters/inits
		else if (node instanceof ForNode)
		{
			ForNode fnode = (ForNode) node;
			
			Condition c = fnode.getCondition();
			
			out_flowset.addCondition(c);
		}
		
		//entering enhanced for?  add assignment
		else if (node instanceof EnhancedForNode)
		{
			EnhancedForNode fnode = (EnhancedForNode) node;
			
			Declaration d = fnode.getDeclaration();
			Expression e = fnode.getExpression();
			
			out_flowset.addAssignment(d.getVariable(), new ElementInExpression(e));
		}
		
		// leaving loop body?
		else if (node instanceof ExitLoopNode)
		{
			ExitLoopNode exode = (ExitLoopNode) node;
			
			CFGNode n = exode.getLoopHead();
			
			if (n instanceof WhileNode)
			{
				Condition loopCondition = ((WhileNode) n).getCondition();
				
				if (loopCondition != null)
				{
					out_flowset.removeCondition(loopCondition);
					//out_flowset.addCondition(loopCondition.negated());
				}
			}
			else if (n instanceof ForNode)
			{
				Condition loopCondition = ((ForNode) n).getCondition();
				
				if (loopCondition != null)
				{
					out_flowset.removeCondition(loopCondition);
					//out_flowset.addCondition(loopCondition.negated());
				}
			}
			else if (n instanceof EnhancedForNode)
			{
				EnhancedForNode fnode = (EnhancedForNode) n;
				
				Declaration d = fnode.getDeclaration();
				
				out_flowset.clearVariable(d.getVariable());
			}
		}
		
		//System.out.println("Out flowset: " + out_flowset);
		
		return out_flowset;
	}
	
	/**
	 * How to access the results of the execution
	 * @param node
	 * @return
	 */
	public List<FlowSet> getFlowSetsFor(CFGNode node)
	{
		List<FlowSet> flowsets = new LinkedList<FlowSet>();
		
		for (Path p : pathsExecuted)
		{
			if (!p.contains(node))
			{
				continue;
			}
			
			flowsets.add(flowSetMap.get(new Tuple<Path, CFGNode>(p, node)));
			
		}
		return flowsets;
	}
	
}
