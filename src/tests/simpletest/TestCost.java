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

import com.googlecode.javastar.Cost;

public class TestCost implements Cost<TestCost> {

	private double value;

	public TestCost(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	@Override
	public TestCost add(TestCost cost) {
		return new TestCost(getValue() + cost.getValue());
	}

	@Override
	public int compareTo(TestCost other) {
		if (getValue() > other.getValue())
			return 1;
		if (getValue() < other.getValue())
			return -1;
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		try {
			return ((TestCost) obj).getValue() == getValue();
		} catch (ClassCastException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
	}

	@Override
	public String toString() {
		return "TestCost(" + Math.round(getValue()) + ")";
	}

}
