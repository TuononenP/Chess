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
import tools.Index;

/**
 * 
 * @author Petri Tuononen
 * @since 26/11/2009
 */
public class Move {

	//global variables
	private Board board;
	private HistoryOfMoves history;
	
	/**
	 * Constructor
	 * @param board
	 */
	public Move(Board board) {
		this.board = board;
		history = new HistoryOfMoves();
	}

	private Board getBoard() {
		return board;
	}

	/**
	 * Returns history of all piece moves.
	 * @return HistoryOfMoves
	 */
	public HistoryOfMoves getHistoryOfMoves() {
		return history;
	}
	
	/**
	 * Set history of all piece moves.
	 * @param history
	 */
	public void setHistoryOfMoves(HistoryOfMoves history) {
		this.history=history;
	}
	
	/**
	 * Move piece in selected square to destination square.
	 * Returns true if the move was successful.
	 * @param player
	 * @param srcSq
	 * @param destSq
	 * @return boolean
	 */
	public boolean doMove(Player player, String srcSq, String destSq) {
		//check source square notation for validity
		if (checkSqValidity(srcSq)) {
			//check destination square notation for validity
			if (checkSqValidity(destSq)) {
				//find the piece based on the source square notation.
				Piece piece = getBoard().notationToPiece(srcSq);
				//make sure the piece is owned by the player
				if (piece.getColor()==player.getSide()) {
					//get all movements that are allowed for the selected piece
					ArrayList<ArrayList<Integer>> legalMoves = possiblePieceMoves(piece, false);
					//array coordinates for new destination
					Index newLoc = getBoard().notationToIndex(destSq);
					//find out if destination location is included in the legal moves list
					ArrayList<Integer> x = legalMoves.get(0); //list of row numbers
					ArrayList<Integer> y = legalMoves.get(1); //list of column numbers
					ListIterator<Integer> xList = x.listIterator();  //row iterator
					ListIterator<Integer> yList = y.listIterator();  //column iterator
					int xL, yL;
					while (xList.hasNext() && yList.hasNext()) { //while lists have coordinates
						//listiterator next() method doesn't work inside if statement -> assign to variables
						xL = xList.next();
						yL = yList.next();
						if (newLoc.getX()==xL && newLoc.getY()==yL) { //legal move
							getBoard().removePiece(newLoc.getX(), newLoc.getY()); //remove captured piece from the board
							piece.setRow(newLoc.getX()); //change piece row
							piece.setCol(newLoc.getY()); //change piece column
//							board.updateGameState(); //populate the board with new location of pieces.
							//place source and destination square to history of moves
							if (player.getSide()==0) { //if white
								getHistoryOfMoves().addWhiteMove(srcSq, destSq); //add white piece move to history
							} else if (player.getSide()==1) { //if black
								getHistoryOfMoves().addBlackMove(srcSq, destSq); //add black piece move to history
							}
							//promote pawns to queens if they reach enemy's end
							getBoard().promotePawnsToQueen(player.getSide());
							return true; //move successful
						}
					}
				}
			} else {
				System.out.println("Not a valid destination square. ");
			}
		} else {
			System.out.println("Not a valid source notation.");
		}
		return false; //move failed, not own piece
	}
	
	/**
	 * Checks if the given notation is valid.
	 * @param sq
	 * @return boolean
	 */
	public boolean checkSqValidity(String sq) {
		char[] validColumns = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
		char[] validRows = {'1', '2', '3', '4', '5', '6', '7', '8'};
		if (sq.length()==2) {
			char letter = sq.charAt(0); //save 1st char from notation as a char
			char number = sq.charAt(1); //save 2nd char from notation as an integer
			for (char c : validColumns) { 
				//if letter used in notation is valid
				if (c==letter) {
					for (char r : validRows) { 
						//if number used in notation is valid
						if (r==number) {
							return true;
						}
					}
				}
			}			
		}
		return false;
	}
	
