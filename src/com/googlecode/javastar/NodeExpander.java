/*******************************************************************************
 *    Copyright (c) 2012 Steven Roose
 *    This file is licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/

package com.googlecode.javastar;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is where most calculations are performed.
 * 
 * @author 	Steven Roose
 * @license	Apache License, Version 2.0
 *
 */
public abstract class NodeExpander<N extends Node, C extends Cost<C>> {
	
	private N startNode;
	private N goalNode;
	
	/**
	 * Create a new NodeExpander object with a start and a goal node given.
	 * 
	 * @param	startNode
	 * 			the start node
	 * @param	goalNode
	 * 			the goal node
	 */
	public NodeExpander(N startNode, N goalNode) {
		this.startNode = startNode;
		this.goalNode = goalNode;
	}
	
	/**
	 * Get all nodes that can be expanded from <code>node</code>.
	 *  
	 * @param 	node
	 * 			the node to expand
	 * 
	 * @return	the expansion of <code>node</code>
	 */
	public abstract Set<N> expand(N node);
	
	/**
	 * Calculate the heuristic value of <code>node</code>.
	 * 
	 * @param 	node
	 * 			the node to calculate the heuristic value from
	 * 
	 * @return	the heuristic value of <code>node</code>
	 */
	public abstract C calculateHeuristic(N node);
	
	/**
	 * Calculate the cost to move from node <code>from</code> to 
	 * adjacent node <code>to</code>.
	 * <br />
	 * Throws an exception if the two nodes are not adjacent.
	 * 
	 * @param 	from
	 * 			the "from" node
	 * @param 	to
	 * 			the "to" node
	 * 
	 * @return	the cost needed to move from node <code>from</code> to node <code>to</code>
	 * 
	 * @throws 	NodesNotAdjacentException
	 * 			If <code>from</code> and <code>to</code> are not adjacent.
	 * 			<br /> | <code> ! areAdjacent(from, to) </code>
	 */
	public abstract C getCostBetweenNodes(N from, N to) throws NodesNotAdjacentException;
	
	/**
	 * Method for retrieving a cost object with value zero.
	 * <br />
	 * The following must hold:
	 * <br /><code>result.add(someCost).equals(someCost)</code>
	 * 
	 * @return	a cost with value zero
	 */
	public abstract C getZeroCost();
	
	/**
	 * Check of <code>node1</code> and <code>node2</code> are adjacent nodes in the network.
	 * 
	 * @param 	node1
	 * 			the first node
	 * @param 	node2
	 * 			the second node
	 * 
	 * @return	true if the two nodes are adjacent, false if not so
	 */
	public abstract boolean areAdjacent(N node1, N node2);
	
	/**
	 * This method allows you to define your own stop criterium for the calculation.
	 * This method is called before every node gets expanded.
	 * 
	 * The default value is <code>false</code>.
	 * 
	 * @return	<code>true</code> is the calculation should stop, 
	 * 			<code>false</code> if it should continue
	 */
	public boolean isStopCriteriumReached() {
		return false;
	}
	
	/**
	 * Expand a StateNode to a Set of StateNodes.
	 * This method uses the NodeExpander.expand() method and adds cost and heuristic values.
	 * 
	 * @param 	node
	 * 			the node to expand
	 * 
	 * @return	the expansion of <code>node</code>
	 */
	public final Set<StateNode<N, C>> expand(StateNode<N, C> node) {
		Set<StateNode<N, C>> expansion = new HashSet<StateNode<N,C>>();
		for(N n : expand(node.getNode())) {
			if(n.equals(node.getPreviousNode()))
				continue;
			expansion.add(new StateNode<N, C>(n, 
					node.getAcumulatedCost().add(getCostBetweenNodes(node.getNode(), n)), 
					calculateHeuristic(n), 
					node));
		}
		return expansion;
	}
	
	/**
	 * Get the start node of the network.
	 * 
	 * @return	the start node of the network
	 */
	public final N getStartNode() {
		return startNode;
	}
	
	/**
	 * Get the goal node of the network.
	 * 
	 * @return	the goal node of the network
	 */
	public final N getGoalNode() {
		return goalNode;
	}
	
	/**
	 * The exception exists because certain methods used in this class only accept nodes 
	 * that are adjacent to each other. If they are not, this exception will be thrown.
	 * <br /><code>getCostBetweenNodes(Node, Node)</code> is an example of such a method.
	 * <br />It's up to the user of this class to choose whether or not to use this exception.
	 * 
	 * @author 	Steven Roose
	 * @license	Apache License, Version 2.0
	 *
	 */
	@SuppressWarnings("serial")
	public static class NodesNotAdjacentException extends RuntimeException {
		
	}
	
}