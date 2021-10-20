/**
 * Minesweeper.java
 * @author Serwin Limbu
 */

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * This program automatically creates a Minesweeper board according to the difficulty
 * chosen by the user, with randomly generated bombs. It allows the user to play the Minesweeper
 * game using text interaction.
 */

public class Minesweeper {
	
	private static Cell[][] board; // represents the Minesweeper board
	private static int YLIMIT;  // row limit of the board
	private static int XLIMIT;  // column limit of the board
	private static int numOfBombs;  // bomb limit of the board
	
	public static Cell[][] getBoard() {
		return board;
	}
	
	public static int getYLimit() {
		return YLIMIT;
	}
	
	public static int getXLimit() {
		return XLIMIT;
	}
	
	
	public static void testPrint() {
		for(Cell[] row : board) {
			for(Cell c : row) {
				c.toggleRevealed();
				System.out.print(c.getVal() + "  ");
				c.toggleRevealed();
			}
			System.out.println();
		}
	}
	
	public static void printBoard() {
		for(Cell[] row : board) {
			for(Cell c : row) {
				System.out.print(c.getVal() + "  ");
			}
			System.out.println();
		}
	}
	
	public static void printWinBoard() {
		for(Cell[] row : board) {
			for(Cell c : row) {
				if (c.getVal().equals("?")) {
					c.toggleRevealed();
				}
				System.out.print(c.getVal() + "  ");
			}
			System.out.print("\n");
		}
	}
	
	public static void printLoseBoard() {
		for(Cell[] row : board) {
			for(Cell c : row) {
				if (c.getVal().equals("?")) {
					c.toggleRevealed();
					if (c.getVal().equals("*")) {
						System.out.print(c.getVal() + "  ");
					} else {
						System.out.print("?  ");
					}
				} else {
					System.out.print(c.getVal() + "  ");
				}
			}
			System.out.print("\n");
		}
	}
	
	
	private static void incrementTopBomb(int row, int col) {
		if (col == 0) {		// top left cell
			board[row][col+1].increment();
			board[row+1][col].increment();
			board[row+1][col+1].increment();
		} else if (col == XLIMIT) {		// top right cell
			board[row][col-1].increment();
			board[row+1][col].increment();
			board[row+1][col-1].increment();
		} else {		// top row cell
			board[row][col-1].increment();
			board[row][col+1].increment();
			board[row+1][col-1].increment();
			board[row+1][col].increment();
			board[row+1][col+1].increment();
		}
	}
	
	private static void incrementBottomBomb(int row, int col) {
		if (col == 0) {		// bottom left cell
			board[row][col+1].increment();
			board[row-1][col].increment();
			board[row-1][col+1].increment();
		} else if (col == XLIMIT) {		// bottom right cell
			board[row][col-1].increment();
			board[row-1][col].increment();
			board[row-1][col-1].increment();
		} else {		// bottom row cell
			board[row][col-1].increment();
			board[row][col+1].increment();
			board[row-1][col-1].increment();
			board[row-1][col].increment();
			board[row-1][col+1].increment();
		}
	}
	
	private static void incrementLeftMostBomb(int row, int col) {
		board[row-1][col].increment();
		board[row-1][col+1].increment();
		board[row][col+1].increment();
		board[row+1][col].increment();
		board[row+1][col+1].increment();
	}
	
	private static void incrementRightMostBomb(int row, int col) {
		board[row-1][col].increment();
		board[row-1][col-1].increment();
		board[row][col-1].increment();
		board[row+1][col].increment();
		board[row+1][col-1].increment();
	}
	
