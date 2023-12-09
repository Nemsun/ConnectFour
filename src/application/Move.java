package application;

import java.awt.Color;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class Move {
	int row;
	int col;
	Color playerColor;
	boolean isChecked;
	/**
	 * Constructor for the move object.
	 * @param playedRow The next available row.
	 * @param playedCol The played column.
	 * @param playerMoveColor The current player color.
	 */
	protected Move(int playedRow, int playedCol, Color playerMoveColor) {
		this.row = playedRow;
		this.col = playedCol;
		this.playerColor = playerMoveColor;
		this.isChecked = false;
	}
	
	/* SETTERS */
	protected void setRow(int playedRow) { row = playedRow; }
	protected void setCol(int playedCol) { row = playedCol; }
	protected void setColor(Color playerMoveColor) { playerColor = playerMoveColor; }
	protected void setChecked(boolean checked) { isChecked = checked; }
	
	/* GETTERS */
	protected int getRow() { return row;}
	protected int getCol() { return col; }
	protected Color getColor() { return playerColor; }
	protected boolean getChecked() { return isChecked; }
	
	@Override
	public String toString() {
		return "ROW: " + getRow() + " COL: " + getCol() + " COLOR: " + getColor();
	}
}
