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

/**
 * This class represents nodes in the graph while the algorithm is expanding it.
 * Each StateNode contains of a Node to represent the node in the network and of cost
 * and heuristic values, as well as a previous node from which this node has been expanded.
 * <br />
 * This class should not be edited to use JavAstar.
 * 
 * @author 	Steven Roose
 * @license	Apache License, Version 2.0
 *
 */
public class StateNode<N extends Node, C extends Cost<C>> {
	
	private N node;
	
	private C accumulatedCost;
	
	private C heuristic;
	
	private StateNode<N, C> previousNode;
	
	private int pathLength;
	
	/**
	 * Create a new StateNode
	 * 
	 * @param 	node
	 * 			the node this StateNode should represent
	 * @param 	accumulatedCost
	 * 			the cost of the node
	 * @param 	heuristic
	 * 			the heuristic value of the node
	 * @param 	previousNode
	 * 			the node from which this node has been expanded
	 * 
	 * @throws 	IllegalArgumentException
	 * 			If either of <code>node</code>, <code>cumulatedCost</code> or 
	 * 			<code>heuristic</code> is <code>null</code>.
	 */
	public StateNode(N node, C accumulatedCost, C heuristic, StateNode<N, C> previousNode) throws IllegalArgumentException {
		if(node == null)
			throw new IllegalArgumentException("A StateNode's node cannot be null");
		if(accumulatedCost == null)
			throw new IllegalArgumentException("A StateNode's cost cannot be null");
		if(heuristic == null)
			throw new IllegalArgumentException("A StateNode's heuristic value cannot be null");
		
		this.node = node;
		this.accumulatedCost = accumulatedCost;
		this.heuristic = heuristic;
		this.previousNode = previousNode;
		
		// increment previous pathLength or set to zero for start node
		if(previousNode != null)
			pathLength = previousNode.getPathLength() + 1;
		else
			pathLength = 0;
	}
	
	/**
	 * Returns the node that is represented by this StateNode
	 * 
	 * @return	the node that is represented by this StateNode
	 */
	public N getNode() {
		return node;
	}
	
	/**
	 * Returns the accumulated cost of the path from the start node until this node.
	 * 
	 * @return	the accumulated cost of the path from the start node until this node
	 */
	public C getAcumulatedCost() {
		return accumulatedCost;
	}
	
	/**
	 * The heuristic value of a node is an estimate of the total cost to the goal.
	 * It's important that the heuristic value always is AN UNDERESTIMATE 
	 * of the total cost from this node to the goal (which is called an admissible heuristic).
	 * The heuristic value is defined by NodeExpaner.calculateHeuristic().
	 * 
	 * @return	the heuristic value from this node
	 */
	public C getHeuristic() {
		return heuristic;
	}
	
	/**
	 * The score of a node is defined as the sum of the cumulated cost and the heuristic value.
	 * <br />
	 * The following always holds:
	 * <br /><code>getCost() == getCumulatedCost() + getHeuristic()</code>
	 * 
	 * @return	the sum of the cumulated cost and the heuristic value from this node
	 */
	public C getScore() {
		return getAcumulatedCost().add(getHeuristic());
	}
	
	/**
	 * The path length of a node is the number of nodes between this node and the 
	 * start node.
	 * The start node has path length zero.
	 * 
	 * @return	the number of this node from the start node
	 */
	public int getPathLength() {
		return pathLength;
	}
	
	/**
	 * Returns the node from which this node has been expanded. 
	 * The first node, and the first node only, has a <code>null</code> value for this property;
	 * 
	 * @return	The node from which this node has been expanded.
	 * 			<code>null</code> if this is the start node.
	 */
	public StateNode<N, C> getPreviousNode() {
		return previousNode;
	}
	
	@Override
	public String toString() {
		return "StateNode(node="+getNode().toString()+"; score="+getScore().toString()+
				"; cost="+getAcumulatedCost().toString()+
				"; heuristic="+getHeuristic().toString()+")";
	}
	
}
