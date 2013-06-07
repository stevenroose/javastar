/*******************************************************************************
 * Copyright (c) 2012 Steven Roose
 * This file is licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.googlecode.javastar;

import com.googlecode.javastar.AstarResult.ResultType;

import java.util.*;

/**
 * The actual A star calculation happens within this class.
 * <br />
 * This class heavily relies on the AstarHelper subclass that is provided upon construction.
 *
 * @author Steven Roose
 * @license Apache License, Version 2.0
 */
public class AstarCalculator<N extends Node, C extends Cost<C>> {

	private enum State {
		WAITING,
		RUNNING,
		FINISHED
	}

	private State state;

	/**
	 * This HashMap is used for <code>get()</code>, <code>put()</code> and <code>containsKey()</code>
	 * operations, which happen in constant time.
	 */
	private HashMap<N, PathNode<N, C>> frontier;
	/**
	 * This PriorityQueue is used for sorting the frontier, but based on the keys only.
	 * Primarily the methods <code>peek()</code> and <code>poll()</code> are used, which happen
	 * in constant and <code>O(log(n))</code> time respectively.
	 * Sometimes the <code>remove(Object)</code> method is used, which happens in linear time.
	 */
	private PriorityQueue<N> queue;

	private AstarHelper<N, C> helper;

	/**
	 * A counter for the total number of nodes that are expanded.
	 */
	private long numberOfNodesExpanded;

	/**
	 * The type of the produced result.
	 */
	private ResultType resultType;
	/**
	 * The produced result.
	 */
	private AstarResult<N, C> result;

	/**
	 * The time on whoch the actual calculation has begon.
	 */
	private long startTime;

	private long maximumNumberOfNodesToExpand = 0;


	/**
	 * Create a new A star calculator object with your own AstarHelper subclass.
	 */
	public AstarCalculator(AstarHelper<N, C> astarHelper) {
		this.helper = astarHelper;
		this.state = State.WAITING;
	}

	//TODO Should this be part of the helper class?

