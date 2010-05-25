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
import general.Board;
import general.Move;
import general.Piece;

import java.util.*;


/**
 * @author Petri Tuononen
 * @since 7/1/2010
 */
public class PlayerAI extends Player {

	//global variables
	private Board board;
	private Move move;
	
	/**
	 * Constructor
	 * @param side
	 */
	public PlayerAI(Board board, Move move, int side) {
		super(side);
		this.board = board;
		this.move = move;
	}
	
	/**
	 * Get all squares pieces are allowed to move.
	 * First arraylist contains row numbers and second contains
	 * column numbers.
	 * @param side
	 * @return ArrayList<ArrayList<Integer>>
	 */
	public ArrayList<ArrayList<Integer>> getAllSquaresPossibleToMove(int side) {
		ArrayList<ArrayList<Integer>> allSquaresPossibleToMove = new ArrayList<ArrayList<Integer>>();
		//get all pieces from black or white
		ArrayList<Piece> allPiecesFromOneSide = board.getPiecesFromOneSide(side);
		ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> x = new ArrayList<Integer>(), y = new ArrayList<Integer>();
		for (Piece ownPiece : allPiecesFromOneSide) {
			//all squares piece can move to
			moves = move.possiblePieceMoves(ownPiece, false);
			x.addAll(moves.get(0));
			y.addAll(moves.get(1));
		}
		allSquaresPossibleToMove.add(x);
		allSquaresPossibleToMove.add(y);
		return allSquaresPossibleToMove;
	}
	
	/**
	 * Returns two arraylists of piece notations and where they can move. 
	 * @param side
	 * @return ArrayList<ArrayList<String>>
	 */
	public ArrayList<ArrayList<String>> getAllSquaresPossibleToMoveAndPiece(int side) {
		ArrayList<String> ownPieces = new ArrayList<String>(), squares = new ArrayList<String>();
		ArrayList<ArrayList<String>> allSquaresPossibleToMove = new ArrayList<ArrayList<String>>();
		//get all pieces from black or white
		ArrayList<Piece> allPiecesFromOneSide = board.getPiecesFromOneSide(side);
		ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
		int xL, yL;
		ListIterator<Integer> xList, yList;
		ArrayList<Integer> x = new ArrayList<Integer>(), y = new ArrayList<Integer>();
		for (Piece ownPiece : allPiecesFromOneSide) {
			//all squares piece can move to
			moves = move.possiblePieceMoves(ownPiece, false);
			x.addAll(moves.get(0));
			y.addAll(moves.get(1));
			x = moves.get(0); //list of row numbers
			y = moves.get(1); //list of column numbers
			xList = x.listIterator();  //row iterator
			yList = y.listIterator();  //column iterator
			while (xList.hasNext() && yList.hasNext()) { //while lists have coordinates
				xL = xList.next(); //x coordinate on the board
				yL = yList.next(); //y coordinate on the board
				String squareNotation = board.coordinatesToNotation(xL, yL);
				squares.add(squareNotation);
				ownPieces.add(board.coordinatesToNotation(ownPiece.getRow(), ownPiece.getCol()));
			}	
		}
		allSquaresPossibleToMove.add(ownPieces);
		allSquaresPossibleToMove.add(squares);
		return allSquaresPossibleToMove;
	}
	
	/**
	 * Get all enemy pieces that can be captured. Returns an arraylist containing two arraylists.
	 * First arraylist indicates own piece that can capture and second arraylist enemy piece to capture.
	 * @param side
	 * @return ArrayList<ArrayList<Piece>>
	 */
	public ArrayList<ArrayList<Piece>> getPiecesPossibleToCapture(int side, boolean kingCapture) {
		ArrayList<Piece> ownPieces = new ArrayList<Piece>(), enemyPieces = new ArrayList<Piece>();
		ArrayList<ArrayList<Piece>> ownAndEnemyPieces = new ArrayList<ArrayList<Piece>>();
		//get all pieces from black or white
		ArrayList<Piece> allPiecesFromOneSide = board.getPiecesFromOneSide(side);
		ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> x, y;
		ListIterator<Integer> xList, yList;
		int xL, yL, enemySide;
		for (Piece ownPiece : allPiecesFromOneSide) {
			//all squares piece can move to
			moves = move.possiblePieceMoves(ownPiece, kingCapture);
			x = moves.get(0); //list of row numbers
			y = moves.get(1); //list of column numbers
			xList = x.listIterator();  //row iterator
			yList = y.listIterator();  //column iterator
			if (side==0) {
				enemySide=1;
			} else {
				enemySide=0;
			}
			while (xList.hasNext() && yList.hasNext()) { //while lists have coordinates
				//listIterator next() method doesn't work inside if statement -> assign to variables
				xL = xList.next(); //x coordinate on the board
				yL = yList.next(); //y coordinate on the board
				//check if an enemy piece is found from the square
				if (move.isPieceOnSquare(xL, yL, enemySide)) {
					//add enemy piece to arraylist
					enemyPieces.add(board.getPiece(xL, yL, enemySide));
					//add own piece to arraylist
					ownPieces.add(ownPiece);
				}
			}	
		}
		ownAndEnemyPieces.add(ownPieces);
		ownAndEnemyPieces.add(enemyPieces);
		return ownAndEnemyPieces;
	}
	
