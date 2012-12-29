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

import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the result of an A star calculation.
 * <br />
 * The following always holds:
 * <br /><code>getType() == FAIL => ( getCost() == null && getPath() == null )</code> 
 * 
 * @author 	Steven Roose
 * @license	Apache License, Version 2.0
 *
 */
public class AstarResult<N extends Node, C extends Cost<C>> {
	
	/**
	 * All the possible types a result can have.
	 * 
	 * @author Steven Roose
	 * @license	Apache License, Version 2.0
	 *
	 */
	public enum ResultType {
		/**
		 * The calculation was successful and a path was found.
		 */
		SUCCESS (true),
		/**
		 * The calculation failed. This usually means the goal is not reachable.
		 */
		FAIL (false),
		/**
		 * Failed because all nodes were expanded and no solution was found.
		 */
		FAIL_ALL_NODES_EXPANDED (false),
		/**
		 * The maximum number of nodes to expand has been reached.
		 */
		FAIL_MAX_NODES_EXPANDED (false),
		/**
		 * Failed because the user-defined stop criterium was reached.
		 */
		FAIL_USER_STOP_CRITERIUM (false);
		
		private final boolean success;
		
		/**
		 * The ResultType constructor.
		 * 
		 * @param 	success
		 * 			whether or not the calculation was a success
		 */
		private ResultType(boolean success) {
			this.success = success;
		}
		
		/**
		 * Check if this ResultType means that the calculation was a success or not.
		 * 
		 * @return	<code>true</code> if the calculation succeeded, <code>false</code> if it didn't
		 */
		public boolean isSuccess() {
			return success;
		}
	}
	
	private final ResultType type;
	
	private final StateNode<N, C> endNode;
	
	private final long numberOfNodesExpanded;
	
	private long calculationTime;

	/**
	 * Initialize a new result object.
	 * 
	 * @param	type
	 * 			the type of this result
	 * @param	endNode
	 * 			the last node in the path
	 */
	public AstarResult(ResultType type, StateNode<N, C> endNode, long numberOfNodesExpanded, long calculationTime) {
		this.type = type;
		this.endNode = endNode;
		this.numberOfNodesExpanded = numberOfNodesExpanded;
		this.calculationTime = calculationTime;
	}
	
	@Override
	public String toString() {
		if(getType().isSuccess()) {
			return "AstarResult(type=" + getType().toString() + "; endNode=" 
					+ endNode.getNode().toString() + "; cost=" + getCost().toString() + ")";
		}
		else {
			return "AstarResult(type=" + getType().toString() + ")";
		}
	}
	
	/**
	 * Get the type of the result. Can be any of the ResultType values.
	 * 
	 * @return	the type of the result
	 */
	public ResultType getType() {
		return type;
	}
	
	/**
	 * Returns the cost of the resulting path.
	 * 
	 * @return	the cost of the resulting path
	 */
	public C getCost() {
		if(!getType().isSuccess())
			return null;
		return endNode.getAcumulatedCost();
	}
	
	/**
	 * Get a list of all nodes that form the resulting path.
	 * The first element is the start node and the last element is the goal node.
	 * 
	 * @return a list of all nodes that form the resulting path
	 */
	public List<N> getPath() {
		if(!getType().isSuccess())
			return null;
		
		LinkedList<N> result = new LinkedList<N>();
		StateNode<N, C> curr = endNode;
		result.addFirst(curr.getNode());
		while(curr.getPreviousNode() != null) {
			curr = curr.getPreviousNode();
			result.addFirst(curr.getNode());
		}
		return result;
	}
	
	/**
	 * Get the path length of the resulting path. 
	 * The path length is expressed in the number of nodes the path contains.
	 * If you want to know the path length expressed in the cost, use <code>getCost()</code>.   
	 * 
	 * @return	the number of nodes in the path
	 */
	public int getPathLength() {
		if(!getType().isSuccess())
			return 0;
		return endNode.getPathLength();
	}
	
	/**
	 * This is the total number of nodes that were expanded in the process of finding the 
	 * resulting solution.
	 * 
	 * @return	the total number of nodes expanded by the algorithm
	 */
	public long getNumberOfNodesExpanded() {
		return numberOfNodesExpanded;
	}
	
	/**
	 * Get the total time the algorithm needed to calculate this result in milliseconds.
	 * 
	 * @return	the calculation time in milliseconds
	 */
	public long getCalculationTime() {
		return calculationTime;
	}

}