	/**
	 * Returns true if the piece is found from the given location.
	 * @param x
	 * @param y
	 * @return boolean
	 */
	public boolean isPieceOnSquare(int x, int y) {
		Piece piece;
		for (Piece p : getBoard().getPieces()) { //pieces left
			piece = p; //get next piece
	    	//if piece row&col numbers match to parameters
		    if (piece.getRow() == x && piece.getCol() == y) { 
		    	return true; //piece found
		    }
		}
		return false; //piece not found
	}
	
	/**
	 * Returns true if the piece is found from the given location.
	 * @param notation
	 * @return boolean
	 */
	public boolean isPieceOnSquare(String notation) {
		Index index = getBoard().notationToIndex(notation);
		Piece piece;
		for (Piece p : getBoard().getPieces()) {
		    piece = p;
		    if (piece.getRow() == index.getX() && piece.getCol() == index.getY()) {
		    	return true;
		    }
		}
		return false; //piece not found
	}
	
	/**
	 * Returns true if piece with defined color is found from the given location.
	 * @param x
	 * @param y
	 * @param color
	 * @return boolean
	 */
	public boolean isPieceOnSquare(int x, int y, int color) {
		for (Piece p : getBoard().getPieces()) {
		    if (p.getColor() == color) {
			    if (p.getRow() == x && p.getCol() == y) {
			    	return true;
			    }
		    }
		}
		return false; //piece not found
	}
	
	/**
	 * Check is King piece is found from the square.
	 * @param x
	 * @param y
	 * @param color
	 * @return
	 */
	public boolean isKingOnSquare(int x, int y, int color) {
		for (Piece p : getBoard().getPieces()) {
		    if (p.getColor() == color) {
			    if (p.getRow() == x && p.getCol() == y) {
			    	if (p.getType()==1) {
			    		return true;
			    	}
			    }
		    }
		}
		return false; //piece not found
	}
	
	/**
	 * Returns true if piece with defined color is found from the given location.
	 * @param notation
	 * @param color
	 * @return boolean
	 */
	public boolean isPieceOnSquare(String notation, int color) {
		Index index = getBoard().notationToIndex(notation);
		for (Piece p : getBoard().getPieces()) {
		    if (p.getColor() == color) {
			    if (p.getRow() == index.getX() && p.getCol() == index.getY()) {
			    	return true;
			    }
		    }
		}
		return false;
	}
	