	/**
	 * Prints a list of which own piece can capture enemy piece.
	 * e.g. a8, a2 (black rook can capture white pawn)
	 * @param side
	 */
	public void printPiecesToCaptureAsNotation(int side) {
		ArrayList<ArrayList<Piece>> pieces = getPiecesPossibleToCapture(side, false);
		ArrayList<Piece> ownPieces = pieces.get(0);
		ArrayList<Piece> enemyPieces = pieces.get(1);
		ListIterator<Piece> ownList = ownPieces.listIterator(); 
		ListIterator<Piece> enemyList = enemyPieces.listIterator(); 
		Piece ownPiece, enemyPiece;
		while (ownList.hasNext() && enemyList.hasNext()) { 
			ownPiece = ownList.next();
			enemyPiece = enemyList.next();
			System.out.print(board.coordinatesToNotation(ownPiece.getRow(), ownPiece.getCol()));
			System.out.print(", ");
			System.out.println(board.coordinatesToNotation(enemyPiece.getRow(), enemyPiece.getCol()));
		}	
	}
	
	/**
	 * Returns an array that contains two cells which first cell
	 * contains own piece that can be capture highest value enemy piece
	 * and second cell contains highest value enemy piece.
	 * @param side
	 * @return Piece[]
	 */
	public Piece[] highestValuePieceAbleToCapture(int side) {
		Piece[] highestValuePiece = new Piece[2];
		ArrayList<ArrayList<Piece>> pieces = getPiecesPossibleToCapture(side, false);
		ArrayList<Piece> ownPieces = pieces.get(0);
		ArrayList<Piece> enemyPieces = pieces.get(1);
		ListIterator<Piece> ownList = ownPieces.listIterator();
		ListIterator<Piece> enemyList = enemyPieces.listIterator(); 
		Piece ownPiece, enemyPiece;
		int highestValue = 0, tempValue = 0;
		while (ownList.hasNext() && enemyList.hasNext()) { 
			ownPiece = ownList.next();
			enemyPiece = enemyList.next();
			tempValue = enemyPiece.getValue();
			if (tempValue > highestValue) {
				highestValue = tempValue;
				highestValuePiece[0] = ownPiece;
				highestValuePiece[1] = enemyPiece;
			}
		}	
		return highestValuePiece;	
	}
	
