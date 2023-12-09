package application;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AI extends GameLog {
	Color AIColor;
	boolean difficulty;
	
	static Color defaultAIColor = new Color(253,253,150);
	
	protected AI() {
		super();
		this.AIColor = defaultAIColor;
	}
	
	protected AI (int userRows, int userCols) {
		super(userRows, userCols);
		this.AIColor = defaultAIColor;
	}
	
	protected AI (int userRows, int userCols, Color color) {
		super(userRows, userCols);
		this.AIColor = color;
	}
	
	protected AI(Color color) {
		super();
		this.AIColor = color;
	}
	
	protected void setAIDifficulty(boolean newDifficulty) { difficulty = newDifficulty; }
	protected boolean getAIDifficulty() { return difficulty; } 
	
	/* LOOP FOREVER -> a valid random col is selected */
	/* SMART AI -> CHECK AROUND PIECE RECURSIVELY */
	
	/**
	 * Creates a move based on the difficulty of the AI.
	 * @param isSmart If "OMEGA BEAST AI" has been selected boolean.
	 * @param gameState GameLog object that holds the current game state.
	 * @param board The current board.
	 * @return The best move or a random move.
	 */
	protected Move difficulty(boolean isSmart, GameLog gameState, HBox board) {
		Move newMove = null;
		if (isSmart) {
			/* HASHMAP
			 * KEY : VALUE
			 * MOVE : COUNT -> LEFT, RIGHT, DOWN, DL, DR, UL, UR -> HIGHER COUNT = BETTER MOVE
			 * ITERATE THROUGH EVERY POSSIBLE MOVE -> IF MOVE EXISTS IN GAMESTATE -> IGNORE
			 * SUM ALL NEIGHBORS AROUND MOVE -> MOVE.PUT(SUM)
			 * 
			 * FIND BEST MOVE -> MAX SCORE
			 * */
			ArrayList<Color> possibleColors = new ArrayList<>();
			HashMap<Move, Double> possibleMoves = new HashMap<>();
			possibleColors.add(gameState.getColor());
			possibleColors.add(AIColor);
			for (int i = 0; i < 2; i++) {
				Color currColor = possibleColors.get(i);
				for (int c = 0; c < gameState.getCols(); c++) {
					int rowIndex = getRowFromCol(board, c);
					if (rowIndex != -1) {
						Move currMove = new Move(rowIndex, c, currColor);
						double score = evaluateMove(currMove, gameState);
						possibleMoves.put(currMove, score);
					}
				}
			}
			newMove = getMaxMove(possibleMoves);
			newMove.setColor(AIColor);
		} else {
			int randomCol = this.getRandomCol();
			int rowIndex = this.getRowFromCol(board, randomCol);
			if (rowIndex == -1) {
				while (rowIndex == -1) {
					randomCol = this.getRandomCol();
					rowIndex = this.getRowFromCol(board, randomCol);
					if (rowIndex != -1) {
						break;
					}
				}
			}
			newMove = new Move(rowIndex, randomCol, AIColor);
			return newMove;
		}
		return newMove;
	}
	
	/**
	 * Evaluates a move.
	 * @param currMove The current move being evaluated.
	 * @param gameState The current game state.
	 * @return A double score.
	 */
	protected double evaluateMove(Move currMove, GameLog gameState) {
	    double score = 0;
	    int count = 0;
	    int winVertical   = gameState.countDown(currMove, count);
	    
	    int winHorizontal = gameState.countLeft(currMove, count) +
	            			gameState.countRight(currMove, count);
	    
	    int winRightDiag  = gameState.countUpRightDiagonal(currMove, count) +
	            		    gameState.countDownLeftDiagonal(currMove, count);
	    
	    int winLeftDiag   = gameState.countUpLeftDiagonal(currMove, count) +
	            		    gameState.countDownRightDiagonal(currMove, count);

	    if (winVertical == 3 || winHorizontal == 3 || winRightDiag == 3 || winLeftDiag == 3) {
	        if (currMove.getColor() != AIColor) {
	            score = 1 * 0.9;
	        } else {
	            score = 1;
	        }
	    } else {
	         if (winVertical == 2 || winHorizontal == 2 || winRightDiag == 2 || winLeftDiag == 2) {
	        	 if (currMove.getColor() != AIColor) {
	        		 score = 1 * 0.7;
	        	 } else {
	        		score = 1 * 0.8; 
	        	 }
	        }
	    }
	    return score;
	}

	/**
	 * Simple max value algorithm that returns the best move.
	 * @param moveMap A HashMap of all possible moves.
	 * @return The best move in the move map.
	 */
	protected Move getMaxMove(HashMap<Move, Double> moveMap) {
		Move bestMove = null;
		double maxScore = -999;
		for (Map.Entry<Move, Double> entry : moveMap.entrySet()) {
			Move key = entry.getKey();
			Double value = entry.getValue();
			if (value >= maxScore) {
				maxScore = value;
				bestMove = key;
			}
		}
		return bestMove;
	}
	
	/**
	 * Returns a random column from the board dimensions.
	 * @return A random integer from 0 - GameBoard.getCols();
	 */
	protected int getRandomCol() {
		int max = this.getCols() - 1;
		int min = 0;
		int randomCol = (int) Math.floor(Math.random() * (max - min + 1) + min);
		return randomCol;
	}
	
	/**
	 * Returns the next available row index in a given column.
	 * @param board The game board.
	 * @param col The specified column.
	 * @return The index of the row.
	 */
	protected int getRowFromCol(HBox board, int col) {
		VBox column = (VBox) board.getChildren().get(col);
		for (int r = column.getChildren().size() - 1; r >= 0; r--) {
			Button currButton = (Button) column.getChildren().get(r);
			if (!currButton.isDisable()) {
				return r;
			}
		}
		return -1;
	}
	
	@Override
	protected Color getColor() { return AIColor; }
}
