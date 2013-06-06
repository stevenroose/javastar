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
 * This class represents nodes in the graph while the algorithm is expanding it.
 * Each PathNode contains of a Node to represent the node in the network and of cost
 * and heuristic values, as well as a previous node from which this node has been expanded.
 * <br />
 * This class should not be edited to use JavAstar.
 *
 * @author Steven Roose
 * @license Apache License, Version 2.0
 */
public class PathNode<N extends Node, C extends Cost<C>> {

	//TODO remove the IllegalStateExceptions when archived to avoid unnecessary checks

	private N node;

	private C accumulatedCost;

	private C heuristic;

	private PathNode<N, C> previousNode;

	private int pathLength;

	private Set<N> previousNodes; // A HashSet is used for high .contains() performance.

	/**
	 * Create a new PathNode
	 *
	 * @param node            the node this PathNode should represent
	 * @param accumulatedCost the cost of the node
	 * @param heuristic       the heuristic value of the node
	 * @param previousNode    the node from which this node has been expanded
	 * @throws IllegalArgumentException If either of <code>node</code>, <code>cumulatedCost</code> or
	 *                                  <code>heuristic</code> is <code>null</code>.
	 */
	public PathNode(N node, C accumulatedCost, C heuristic, PathNode<N, C> previousNode) throws IllegalArgumentException {
		if (node == null)
			throw new IllegalArgumentException("A PathNode's node cannot be null");
		if (accumulatedCost == null)
			throw new IllegalArgumentException("A PathNode's cost cannot be null");
		if (heuristic == null)
			throw new IllegalArgumentException("A PathNode's heuristic value cannot be null");

		this.node = node;
		this.accumulatedCost = accumulatedCost;
		this.heuristic = heuristic;
		this.previousNode = previousNode;

		if (previousNode == null)
			this.previousNodes = new HashSet<N>();
		else {
			this.previousNodes = previousNode.getPreviousNodes();
			this.previousNodes.add(previousNode.getNode());
		}

		// increment previous pathLength or set to zero for start node
		if (previousNode != null)
			pathLength = previousNode.getPathLength() + 1;
		else
			pathLength = 0;
	}

	/**
	 * Returns the node that is represented by this PathNode
	 *
	 * @return the node that is represented by this PathNode
	 */
	public N getNode() {
		return node;
	}

	/**
	 * Returns the accumulated cost of the path from the start node until this node.
	 *
	 * @return the accumulated cost of the path from the start node until this node
	 *
	 * @throws 	IllegalStateException
	 * 			If the node is already archived.
	 */
	public C getAcumulatedCost() {
		if(isArchived())
			throw new IllegalStateException("Node is already archived.");
		return accumulatedCost;
	}

	/**
	 * The heuristic value of a node is an estimate of the total cost to the goal.
	 * It's important that the heuristic value always is AN UNDERESTIMATE
	 * of the total cost from this node to the goal (which is called an admissible heuristic).
	 * The heuristic value is defined by NodeExpaner.calculateHeuristic().
	 *
	 * @return the heuristic value from this node
	 *
	 * @throws 	IllegalStateException
	 * 			If the node is already archived.
	 */
	public C getHeuristic() {
		if(isArchived())
			throw new IllegalStateException("Node is already archived.");
		return heuristic;
	}

	/**
	 * The score of a node is defined as the sum of the cumulated cost and the heuristic value.
	 * <br />
	 * The following always holds:
	 * <br /><code>getCost() == getCumulatedCost() + getHeuristic()</code>
	 *
	 * @return the sum of the cumulated cost and the heuristic value from this node
	 *
	 * @throws 	IllegalStateException
	 * 			If the node is already archived.
	 */
	public C getScore() {
		if(isArchived())
			throw new IllegalStateException("Node is already archived.");
		return getAcumulatedCost().add(getHeuristic());
	}

	/**
	 * The path length of a node is the number of nodes between this node and the
	 * start node.
	 * The start node has path length zero.
	 *
	 * @return the number of this node from the start node
	 */
	public int getPathLength() {
		return pathLength;
	}

	/**
	 * Returns the node from which this node has been expanded.
	 * The first node, and the first node only, has a <code>null</code> value for this property;
	 *
	 * @return the node from which this node has been expanded
	 * <code>null</code> if this is the start node.
	 *
	 * @throws 	IllegalStateException
	 * 			If the node is already archived.
	 */
	public PathNode<N, C> getPreviousNode() {
		if(isArchived())
			throw new IllegalStateException("Node is already archived.");
		return previousNode;
	}

	/**
	 * Returns all the nodes in the shortest path from the start node to this node.
	 * <p/>
	 * It contains the current node as well.
	 * <p/>
	 * This set is mostly used to check if a node is already in the path. For that purpose,
	 * consider using hasNodeInPath(Node). The use of this method is discouraged.
	 *
	 * @return a set with all the nodes in the shortest path from the start node to this node
	 *
	 * @throws 	IllegalStateException
	 * 			If the node is already archived.
	 */
	protected Set<N> getPreviousNodes() {
		if(isArchived())
			throw new IllegalStateException("Node is already archived.");
		return new HashSet<N>(previousNodes);
	}

	/**
	 * Check if a node is already contained by the shortest path from the start node to this node.
	 * This node itself is <strong>NOT</strong> included.
	 * Use <code>this.getNode().equals(node)</code> for that purpose.
	 *
	 * @param	node the node to check
	 *
	 * @return	<code>true</code> if <code>node</code> is in the shortest path
	 * from the start node to this node, <code>false</code> otherwise
	 *
	 * @throws 	IllegalStateException
	 * 			If the node is already archived.
	 */
	public boolean hasNodeInPath(N node) {
		if(isArchived())
			throw new IllegalStateException("Node is already archived.");
		return previousNodes.contains(node);
	}

	/**
	 * When a node moves out of the set of open nodes, but is still used by other open nodes
	 * to form the linked list that is the total path, this method is called.
	 * It removes all information that is no longer needed to free up space.
	 */
	public void archive() {
		// (clearing primitive types makes no sense)
		//   (making pathLengt an Integer object either because references take 4 bytes 
		//    as well, sometimes even 8)

		accumulatedCost = null;
		heuristic = null;

		previousNode = null;
		previousNodes = null;

		//TODO fix the fact that previousNodes has to be kept.
		//     It should be possible to move that information a node deeper in the tree
		//      so that nodes can be fully archived.
		//     BUT note that only these sets of the open nodes and their previous ones are retained,
		//      still much, so still an opportunity to fix it!
	}

	/**
	 * Check if this node is archived.
	 *
	 * @return	<code>true</code> if <code>archive()</code> is already called, <code>false</code> otherwise
	 */
	public boolean isArchived() {
		return accumulatedCost == null;
	}

	@Override
	public String toString() {
		if(!isArchived()) {
			return "PathNode(node=" + getNode().toString() + "; score=" + getScore().toString() +
					"; cost=" + getAcumulatedCost().toString() +
					"; heuristic=" + getHeuristic().toString() + ")";
		}
		else {
			return "PathNode(node=" + getNode().toString() + ")";
		}
	}

}
