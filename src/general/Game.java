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
import player.PlayerAI;
import player.PlayerHuman;
  
/**
 * 
 * @author Petri Tuononen
 * @since 26/11/2009
 */
public class Game {
	
	private int whosTurn;
	
	//get player color who's turn it is
	public int getWhosTurn() {
		return whosTurn;
	}

	//set player color who's turn it is
	public void setWhosTurn(int whosTurn) {
		this.whosTurn = whosTurn;
	}

	/**
	 * raffle which player gets which color/side to start with.
	 */
	public void raffleSide(Player player1, Player player2) {
		Random gen = new Random();
		int result = gen.nextInt(2); //0 or 1 (0=white or 1=black)
		player1.setSide(result);
		if (result==0) {
			player2.setSide(1);
		} else {
			player2.setSide(0);
		}
	}
	
	/**
	 * If checkmate occurs, inform players and return true.
	 * @param board
	 * @param move
	 * @param player1
	 * @param player2
	 */
	public boolean checkCheckmate(Board board, Move move, Player player1, Player player2) {
		//AI player is needed for isCheckmate method
		PlayerAI AI = new PlayerAI(board, move, 0);
		//check if player1 is in checkmate
		if (AI.isCheckmate(player1)) {
			String winnerSide;
			if (player1.getSide()==0) {
				winnerSide = "black";
			} else {
				winnerSide = "white";
			}
			System.out.println("Checkmate! " +winnerSide+" won!");
			return true;
		} else if (AI.isCheckmate(player2)) {
			String winnerSide;
			if (player2.getSide()==0) {
				winnerSide = "black";
			} else {
				winnerSide = "white";
			}
			System.out.println("Checkmate! " +winnerSide+" won!");
			return true;
		}
		return false;
	}
	
	/**
	 * Main method.
	 * @param args
	 */
	public static void main(String args[]) {
		Game game = new Game();
		
		//create a chess table
		Board board = new Board();
		//create move object
		Move move = new Move(board); 
		Scanner sc = new Scanner(System.in);
		//3 game modes: human vs human, human vs AI, AI vs AI
		System.out.println("Choose game mode");
		System.out.println("1 = human vs human");
		System.out.println("2 = human vs AI");
		System.out.println("3 = AI vs AI");
		int mode = sc.nextInt();
		boolean checkmate = false;
		if (mode==1) {
			//create two human players
			PlayerHuman player1 = new PlayerHuman(0);
			PlayerHuman player2 = new PlayerHuman(1);
			System.out.println("Player1: White");
			System.out.println("Player2: Black");
			int whosTurn = 0; //white starts
			System.out.println();
			System.out.println("Player1's turn");
//			board.showGameStateWithNotation();
			boolean cont = true;
			String src, dest;
			sc.nextLine(); //clear buffer
			while (cont && !checkmate) {
				boolean legalMove = false;
				//repeat until move is legal
				while (!legalMove) {
					System.out.println("What piece you'd like to move? (notation)");
					src = sc.nextLine();
					System.out.println("Where you'd like to move that piece? (notation)");
					dest = sc.nextLine();
					if (whosTurn==player1.getSide()) {
						legalMove = move.doMove(player1, src, dest);
					} else if (whosTurn==player2.getSide()) {
						legalMove = move.doMove(player2, src, dest);
					}
					if (!legalMove) {
						System.out.println("Illegal move");
					}
				}
				//after a successful move, change player
				if (whosTurn == 0) {
					whosTurn = 1;
				} else {
					whosTurn = 0;
				}
//				board.showGameStateWithNotation();
				//inform that player turn changes
				if (whosTurn==player1.getSide()) {
					System.out.println("Player1's turn");
				} else {
					System.out.println("Player2's turn");
				}
				//check for checkmate
				checkmate = game.checkCheckmate(board, move, player1, player2);
//				System.out.println("Continue playing? (y/n)");
//				String ans = sc.nextLine();
//				if (ans.equalsIgnoreCase("y")) {
//					cont = true;
//				} else {
//					cont = false;
//				}
			}
		} else if (mode==2) {
			//create human and AI
			PlayerHuman player1 = new PlayerHuman(0);
			PlayerAI player2 = new PlayerAI(board, move, 1);
			System.out.println("Player1: White");
			System.out.println("Player2: Black");
			int whosTurn = 0; //white starts
			System.out.println();
			System.out.println("Player1's turn");
//			board.showGameStateWithNotation();
			boolean cont = true;
			String src, dest;
			sc.nextLine(); //clear buffer
			while (cont && !checkmate) {
				boolean legalMove = false;
				//repeat until move is legal
				if (whosTurn==0) {
					while (!legalMove) {
						System.out.println("What piece you'd like to move? (notation)");
						src = sc.nextLine();
						System.out.println("Where you'd like to move that piece? (notation)");
						dest = sc.nextLine();
						if (whosTurn==player1.getSide()) {
							legalMove = move.doMove(player1, src, dest);
						}
						if (!legalMove) {
							System.out.println("Illegal move");
						}
					}
					whosTurn = 1;
				} else if (whosTurn==1) {
					player2.doBestMove(player2);
					whosTurn = 0;
				}
//				board.showGameStateWithNotation();
				//inform that player turn changes
				if (whosTurn==player1.getSide()) {
					System.out.println("Player1's turn");
				} else {
					System.out.println("Player2's turn");
				}
				//check for checkmate
				checkmate = game.checkCheckmate(board, move, player1, player2);
			}
		} else if (mode==3) {
			//create two AIs
			PlayerAI player1 = new PlayerAI(board, move, 0);
			PlayerAI player2 = new PlayerAI(board, move, 1);
			System.out.println("Player1: White");
			System.out.println("Player2: Black");
			//limit moves to 50
			loop: for (int i=0; i<50; i++) {
				player2.doBestMove(player1);
				player2.doBestMove(player2);
				System.out.println((i+1)+". round");
//				board.showGameStateWithNotation();
				//check for checkmate
				if (game.checkCheckmate(board, move, player1, player2)) {
					break loop;
				}
			}
		} else {
			System.out.println("Wrong input.");
		}

		//print history of moves
//		HistoryOfMoves history = move.getHistoryOfMoves();
//		history.printOutMoves();
	}

}
