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
	private C cumulatedCost;
	private C heuristic;
	private StateNode<N, C> previousNode;
	
	/**
	 * Create a new StateNode
	 * 
	 * @param 	node
	 * 			the node this StateNode should represent
	 * @param 	cumulatedCost
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
	public StateNode(N node, C cumulatedCost, C heuristic, StateNode<N, C> previousNode) throws IllegalArgumentException {
		if(node == null)
			throw new IllegalArgumentException("A StateNode's node cannot be null");
		if(cumulatedCost == null)
			throw new IllegalArgumentException("A StateNode's cost cannot be null");
		if(heuristic == null)
			throw new IllegalArgumentException("A StateNode's heuristic value cannot be null");
		
		this.node = node;
		this.cumulatedCost = cumulatedCost;
		this.heuristic = heuristic;
		this.previousNode = previousNode;
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
	 * Returns the cumulated cost of the path from the start node until this node.
	 * 
	 * @return	the cumulated cost of the path from the start node until this node
	 */
	public C getCumulatedCost() {
		return cumulatedCost;
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
		return getCumulatedCost().add(getHeuristic());
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
	
}
