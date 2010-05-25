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
package general;
import java.util.*;

import player.Player;

/**
 * @author Petri Tuononen
 * @since 28/11/2009
 * <br>
 * Info: Saves white and black piece movement notation from source to destination.
 * 		 Two ArrayLists for each piece color. One for source and another for destination.
 */
public class HistoryOfMoves {
 
	//global variables
	private ArrayList<String> whiteSrc = new ArrayList<String>();
	private ArrayList<String> whiteDest = new ArrayList<String>();
	private ArrayList<String> blackSrc = new ArrayList<String>();
	private ArrayList<String> blackDest = new ArrayList<String>();
	
	/**
	 * Get white piece source arraylist.
	 * @return whiteSrc
	 */
	public ArrayList<String> getWhiteSrc() {
		return whiteSrc;
	}
	
	/**
	 * Sets a new source arraylist for white side.
	 * @param whiteSrc
	 */
	public void setWhiteSrc(ArrayList<String> whiteSrc) {
		this.whiteSrc = whiteSrc;
	}
	
	/**
	 * Get white piece destination arraylist.
	 * @return whiteDest
	 */
	public ArrayList<String> getWhiteDest() {
		return whiteDest;
	}
	
	/**
	 * Sets a new destination arraylist for white side.
	 * @param whiteDest
	 */
	public void setWhiteDest(ArrayList<String> whiteDest) {
		this.whiteDest = whiteDest;
	}
	
	/**
	 * Get black piece source arraylist.
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getBlackSrc() {
		return blackSrc;
	}
	
	/**
	 * Sets a new source arraylist for black side.
	 * @param blackSrc
	 */
	public void setBlackSrc(ArrayList<String> blackSrc) {
		this.blackSrc = blackSrc;
	}
	
	/**
	 * Get black piece destination arraylist.
	 * @return Arraylist<String>
	 */
	public ArrayList<String> getBlackDest() {
		return blackDest;
	}
	
	/**
	 * Sets a new destination arraylist for black side.
	 * @param blackDest
	 */
	public void setBlackDest(ArrayList<String> blackDest) {
		this.blackDest = blackDest;
	}
	
	/**
	 * Add a source for white piece.
	 * @param notation
	 */
	public void addWhiteSrc(String notation) {
		whiteSrc.add(notation);
	}
	
	/**
	 * Add a new destination for a white piece.
	 * @param notation
	 */
	public void addWhiteDest(String notation) {
		whiteDest.add(notation);
	}
	
	/**
	 * Add a source for black piece.
	 * @param notation
	 */
	public void addBlackSrc(String notation) {
		blackSrc.add(notation);
	}
	
	/**
	 * Add a new destination for a black piece.
	 * @param notation
	 */
	public void addBlackDest(String notation) {
		blackDest.add(notation);
	}
	
	/**
	 * Add white piece source and destination to history.
	 * @param src
	 * @param dest
	 */
	public void addWhiteMove(String src, String dest) {
		addWhiteSrc(src);
		addWhiteDest(dest);
	}
	
	/**
	 * Add black piece source and destination to history.
	 * @param src
	 * @param dest
	 */
	public void addBlackMove(String src, String dest) {
		addBlackSrc(src);
		addBlackDest(dest);
	}
	
	/**
	 * Prints out selected player's history of movements.
	 * Notation used is e.g. a7 -> a6
	 * @param player
	 */
	public void printOutMoves(Player player) {
		int side = player.getSide();
		ListIterator<String> iter;
		ListIterator<String> iter2;
		if (side==0) { //white
			iter = getWhiteSrc().listIterator();
			iter2 = getWhiteDest().listIterator();
		} else { //black
			iter = getBlackSrc().listIterator();
			iter2 = getBlackDest().listIterator();
		}
		while (iter.hasNext() && iter2.hasNext()) {
			System.out.println(iter.next()+" -> "+iter2.next());
		}
	}
	
	/**
	 * Print out both players moves.
	 */
	public void printOutMoves() {
		ListIterator<String> iter, iter2, iter3, iter4;
		iter = getWhiteSrc().listIterator();
		iter2 = getWhiteDest().listIterator();
		iter3 = getBlackSrc().listIterator();
		iter4 = getBlackDest().listIterator();
		int i=1;
		String wSrc, wDest, bSrc, bDest;
		Board board = new Board();
		String wType, bType;
		while (iter.hasNext() && iter4.hasNext()) {
			wSrc=iter.next();
			wDest=iter2.next();
			bSrc=iter3.next();
			bDest=iter4.next();
			wType= board.notationToPiece(wSrc).getAcronym();
			bType= board.notationToPiece(bSrc).getAcronym();
			System.out.println(i+". "+wType+": "+wSrc+"-"+wDest+", "+bType+": "+bSrc+"-"+bDest);
			i++;
		}
	}
	
}