	/**
	 * On the board restriction. Checks that piece is moved
	 * within the 8x8 limits.	
	 * @param x
	 * @param y
	 * @return
	 */
	private Boolean isOnBoard(int x, int y) {
		if ((x>=0 && x<=7) && (y>=0&&y<=7)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Get all possible movements for the bishop piece.
	 * X and Y coordinates are saved in separate arraylists.
	 * Returns an arraylist containing x&y arraylists.
	 * @param piece
	 * @return
	 */
	private ArrayList<ArrayList<Integer>> possibleBishopMvmts(Piece piece, boolean kingCapture) {
		ArrayList<Integer> mvmtX, mvmtY; //lists for x&y coordinates
		  							     //for possible movements
		mvmtX = new ArrayList<Integer>(); //list for x coordinates
		mvmtY = new ArrayList<Integer>(); //list for y coordinates
		//arraylist containing x&y arraylists
		ArrayList<ArrayList<Integer>> xAndY = new ArrayList<ArrayList<Integer>>();
		int x = piece.getRow();
		int y = piece.getCol();
		int color = piece.getColor();
		int opponentColor;
		if (color==0) {
			opponentColor=1;
		} else {
			opponentColor=0;
		}
		/*
		 * 4 diagonal directions
			-up-right  
			-up-left   
			-down-right 
			-down-left 
		 */
		//up-right
		int i=x, j=y;
		i--; j++;
		while (i>=0 && j<=7) {
			//if own piece on the square
			if (isPieceOnSquare(i, j, color)) { //break, don't add coordinates
				break;				
			//if opponent's piece on the square	
			} else if (isPieceOnSquare(i, j, opponentColor)) { //add coordinates and break 
				if (kingCapture) {
					mvmtX.add(i); mvmtY.add(j);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(i, j, opponentColor)) {
						mvmtX.add(i); mvmtY.add(j);	
					}
				}
				break;				
			} else { //add coordinates
				mvmtX.add(i); mvmtY.add(j);	
			}
			i--; j++;
		}
		//up-left
		i=x;
		j=y;
		i--; j--;
		while (i>=0 && j>=0) {
			//if own piece on the square
			if (isPieceOnSquare(i, j, color)) { //break, don't add coordinates
				break;				
			//if opponent's piece on the square	
			} else if (isPieceOnSquare(i, j, opponentColor)) { //add coordinates and break 
				if (kingCapture) {
					mvmtX.add(i); mvmtY.add(j);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(i, j, opponentColor)) {
						mvmtX.add(i); mvmtY.add(j);	
					}
				}
				break;				
			} else { //add coordinates
				mvmtX.add(i); mvmtY.add(j);				
			}
			i--; j--;
		}
		//down-right
		i=x;
		j=y;
		i++; j++;
		while (i<=7 && j<=7) {
			//if own piece on the square
			if (isPieceOnSquare(i, j, color)) { //break, don't add coordinates
				break;				
			//if opponent's piece on the square	
			} else if (isPieceOnSquare(i, j, opponentColor)) { //add coordinates and break 
				if (kingCapture) {
					mvmtX.add(i); mvmtY.add(j);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(i, j, opponentColor)) {
						mvmtX.add(i); mvmtY.add(j);	
					}
				}
				break;				
			} else { //add coordinates
				mvmtX.add(i); mvmtY.add(j);				
			}
			i++; j++;
		}
		//down-left
		i=x;
		j=y;
		i++; j--;
		while (i<=7 && j>=0) {
			//if own piece on the square
			if (isPieceOnSquare(i, j, color)) { //break, don't add coordinates
				break;				
			//if opponent's piece on the square	
			} else if (isPieceOnSquare(i, j, opponentColor)) { //add coordinates and break 
				if (kingCapture) {
					mvmtX.add(i); mvmtY.add(j);
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(i, j, opponentColor)) {
						mvmtX.add(i); mvmtY.add(j);	
					}
				}
				break;				
			} else { //add coordinates
				mvmtX.add(i); mvmtY.add(j);				
			}
			i++; j--;
		}
		xAndY.add(mvmtX);
		xAndY.add(mvmtY);
		return xAndY;
	}
	
	/**
	 * Get all possible movements for the rook piece.
	 * X and Y coordinates are saved in separate arraylists.
	 * Returns an arraylist containing x&y arraylists.
	 * @param piece
	 * @return
	 */
	private ArrayList<ArrayList<Integer>> possibleRookMvmts(Piece piece, boolean kingCapture) {
		//possible movements horizontal and vertical within board 8x8 limits
		//(i, 0<=j<8) horizontal
		//(0<=i<8, j) vertical
		/*
		 * 4 directions
		 * up
		 * right
		 * down
		 * left
		 */
		int x = piece.getRow();
		int y = piece.getCol();
		ArrayList<Integer> mvmtX, mvmtY; //lists for x&y coordinates
										 //for possible movements
		mvmtX = new ArrayList<Integer>(); //list for x coordinates
		mvmtY = new ArrayList<Integer>(); //list for y coordinates
		//arraylist containing x&y arraylists
		ArrayList<ArrayList<Integer>> xAndY = new ArrayList<ArrayList<Integer>>(); 
		int opponentColor;
		if (piece.getColor()==0) {
			opponentColor=1;
		} else {
			opponentColor=0;
		}
		int i, j;
		//up
		i=x;
		i--;
		while (i>=0) {
			//if own piece on the square
			if (isPieceOnSquare(i, y, piece.getColor())) { //break, don't add coordinates
				break;	
			//if opponent's piece on the square
			} else if (isPieceOnSquare(i, y, opponentColor)) { //add coordinates and break 
				if (kingCapture) {
					mvmtX.add(i); mvmtY.add(y);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(i, y, opponentColor)) {
						mvmtX.add(i); mvmtY.add(y);	
					}
				}
				break;		
			} else {
				mvmtX.add(i); mvmtY.add(y);	
			}
			i--;
		}
		//down
		i=x;
		i++;
		while (i<=7) {
			//if own piece on the square
			if (isPieceOnSquare(i, y, piece.getColor())) { //break, don't add coordinates
				break;	
			//if opponent's piece on the square
			} else if (isPieceOnSquare(i, y, opponentColor)) { //add coordinates and break 
				if (kingCapture) {
					mvmtX.add(i); mvmtY.add(y);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(i, y, opponentColor)) {
						mvmtX.add(i); mvmtY.add(y);	
					}
				}
				break;		
			} else {
				mvmtX.add(i); mvmtY.add(y);	
			}
			i++;
		}
		//right
		j=y;
		j++;
		while (j<=7) {
			//if own piece on the square
			if (isPieceOnSquare(x, j, piece.getColor())) { //break, don't add coordinates
				break;	
			//if opponent's piece on the square
			} else if (isPieceOnSquare(x, j, opponentColor)) { //add coordinates and break 
				if (kingCapture) {
					mvmtX.add(x); mvmtY.add(j);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(x, j, opponentColor)) {
						mvmtX.add(x); mvmtY.add(j);	
					}
				}
				break;		
			} else {
				mvmtX.add(x); mvmtY.add(j);	
			}
			j++;
		}
		//left
		j=y;
		j--;
		while (j>=0) {
			//if own piece on the square
			if (isPieceOnSquare(x, j, piece.getColor())) { //break, don't add coordinates
				break;	
			//if opponent's piece on the square
			} else if (isPieceOnSquare(x, j, opponentColor)) { //add coordinates and break 
				if (kingCapture) {
					mvmtX.add(x); mvmtY.add(j);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(x, j, opponentColor)) {
						mvmtX.add(x); mvmtY.add(j);	
					}
				}
				break;		
			} else {
				mvmtX.add(x); mvmtY.add(j);	
			}
			j--;
		}
		xAndY.add(mvmtX);
		xAndY.add(mvmtY);
		return xAndY;
	}
	
	/**
	 * Get all possible movements for the knight piece.
	 * X and Y coordinates are saved in separate arraylists.
	 * Returns an arraylist containing x&y arraylists.
	 * @param piece
	 * @return
	 */
	private ArrayList<ArrayList<Integer>> possibleKnightMvmts(Piece piece, boolean kingCapture) {
		ArrayList<Integer> mvmtX, mvmtY; //lists for x&y coordinates
										 //for possible movements
		mvmtX = new ArrayList<Integer>(); //list for x coordinates
		mvmtY = new ArrayList<Integer>(); //list for y coordinates
		//arraylist containing x&y arraylists
		ArrayList<ArrayList<Integer>> xAndY = new ArrayList<ArrayList<Integer>>();
		int x = piece.getRow();
		int y = piece.getCol();
		//get opponent color
		int opponentColor;
		if (piece.getColor()==0) {
			opponentColor=1;
		} else {
			opponentColor=0;
		}
		//possible movements for the knight piece.
		//up-right
		//1 up, 2 right (x+2, y-1)
		if (isOnBoard(x+2, y-1)) {
			//if no own piece on the square
			if (!isPieceOnSquare(x+2, y-1, piece.getColor())) {
				if (kingCapture) {
					mvmtX.add(x+2); mvmtY.add(y-1);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(x+2, y-1, opponentColor)) {
						mvmtX.add(x+2); mvmtY.add(y-1);	
					}
				}
			}
		}
		//2 up, 1 right (x+1, y-2)
		if (isOnBoard(x+1, y-2)) {
			//if no own piece on the square
			if (!isPieceOnSquare(x+1, y-2, piece.getColor())) {
				if (kingCapture) {
					mvmtX.add(x+1); mvmtY.add(y-2);
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(x+1, y-2, opponentColor)) {
						mvmtX.add(x+1); mvmtY.add(y-2);
					}
				}
			}
		}
		//up-left
		//1 up, 2 left (x-2, y-1)
		if (isOnBoard(x-2, y-1)) {
			//if no own piece on the square
			if (!isPieceOnSquare(x-2, y-1, piece.getColor())) {
				if (kingCapture) {
					mvmtX.add(x-2); mvmtY.add(y-1);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(x-2, y-1, opponentColor)) {
						mvmtX.add(x-2); mvmtY.add(y-1);	
					}
				}
			}
		}
		//2 up, 1 left (x-1, y-2)
		if (isOnBoard(x-1, y-2)) {
			//if no own piece on the square
			if (!isPieceOnSquare(x-1, y-2, piece.getColor())) {
				if (kingCapture) {
					mvmtX.add(x-1); mvmtY.add(y-2);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(x-1, y-2, opponentColor)) {
						mvmtX.add(x-1); mvmtY.add(y-2);	
					}
				}
			}
		}
		//down-right
		//1 down, 2 right (x+2, y+1)
		if (isOnBoard(x+2, y+1)) {
			//if no own piece on the square
			if (!isPieceOnSquare(x+2, y+1, piece.getColor())) {
				if (kingCapture) {
					mvmtX.add(x+2); mvmtY.add(y+1);
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(x+2, y+1, opponentColor)) {
						mvmtX.add(x+2); mvmtY.add(y+1);	
					}
				}
			}
		}
		//2 down, 1 right (x+1, y+2)
		if (isOnBoard(x+1, y+2)) {
			//if no own piece on the square
			if (!isPieceOnSquare(x+1, y+2, piece.getColor())) {
				if (kingCapture) {
					mvmtX.add(x+1); mvmtY.add(y+2);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(x+1, y+2, opponentColor)) {
						mvmtX.add(x+1); mvmtY.add(y+2);	
					}
				}
			}
		}
		//down-left
		//1 down, 2 left (x-2, y+1)
		if (isOnBoard(x-2, y+1)) {
			//if no own piece on the square
			if (!isPieceOnSquare(x-2, y+1, piece.getColor())) {
				if (kingCapture) {
					mvmtX.add(x-2); mvmtY.add(y+1);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(x-2, y+1, opponentColor)) {
						mvmtX.add(x-2); mvmtY.add(y+1);	
					}
				}
			}
		}
		//2 down, 1 left (x-1, y+2)
		if (isOnBoard(x-1, y+2)) {
			//if no own piece on the square
			if (!isPieceOnSquare(x-1, y+2, piece.getColor())) {
				if (kingCapture) {
					mvmtX.add(x-1); mvmtY.add(y+2);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(x-1, y+2, opponentColor)) {
						mvmtX.add(x-1); mvmtY.add(y+2);	
					}
				}
			}
		}
		xAndY.add(mvmtX);
		xAndY.add(mvmtY);
		return xAndY;
	}
	
	/**
	 * Get all possible movements for the knight piece.
	 * X and Y coordinates are saved in separate arraylists.
	 * Returns an arraylist containing x&y arraylists.
	 * @param piece
	 * @return
	 */
	private ArrayList<ArrayList<Integer>> possiblePawnMvmts(Piece piece, boolean kingCapture) {
		ArrayList<Integer> mvmtX, mvmtY; //lists for x&y coordinates
										 //for possible movements
		mvmtX = new ArrayList<Integer>(); //list for x coordinates
		mvmtY = new ArrayList<Integer>(); //list for y coordinates
		//arraylist containing x&y arraylists
		ArrayList<ArrayList<Integer>> xAndY = new ArrayList<ArrayList<Integer>>(); 
		int x = piece.getRow();
		int y = piece.getCol();
		//can only move forwards or diagonally forwards when
		//capturing other piece.
		//At start the 2nd row pawn can move 1 or 2 squares forward,
		//otherwise only 1 as long as there's no obstructing pieces.
		int opponentColor;
		if (piece.getColor()==0) {
			opponentColor = 1;
		} else {
			opponentColor = 0;
		}
		if (piece.getType()==6 && piece.getColor()==0) { //white pawn
			if (x==6) { //can move 1 or 2 squares forward
				if (!isPieceOnSquare(x-1, y)) { //if no obstructing piece
					mvmtX.add(x-1); mvmtY.add(y); //1 square forward
					if (!isPieceOnSquare(x-2, y)) { //if no obstructing piece
						mvmtX.add(x-2); mvmtY.add(y); //2 squares forward
					}
				}
			} else { //can move 1 square forward
				if (x-1>=0 && !isPieceOnSquare(x-1, y)) { //if no obstructing piece
					mvmtX.add(x-1); mvmtY.add(y);	
				}
			}
			//diagonal moves if opponent's piece can be captured
			//up-right if opponent's piece on the square
			if (isPieceOnSquare(x-1, y+1, opponentColor)) {
				if (kingCapture) {
					mvmtX.add(x-1); mvmtY.add(y+1);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(x-1, y+1, opponentColor)) {
						mvmtX.add(x-1); mvmtY.add(y+1);	
					}
				}
			}
			//up-left if opponent's piece on the square
			if (isPieceOnSquare(x-1, y-1, opponentColor)) { 
				if (kingCapture) {
					mvmtX.add(x-1); mvmtY.add(y-1);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(x-1, y-1, opponentColor)) {
						mvmtX.add(x-1); mvmtY.add(y-1);	
					}
				}
			}
		} else if (piece.getType()==6 && piece.getColor()==1) { //black pawn
			if (x==1) { //can move 1 or 2 squares forward
				if (!isPieceOnSquare(x+1, y)) { //if no obstructing piece
					mvmtX.add(x+1); mvmtY.add(y); //1 square forward						
					if (!isPieceOnSquare(x+2, y)) { //if no obstructing piece
						mvmtX.add(x+2); mvmtY.add(y); //2 squares forward							
					}
				}
			} else { //can move 1 square forward
				if (x+1<8 && !isPieceOnSquare(x+1, y)) { //if no obstructing piece
					mvmtX.add(x+1); mvmtY.add(y);						
				}
			}
			//diagonal moves if opponent's piece can be captured
			//if opponent's piece on the square
			if (isPieceOnSquare(x+1, y+1, opponentColor)) {
				if (kingCapture) {
					mvmtX.add(x+1); mvmtY.add(y+1);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(x+1, y+1, opponentColor)) {
						mvmtX.add(x+1); mvmtY.add(y+1);	
					}
				}
			}
			//if opponent's piece on the square
			if (isPieceOnSquare(x+1, y-1, opponentColor)) { 
				if (kingCapture) {
					mvmtX.add(x+1); mvmtY.add(y-1);	
				} else {
					//if enemy king is not on the square
					if (!isKingOnSquare(x+1, y-1, opponentColor)) {
						mvmtX.add(x+1); mvmtY.add(y-1);	
					}
				}
			}
		}
		xAndY.add(mvmtX);
		xAndY.add(mvmtY);
		return xAndY;
	}	
	
	/**
	 * Get all possible movements for the king piece.
	 * X and Y coordinates are saved in separate arraylists.
	 * Returns an arraylist containing x&y arraylists
	 * @param piece
	 * @return
	 */
	private ArrayList<ArrayList<Integer>> possibleKingMvmts(Piece piece, boolean kingCapture) {
		ArrayList<Integer> mvmtX, mvmtY; //lists for x&y coordinates
	     								 //for possible movements
		mvmtX = new ArrayList<Integer>(); //list for x coordinates
		mvmtY = new ArrayList<Integer>(); //list for y coordinates
		//arraylist containing x&y arraylists
		ArrayList<ArrayList<Integer>> xAndY = new ArrayList<ArrayList<Integer>>();
		int x = piece.getRow();
		int y = piece.getCol();
		int color = piece.getColor();
		int opponentColor;
		if (color==0) {
			opponentColor=1;
		} else {
			opponentColor=0;
		}
		//possible horizontal and lateral movements
		//horizontal
		//check that piece stays on the board and there's no own piece blocking the way
		if (isOnBoard(x, y+1) && !isPieceOnSquare(x, y+1, color)) { //right
			if (kingCapture) {
				mvmtX.add(x); mvmtY.add(y+1); //(x, y+1)
			} else {
				//if enemy king is not on the square
				if (!isKingOnSquare(x, y+1, opponentColor)) {
					mvmtX.add(x); mvmtY.add(y+1); //(x, y+1)
				}
			}
		}
		if (isOnBoard(x, y-1) && !isPieceOnSquare(x, y-1, color)) { //left
			if (kingCapture) {
				mvmtX.add(x); mvmtY.add(y-1); //(x, y-1)
			} else {
				//if enemy king is not on the square
				if (!isKingOnSquare(x, y-1, opponentColor)) {
					mvmtX.add(x); mvmtY.add(y-1); //(x, y-1)
				}
			}
		}
		//vertical
		if (isOnBoard(x-1, y) && !isPieceOnSquare(x-1, y, color)) { //up
			if (kingCapture) {
				mvmtX.add(x-1); mvmtY.add(y); //(x-1, y)
			} else {
				//if enemy king is not on the square
				if (!isKingOnSquare(x-1, y, opponentColor)) {
					mvmtX.add(x-1); mvmtY.add(y); //(x-1, y)
				}
			}
		}
		if (isOnBoard(x+1, y) && !isPieceOnSquare(x+1, y, color)) { //down
			if (kingCapture) {
				mvmtX.add(x+1); mvmtY.add(y); //(x+1, y)
			} else {
				//if enemy king is not on the square
				if (!isKingOnSquare(x+1, y, opponentColor)) {
					mvmtX.add(x+1); mvmtY.add(y); //(x+1, y)
				}
			}
		}
		//possible diagonal movements
		//up-right
		if (isOnBoard(x-1, y+1) && !isPieceOnSquare(x-1, y+1, color)) {
			if (kingCapture) {
				mvmtX.add(x-1); mvmtY.add(y+1); //(x-1, y+1)
			} else {
				//if enemy king is not on the square
				if (!isKingOnSquare(x-1, y+1, opponentColor)) {
					mvmtX.add(x-1); mvmtY.add(y+1); //(x-1, y+1)
				}	
			}
		}
		//up-left
		if (isOnBoard(x-1, y-1) && !isPieceOnSquare(x-1, y-1, color)) {
			if (kingCapture) {
				mvmtX.add(x-1); mvmtY.add(y-1); //(x-1, y-1)
			} else {
				//if enemy king is not on the square
				if (!isKingOnSquare(x-1, y-1, opponentColor)) {
					mvmtX.add(x-1); mvmtY.add(y-1); //(x-1, y-1)
				}
			}
		}
		//down-right
		if (isOnBoard(x+1, y+1) && !isPieceOnSquare(x+1, y+1, color)) {
			if (kingCapture) {
				mvmtX.add(x+1); mvmtY.add(y+1); //(x+1, y+1)
			} else {
				//if enemy king is not on the square
				if (!isKingOnSquare(x+1, y+1, opponentColor)) {
					mvmtX.add(x+1); mvmtY.add(y+1); //(x+1, y+1)
				}
			}
		}
		//down-left
		if (isOnBoard(x+1, y-1) && !isPieceOnSquare(x+1, y-1, color)) {
			if (kingCapture) {
				mvmtX.add(x+1); mvmtY.add(y-1); //(x+1, y-1)
			} else {
				//if enemy king is not on the square
				if (!isKingOnSquare(x+1, y-1, opponentColor)) {
					mvmtX.add(x+1); mvmtY.add(y-1); //(x+1, y-1)
				}
			}
		}
		xAndY.add(mvmtX);
		xAndY.add(mvmtY);
		return xAndY;
	}
	
	/**
	 * Get all possible movements for the queen piece.
	 * X and Y coordinates are saved in separate arraylists.
	 * Returns an arraylist containing x&y arraylists
	 * @param piece
	 * @return
	 */
	private ArrayList<ArrayList<Integer>> possibleQueenMvmts(Piece piece, boolean kingCapture) {
		ArrayList<Integer> mvmtX, mvmtY; //lists for x&y coordinates
				 						 //for possible movements
		mvmtX = new ArrayList<Integer>(); //list for x coordinates
		mvmtY = new ArrayList<Integer>(); //list for y coordinates
		ArrayList<ArrayList<Integer>> bishopXAndY, rookXAndY, xAndY;
		//arraylist containing x&y arraylists
		xAndY = new ArrayList<ArrayList<Integer>>();
		//combined bishop & rook movements
		//get bishop movement possibilities
		bishopXAndY = possibleBishopMvmts(piece, kingCapture);
		mvmtX.addAll(bishopXAndY.get(0));
		mvmtY.addAll(bishopXAndY.get(1));
		//get rook movement possibilities
		rookXAndY = possibleRookMvmts(piece, kingCapture);
		mvmtX.addAll(rookXAndY.get(0));
		mvmtY.addAll(rookXAndY.get(1));
		xAndY.add(mvmtX);
		xAndY.add(mvmtY);
		return xAndY;
	}
	
	/**
	 * Returns an arraylist containing two arraylists for possible
	 * coordinates the selected piece can move. 
	 * @param piece
	 * @return ArrayList<ArrayList<Integer>>
	 */
	public ArrayList<ArrayList<Integer>> possiblePieceMoves(Piece piece, boolean kingCapture) {
		int type;
		ArrayList<Integer> mvmtX, mvmtY; //lists for x&y coordinates
									     //for possible movements
		ArrayList<ArrayList<Integer>> kingXAndY, queenXAndY,
			bishopXAndY, rookXAndY, knightXAndY, pawnXAndY;
		//arraylist containing x&y arraylists for possible piece movement coordinates
		ArrayList<ArrayList<Integer>> mvmtXAndY = new ArrayList<ArrayList<Integer>>(); 
		//get x & y from the Piece object
		type = piece.getType(); //piece type
		mvmtX = new ArrayList<Integer>();
		mvmtY = new ArrayList<Integer>();
		switch(type) {
		case 1: //king
			//get king movement possibilities
			kingXAndY = possibleKingMvmts(piece, kingCapture); //get arraylist containing
												 //2 arraylists
			mvmtX = kingXAndY.get(0); //arraylist of x coordinates
			mvmtY = kingXAndY.get(1); //arraylist of y coordinates
			break;
		case 2: //queen
			//get queen movement possibilities
			queenXAndY = possibleQueenMvmts(piece, kingCapture); 
			mvmtX = queenXAndY.get(0); //arraylist of x coordinates
			mvmtY = queenXAndY.get(1); //arraylist of y coordinates
			break;
		case 3: //rook
			rookXAndY = possibleRookMvmts(piece, kingCapture);
			mvmtX = rookXAndY.get(0); //arraylist of x coordinates
			mvmtY = rookXAndY.get(1); //arraylist of y coordinates
			break;
		case 4: //knight
			knightXAndY = possibleKnightMvmts(piece, kingCapture);
			mvmtX = knightXAndY.get(0); //arraylist of x coordinates
			mvmtY = knightXAndY.get(1); //arraylist of y coordinates
			break;
		case 5: //bishop
			bishopXAndY = possibleBishopMvmts(piece, kingCapture);
			mvmtX = bishopXAndY.get(0); //arraylist of x coordinates
			mvmtY = bishopXAndY.get(1); //arraylist of y coordinates
			break;
		case 6: //pawn
			pawnXAndY = possiblePawnMvmts(piece, kingCapture);
			mvmtX = pawnXAndY.get(0); //arraylist of x coordinates
			mvmtY = pawnXAndY.get(1); //arraylist of y coordinates
			break;
		default:
			break;
		}
		mvmtXAndY.add(mvmtX);
		mvmtXAndY.add(mvmtY);
		return mvmtXAndY;
	}
	
	/**
	 * Check if King can move.
	 * @param color
	 * @return boolean
	 */
	public boolean canKingMove(int color) {
		//get all pieces
		ArrayList<Piece> pieces = getBoard().getPieces();
		ArrayList<ArrayList<Integer>> kingMvmts = new ArrayList<ArrayList<Integer>>();
		for (Piece p : pieces) {
			if (p.getType() == 1 && p.getColor() == color) {
				kingMvmts = possibleKingMvmts(p, false);
				break;
			}
		}
		//check if any movements
		ArrayList<Integer> mvmtX = kingMvmts.get(0); //x coordinates
		//if list is empty
		if (mvmtX.isEmpty()) {
			return false;
		} else { //list not empty
			return true;
		}
	}
	
}
