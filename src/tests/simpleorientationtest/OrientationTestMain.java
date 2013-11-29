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

import be.stevenroose.javastar.AstarCalculator;
import be.stevenroose.javastar.AstarHelper;
import be.stevenroose.javastar.AstarResult;
import tests.simpletest.TestCost;

public class OrientationTestMain {

	AstarHelper<TestNode, TestCost> expander;

	public OrientationTestMain() {
		expander = new TestNodeExpander(
				new TestNode(1, 1, Orientation.UP), new TestNode(8, 8, Orientation.DOWN), 4);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		OrientationTestMain main = new OrientationTestMain();
		main.run();
	}

	public void run() {
		AstarCalculator<TestNode, TestCost> calculator =
				new AstarCalculator<TestNode, TestCost>(expander);
		calculator.run();
		AstarResult<TestNode, TestCost> result = calculator.getResult();
		System.out.println(result.toString());
		System.out.println(result.getNumberOfNodesExpanded());
		System.out.println(result.getCalculationTime());
	}

}
