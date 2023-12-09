package application;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

/**
 * Controller class for the game.
 */
public class SampleController {
	
	private GameBoard gameBoard;
	private GameLog gameState;
	private AI ai;
	@FXML private HBox board; /* Container that holds the game */
	@FXML private Button reset;
	@FXML private Button start;
	@FXML private Button save;
	@FXML private Button load;
	@FXML private RadioButton simple;
	@FXML private RadioButton omega;
	@FXML private ToggleGroup AIGroup;
	boolean playerTurn;
	
	/**
	 * Initializes the game and calls all necessary game logic functions.
	 */
	public void initialize() {
		gameBoard = new GameBoard(/* change parameters -> rows, cols, color */);
		gameState = new GameLog(/* any changes you make in game board please add here */);
		ai = new AI(/* any changes you make in game board please add here */);
		gameBoard.createBoard(board);
		initializeAISelection();
		initializeButtons();
		playerTurn = true;
	}	
	
	/* GAME LOGIC FUNCTIONS */
	/**
	 * Initializes buttons and calls all necessary event handlers.
	 */
	public void initializeButtons() {
		start.setDisable(true);
		reset.setOnAction(event -> {
			handleResetButton(reset);
		});
		simple.setOnAction(event -> {
			handleSimpleAIPress();
		});
		omega.setOnAction(event -> {
			handleOmegaAIPress();
		});
		start.setOnAction(event -> {
			handleStartButton(start);
		});
		save.setOnAction(event -> {
			handleSavePress(save);
		});
		load.setOnAction(event -> {
			handleLoadPress();
		});
	}
	
