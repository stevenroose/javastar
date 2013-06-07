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

public class TestWorld {

	private final int[][] world1 = new int[][]{
			new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
			new int[]{1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
			new int[]{1, 0, 1, 0, 1, 0, 1, 1, 0, 1},
			new int[]{1, 0, 1, 0, 1, 0, 0, 1, 0, 1},
			new int[]{1, 0, 1, 0, 1, 0, 1, 1, 0, 1},
			new int[]{1, 0, 1, 0, 1, 0, 1, 1, 0, 1},
			new int[]{1, 0, 1, 0, 1, 0, 1, 1, 0, 1},
			new int[]{1, 0, 0, 0, 1, 0, 1, 1, 0, 1},
			new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

	private final int[][] world2 = new int[][]{
			new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			new int[]{1, 0, 1, 0, 0, 0, 1, 0, 0, 1},
			new int[]{1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
			new int[]{1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
			new int[]{1, 0, 1, 0, 1, 0, 0, 0, 0, 1},
			new int[]{1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
			new int[]{1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
			new int[]{1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
			new int[]{1, 0, 0, 0, 1, 0, 1, 0, 0, 1},
			new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

	private final int[][] world3 = new int[][]{
			new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			new int[]{1, 0, 1, 0, 1, 0, 0, 0, 0, 1},
			new int[]{1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
			new int[]{1, 0, 1, 0, 0, 0, 1, 0, 0, 1},
			new int[]{1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
			new int[]{1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
			new int[]{1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
			new int[]{1, 0, 1, 0, 1, 0, 1, 0, 0, 1},
			new int[]{1, 0, 0, 0, 1, 0, 1, 0, 0, 1},
			new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

	private final int[][] world4 = new int[][]{
			new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
			new int[]{1, 0, 1, 1, 1, 0, 0, 0, 0, 1},
			new int[]{1, 0, 0, 1, 1, 1, 1, 0, 0, 1},
			new int[]{1, 1, 0, 0, 1, 0, 1, 0, 0, 1},
			new int[]{1, 0, 1, 0, 0, 1, 1, 0, 0, 1},
			new int[]{1, 0, 1, 1, 0, 0, 1, 1, 0, 1},
			new int[]{1, 0, 1, 1, 1, 0, 0, 1, 1, 1},
			new int[]{1, 0, 1, 0, 1, 1, 0, 0, 1, 1},
			new int[]{1, 0, 0, 0, 1, 1, 1, 0, 0, 1},
			new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};

	private final int[][] world;

	public TestWorld(int worldId) {
		if (worldId == 1)
			world = world1;
		else if (worldId == 2)
			world = world2;
		else if (worldId == 3)
			world = world3;
		else if (worldId == 4)
			world = world4;
		else
			throw new IllegalArgumentException();
	}

	public boolean isValidPosition(int x, int y) {
		try {
			@SuppressWarnings("unused")
			int pos = world[x][y];
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	public boolean isWall(int x, int y) {
		return world[x][y] > 0;
	}

}
