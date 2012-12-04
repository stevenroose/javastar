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
 * Node classes should extend this abstract class. The reason this class exists is to
 * enforce users to override the <code>equals()</code> and <code>hashCode()</code> methods 
 * because the algorithm heavily relies on them.
 * <br />
 * Using good hashCode values means better performance of the algorithm.
 * 
 * @author 	Steven Roose
 * @license	Apache License, Version 2.0
 *
 */
public abstract class Node {
	
	@Override
	public abstract boolean equals(Object obj);
	
	@Override
	public abstract int hashCode();
	
}