	/**
	 * Handles when the load button has been pressed.
	 */
	public void handleLoadPress() {
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		DialogPane dialogPane = gameBoard.createDialogBox(confirmation, "Load Confirmation", "Are you sure you want to load the save file?");
		confirmation.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
		confirmation.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				gameBoard.resetBoard(board, start, AIGroup, gameState);
				gameState.setSaveState(true);
				gameState.importFile("save_file.txt", ai);
				if (gameState.getSaveState()) {
					gameBoard.setRows(gameState.getRows());
					gameBoard.setCols(gameState.getCols());
					gameBoard.createBoard(board);
					gameState.drawSavedBoard(board);
					initializePieces();
					
					ai.setRows(gameState.getRows());
					ai.setCols(gameState.getCols());
					if (ai.getAIDifficulty() == false) {
						AIGroup.getToggles().get(0).setSelected(true);
					} else {
						AIGroup.getToggles().get(1).setSelected(true);
					}
					AIGroup.getToggles().forEach(toggle -> {
						Node node = (Node) toggle;
						node.setDisable(true);
					});
				} else {
					gameBoard.createBoard(board);
				}
			}
		});
	}
	
	/**
	 * Initializes game pieces and sets the event handler to the pieces.
	 */
	public void initializePieces() {
		for (int c = 0; c < gameBoard.getCols(); c++) {
			VBox column = (VBox) board.getChildren().get(c);
			for (int r = 0; r < column.getChildren().size(); r++) {
				Button currButton = (Button) column.getChildren().get(r);
				currButton.setOnAction(event -> {
					handleButtonPress(currButton);
				});
			}
		}
	}
	
	
	/**
	 * Initializes AI Selection and disables toggle group once a selection has been made.
	 * Users are able to have a confirmation window before the toggle group is disabled.
	 */
	public void initializeAISelection() {
		AIGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if (gameState.getSaveState()) {
				return;
			}
			if (newValue != null && !gameState.getSaveState()) {
				boolean confirm = gameBoard.createConfirmation();
				if (confirm) {
					AIGroup.getToggles().forEach(toggle -> {
						Node node = (Node) toggle;
						if (node != newValue) {
							node.setDisable(true);
						}
					});
					start.setDisable(false);
				} else {
					AIGroup.getSelectedToggle().setSelected(false);
				}
			}
		});
	}
	
	/* EVENT LISTENERS */
	
	/**
	 * Handles when save button has been pressed. Ask users if he wants it to be saved. If confirmed
	 * export save file to current working directory and reset board and quit the game.
	 * @param btn The button pressed.
	 */
	public void handleSavePress(Button btn) {
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		DialogPane dialogPane = gameBoard.createDialogBox(confirmation, "Save Confirmation", "Are you sure you want to save the game?");
		confirmation.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
		confirmation.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				gameState.exportFile(ai);
				gameBoard.resetBoard(board, start, AIGroup, gameState);
				gameBoard.createBoard(board);
				System.exit(0);
			}
		});
	}
	
	/**
	 * Handles when the simple AI option has been chosen.
	 * Sets the difficulty level to false (DEFAULT LEVEL).
	 */
	public void handleSimpleAIPress() {
		ai.setAIDifficulty(false);
	}
	
	/**
	 * Handles when the smart AI option has been chosen.
	 * Sets the difficulty level to true.
	 */
	public void handleOmegaAIPress() {
		ai.setAIDifficulty(true);
	}
	
	/**
	 * Handles when a chip piece has been pressed.
	 * @param btn
	 */
	public void handleButtonPress(Button btn) {
		PauseTransition pause2 = new PauseTransition(Duration.seconds(1.5));
		disableButtons();
		handlePlayerTurn(btn);
		
		pause2.setOnFinished(event -> {
			enableButtons();
			
			if (gameState.isGameDraw(gameBoard)) {
				handleDraw();
			}
			
			PauseTransition pause = new PauseTransition(Duration.seconds(0.25));
			
			pause.setOnFinished(event2 -> {
				if (!playerTurn) {
					Move AIMove = ai.difficulty(ai.getAIDifficulty(), gameState, board);
					handleAITurn(AIMove);
					gameState.addMove(AIMove);
					if (gameState.checkWin(AIMove)) {
						handleWinner();
					}
					if (gameState.isGameDraw(gameBoard)) {
						handleDraw();
					}
				}
			});
			
			pause.play();
			
		});
		
		pause2.play();
		
	}
	
	
	/**
	 * Handles the player turn. Animates the chip falling and checks if there is a win.
	 * @param btn The column line pressed.
	 */
	public void handlePlayerTurn(Button btn) {
		int colIndex = gameBoard.getColIndex(board, btn);
		VBox column = (VBox) board.getChildren().get(colIndex);
		int rowIndex = gameBoard.getNextAvailableRow(column);
		Button availableRowButton = (Button) column.getChildren().get(rowIndex);
		gameBoard.animateSinkEffect(column, gameBoard.getColor());
		gameBoard.disableRow(availableRowButton, rowIndex, gameBoard.getColor());
		Move move = new Move(rowIndex, colIndex, gameBoard.getColor());
		gameState.addMove(move);
		if (gameState.checkWin(move)) {
			handleWinner();
			playerTurn = true;
		} else {
			playerTurn = false;
		}
	}
	/**
	 * Handles the AI move. Animates the AI move and checks if there is a win.
	 * @param AIMove The AI move.
	 */
	public void handleAITurn(Move AIMove) {
		VBox AIColumn = (VBox) board.getChildren().get(AIMove.getCol());
		gameBoard.animateSinkEffect(AIColumn, ai.getColor());
		Button AIAvailableRowButton = (Button) AIColumn.getChildren().get(AIMove.getRow());
		gameBoard.disableRow(AIAvailableRowButton, AIMove.getRow(), ai.getColor());
		AIAvailableRowButton.setStyle("-fx-background-color: " + gameBoard.hexCode(ai.getColor()));
		playerTurn = true;
	}
	
	/**
	 * Handles if a player or AI wins.
	 */
	public void handleWinner() {
		Alert winAlert = new Alert(AlertType.INFORMATION);
		DialogPane dialogPane = gameBoard.createDialogBox(winAlert, "WINNER", gameState.getWinner());
		winAlert.show();
		for (int i = 0; i < board.getChildren().size(); i++) {
			board.getChildren().get(i).setDisable(true);
		}
	}
	
	/**
	 * Handles if a draw has occurred.
	 */
	public void handleDraw() {
		Alert winAlert = new Alert(AlertType.INFORMATION);
		DialogPane dialogPane = gameBoard.createDialogBox(winAlert, "DRAW", "No one won...");
		winAlert.show();
		for (int i = 0; i < board.getChildren().size(); i++) {
			board.getChildren().get(i).setDisable(true);
		}
	}
	
	/**
	 * Disables all buttons on board.
	 */
	public void disableButtons() {
		for (int i = 0; i < board.getChildren().size(); i++) {
			VBox column = (VBox) board.getChildren().get(i);
			for (int j = 0; j < column.getChildren().size(); j++) {
				Button currButton = (Button) column.getChildren().get(j);
				currButton.setOnAction(null);
			}
		}
	}
	
	/**
	 * Enables all buttons on board.
	 */
	public void enableButtons() {
		for (int i = 0; i < board.getChildren().size(); i++) {
			VBox column = (VBox) board.getChildren().get(i);
			for (int j = 0; j < column.getChildren().size(); j++) {
				Button currButton = (Button) column.getChildren().get(j);
				currButton.setOnAction(event -> {
					handleButtonPress(currButton);
				});
			}
		}
	}
	
	/**
	 * Handles when the reset button has been pressed, in which it resets the board.
	 * @param btn The reset button.
	 */
	public void handleResetButton(Button btn) {
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		DialogPane dialogPane = gameBoard.createDialogBox(confirmation, "Reset Confirmation", "Are you sure you want to reset the board?");
		confirmation.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
		confirmation.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				initializeButtons();
				playerTurn = false;
				gameState.deleteOldSaveFile();
				gameBoard.resetBoard(board, start, AIGroup, gameState);
				gameBoard.createBoard(board);
			}
		});
	}
	
	/**
	 * Handles when the start button, in which it starts the game.
	 * @param btn The start button.
	 */
	public void handleStartButton(Button btn) {
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		String title = "Start Confirmation";
		String message = "Start Game?";
		DialogPane dialogPane = gameBoard.createDialogBox(confirmation, title, message);
		confirmation.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		confirmation.showAndWait().ifPresent(response -> {
			if (response == ButtonType.YES) {
				initializePieces();
				playerTurn = false;
			}
		});
		btn.setDisable(true);
	}
}
