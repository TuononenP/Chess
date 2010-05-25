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
package player;

import general.Piece;

/**
 * @author Petri Tuononen
 * @since 26/11/2009
 */
public class Player {

	//global variables
	private int side; //white 0, black 1

	/**
	 * Default constructor.
	 */
	public Player() {
		
	}
	
	/**
	 * Constructor which allows to set side.
	 * @param side
	 */
	public Player(int side) {
		setSide(side);
	}
	
	/**
	 * Get side. Black or white.
	 * @return side
	 */
	public int getSide() {
		return side;
	}

	/**
	 * Set side. Black or white.
	 * @param side
	 */
	public void setSide(int side) {
		this.side = side;
	}
	
	/**
	 * Get enemy side.
	 * @return int
	 */
	public int getEnemySide() {
		if (getSide()==0) {
			return 1;
		} else {
			return 0;
		}
	}
	
	/**
	 * Find out if piece is owned by player.
	 * @param piece
	 * @return boolean
	 */
	public boolean isOwnPiece(Piece piece) {
		if (piece.getColor() == getSide()) {
			return true;
		} else {
			return false;
		}
	}
	
}
