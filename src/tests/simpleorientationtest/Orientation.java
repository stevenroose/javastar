
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

/**
 * This enumeration represents the possible orientations in a 2D-grid.
 *
 * @author Steven Roose & Erik De Smedt
 */
public enum Orientation {
	/**
	 * Heading north when the origin is north-west.
	 */
	UP(0),
	/**
	 * Heading east when the origin is north-west.
	 */
	RIGHT(1),
	/**
	 * Heading south when the origin is north-west.
	 */
	DOWN(2),
	/**
	 * Heading west when the origin is north-west.
	 */
	LEFT(3);

	private int code;

	private Orientation(int code) {
		// Normally no other values than 0, 1, 2, 3 are entered for code,
		// but the procedure (code % 4 + 4) % 4 is used to be 100% sure a right code value is set.
		this.code = (code % 4 + 4) % 4;
	}

	/**
	 * Returns the integer value that is equivalent to this orientation.
	 *
	 * @return The integer value that is equivalent to this orientation
	 *         | (this == UP)    => result == 0
	 *         | (this == RIGHT) => result == 1
	 *         | (this == DOWN)  => result == 2
	 *         | (this == LEFT)  => result == 3
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Returns the Orientation value that is equivalent to integer value <code>code</code>.
	 *
	 * @param code The integer value of which the equivalent Orientation will be returned.
	 * @return The Orientation value that is equivalent to integer value <code>code</code>
	 *         | result.getCode() == (code % 4 + 4) % 4
	 */
	public static Orientation fromCode(int code) {
		// (code % 4 + 4) % 4 is used because sometimes java gives negative modulo values.
		// f.e. -1 % 4 sometimes gives -1 and sometimes 3. 
		// By adding 4 and performing another modulo operation, this issue is fixed. 
		code = (code % 4 + 4) % 4;
		switch (code) {
			case 0:
				return Orientation.UP;
			case 1:
				return Orientation.RIGHT;
			case 2:
				return Orientation.DOWN;
			case 3:
				return Orientation.LEFT;
		}
		// (code % 4 + 4) % 4 always is a value between 0 and 3, 
		// so this place is never reached
		return null;
	}

	/**
	 * Returns the orientation that this orientation will be when turning clockwise.
	 *
	 * @return The orientation that this orientation will be when turning clockwise
	 *         | result == fromCode(this.getCode() + 1)
	 */
	public Orientation clockwise() {
		return fromCode(this.getCode() + 1);
	}

	/**
	 * Returns the orientation that this orientation will be when turning counterclockwise.
	 *
	 * @return The orientation that this orientation will be when turning counterclockwise
	 *         | result == fromCode(this.getCode() - 1)
	 */
	public Orientation counterclockwise() {
		return fromCode(this.getCode() - 1);
	}
}