	private static void incrementMiddleBomb(int row, int col) {
		board[row-1][col-1].increment();
		board[row-1][col].increment();
		board[row-1][col+1].increment();
		board[row][col-1].increment();
		board[row][col+1].increment();
		board[row+1][col-1].increment();
		board[row+1][col].increment();
		board[row+1][col+1].increment();
	}
	
	
	/**
	 * Sets the number cells for the board. The number represents the amount of bomb cells present
	 * in its surrounding cells.
	 * The surrounding cells of a bomb cell is incremented by 1.
	 */
	public static void createNumberCells() {
		for (int i = 0; i <= YLIMIT; i++) {
			for (int j = 0; j <= XLIMIT; j++) {
				board[i][j].toggleRevealed();
				if (board[i][j].getVal().equals("*")) {  // checks where bomb cell is
					if (i == 0) {  // top row
						incrementTopBomb(i, j);
					} else if (i == YLIMIT) {  // bottom row
						incrementBottomBomb(i, j);
					} else if (j == 0) {  // left-most
						incrementLeftMostBomb(i, j);
					} else if (j == XLIMIT) {   // right-most
						incrementRightMostBomb(i, j);
					} else {
						incrementMiddleBomb(i, j);
					}
				}
				board[i][j].toggleRevealed();
			}
		}
	}
	
	
	/**
	 * Randomly generates positions in the board for the bomb cells and sets those cells as bombs (*).
	 */
	public static void createBombCells() {
		ArrayList<RowCol> bombPositions = new ArrayList<>();  // stores positions of bomb cells
		int bombCount = 0;   // tracks number of bombs that have been created
		Random rnd = new Random();  // used to randomly set the position of bomb cells
				
		while (bombCount != numOfBombs) {
			int row = rnd.nextInt(YLIMIT + 1);  // get random position
			int col = rnd.nextInt(XLIMIT + 1);
			boolean positionTaken = false;
			
			/** Adds new position to the bombPositions array list **/
			if (bombPositions.size() > 0) {
				for (RowCol position: bombPositions) {
					if ((position.getRow() == row) && (position.getCol() == col)) {  // checks if position is already taken by a bomb
						positionTaken = true;
						break;
					}
				}

				if (!positionTaken) {  // if position is not taken then add it to the list.
					bombPositions.add(new RowCol(row, col));
					bombCount++;
				}
				
			} else {
				bombPositions.add(new RowCol(row, col));
				bombCount++;
			}
			
		}
		
		/** Sets the bomb cells using the bombPosition array list **/
		for (RowCol position: bombPositions) {
			board[position.getRow()][position.getCol()] = new Cell("*");
		}
		
		/** Temporarily sets all cells except bomb cells, to blank cells **/
		for (int i = 0; i <= YLIMIT; i++) {
			for (int j = 0; j <= XLIMIT; j++) {
				if (board[i][j] == null) {
					board[i][j] = new Cell("-");
				}
			}
		}
		
	}
	
	
	/**
	 * Creates a board made of cells that are either blank (-), numbers (2) or bombs (*).
	 * The size of the board is determined by user's difficulty input.
	 * Easy = 9x9, Medium = 13x15, Hard = 16x30
	 */
	public static void createBoard() {
		Scanner menuScanner =  new Scanner(System.in);
		System.out.print("Choose difficulty (Easy = e, medium = m, Hard = h): ");
		String difficulty = menuScanner.next();
		
		switch (difficulty) {
		case "e":
			board = new Cell[9][9];
			YLIMIT = 8;
			XLIMIT = 8;
			numOfBombs = 10;
			break;
		case "m":
			board = new Cell[13][15];
			YLIMIT = 12;
			XLIMIT = 14;
			numOfBombs = 40;
			break;
		case "h":
			board = new Cell[16][30];
			YLIMIT = 15;
			XLIMIT = 29;
			numOfBombs = 99;
			break;
		}
		
		createBombCells();
		createNumberCells();
	}
	
	public static boolean gameWon() {
		for (Cell[] row: board) {
			for (Cell c: row) {
				if (c.getVal().equals("?")) {   // check if the not revealed cells are bombs
					c.toggleRevealed();
					if (!c.getVal().equals("*")) {  // if cell is not a bomb then return false
						c.toggleRevealed();
						return false;
					}
					c.toggleRevealed();
				}
			}
		}
		
		return true;
	}
	
	public static void updateTopRow(int row, int col) {
		if (col == 0) {					// If cell is at top left
			updateBoard(row, col+1);
			updateBoard(row+1, col);
			updateBoard(row+1, col+1);
		} else if (col == XLIMIT) {		// If cell is at top right
			updateBoard(row, col-1);
			updateBoard(row+1, col);
			updateBoard(row+1, col-1);
		} else {						// If cell is at top row
			updateBoard(row, col-1);
			updateBoard(row, col+1);
			updateBoard(row+1, col-1);
			updateBoard(row+1, col);
			updateBoard(row+1, col+1);
		}
	}
	
	public static void updateBottomRow(int row, int col) {
		if (col == 0) {					// If cell is at bottom left
			updateBoard(row, col+1);
			updateBoard(row-1, col);
			updateBoard(row-1, col+1);
		} else if (col == XLIMIT) {		// If cell is at bottom right
			updateBoard(row, col-1);
			updateBoard(row-1, col);
			updateBoard(row-1, col-1);
		} else {						// If cell is at bottom row
			updateBoard(row, col-1);
			updateBoard(row, col+1);
			updateBoard(row-1, col-1);
			updateBoard(row-1, col);
			updateBoard(row-1, col+1);
		}
	}
	
	public static void updateLeftmostColumn(int row, int col) {
		updateBoard(row-1, col);
		updateBoard(row-1, col+1);
		updateBoard(row, col+1);
		updateBoard(row+1, col);
		updateBoard(row+1, col+1);
	}
	