	/**
	 * Returns an array of enemy pieces that can be captured in
	 * value descending order.
	 * @param side
	 * @return ArrayList<ArrayList<Piece>>
	 */
	public ArrayList<ArrayList<Piece>> piecesAbleToCaptureInValueDescOrder(int side) {
		//get all pieces the selected player is able to capture
		ArrayList<ArrayList<Piece>> pieces = getPiecesPossibleToCapture(side, false);
		ArrayList<ArrayList<Piece>> piecesNewOrder = new ArrayList<ArrayList<Piece>>();
		ArrayList<Piece> ownPieces = pieces.get(0); //get own pieces
		ArrayList<Piece> enemyPieces = pieces.get(1); //get enemy pieces
		//convert arraylist into an array
		Piece[] newEnemyOrder = new Piece[enemyPieces.size()];
	    for (int i=0; i < newEnemyOrder.length; i++) {
	    	newEnemyOrder[i] = enemyPieces.get(i);
	    }
	    //order by ascending piece value
	    sortPieceArray(newEnemyOrder, newEnemyOrder.length);
	    //reverse order array -> descending order by piece value
	    List<Piece> list = Arrays.asList(newEnemyOrder);
	    Collections.reverse(list);
	    newEnemyOrder = (Piece[]) list.toArray();
		//convert own piece arraylist into an array
		Piece[] origOwnOrder = new Piece[ownPieces.size()];
		ownPieces.toArray(origOwnOrder);
		//convert enemy piece arraylist into an array
		Piece[] origEnemyOrder = new Piece[enemyPieces.size()];
		enemyPieces.toArray(origEnemyOrder);
		Piece[] newOwnOrder = new Piece[newEnemyOrder.length];
	    for (int i=0; i<newEnemyOrder.length; i++) {
		    for (int j=0; j<origEnemyOrder.length; j++) {
		    	//find same piece from original order of enemy pieces
		    	if (newEnemyOrder[i]==origEnemyOrder[j]) {
		    		//copy own piece to same order as newly ordered enemy pieces
	    			newOwnOrder[i] = origOwnOrder[j];
		    	}
		    }
	    }
	    ArrayList<Piece> newOwn = new ArrayList<Piece>(Arrays.asList(newOwnOrder));
	    ArrayList<Piece> newEnemy = new ArrayList<Piece>(Arrays.asList(newEnemyOrder));
	    piecesNewOrder.add(newOwn); //own pieces on new order
	    piecesNewOrder.add(newEnemy); //enemy pieces on new order
	    return piecesNewOrder;
	}
	
