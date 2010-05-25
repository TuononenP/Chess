/*
	Copyright (C) 2010 Petri Tuononen

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package tools;

/**
 * @author Petri Tuononen
 * @since 24/11/2009
 * <br>
 * Info: A very simple but useful data type. It has attributes
 * 		 x&y which can represent a coordinate.
 */
public class Index {

	//global variables
	private int x, y;
	
	/**
	 * Constructor.
	 * @param x
	 * @param y
	 */
	public Index(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Get value of x.
	 * @return x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Get value of y.
	 * @return y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Set value for x.
	 * @param x
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Set value of y.
	 * @param y
	 */
	public void setY(int y) {
		this.y = y;
	}
	
}
