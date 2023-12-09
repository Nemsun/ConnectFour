package application;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameLog extends GameBoard {
	/* This class will keep track of game moves
	 * This class should track the coordinates of the moves
	 * This class should track the color of the pieces of the moves (distinguish player or AI)
	 * IDEA: ARRAYLIST<MOVES> = MOVES(ROW, COL, COLOR)
	 * 
	 * Functions:
	 * export file -> ToString();
	 * import file -> readString();
	 * */
	ArrayList<Move> gameLog;
	boolean saveState;
	
	protected GameLog() {
		super();
		gameLog = new ArrayList<>();
		saveState = false;
	}
	
	protected GameLog(int userRows, int userCols) {
		super(userRows, userCols);
		gameLog = new ArrayList<>();
		saveState = false;
	}
	
	protected GameLog(int userRows, int userCols, Color color) {
		super(userRows, userCols, color);
		gameLog = new ArrayList<>();
		saveState = false;
	}
	
	protected GameLog(Color color) {
		super(color);
		gameLog = new ArrayList<>();
		saveState = false;
	}
	
	protected void setSaveState(boolean newSaveState) { saveState = newSaveState; }
	protected boolean getSaveState() { return saveState; }
	
	protected void addMove(Move move) {
		this.gameLog.add(move);
	}
	
	protected void resetGameLog() {
		this.gameLog.clear();
	}
	
	protected void resetMoves() {
		for (int i = 0; i < gameLog.size(); i++) {
			gameLog.get(i).setChecked(false);
		}
	}
	protected boolean checkWin(Move currMove) {
		if (this.gameLog.isEmpty()) { return false; }
		int count = 0;
		/* [MOVE, MOVE, MOVE, MOVE] 
		 *   v		v	  v		v	
		 *  (ROW, COL, COLOR), (ROW, COL, COLOR), (ROW, COL, COLOR), (ROW, COL, COLOR)
		 * 
		 * */
		/* CHECK VERTICAL, CHECK HORIZONTAL */
		if (checkVertical(currMove, count)) {
			return true;
		}
		if (checkHorizontal(currMove, count)) {
			return true;
		}
		/* CHECK UP DIAGONALS, CHECK DOWN DIAGONALS */		
		if (checkDiagonals(currMove, count)) {
			return true;
		}
		return false;
	}
	
	protected boolean checkDiagonals(Move currMove, int count) {
		int countDLD = countDownLeftDiagonal(currMove, count);
		int countDRD = countDownRightDiagonal(currMove, count);
		int countULD = countUpLeftDiagonal(currMove, count);
		int countURD = countUpRightDiagonal(currMove, count);
		if ((countDLD + countURD + 1) >= 4) {
			return true;
		}
		if ((countDRD + countULD + 1) >= 4) {
			return true;
		}
		return false;
	}
	
	
	protected int countDownRightDiagonal(Move currMove, int count) {
		currMove.setChecked(true);
		int currRow = currMove.getRow();
		int currCol = currMove.getCol();
		Color playerColor = currMove.getColor();
		for (Move tempMove : this.gameLog) {
			int tempRow = tempMove.getRow();
			int tempCol = tempMove.getCol();
			if (tempCol == (currCol + 1) && tempRow == (currRow + 1)) {
				if (!tempMove.getChecked()) {
					if (tempMove.getColor() == playerColor) {
						return countDownRightDiagonal(tempMove, count + 1);
					}
				}
			}
		}
		resetMoves();
		return count;
	}
	
	protected int countDownLeftDiagonal(Move currMove, int count) {
		currMove.setChecked(true);
		int currRow = currMove.getRow();
		int currCol = currMove.getCol();
		Color playerColor = currMove.getColor();
		for (Move tempMove : this.gameLog) {
			int tempRow = tempMove.getRow();
			int tempCol = tempMove.getCol();
			if (tempCol == (currCol - 1) && tempRow == (currRow + 1)) {
				if (!tempMove.getChecked()) {
					if (tempMove.getColor() == playerColor) {
						return countDownLeftDiagonal(tempMove, count + 1);
					}
				}
			}
		}
		resetMoves();
		return count;
	}
	
	
	protected int countUpRightDiagonal(Move currMove, int count) {
		currMove.setChecked(true);
		int currRow = currMove.getRow();
		int currCol = currMove.getCol();
		Color playerColor = currMove.getColor();
		for (Move tempMove : this.gameLog) {
			int tempRow = tempMove.getRow();
			int tempCol = tempMove.getCol();
			if (tempCol == (currCol + 1) && tempRow == (currRow - 1)) {
				if (!tempMove.getChecked()) {
					if (tempMove.getColor() == playerColor) {
						return countUpRightDiagonal(tempMove, count + 1);
					}
				}
			}
		}
		resetMoves();
		return count;
	}
	
	protected int countUpLeftDiagonal(Move currMove, int count) {
		currMove.setChecked(true);
		int currRow = currMove.getRow();
		int currCol = currMove.getCol();
		Color playerColor = currMove.getColor();
		for (Move tempMove : this.gameLog) {
			int tempRow = tempMove.getRow();
			int tempCol = tempMove.getCol();
			if (tempCol == (currCol - 1) && tempRow == (currRow - 1)) {
				if (!tempMove.getChecked()) {
					if (tempMove.getColor() == playerColor) {
						return countUpLeftDiagonal(tempMove, count + 1);
					}
				}
			}
		}
		resetMoves();
		return count;
	}
	
	protected boolean checkVertical(Move currMove, int count) {
		int countDown = countDown(currMove, count);
		return (countDown + 1) >= 4;
	}
	
	protected int countDown(Move currMove, int count) {
		currMove.setChecked(true);
		int currRow = currMove.getRow();
		int currCol = currMove.getCol();
		Color playerColor = currMove.getColor();
		for (Move tempMove : this.gameLog) {
			int tempRow = tempMove.getRow();
			int tempCol = tempMove.getCol();
			if (tempRow == (currRow + 1) && tempCol == (currCol)) {
				if (!tempMove.getChecked()) {
					if (tempMove.getColor() == playerColor) {
						return countDown(tempMove, count + 1);
					}
				}
			}
		}
		resetMoves();
		return count;
	}
	
	protected boolean checkHorizontal(Move currMove, int count) {
		int countRight = countRight(currMove, count);
		int countLeft = countLeft(currMove, count);
		return (countRight + countLeft + 1) >= 4;
	}
	
	protected int countRight(Move currMove, int count) {
		currMove.setChecked(true);
		int currRow = currMove.getRow();
		int currCol = currMove.getCol();
		Color playerColor = currMove.getColor();
		for (Move tempMove : this.gameLog) {
			int tempRow = tempMove.getRow();
			int tempCol = tempMove.getCol();
			if (tempCol == (currCol + 1) && tempRow == (currRow)) {
				if (!tempMove.getChecked()) {
					if (tempMove.getColor() == playerColor) {
						return countRight(tempMove, count + 1);
					}
				}
			}
		}
		resetMoves();
		return count;
	}
	
	protected int countLeft(Move currMove, int count) {
		currMove.setChecked(true);
		int currRow = currMove.getRow();
		int currCol = currMove.getCol();
		Color playerColor = currMove.getColor();
		for (Move tempMove : this.gameLog) {
			int tempRow = tempMove.getRow();
			int tempCol = tempMove.getCol();
			if (tempCol == (currCol - 1) && tempRow == (currRow)) {
				if (!tempMove.getChecked()) {
					if (tempMove.getColor() == playerColor) {
						return countLeft(tempMove, count + 1);
					}
				}
			}
		}
		resetMoves();
		return count;
	}
	
	/**
	 * Gets the winner from the last move made.
	 * @return A String determining the winner.
	 */
	protected String getWinner() {
		if (this.gameLog.get(this.gameLog.size() - 1).getColor() != this.getColor()) {
			return "AI WINS";
		}
		return "PLAYER WINS";
	}


	/**
	 * Checks if the game board is filled.
	 * @param gameBoard The game board.
	 * @return true if filled and false if not filled.
	 */
	protected boolean isGameDraw(GameBoard gameBoard) {
		if (this.gameLog.size() == (gameBoard.getCols() * gameBoard.getRows())) {
			return true;
		}
		return false;
	}
	/** 
	 * Exports a txt file that will be used to save the game state.
	 * @param ai The AI.
	 */
	protected void exportFile(AI ai) {
		/* DATA TO BE EXPORTED 
		 * [GameBoard: ROW | COL ]
		 * [AI: DIFFICULTY LEVEL ]
		 * [GameLog: ALL MOVES ]
		 * */
		String fileName = "save_file.txt";
		File file = new File(fileName);
		try {
            if (file.exists()) {
                System.out.println("FILE EXISTS: Appending new moves to the file.");
                List<String> existingMoves = readExistingMoves(fileName);
                try (PrintWriter outfile = new PrintWriter(new FileWriter(fileName, true))) {
                    for (Move currMove : this.gameLog) {
                        String moveString = "MOVE: " + currMove;
                        if (!existingMoves.contains(moveString)) {
                            outfile.println(moveString);
                        }
                    }
                }
            } else {
                try (PrintWriter outfile = new PrintWriter(new FileWriter(fileName))) {
                    outfile.println("ROWS: " + this.getRows());
                    outfile.println("COLS: " + this.getCols());
                    outfile.println("DIFFICULTY: " + (ai.getAIDifficulty() ? "1" : "0"));
                    if (!this.gameLog.isEmpty()) {
                        for (Move currMove : this.gameLog) {
                            outfile.println("MOVE: " + currMove);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not create/write to the file");
            e.printStackTrace();
        }
	}
	
	/**
	 * Reads the existing moves in the game log.
	 * @param fileName The save file.
	 * @return A list of existing moves to ignore.
	 * @throws IOException
	 */
	protected List<String> readExistingMoves(String fileName) throws IOException {
        List<String> existingMoves = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                existingMoves.add(scanner.nextLine());
            }
        }
        return existingMoves;
    }
	
	/**
	 * Load function.
	 * @param fileName The save file.
	 * @param ai The current AI object.
	 */
	protected void importFile(String fileName, AI ai) {
		Scanner infile = null;
		ArrayList<Move> savedMoveList = new ArrayList<>();
		String ignore = "";
		int boardRow = 0, boardCol = 0, moveRow = 0, moveCol = 0, difficulty = 0;
		try {
			infile = new Scanner(new FileReader(fileName));
			while (infile.hasNextLine()) {
				String line = infile.nextLine();
				StringTokenizer tokenizer = new StringTokenizer(line);
				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					if (token.equals("ROWS:")) {
						boardRow = Integer.parseInt(tokenizer.nextToken());
						this.setRows(boardRow);
					}
					if (token.equals("COLS:")) {
						boardCol = Integer.parseInt(tokenizer.nextToken());
						this.setCols(boardCol);
					}
					if (token.equals("DIFFICULTY:")) {
						difficulty = Integer.parseInt(tokenizer.nextToken());
						if (difficulty == 1) {
							ai.setAIDifficulty(true);
						}
					}
					if (token.equals("MOVE:")) {
						ignore = tokenizer.nextToken();
						moveRow = Integer.parseInt(tokenizer.nextToken());
						ignore = tokenizer.nextToken();
						moveCol = Integer.parseInt(tokenizer.nextToken());
						ignore = tokenizer.nextToken();
						String color = tokenizer.nextToken();
						int startBracketIndex = color.indexOf("[");
						color = color.substring(startBracketIndex + 1, color.length() - 1);
						String[] RGBComps = color.split(",");
						for (int i = 0; i < RGBComps.length; i++) {
							int equalsIndex = RGBComps[i].lastIndexOf("=") + 1;
							RGBComps[i] = RGBComps[i].substring(equalsIndex);
						}
						int red = Integer.parseInt(RGBComps[0]);
						int green = Integer.parseInt(RGBComps[1]);
						int blue = Integer.parseInt(RGBComps[2]);
						savedMoveList.add(new Move(moveRow, moveCol, new Color(red, green, blue)));
					}
				}
			}
			infile.close();
		}  	catch (FileNotFoundException e) {
			System.out.println("File not found.");
			e.printStackTrace();
			Alert confirmation = new Alert(AlertType.WARNING);
			DialogPane dialogPane = this.createDialogBox(confirmation, "ERROR", "Save file not found!");
			confirmation.show();
			this.setSaveState(false);
		}
		this.gameLog = savedMoveList;
		System.out.println("Loaded");
	}
	
	/**
	 * Draws the saved board from the game log.
	 * @param board The current game board.
	 */
	protected void drawSavedBoard(HBox board) {
		for (Move currMove : this.gameLog) {
			VBox column = (VBox) board.getChildren().get(currMove.getCol());
			Button rowButton = (Button) column.getChildren().get(currMove.getRow());
			this.disableRow(rowButton, currMove.getRow(), currMove.getColor());
		}
	}
	
	/**
	 * Deletes the old save file when a game has been reset.
	 */
	protected void deleteOldSaveFile() {
		String fileName = "save_file.txt";
		File saveFile = new File(fileName);
		if (saveFile.delete()) {
			System.out.println("Deleted file successfully");
		} else {
			System.out.println("Failed to delete file");
		}
	}
}