	/**
	 * Check if player's King is in checkmate.
	 * @param player
	 * @return boolean
	 */
	public boolean isCheckmate(Player player) {
		//check if King is under attack
		//check if there's safe square to move
		ArrayList<ArrayList<Piece>> pieces = getPiecesPossibleToCapture(player.getEnemySide(), true);
		ArrayList<Piece> piecesAbleToCapture = pieces.get(1);
		ArrayList<ArrayList<Integer>> kingMoves = new ArrayList<ArrayList<Integer>>();
		//check if King is found from the list of pieces enemy is able to capture
		for (Piece p : piecesAbleToCapture) {
			if (p.getType()==1) {
				kingMoves = move.possiblePieceMoves(p, false);
				break;
			}
		}
		if (!kingMoves.isEmpty()) {
			ArrayList<Integer> x = kingMoves.get(0);
			ArrayList<Integer> y = kingMoves.get(1);
			ListIterator<Integer> xList, yList;
			xList = x.listIterator();  //row iterator
			yList = y.listIterator();  //column iterator
			if (!x.isEmpty()) {
				while (xList.hasNext()) {
					//if there's safe square for the King to move
					if (isSquareSafe(xList.next(), yList.next(), player)) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Sorts an array of Piece objects in ascending order by value.
	 * @param list
	 * @param length
	 */
	public void sortPieceArray(Piece[] list, int length) {
	    int outOfOrder, location;
	    Piece temp;
	    //from second until the end of the array
	    for(outOfOrder = 1; outOfOrder < length; outOfOrder++) { 
	    	//if out of order, move to the right place
	        if(list[outOfOrder].getValue() < list[outOfOrder - 1].getValue()) { 
	            temp = list[outOfOrder];
	            location = outOfOrder;
	            do { //move down the array until correct place is found
	                list[location] = list[location-1];
	                location--;
	            }
	            while (location > 0 && list[location-1].getValue() > temp.getValue());
	            list[location] = temp;
	        }
	    }
	}
	
	/**
	 * Prints the highest value piece that can be captured and piece that can capture it.
	 * Printed in notation format so that first comes own piece then separated by
	 * comma the enemy piece.
	 * @param side
	 */
	public void printHighestValuePieceAbleToCapture(int side) {
		Piece[] p = highestValuePieceAbleToCapture(side);
		try {
			Piece ownP = p[0];
			Piece enemyP = p[1];
			System.out.print(board.coordinatesToNotation(ownP.getRow(), ownP.getCol()));
			System.out.print(", ");
			System.out.println(board.coordinatesToNotation(enemyP.getRow(), enemyP.getCol()));
		} catch(NullPointerException e){
			//System.out.println("NullPointerException");
		}
	}
	
	/**
	 * Performs the best possible move in different situations.
	 */
	public void doBestMove(Player player) {
		/*
		 * check first that own pieces are not in danger
		 * if they are, move highest value piece to a safe spot
		 */
		//get an array containing enemy piece and the highest value piece it can capture
		Piece[] highestValPiece = highestValuePieceAbleToCapture(player.getEnemySide());
		if (highestValPiece[0] != null) { //if there's piece in danger
			defense(player);
		} else {
			/*
			 * if own pieces are not in danger, check if it's possible
			 * to capture pieces starting from the highest value piece.
			 * 
			 * if there's no piece to capture, advance piece normally.
			 */
			attack(player);
		}
	}
	
	/**
	 * If piece in danger move to a safe square.
	 * @param player
	 */
	public void defense(Player player) {
		//get an array containing enemy piece and the highest value piece it can capture
		Piece[] highestValPiece = highestValuePieceAbleToCapture(player.getEnemySide());
		//get the highest value piece that enemy can capture
		Piece pieceInDanger = highestValPiece[1];
		//find possible squares where piece can move
		ArrayList<ArrayList<Integer>> possibleMoves = move.possiblePieceMoves(pieceInDanger, false);
		ArrayList<Integer> possibleMovesX = possibleMoves.get(0); //get x coordinates
		ArrayList<Integer> possibleMovesY = possibleMoves.get(1); //get y coordinates
		//find possible squares enemy pieces can move
		ArrayList<ArrayList<Integer>> possibleEnemyMoves = getAllSquaresPossibleToMove(player.getEnemySide());
		ArrayList<Integer> possibleEnemyMovesX = possibleEnemyMoves.get(0); //get x coordinates
		ArrayList<Integer> possibleEnemyMovesY = possibleEnemyMoves.get(1); //get y coordinates
		//find a square own piece can move safely
		/*
		 * compare possible own piece movements to enemy piece movements to find
		 * a safe square.
		 */
		ListIterator<Integer> pmxList, pmyList; //iterators for possible own piece moves
		pmxList = possibleMovesX.listIterator();  //row iterator
		pmyList = possibleMovesY.listIterator();  //column iterator
		//convert arraylist of enemy x movement coordinates into an array
		int[] pexArray = new int[possibleEnemyMovesX.size()];
	    for (int i=0; i < pexArray.length; i++) {
	    	pexArray[i] = possibleEnemyMovesX.get(i).intValue();
	    }
	    //convert arraylist of enemy y movement coordinates into an array
		int[] peyArray = new int[possibleEnemyMovesY.size()];
	    for (int i=0; i < peyArray.length; i++) {
	    	peyArray[i] = possibleEnemyMovesY.get(i).intValue();
	    }
		int pmx=0, pmy=0, pex, pey;
		boolean match = true;
		//check possible own piece moves until safe square is found or possible moves run out
		while (pmxList.hasNext() && pmyList.hasNext() && match) {
			match = false; //reset to false
			//get next own piece move
			pmx = pmxList.next(); 
			pmy = pmyList.next();
			enemy: for (int i=0; i<pexArray.length; i++) {
				//get enemy piece move
				pex = pexArray[i];
				pey = peyArray[i];
				//compare
				//if match found no need to check other enemy moves
				if (pmx==pex && pmy==pey) {
					match = true;
					break enemy;
				}
			}
		}
		//if none of the enemy moves match -> move own piece to that square
		if (!match) {
			move.doMove(player, board.coordinatesToNotation(pieceInDanger.getRow(),
					pieceInDanger.getCol()), board.coordinatesToNotation(pmx, pmy));
		} else if (match) {
			/*
			 * if the piece can't be safed by moving it to a safe square
			 * try to trade a less valuable piece.
			 * 
			 * if that doesn't work capture highest value enemy piece.
			 */
//			if (pieceInDanger.getType()!=6) { //only worth trading if piece is not pawn
//				
//			} else {
//				attack(player);
//			}
			attack(player);
		}
	}
	
	/**
	 * Tries to capture highest value enemy piece.
	 * Tries to capture second highest and so on if piece that
	 * tries capturing would be lost in trade to a less valuable piece.
	 * If no piece can be captured move normally.
	 * @param player
	 */
	public void attack(Player player) {
		/*
		 * get all pieces that can be captured by current player.
		 * first arraylist contains own pieces that can capture.
		 * second arraylist contains enemy pieces.
		 * lists are enemy piece value descending order.
		 */
		boolean moveDone = false;
		ArrayList<ArrayList<Piece>> piecesAbleToCapture = piecesAbleToCaptureInValueDescOrder(player.getSide());
		ArrayList<Piece> ownPieceArray = piecesAbleToCapture.get(0); //own pieces that can capture
		ArrayList<Piece> enemyPieceArray = piecesAbleToCapture.get(1); //pieces that can be captured
		ListIterator<Piece> ownList, enemyList; //iterators
		ownList = ownPieceArray.listIterator();  
		enemyList = enemyPieceArray.listIterator();
		if (!ownPieceArray.isEmpty()) { //no pieces to capture
			//while pieces left and suitable move not found
			while (ownList.hasNext() && enemyList.hasNext() && !moveDone) { 
				Piece ownPiece = ownList.next(); //own piece
				Piece enemyPiece = enemyList.next(); //piece to capture
				/*
				 * before making move, check that own piece can't be captured during next move
				 * or at least the captured enemy piece is more valuable
				 */
				//temporarily make target square empty
				board.removePiece(enemyPiece.getRow(), enemyPiece.getCol()); //remove piece from the square
				//get all squares enemy can move during the next turn (now including the square
				//where enemy's piece was located)
				ArrayList<ArrayList<Integer>> possibleEnemyMoves = getAllSquaresPossibleToMove(player.getEnemySide());
				//place the removed piece on board
				board.addPiece(enemyPiece);
				ArrayList<Integer> possibleEnemyMovesX = possibleEnemyMoves.get(0); //get x coordinates
				ArrayList<Integer> possibleEnemyMovesY = possibleEnemyMoves.get(1); //get y coordinates
				//convert arraylist of enemy x movement coordinates into an array
				int[] pexArray = new int[possibleEnemyMovesX.size()];
			    for (int i=0; i < pexArray.length; i++) {
			    	pexArray[i] = possibleEnemyMovesX.get(i).intValue();
			    }
			    //convert arraylist of enemy y movement coordinates into an array
				int[] peyArray = new int[possibleEnemyMovesY.size()];
			    for (int i=0; i < peyArray.length; i++) {
			    	peyArray[i] = possibleEnemyMovesY.get(i).intValue();
			    }
			    //check if enemy can do counter attack on next move
			    boolean capturedNextRound = false;
				comp: for (int i=0; i<pexArray.length; i++) {
					//compare
					if (enemyPiece.getRow()==pexArray[i] && enemyPiece.getCol()==peyArray[i]) {
						capturedNextRound = true;
						break comp;
					}
				}
			    //check is enemy piece is more valuable
			    boolean enemyMoreValuable = false;
			    //if enemy piece is at least as valuable as own piece
			    if (enemyPiece.getValue() >= ownPiece.getValue()) {
			    	enemyMoreValuable = true;
			    }
				/*
				 * do move if enemy can't counter attack during next move or it can but
				 * traded piece is less valuable
				 */
			    if (!capturedNextRound || (capturedNextRound && enemyMoreValuable)) {
					move.doMove(player, board.coordinatesToNotation(ownPiece.getRow(),
							ownPiece.getCol()), board.coordinatesToNotation(enemyPiece.getRow(),
							enemyPiece.getCol()));
					moveDone=true;
			    } else {
					advance(player);
			    }
			}
		} else { //no pieces to capture, no own pieces in danger either
			/*
			 * if there's no possibility to capture move some piece to a safe spot.
			 */
			advance(player);
		}
	}
	
	/**
	 * check that enemy can't move to selected square on next round.
	 * @param x
	 * @param y
	 * @param player
	 * @return boolean
	 */
	public boolean isSquareSafe(int x, int y, Player player) {
		//get all moves enemy side can do
		ArrayList<ArrayList<Integer>> possibleEnemyMoves = getAllSquaresPossibleToMove(player.getEnemySide());
		ArrayList<Integer> possibleEnemyMovesX = possibleEnemyMoves.get(0); //get x coordinates
		ArrayList<Integer> possibleEnemyMovesY = possibleEnemyMoves.get(1); //get y coordinates	
		ListIterator<Integer> pexIter = possibleEnemyMovesX.listIterator(); //x iterator
		ListIterator<Integer> peyIter = possibleEnemyMovesY.listIterator(); //y iterator
		//look for a matching move
		while (pexIter.hasNext() && peyIter.hasNext()) {
			if(pexIter.next()==x && peyIter.next()==y) { //match found
				return false; //not a safe square as enemy piece can move there
			}
		}
		return true;
	}
	
	/**
	 * Advance piece.
	 * @param player
	 */
	public void advance(Player player) {
		/* get own pieces that can move
		 * put pieces in random order
		 * choose first piece from the list
		 * get all moves piece can do
		 * put moves in random order
		 * select first move
		 * check that square is safe
		 *   try to find advancing piece
		 *     if found -> move piece, else put safe move in reserve
		 * if no safe moves -> get next piece from the list
		 * loop until advancing piece move is found or if no pieces
		 * with advancing move, get the latest move in reserve
		*/
		
		//get all own pieces
		ArrayList<Piece> allPiecesFromOneSide = board.getPiecesFromOneSide(player.getSide());
		//put list of pieces in a random order
		Collections.shuffle(allPiecesFromOneSide);
		int amountOfPieces = allPiecesFromOneSide.size();
		int piecesChecked = 0;
		boolean advancingMove = false;
		Piece reservePiece = new Piece(), lastHope = new Piece();
		int reserveX=0, reserveY=0, lastHopeX=0, lastHopeY=0;
		Piece p;
		ArrayList<ArrayList<Integer>> moves;
		ArrayList<Integer> xList, yList, moveCheckOrder;
		int moveAmount;
		int moveLoc;
		boolean moveDone = false;
		//until advancing move is found or no pieces left to check
		while (!advancingMove && piecesChecked<amountOfPieces) {
			//get a piece
			p = allPiecesFromOneSide.get(piecesChecked);
			//get all possible moves that piece can do
			moves = move.possiblePieceMoves(p, false);
			xList = moves.get(0); //x coordinates
			yList = moves.get(1); //y coordinates
			moveAmount = xList.size(); //amount of moves
			moveLoc = 0; //move index in list
			//create order to check moves
			moveCheckOrder = new ArrayList<Integer>();
			for (int i=0; i<moveAmount; i++) {
				moveCheckOrder.add(i);
			}
			//randomize order in which moves are checked
			Collections.shuffle(moveCheckOrder);
			int counter = 0;
			if (moveAmount>0) { //if there's moves at all
				//save first move as a reserve/last chance move
				lastHope = p;
				int firstMove = moveCheckOrder.get(0);
				lastHopeX=xList.get(firstMove);
				lastHopeY=yList.get(firstMove);
				//while advancing move not found and still moves left
				while (!advancingMove && counter<moveAmount) {
					moveLoc = moveCheckOrder.get(counter); //get next move
					//check if move is safe (enemy can't attack during next move)
					if (isSquareSafe(xList.get(moveLoc), yList.get(moveLoc), player)) {
						//best move is to advance/go forward: white -vertical, black +vertical
						if(player.getSide()==0) { //if white side
							//if advancing move
							if(xList.get(moveLoc)<p.getRow()) {
								advancingMove=true;
							}
						}
						else { //black side
							//if advancing move
							if(xList.get(moveLoc)>p.getRow()) {
								advancingMove=true;	
							}	
						}
						//if not found a safe piece which can move forward
						//put piece in reserve because at least it's safe
						if(!advancingMove) {
							reservePiece=p;
							reserveX=xList.get(moveLoc);
							reserveY=yList.get(moveLoc);
						}
					}
					counter++;
				}
				if (advancingMove) { //advancing move found
					move.doMove(player, board.coordinatesToNotation(p.getRow(),
							p.getCol()), board.coordinatesToNotation(xList.get(moveLoc),
							yList.get(moveLoc)));
					moveDone=true;
				}
			}
			piecesChecked++;
		}
		if (!advancingMove) { //advancing move not found so use safe move in reserve
			move.doMove(player, board.coordinatesToNotation(reservePiece.getRow(),
					reservePiece.getCol()), board.coordinatesToNotation(reserveX,
					reserveY));
			moveDone=true;
		}
		//if no piece can advance
		if (!moveDone) {
			//move last piece that has at least one legal move
			move.doMove(player, board.coordinatesToNotation(lastHope.getRow(),
					lastHope.getCol()), board.coordinatesToNotation(lastHopeX,
							lastHopeY));
			//skip turn
			//add skip turn to history of moves
//			HistoryOfMoves history = move.getHistoryOfMoves(); //get current history
//			if(player.getSide()==0) {
//				history.addWhiteMove("0", "0");
//			} else {
//				history.addBlackMove("0", "0");
//			}
//			move.setHistoryOfMoves(history); //set updated history
		}
	}

}