	public static void updateRightmostColumn(int row, int col) {
		updateBoard(row-1, col);
		updateBoard(row-1, col-1);
		updateBoard(row, col-1);
		updateBoard(row+1, col);
		updateBoard(row+1, col-1);		
	}
	
	public static void updateMiddle(int row, int col) {
		updateBoard(row-1, col-1);
		updateBoard(row-1, col);
		updateBoard(row-1, col+1);
		updateBoard(row, col-1);
		updateBoard(row, col+1);
		updateBoard(row+1, col-1);
		updateBoard(row+1, col);
		updateBoard(row+1, col+1);
	}
	
	public static void updateBoard(int row, int col) {
		if (board[row][col].getVal().equals("?")) {
			board[row][col].toggleRevealed();
			
			if (board[row][col].getVal().equals("*")) {
				board[row][col].toggleRevealed();
			} else if (board[row][col].getVal().equals("-")) {
				if (row == 0) {						// If cell is at top row
					updateTopRow(row , col);
				} else if (row == YLIMIT) {			// If cell is at bottom row
					updateBottomRow(row, col);
				} else if (col == 0) {				// If cell is at left-most column
					updateLeftmostColumn(row, col);
				} else if (col == XLIMIT) {			// If cell is at right-most column
					updateRightmostColumn(row, col);
				} else {							// If cell is not at any boundary
					updateMiddle(row, col);
				}
			}
		}
		
	}
	
	
	public static void writeBoard() {
		try {
			FileWriter boardWriter = new FileWriter("mineboard.txt");
			
			for (Cell[] row: board) {
				String rowLineOutput = "";
				for (Cell c: row) {
					c.toggleRevealed();
					rowLineOutput += c.getVal() + "  ";
					c.toggleRevealed();
				}
				boardWriter.write(rowLineOutput);
				boardWriter.write("\r\n");
			}
			boardWriter.close();
		} catch (IOException e) {
			System.out.println("Could not write board to file.");
			System.exit(0);
		}
		
		
	}
	
	/**
	 * Checks what is underneath user's first picked cell. If it's a bomb then it
	 * changes the bomb's position and updates the board for the correct number cells.
	 * @param in
	 */
	public static void firstMove(Scanner in) {
		System.out.print("Row: ");
		int firstRow = in.nextInt();
		System.out.print("Column: ");
		int firstCol = in.nextInt();
		
		Cell firstMoveCell = board[firstRow][firstCol];
		
		firstMoveCell.toggleRevealed();
		if (firstMoveCell.getVal().equals("*")) {
			firstMoveCell.setVal("-");
			firstMoveCell.toggleRevealed();
			Random rnd = new Random();
			boolean newPositionSet = false;
			
			// Set bomb to a new position/cell
			while (!newPositionSet) {
				int row = rnd.nextInt(YLIMIT + 1);  // get random position
				int col = rnd.nextInt(XLIMIT + 1);
				board[row][col].toggleRevealed();
				
				if (!(board[row][col].getVal().equals("*"))) {
					board[row][col].setVal("*");
					newPositionSet = true;
				}
				board[row][col].toggleRevealed();
			}
			
			
			for (int i = 0; i <= YLIMIT; i++) {
				for (int j = 0; j <= XLIMIT; j++) {
					board[i][j].toggleRevealed();
					board[i][j].resetBombProxNum();
					if (!(board[i][j].getVal().equals("*"))) {  // set all cells (except bomb) to blank
						board[i][j].setVal("-");
					}
					board[i][j].toggleRevealed();
				}
			}
			
			
			createNumberCells();
			updateBoard(firstRow, firstCol);
			printBoard();
			
		} else {
			firstMoveCell.toggleRevealed();
			updateBoard(firstRow, firstCol);
			printBoard();
		}
	
	}
		

	public static void playGame() {
		Scanner in = new Scanner(System.in);
		boolean gameOver = false;
		boolean bombTripped = false;
		
		firstMove(in);	
		
		while (!gameOver) {
			System.out.print("Row: ");
			int row = in.nextInt();
			System.out.print("Column: ");
			int col = in.nextInt();
			
			board[row][col].toggleRevealed();
			bombTripped = (board[row][col].getVal().equals("*"));  // check if selected cell was a bomb
			board[row][col].toggleRevealed();
			
			updateBoard(row, col);
			
			if (bombTripped) {		// Game is lost, if bomb cell is selected
				gameOver = true;
				printLoseBoard();
				System.out.println("YOU LOSE!");
			} else if (gameWon()) {    // Game is won, if all cells except bomb cells are revealed
				gameOver = true;
				printWinBoard();
				System.out.println("CONGRATULATIONS! YOU WIN!");
			} else {		// else, Game continues
				printBoard();
			}
		}
	}
	
	
	public static void main(String[] args) {
		createBoard();
		writeBoard();
		printBoard();
		playGame();
	}
}