	/**
	 * Specify how many nodes should be expanded at most.
	 * The algorithm will stop if this number of nodes have been expanded.
	 *
	 * @param maximumNumberOfNodesToExpand the maximum number of nodes to expand
	 * @throws IllegalArgumentException if <code>maximumNumberOfNodesToExpand</code> is lower than 0
	 * @throws IllegalStateException    if the calculation is already running
	 */
	public void setMaximumNumberOfNodesToExpand(long maximumNumberOfNodesToExpand)
			throws IllegalStateException, IllegalArgumentException {
		if (state == State.RUNNING)
			throw new IllegalStateException("Already running.");
		if (maximumNumberOfNodesToExpand < 0)
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
	 * @return the result of the last A star calculation
	 * @throws IllegalStateException If the calculation is not yet finished.
	 */
	public AstarResult<N, C> getResult() {
		if (state != State.FINISHED)
			throw new IllegalStateException("Calculation is not yet finished.");
		return result;
	}

	/**
	 * Run the calculation.
	 * <p/>
	 * When the calculation is finished, the result will be in <code>getResult()</code>.
	 */
	public synchronized void run() {
		state = State.RUNNING;
		startTime = System.currentTimeMillis();
		initialize();
		addNode(new PathNode<N, C>(helper.getStartNode(),
				helper.getZeroCost(),
				helper.calculateHeuristic(helper.getStartNode()),
				null));
		expandAll();
		createResult();
		state = State.FINISHED;
	}

	/**
	 * Initialize the necessary variables needed for the calculation.
	 */
	private void initialize() {
		result = null;
		numberOfNodesExpanded = 0;

		frontier = new HashMap<N, PathNode<N, C>>();
		queue = new PriorityQueue<N>(10, new Comparator<N>() {
			@Override
			public int compare(N node1, N node2) {
				return frontier.get(node1).getScore().compareTo(
						frontier.get(node2).getScore());
			}
		});

		// check the setup for correct implementation
		helperImplementationTest();
	}

	private void helperImplementationTest() {
		// the goal node's heuristic value must be zero if it is monotone
		if (helper.isHeuristicMonotone()) {
			if (!helper.calculateHeuristic(helper.getGoalNode()).equals(helper.getZeroCost())) {
				throw new IllegalStateException("The goal node must have a heuristic value of zero under monotonicity.");
			}
		}

		// testing Node.equals and Node.hashCode value
		// and Cost.compareTo() implementation

		Set<N> set1 = helper.expand(helper.getStartNode());
		Set<N> set2 = helper.expand(helper.getStartNode());
		// sets must contain only equal items, will not check for same order:
		for (N n1 : new HashSet<N>(set1)) {
			for (N n2 : new HashSet<N>(set2)) {
				if (n1.equals(n2) && n1.hashCode() == n2.hashCode()) {
					// check of costs between nodes are all correct
					C c1 = helper.getCostBetweenNodes(helper.getStartNode(), n1);
					C c2 = helper.getCostBetweenNodes(helper.getStartNode(), n2);
					if (c1.compareTo(c2) != 0) {
						throw new IllegalStateException("Cost.compareTo() is not implemented correctly!");
					}
					if (c1.compareTo(helper.getZeroCost()) <= 0) {
						throw new IllegalStateException("Either Cost.compareTo() or " +
								"helper.getZeroCost() is not implemented correctly");
					}
					// remove the 2 matching nodes
					set1.remove(n1);
					set2.remove(n2);
				}
			}
		}
		if (!(set1.isEmpty() && set2.isEmpty())) {
			throw new IllegalStateException("equals and hashCode functions of Nodes need to " +
					"be implemented according to their specification!");
		}
	}

	/**
	 * Perform the actual expansion of the network.
	 */
	private void expandAll() {
		while (shouldExtractMore()) {
			PathNode<N, C> first = frontier.get(queue.poll());
			expand(first);
		}
	}

	/**
	 * Defines whether or not to do another expansion.
	 *
	 * @return whether or not another expansion should be done
	 */
	private boolean shouldExtractMore() {
		if (queue.isEmpty()) {
			// no more nodes to expand
			resultType = ResultType.FAIL_ALL_NODES_EXPANDED;
			return false;
		}

		if (queue.peek().equals(helper.getGoalNode())) {
			// solution found
			resultType = ResultType.SUCCESS;
			return false;
		}

		if (maximumNumberOfNodesToExpand != 0 &&
				numberOfNodesExpanded >= maximumNumberOfNodesToExpand) {
			// expanded the maximum number of nodes
			resultType = ResultType.FAIL_MAX_NODES_EXPANDED;
			return false;
		}

		if (helper.isStopCriteriumEnabled()) {
			PathNode<N, C> currentNode = frontier.get(queue.peek());
			if (helper.isStopCriteriumReached(numberOfNodesExpanded,
					currentNode.getAcumulatedCost(),
					currentNode.getHeuristic(),
					currentNode.getPathLength())) {
				// user-defined stop criterium reached
				resultType = ResultType.FAIL_USER_STOP_CRITERIUM;
				return false;
			}
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
	 * contains the one with the lowest score (cost + heuristic) value.
	 *
	 * @param node the node to expand
	 */
	private void expand(PathNode<N, C> node) {
		Set<PathNode<N, C>> expansion = helper.expand(node);

		for (PathNode<N, C> n : expansion) {
			exploreNode(node, n);
		}

		numberOfNodesExpanded++;

		// remove the node
		frontier.remove(node.getNode());
		node.archive();

		//TODO deleteRedundantPaths
	}

	/**
	 * This method is called for every new node that is explored.
	 * It performs checks and adds the node to the list of open nodes if it qualifies.
	 *
	 * @param fromNode the node from which <code>newNode</code> is explored
	 * @param newNode  the node that is being explored and should be checked
	 */
	private void exploreNode(PathNode<N, C> fromNode,
							 PathNode<N, C> newNode) {
		// check if the node is already in the frontier
		if (frontier.containsKey(newNode.getNode())) {
			if (helper.isHeuristicMonotone())
				return; // new node is always worse than first one found
			if (frontier.get(newNode.getNode()).getScore().compareTo(newNode.getScore()) > 0) {
				removeNode(frontier.get(newNode.getNode()));
				addNode(newNode);
			}
			return; // duplicate already in frontier has better score
		}

		// LOOP CHECKING
		//TODO make setting to disable loopchecking
		if (true) {
			if (containsLoops(fromNode))
				return;
		}

		addNode(newNode);
	}

	/**
	 * Checks the node for loops.
	 *
	 * @param node the node to check
	 * @return <code>true</code> if <code>node</code> contains loops, <code>false</code> otherwise
	 */
	private boolean containsLoops(PathNode<N, C> node) {
		return node.hasNodeInPath(node.getNode());
	}

	/**
	 * Add a node to the frontier.
	 *
	 * @param node the node to add
	 */
	private void addNode(PathNode<N, C> node) {
		frontier.put(node.getNode(), node);
		queue.add(node.getNode());
	}

	/**
	 * Remove a node from the frontier.
	 *
	 * @param node the node to remove
	 */
	private void removeNode(PathNode<N, C> node) {
		queue.remove(node.getNode());
		frontier.remove(node.getNode());
	}

	/**
	 * Create the result object.
	 */
	private void createResult() {
		if (resultType.isSuccess()) {
			// success
			result = new AstarResult<N, C>(resultType,
					frontier.get(queue.peek()),
					numberOfNodesExpanded,
					System.currentTimeMillis() - startTime);
		} else {
			// fail
			result = new AstarResult<N, C>(resultType,
					null,
					numberOfNodesExpanded,
					System.currentTimeMillis() - startTime);
		}
	}

}
