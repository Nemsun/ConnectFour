package application;

import java.awt.Color;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class GameBoard {
	
	int rows;
	int cols;
	
	static String idleButtonStyle;
	static String hoverButtonStyle;
	Color playerColor;
	
	static Color defaultPlayerColor = new Color(255, 105, 97);
	
	/* CONSTRUCTORS */
	/**
	 * Constructs a GameBoard with default dimensions and player color.
	 */
	protected GameBoard() {
		this(6, 7, defaultPlayerColor);
	}
	
	/**
	 * Constructs a GameBoard with specified rows and columns using the default player color.
	 * @param userRows The number of rows for the GameBoard.
	 * @param userCols The number of columns for the GameBoard.
	 */
	protected GameBoard(int userRows, int userCols) {
		this(userRows, userCols, defaultPlayerColor);
	}
	
	/**
	 * Constructs a GameBoard with specified rows, columns, and player color.
	 * @param userRows The number of rows for the GameBoard.
	 * @param userCols The number of columns for the GameBoard.
	 * @param color The player color for the GameBoard.
	 */
	protected GameBoard(int userRows, int userCols, Color color) {
		this.rows = userRows;
		this.cols = userCols;
		this.playerColor = color;
		setDefaultStyle();
	}
	
	/**
	 * Constructs a GameBoard with specified player color using default dimensions.
	 * @param color The player color for the GameBoard.
	 */
	protected GameBoard(Color color) {
		this();
		this.playerColor = color;
		setDefaultStyle();
	}
	
	/* MUTATORS */
	
	/**
	 * Sets the number of rows for the GameBoard.
	 * @param newRows
	 */
	protected void setRows(int newRows) { 
		rows = newRows; 
	}
	
	/**
	 * Sets the number of columns for the GameBoard.
	 * @param newCols
	 */
	protected void setCols(int newCols) {
		cols = newCols;
	
	}
	
	/**
	 * Sets the player color for the game board.
	 * @param newColor
	 */
	protected void setColor(Color newColor) { 
		playerColor = newColor;
		setDefaultStyle();
	}
	
	/**
	 * Sets the default style for the player and GameBoard.
	 */
	protected void setDefaultStyle() {
		idleButtonStyle = "-fx-background-color: WHITESMOKE";
		hoverButtonStyle = "-fx-background-color: " + hexCode(playerColor) + ";";
	}
	
	/* ACCESSORS */
	
	/**
	 * Gets the number of rows in the GameBoard
	 * @return The number of rows in the GameBoard.
	 */
	protected int getRows() { 
		return rows; 
	}
	
	/**
	 * Gets the number of columns in the GameBoard. 
	 * @return The number of columns in the GameBoard.
	 */
	protected int getCols() { 
		return cols; 
	}
	
	/**
	 * Gets the player color.
	 * @return The player color.
	 */
	protected Color getColor() { 
		return playerColor;
	};
	
	/**
	 * Gets the style for a idle button.
	 * @return The style for the idle button.
	 */
	protected String getIdleStyle() { 
		return idleButtonStyle; 
	}
	
	/**
	 * Gets the style for a hovered button.
	 * @return The style for a hovered button.
	 */
	protected String getHoverStyle() { 
		return hoverButtonStyle;
	}
		
	/**
	 * Creates a hex code for a given color.
	 * @param color The color object.
	 * @return The hex code string.
	 */
	protected String hexCode(Color color) { /* RETURN HEXCODE OF PLAYER COLOR */
		String hexCode = "#" + Integer.toHexString(color.getRGB()).substring(2);
		return hexCode;
	}
	
	/**
	 * Resets the board to the initial state.
	 * @param board The game board container.
	 * @param start The start button.
	 * @param AIGroup The AI radio button toggle group.
	 */
	protected void resetBoard(HBox board, Button start, ToggleGroup AIGroup, GameLog gameState) {
		start.setDisable(true);
		board.getChildren().clear();
		gameState.setSaveState(false);
		gameState.resetGameLog();
		AIGroup.getToggles().forEach(toggle -> {
			Node node = (Node) toggle;
			if (node.isDisable()) {
				node.setDisable(false);
			}
		});
		AIGroup.selectToggle(null);
	}
	
	/**
	 * Creates and initializes the game board based on GameBoard object.
	 * @param board The game board container.
	 */
	protected void createBoard(HBox board) {
		for (int c = 0; c < this.getCols(); c++) {
			VBox tempVBox = this.createVBox(board);
			for (int r = 0; r < this.getRows(); r++) {
				Button tempButton = this.createChip(board);
				tempVBox.getChildren().addAll(tempButton);
			}
			tempVBox.hoverProperty().addListener((observable, oldValue, newValue) -> {
				if (newValue) {
					tempVBox.getChildren().forEach(button -> {
						Node node = (Node) button;
						if (node.isDisable()) {
							if (node.getStyle().contains(this.getHoverStyle())) {
								node.setStyle("-fx-opacity: 0.5;" + this.getHoverStyle());
							}
						} else {
							node.setStyle(this.getHoverStyle());
						}
						node.hoverProperty().addListener((obs, ov, nv) -> {
							if (nv) {
								if (!node.isDisable()) {
									node.setStyle("-fx-background-color: #7289DA;");
								}
							} else {
								node.setStyle(this.getHoverStyle());
							}
						});
					});
				} else {
					tempVBox.getChildren().forEach(button -> {
						Node node = (Node) button;
						if (node.isDisable()) {
							if (node.getStyle().contains(this.getHoverStyle())) {
								node.setStyle("-fx-opacity: 0.5;" + this.getHoverStyle());
							}
						} else {
							node.setStyle(this.getIdleStyle());
						}
						node.hoverProperty().addListener((obs, ov, nv) -> {
							if (nv) {
								if (!node.isDisable()) {
									node.setStyle("-fx-background-color: : #7289DA;");
								}
							} else {
								node.setStyle(this.getIdleStyle());
							}
						});
					});
				}
			});
			board.getChildren().addAll(tempVBox);
		}
	}
	
	/**
	 * Creates and returns a VBox object for the game board.
	 * @param board The game board container.
	 * @return A configured VBox object for the game board.
	 */
	protected VBox createVBox(HBox board) {
		VBox vbox = new VBox();
		int prefWidth = (int) (board.getPrefWidth() / this.getCols());
		vbox.setPrefWidth(prefWidth);
		vbox.setPrefHeight(board.getPrefHeight());
		return vbox;
	}
	
	/**
	 * Creates and return a game piece for the game board.
	 * @param board The game board container.
	 * @return A configured game piece.
	 */
	protected Button createChip(HBox board) {
		Button chip = new Button();
		int prefWidth = (int) (board.getPrefWidth() / this.getCols());
		
		/* Button Styling */
		chip.setShape(new Circle(1));
		chip.setStyle(this.getIdleStyle());
		chip.setPrefWidth(prefWidth);
		chip.setPrefHeight(board.getPrefHeight() / this.getRows());
		
		/* DEFAULT SET ACTION: WARNING */
		chip.setOnAction(event -> {
			String title = "Warning!";
			String message = "Please start game first!";
			warningMessage(title, message);
		});
		return chip;
	}
	
	/**
	 * Creates a warning message dialog pane.
	 * @param title The specified title.
	 * @param message The specified message.
	 */
	protected void warningMessage(String title, String message) {
		Alert alert = new Alert(AlertType.WARNING);
		DialogPane dialogPane = createDialogBox(alert, title, message);
		alert.show();
	}
	
	/**
	 * Creates a dialog box with the specified parameters.
	 * @param confirmation The alert.
	 * @param title The String title.
	 * @param message The String message. 
	 * @return Returns DialogPane with the title and message.
	 */
	protected DialogPane createDialogBox(Alert alert, String title, String message) {
		alert.initStyle(StageStyle.UNDECORATED);
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		alert.setTitle(title);
		alert.setContentText(message);
		alert.setHeaderText(null);
		return dialogPane;
	}
	
	/**
	 * Creates a pop-up window that asks for user's confirmation of their selection.
	 * @return A boolean flag that is either YES or NO
	 */
	protected boolean createConfirmation() {
		Alert confirmation = new Alert(AlertType.CONFIRMATION);
		DialogPane dialogPane = this.createDialogBox(confirmation, "Confirmation", "Are you sure about making this selection?");
		confirmation.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		ButtonType result = confirmation.showAndWait().orElse(ButtonType.NO);
		return result == ButtonType.YES;
	}
	
	/**
	 * Creates a sink effect animation using keyframes and timeline.
	 * @param btn The button being handled.
	 * @param offset The duration of the animation.
	 */
	protected void sinkEffect(Button btn, int offset, Color player) {
		Duration duration = Duration.seconds(offset * 0.1);
		KeyFrame keyFrame = new KeyFrame(duration, event -> {
			btn.setStyle("-fx-background-color: " + this.hexCode(player));
		});
		
		Timeline timeline = new Timeline(keyFrame);
		
		timeline.setOnFinished(event -> {
			
			Timeline resetTimeline = new Timeline(
				new KeyFrame(Duration.seconds(0.1), e -> {
					btn.setStyle(this.getIdleStyle());
				})
			);
			resetTimeline.play();
		});
		timeline.play();
	}
	
	/**
	 * Animates sink effect.
	 * @param column The column in which the piece is played.
	 */
	protected void animateSinkEffect(VBox column, Color player) {
		int rowIndex = getNextAvailableRow(column);
		for (int r = 0; r < rowIndex; r++) {
			Button currButton = (Button) column.getChildren().get(r);
			sinkEffect(currButton, rowIndex + r, player);
			final int currRow = r;
			PauseTransition pause = new PauseTransition(Duration.seconds(rowIndex * 0.1 * 2));
			pause.setOnFinished(event -> {
				if (currRow == rowIndex) {
					currButton.setDisable(true);
				}
			});
			pause.play();
		}
	}
	
	/**
	 *
	 * Gets the column index of the button.
	 * @param board The container for the game board.
	 * @param btn The button being pressed.
	 * @return The column index of the button.
	 */
	public int getColIndex(HBox board, Button btn) {
		return board.getChildren().indexOf(btn.getParent());
	}
	
	/**
	 * Gets the next available row in a column.
	 * @param column The VBox representing the column.
	 * @return The next available row in the column.
	 */
	protected int getNextAvailableRow(VBox column) {
		for (int r = column.getChildren().size() - 1; r >= 0; r--) {
			Button currButton = (Button) column.getChildren().get(r);
			if (!currButton.isDisable()) {
				return r;
			}
		}
		return 0;
	}
	
	/**
	 * Disables the row in which the piece is played.
	 * @param btn The button being disabled.
	 * @param rowIndex The row index of the button.
	 */
	public void disableRow(Button btn, int rowIndex, Color player) {
		PauseTransition pause = new PauseTransition(Duration.seconds(rowIndex * 0.1 * 2));
		pause.setOnFinished(event -> {
			btn.setDisable(true);
		});
		pause.play();
		
		btn.disabledProperty().addListener((observer, oldValue, newValue) -> {
			if (newValue) {
				btn.setStyle("-fx-opacity: 0.5; -fx-background-color: " + this.hexCode(player));
			}
		});
	}
	
}
