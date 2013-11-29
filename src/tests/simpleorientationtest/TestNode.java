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

package tests.simpleorientationtest;

import be.stevenroose.javastar.Node;

public class TestNode implements Node {

	private int x;
	private int y;
	private Orientation orientation;

	public TestNode(int x, int y, Orientation orientation) {
		this.x = x;
		this.y = y;
		this.orientation = orientation;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Orientation getOrientation() {
		return orientation;
	}

	@Override
	public boolean equals(Object obj) {
		try {
			TestNode n = (TestNode) obj;
			return n.getX() == getX() &&
					n.getY() == getY() &&
					n.getOrientation() == getOrientation();
		} catch (ClassCastException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		return "TestNode(" + x + "," + y + "," + orientation.toString() + ")";
	}

}
