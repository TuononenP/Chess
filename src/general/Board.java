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

import tools.Index;

/**
 * @author Petri Tuononen
 * @since 24/11/2009
 * notice: it's important to notice that x is equal to board row
 *         and y is equal to board column.
 * modifications 8th May 2010
 * -enabled initBoard() in constructor
 */
public class Board {

	//global variables
	private Piece[][] board = new Piece[8][8]; //to show game status in text based version
	private ArrayList<Piece> pieces = new ArrayList<Piece>(32); //create 32 pieces, 16 for both players
	
	/**
	 * Constructor.
	 */
	public Board() {
		initBoard(); //places pieces on the board 
	}
	
//	/**
//	 * Shows the current game state which is 8x8 board with pieces on it.
//	 * For text based version.
//	 */
//	public void showGameState() {
//		System.out.println();
//		System.out.println("###########################");
//		for (int i=0; i<=7; i++) {
//			System.out.print("#|"); //all rows begin with a divider
//			for (int j=0; j<=7; j++) {
//				if (board[i][j] == null) { //if empty square
//					System.out.print("  "); //print empty string
//					System.out.print("|"); //print divider
//				} else {
//					System.out.print(board[i][j].getAcronym()); //print acronym
//					System.out.print("|"); //print divider
//				}
//			}
//			System.out.println("#"); //next row
//			if (i!=7) {
//				System.out.println("#|-----------------------|#"); //line break between rows
//			}
//		}
//		System.out.println("###########################");
//		System.out.println();
//	}
	
//	/**
//	 * Shows the current game state which is 8x8 board with pieces on it.
//	 * Shows notation on each side.
//	 * For text based version.
//	 */
//	public void showGameStateWithNotation() {
//		System.out.println();
//		System.out.println("     a  b  c  d  e  f  g  h"); //alphabetic notation
//		System.out.println("  ###########################");
//		for (int i=0; i<=7; i++) { //rows
//			System.out.print(8-i+" #|"); //all rows begin with a divider
//			for (int j=0; j<=7; j++) { //columns
//				if (board[i][j] == null) { //if empty square
//					System.out.print("  "); //print empty string
//					System.out.print("|"); //print divider
//				} else {
//					System.out.print(board[i][j].getAcronym()); //print acronym
//					System.out.print("|"); //print divider
//				}
//			}
//			System.out.println("# " +(8-i)); //next row
//			if (i!=7) {
//				System.out.println("  #|-----------------------|#"); //linebreak between rows
//			}
//		}
//		System.out.println("  ###########################");
//		System.out.println("     a  b  c  d  e  f  g  h"); //alphabetic notation
//		System.out.println();
//	}
	
	/**
	 * Forms notation from x and y.
	 * @param x
	 * @param y
	 * @return string
	 */
	public String coordinatesToNotation(int x, int y) {
		int number = 0;
		char letter = 'z';
		String notation;
		
		switch(x) {
		case 0:
			number=8;
			break;
		case 1:
			number=7;
			break;
		case 2:
			number=6;
			break;
		case 3:
			number=5;
			break;
		case 4:
			number=4;
			break;
		case 5:
			number=3;
			break;
		case 6:
			number=2;
			break;
		case 7:
			number=1;
			break;
		default:
			break;
		}
		
		switch(y) {
		case 0:
			letter='a';
			break;
		case 1:
			letter='b';
			break;
		case 2:
			letter='c';
			break;
		case 3:
			letter='d';
			break;
		case 4:
			letter='e';
			break;
		case 5:
			letter='f';
			break;
		case 6:
			letter='g';
			break;
		case 7:
			letter='h';
			break;
		default:
			break;
		}
		
		notation = letter+Integer.toString(number);
		return notation;
	}
	
	/**
	 * Converts e.g. c5 to row4 col2 so that saves row and column number to Index object.
	 * @param notation
	 * @return Index
	 */
	public Index notationToIndex(String notation) {
		char letter;
		char number;
		int x=0, y=0;
		letter = notation.charAt(0); //save 1st char from notation as a char
		number = notation.charAt(1); //save 2nd char from notation as an integer
		
		switch(letter) {
		case 'a':
			y=0;
			break;
		case 'b':
			y=1;
			break;
		case 'c':
			y=2;
			break;
		case 'd':
			y=3;
			break;
		case 'e':
			y=4;
			break;
		case 'f':
			y=5;
			break;
		case 'g':
			y=6;
			break;
		case 'h':
			y=7;
			break;
		default:
			break;
		}
		
		switch(number) {
		case '1':
			x=7;
			break;
		case '2':
			x=6;
			break;
		case '3':
			x=5;
			break;
		case '4':
			x=4;
			break;
		case '5':
			x=3;
			break;
		case '6':
			x=2;
			break;
		case '7':
			x=1;
			break;
		case '8':
			x=0;
			break;
		default:
			break;
		}
		
		Index index = new Index(x, y); 
		return index;
	}

