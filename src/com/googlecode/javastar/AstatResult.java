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
public class AstatResult<N extends Node, C extends Cost<C>> {
	
	/**
	 * All the possible types a result can have.
	 * 
	 * @author Steven Roose
	 *
	 */
	public enum ResultType {
		/**
		 * The calculation failed. This usually means the goal is not reachable.
		 */
		FAIL,
		/**
		 * The calculation was successful and a path was found.
		 */
		SUCCESS
	}
	
	private final ResultType type;
	private final StateNode<N, C> endNode;

	/**
	 * Initialize a new result object.
	 * 
	 * @param	type
	 * 			the type of this result
	 * @param	endNode
	 * 			the last node in the path
	 */
	public AstatResult(ResultType type, StateNode<N, C> endNode) {
		this.type = type;
		this.endNode = endNode;
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
		if(getType() == ResultType.FAIL)
			return null;
		return endNode.getCumulatedCost();
	}
	
	/**
	 * Get a list of all nodes that form the resulting path.
	 * The first element is the start node and the last element is the goal node.
	 * 
	 * @return a list of all nodes that form the resulting path
	 */
	public List<N> getPath() {
		if(getType() == ResultType.FAIL)
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

}
