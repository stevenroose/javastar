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

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Set;

import com.googlecode.javastar.AstatResult.ResultType;

/**
 * The actual A star calculation happens within this class.
 * <br />
 * This class heavily relies on the NodeExpander subclass that is provided upon construction.
 * 
 * @author 	Steven Roose
 * @license	Apache License, Version 2.0
 *
 */
public class AstarCalculator<N extends Node, C extends Cost<C>> {
	
	private enum State {
		WAITING,
		RUNNING,
		FINISHED
	}
	
	private State state;
	
	private HashMap<N, StateNode<N, C>> frontier;
	private PriorityQueue<N> queue;
	
	private NodeExpander<N, C> expander;
	
	private long numberOfNodesExpanded;
	
	private ResultType resultType;
	private AstatResult<N, C> result;
	
	private long maximumNumberOfNodesToExpand = 0;
	
	
	/**
	 * Create a new A star calculator object with your own NodeExpander subclass.
	 */
	public AstarCalculator(NodeExpander<N, C> nodeExpander) {
		this.expander = nodeExpander;
		this.state = State.WAITING;
	}
	
	/**
	 * Start the calculation.
	 * 
	 * When the calculation is finished, the result will be in <code>getResult()</code>.
	 */
	public synchronized void start() {
		state = State.RUNNING;
		initialize();
		addNode(new StateNode<N, C>(expander.getStartNode(), 
				expander.getZeroCost(), 
				expander.calculateHeuristic(expander.getStartNode()), 
				null));
		expandAll();
		createResult();
	}
	
	/**
	 * Specify how many nodes should be expanded at most. 
	 * The algorithm will stop if this number of nodes have been expanded.
	 *  
	 * @param 	maximumNumberOfNodesToExpand
	 * 			the maximum number of nodes to expand
	 * 
	 * @throws	IllegalArgumentException
	 * 			if <code>maximumNumberOfNodesToExpand</code> is lower than 0
	 * @throws	IllegalStateException
	 * 			if the calculation is already running
	 */
	public void setMaximumNumberOfNodesToExpand(long maximumNumberOfNodesToExpand) 
			throws IllegalStateException, IllegalArgumentException {
		if(state == State.RUNNING)
			throw new IllegalStateException("Already running.");
		if(maximumNumberOfNodesToExpand < 0)
			throw new IllegalArgumentException("Argument must be at least 0.");
		this.maximumNumberOfNodesToExpand = maximumNumberOfNodesToExpand;
	}
	
	/**
	 * Retrieve the result of the last A star calculation.
	 * When no calculation has been performed, <code>null</code> is returned.
	 * <br />
	 * The following holds:<br />
	 * - <code>result.getType() == SUCCESS</code> if the calculation was successful <br />
	 * - <code>result.getType() == FAIL</code> if the calculation failed
	 * 
	 * @return	the result of the last A star calculation
	 */
	public AstatResult<N, C> getResult() {
		return result;
	}
	
	/**
	 * Initialize the necessary variables needed for the calculation.
	 */
	private void initialize() {
		result = null;
		numberOfNodesExpanded = 0;
		
		frontier = new HashMap<N, StateNode<N,C>>();
		queue = new PriorityQueue<N>(10, new Comparator<N>() {
			
			@Override
			public int compare(N node1, N node2) {
				return frontier.get(node1).getScore().compareTo(
						frontier.get(node2).getScore());
			}
		});
	}
	
	/**
	 * Perform the actual expansion of the network.
	 */
	private void expandAll() {
		while(shouldExtractMore()) {
			StateNode<N, C> first = frontier.get(queue.poll());
			expand(first);
		}
	}
	
	/**
	 * Defines the condition whether or not to do another expansion. 
	 * 
	 * @return	whether or not another expansion should be done
	 */
	private boolean shouldExtractMore() {
		if( queue.peek().equals(expander.getGoalNode()) ) {
			// solution found
			resultType = ResultType.SUCCESS;
			return false;
		}
		
		if( queue.isEmpty() ) {
			// no more nodes to expand
			resultType = ResultType.FAIL_ALL_NODES_EXPANDED;
			return false;
		}
		
		if( maximumNumberOfNodesToExpand != 0 && 
				numberOfNodesExpanded >= maximumNumberOfNodesToExpand ) {
			// expanded the maximum number of nodes
			resultType = ResultType.FAIL_MAX_NODES_EXPANDED;
			return false;
		}
		
		if( expander.isStopCriteriumReached() ) {
			// user-defined stop criterium reached
			resultType = ResultType.FAIL_USER_STOP_CRITERIUM;
			return false;
		}
		
		return true;
	}
	
	/**
	 * Expand a node and put the child nodes into the frontier.
	 * <br />
	 * The following holds after this method has been executed:
	 * <br />- The frontier no longer contains <code>node</code>.
	 * <br />- The frontier contains all nodes adjacent to <code>node</code> that were not in it yet.
	 * <br />- If some nodes adjacent to <code>node</code> were already in the frontier, it now
	 * 			contains the one with the lowest score (cost + heuristic) value. 
	 * 
	 * @param 	node
	 * 			the node to expand
	 */
	private void expand(StateNode<N, C> node) {
		Set<StateNode<N, C>> expansion = expander.expand(node);
		
		for(StateNode<N, C> n : expansion) {
			if(frontier.containsKey(n.getNode())) {
				if(frontier.get(n.getNode()).getScore().compareTo(n.getScore()) > 0) {
					removeNode(frontier.get(n.getNode()));
					addNode(n);
				}
				continue;
			}
			addNode(n);
		}
		
		numberOfNodesExpanded++;
		frontier.remove(node.getNode());
	}
	
	/**
	 * Create the result object.
	 */
	private void createResult() {
		if(resultType.isSuccess()) {
			// success
			result = new AstatResult<N, C>(resultType, 
					frontier.get(queue.peek()), 
					numberOfNodesExpanded);
		}
		else {
			// fail
			result = new AstatResult<N, C>(resultType, 
					null, 
					numberOfNodesExpanded);
		}
		state = State.FINISHED;
	}
	
	/**
	 * Add a node to the frontier.
	 * 
	 * @param 	node
	 * 			the node to add
	 */
	private void addNode(StateNode<N, C> node) {
		frontier.put(node.getNode(), node);
		queue.add(node.getNode());
	}
	
	/**
	 * Remove a node from the frontier.
	 * @param 	node
	 * 			the node to remove
	 */
	private void removeNode(StateNode<N, C> node) {
		queue.remove(node.getNode());
		frontier.remove(node.getNode());
	}

}