	/**
	 * Get piece based on notation.
	 * @param notation
	 * @return Piece
	 */
	public Piece notationToPiece(String notation) {
		boolean pieceFound = false;
		//find the piece based on the source square notation.
		Index srcCrd = notationToIndex(notation); //convert notation to array coordinates
		ArrayList<Piece> pieces = getPieces(); //save all pieces into a arraylist
		Piece piece = null;
		//loop arraylist of pieces
		findPiece: for (Piece p : pieces) { 
		    piece = p;
		    //if piece row and column mathes given piece destination 
		    if (piece.getRow() == srcCrd.getX() && piece.getCol() == srcCrd.getY()) {
		    	pieceFound = true;
		    	break findPiece; //move out of the loop
		    }
		}
		if (pieceFound) {
			return piece;
		} else {
			return null;
		}
	}
	
	/**
	 * Removes piece from the board.
	 * @param x
	 * @param y
	 */
	public void removePiece(int x, int y) {
		ArrayList<Piece> pieces = getPieces(); //get arraylist containing pieces
		for (Piece p : pieces) { //iterate arraylist
		    if (p.getRow()==x && p.getCol()==y) { //piece found
		    	pieces.remove(p); //remove piece
		    	setPieces(pieces); //copy over new arraylist of pieces
		    	updateGameState(); //update board 
		    	break;
		    }
		}
	}
	
	/**
	 * Removes piece from the board.
	 * @param notation
	 */
	public void removePiece(String notation) {
		Piece piece = notationToPiece(notation); //get the right piece
		ArrayList<Piece> pieces = getPieces(); //get arraylist containing pieces
		for (Piece p : pieces) { //iterate arraylist
		    if (p.getRow()==piece.getRow() && p.getCol()==piece.getCol()) { //piece found
		    	pieces.remove(piece); //remove piece from the arraylist
		    	setPieces(pieces); //copy over new arraylist of pieces
		    	updateGameState(); //update board
		    	break;
		    }
		}
	}
	
	/**
	 * Add new piece on the board
	 * @param p
	 */
	public void addPiece(Piece p) {
		ArrayList<Piece> pieces = getPieces(); //get current pieces
		pieces.add(p); //add new piece to arraylist
		setPieces(pieces); //set new arraylist
		updateGameState();
	}
	
	/**
	 * Clear the board from pieces.
	 * For text based version.
	 */
	public void emptyBoard() {
		for (int i=0; i<8; i++) { //rows
			for (int j=0; j<8; j++) { //columns
				board[i][j] = null;
			}
		}
	}
	
	/**
	 * Saves game state to a file.
	 */
	public void saveGame() {
		
	}
	
	/**
	 * Loads game state from a file.
	 */
	public void loadGame() {
		
	}
	
	/**
	 * Add pieces to arraylist.
	 */
	public void createPieces() {
		for (int i=0; i<8; i++) { //add eight pawns for both colors
			pieces.add(new Piece(1, Piece.PAWN, 1, i)); //black pawns to 2nd highest row 
			pieces.add(new Piece(0, Piece.PAWN, 6, i)); //white pawns to 2nd lowest row
		}
		//populate rooks
		pieces.add(new Piece(1, Piece.ROOK, 0, 0)); //black rook on upper left
		pieces.add(new Piece(1, Piece.ROOK, 0, 7)); //black rook on upper right
		pieces.add(new Piece(0, Piece.ROOK, 7, 0)); //white rook on down left
		pieces.add(new Piece(0, Piece.ROOK, 7, 7)); //white rook on down right
		//populate knights
		pieces.add(new Piece(1, Piece.KNIGHT, 0, 1)); //black knight on upper 2nd left
		pieces.add(new Piece(1, Piece.KNIGHT, 0, 6)); //black knight on upper 2nd right
		pieces.add(new Piece(0, Piece.KNIGHT, 7, 1)); //white knight on down 2nd left
		pieces.add(new Piece(0, Piece.KNIGHT, 7, 6)); //white knight on down 2nd right
		//populate bishops
		pieces.add(new Piece(1, Piece.BISHOP, 0, 2)); //black bishop on upper 3rd left
		pieces.add(new Piece(1, Piece.BISHOP, 0, 5)); //black bishop on upper 3rd right
		pieces.add(new Piece(0, Piece.BISHOP, 7, 2)); //white bishop on down 3rd left
		pieces.add(new Piece(0, Piece.BISHOP, 7, 5)); //white bishop on down 3rd right
		//populate queens
		pieces.add(new Piece(1, Piece.QUEEN, 0, 3)); //black queen on upper 4rd left
		pieces.add(new Piece(0, Piece.QUEEN, 7, 3)); //white queen on down 4rd left
		//populate kings
		pieces.add(new Piece(1, Piece.KING, 0, 4)); //black king on upper 5th left
		pieces.add(new Piece(0, Piece.KING, 7, 4)); //white king on down 5th left
	}
	
