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

/**
 * This class is used to represent costs and heuristic values.
 * The reason that this class is not an interface is to force overriding of 
 * the <code>equals()</code> and the <code>compareTo()</code> methods.
 * <br />
 * It is advised to make your <code>Cost</code> implementation an Immutable Class.
 * 
 * @author 	Steven Roose
 * @license	Apache License, Version 2.0
 *
 */
public interface Cost<MyCost> extends Comparable<MyCost> {
	
	/**
	 * Add another cost to this one. The result will be the sum of both.
	 * 
	 * @param 	cost
	 * 			the cost to add to this cost
	 * 
	 * @return	the sum of this cost with <code>cost</code>
	 * 			<br />| <code>result == this + cost</code>
	 */
	public MyCost add(MyCost cost);
	
	@Override
	public int compareTo(MyCost other);
}
