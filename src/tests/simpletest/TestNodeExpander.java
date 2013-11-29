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

package tests.simpletest;

import be.stevenroose.javastar.AstarHelper;

import java.util.HashSet;
import java.util.Set;

public class TestNodeExpander extends
		AstarHelper<TestNode, TestCost> {

	private TestWorld world;

	public TestNodeExpander(TestNode startNode, TestNode goalNode, int worldId) {
		super(startNode, goalNode);
		world = new TestWorld(worldId);
	}

	@Override
	public Set<TestNode> expand(TestNode node) {
		System.out.println("Expanding " + node.toString());

		Set<TestNode> expansion = new HashSet<TestNode>();
		expansion.add(new TestNode(node.getX() + 1, node.getY()));
		expansion.add(new TestNode(node.getX() - 1, node.getY()));
		expansion.add(new TestNode(node.getX(), node.getY() + 1));
		expansion.add(new TestNode(node.getX(), node.getY() - 1));

		for (TestNode n : new HashSet<TestNode>(expansion)) {
			if (!canMoveToNode(n))
				expansion.remove(n);
		}
		return expansion;
	}

	@Override
	public TestCost calculateHeuristic(TestNode node) {
		return new TestCost(Math.abs(getGoalNode().getX() - node.getX()) +
				Math.abs(getGoalNode().getY() - node.getY()));
	}

	@Override
	public TestCost getCostBetweenNodes(TestNode from,
										TestNode to) {
		if (!areAdjacent(from, to))
			throw new NodesNotAdjacentException();

		return new TestCost(1);
	}

	public boolean isValidNode(TestNode node) {
		return world.isValidPosition(node.getX(), node.getY());
	}

	public boolean canMoveToNode(TestNode node) {
		if (!isValidNode(node))
			return false;
		return !world.isWall(node.getX(), node.getY());
	}

	@Override
	public boolean areAdjacent(TestNode node1,
							   TestNode node2) {
		return (Math.abs(node1.getX() - node2.getX())
				+ Math.abs(node1.getY() - node2.getY())) == 1;
	}

	@Override
	public TestCost getZeroCost() {
		return new TestCost(0);
	}


}