	/**
	 * Use arraylist to populate board.
	 * For text based version.
	 */
	private void populateBoard() {
		for (Piece p : pieces) { //white pieces left
		    board[p.getRow()][p.getCol()] = p; //place piece to board
		}	
	}
	
	/**
	 * Create piece objects and populate the board with them.
	 * For text based version.
	 */
	public void initBoard() {
//		emptyBoard(); //removes the board from pieces
		createPieces(); //creates piece objects
//		populateBoard(); //populates the board with piece objects
	}
	
	/**
	 * Updates the current game state.
	 * For text based version.
	 */
	public void updateGameState() {
		emptyBoard(); //removes the board from pieces
		populateBoard(); //populates the board with piece objects
	}
	
	/**
	 * Get all pieces as an arraylist.
	 */
	public ArrayList<Piece> getPieces() {
		return pieces;
	}

	/**
	 * Get pieces from only one black or white as an arraylist.
	 * @param color
	 */
	public ArrayList<Piece> getPiecesFromOneSide(int color) {
		ArrayList<Piece> sidePieces = new ArrayList<Piece>();
		//get all pieces
		ArrayList<Piece> pieces = getPieces();
		for (Piece p : pieces) {
			if (p.getColor() == color) {
				sidePieces.add(p);
			}
		}
		return sidePieces;
	}
	
	/**
	 * Set arraylist wich contains all pieces.
	 * @param pieces
	 */
	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}
	
	/**
	 * Promote piece with certain coordinates to certain type.
	 * @param x
	 * @param y
	 * @param type
	 */
	public void promotePiece(int x, int y, int type) {
		int i=0;
		ArrayList<Piece> pieces = getPieces(); //get arraylist containing pieces
		for (Piece p : pieces) { //iterate arraylist
		    if (p.getRow()==x && p.getCol()==y) { //piece found
		    	p.setType(type); //change piece type
		    	pieces.set(i, p); //replace piece in arraylist
		    	setPieces(pieces); //copy over new arraylist of pieces
		    	updateGameState(); //update board
		    	break;
		    }
		    i++;
		}
	}
	
	/**
	 * Promote all certain color pawns in enemy's end.
	 * @param color
	 */
	public void promotePawnsToQueen(int color) {
		int enemyEndRow;
		if (color==0) {
			enemyEndRow=0;
		} else {
			enemyEndRow=7;
		}
		//get all pieces
		ArrayList<Piece> pieces = getPieces();
		int i=0;
		for (Piece p : pieces) { //iterate arraylist
			//if pawn is on enemy's end and it's certain color
		    if (p.getRow()==enemyEndRow && p.getColor()==color && p.getType()==6) {
		    	p.setType(2); //change piece type to queen
		    	pieces.set(i, p); //replace piece in arraylist
		    	setPieces(pieces); //copy over new arraylist of pieces
		    	updateGameState(); //update board
		    	break;
		    }
		    i++;
		}
	}
	
	/**
	 * Returns piece with given coordinates and color.
	 * @param x
	 * @param y
	 * @param color
	 * @return Piece
	 */
	public Piece getPiece(int x, int y, int color) {
		//pieces from one color
		ArrayList<Piece> pieces = getPiecesFromOneSide(color);
		for (Piece p : pieces) {
			//if piece row and column mathes with the parameter values
			if (p.getRow()==x && p.getCol()==y) {
				return p;
			}
		}
		return new Piece();
	}
	
}
