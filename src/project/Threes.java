package project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Threes {
	public double currentScore;
	public String status;
	public ArrayList<Integer> inSequence;
	public Threes[] nextOptions;
	public int[][] board;
	public int numberOfMovements;
	public int numberOfMapping;

	public Threes() {
		this.board = new int[4][4];
		this.currentScore = 0;
		this.inSequence = new ArrayList<Integer>();
		this.status = "";
		this.nextOptions = new Threes [4];
		this.numberOfMovements = 0;
		this.numberOfMapping = 0;
	}

	public String toString() {
		String currentBoard = "";
		for (int i = 0; i < this.board.length; i++) {
			for (int j = 0; j < this.board.length; j++) {
				currentBoard = currentBoard + this.board[i][j] + "\t";
			}
			currentBoard = currentBoard + "\n";
		}
		return currentBoard;
	}

	public String toStringSequence() {
		return inSequence.toString();
	}

	public void readFile(String fileName) {
		// Open a file
		BufferedReader file;
		try {
			file = new BufferedReader(new FileReader(fileName));
			// Ignore 1-2 lines
			file.readLine();
			file.readLine();

			// Read board
			for (int i = 0; i < 4; i++) {
				String[] currentRow = file.readLine().split(" ");
				for (int j = 0; j < currentRow.length; j++) {
					this.board[i][j] = Integer.parseInt(currentRow[j]);
				}
			}
			// Ignore line 7
			file.readLine();

			// Read until the end of the file
			while (file.ready()) {
				String[] currentRow = file.readLine().split(" ");
				for (int i = 0; i < currentRow.length; i++) {
					if(!currentRow[i].equals(""))
						inSequence.add(Integer.parseInt(currentRow[i]));
				}
			}

			file.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveFile(String fileName, String savedSeq){
		BufferedWriter file;
		try {
			file = new BufferedWriter(new FileWriter(fileName));
			for (String currentLine : savedSeq.split("\n")) {
				file.write(currentLine);
				file.newLine();
			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean up() {
		boolean success = false;
		boolean[] successArray = new boolean[4];
		// Move everyone
		for (int i = 1; i < this.board.length; i++) {
			for (int j = 0; j < this.board.length; j++) {
				if (this.board[i - 1][j] == 0 && this.board[i][j] != 0) {
					this.board[i - 1][j] = this.board[i][j];
					this.board[i][j] = 0;
					success = true;
					successArray[j] = true;
				} else if (this.board[i - 1][j] >= 3 && this.board[i][j] >= 3
						&& this.board[i - 1][j] == this.board[i][j]) {
					this.board[i - 1][j] = this.board[i - 1][j]
							+ this.board[i][j];
					this.board[i][j] = 0;
					success = true;
					successArray[j] = true;
				} else if (this.board[i - 1][j] != 0 && this.board[i][j] != 0
						&& this.board[i - 1][j] + this.board[i][j] == 3) {
					this.board[i - 1][j] = 3;
					this.board[i][j] = 0;
					success = true;
					successArray[j] = true;
				}
			}
		}

		// Insert new tile
		if (success){
			insertTile(successArray, 'u');
			this.numberOfMovements ++;
		}
		return success;

	}

	public boolean down() {
		boolean success = false;
		boolean[] successArray = new boolean[4];
		for (int i = this.board.length - 2; i >= 0; i--) {
			for (int j = 0; j < this.board.length; j++) {
				if (this.board[i + 1][j] == 0 && this.board[i][j] != 0) {
					this.board[i + 1][j] = this.board[i][j];
					this.board[i][j] = 0;
					success = true;
					successArray[j] = true;
				} else if (this.board[i + 1][j] >= 3 && this.board[i][j] >= 3
						&& this.board[i + 1][j] == this.board[i][j]) {
					this.board[i + 1][j] = this.board[i + 1][j]
							+ this.board[i][j];
					this.board[i][j] = 0;
					success = true;
					successArray[j] = true;
				} else if (this.board[i + 1][j] != 0 && this.board[i][j] != 0
						&& this.board[i + 1][j] + this.board[i][j] == 3) {
					this.board[i + 1][j] = 3;
					this.board[i][j] = 0;
					success = true;
					successArray[j] = true;
				}
			}
		}
		if (success){
			insertTile(successArray, 'd');
			this.numberOfMovements ++;
		}

		return success;
	}

	public boolean right() {
		boolean success = false;
		boolean[] successArray = new boolean[4];
		// Move everyone
		for (int j = this.board.length - 2; j >= 0; j--) {
			for (int i = 0; i < this.board.length; i++) {
				if (this.board[i][j + 1] == 0 && this.board[i][j] != 0) {
					this.board[i][j + 1] = this.board[i][j];
					this.board[i][j] = 0;
					success = true;
					successArray[i] = true;
				} else if (this.board[i][j + 1] == this.board[i][j]
						&& this.board[i][j + 1] >= 3 && this.board[i][j] >= 3) {
					this.board[i][j + 1] = this.board[i][j + 1]
							+ this.board[i][j];
					this.board[i][j] = 0;
					success = true;
					successArray[i] = true;
				} else if (this.board[i][j + 1] != 3 && this.board[i][j] != 3
						&& this.board[i][j + 1] + this.board[i][j] == 3) {
					this.board[i][j + 1] = 3;
					this.board[i][j] = 0;
					success = true;
					successArray[i] = true;
				}
			}
		}
		// Insert new tile
		if (success){
			insertTile(successArray, 'r');
			this.numberOfMovements ++;
		}
		return success;
	}

	public boolean left() {
		boolean success = false;
		boolean[] successArray = new boolean[4];
		// Move everyone
		for (int j = 1; j < this.board.length; j++) {
			for (int i = 0; i < this.board.length; i++) {
				if (this.board[i][j - 1] == 0 && this.board[i][j] != 0) {
					this.board[i][j - 1] = this.board[i][j];
					this.board[i][j] = 0;
					successArray[i] = true;
					success = true;
				} else if (this.board[i][j - 1] == this.board[i][j]
						&& this.board[i][j - 1] >= 3 && this.board[i][j] >= 3) {
					this.board[i][j - 1] = this.board[i][j - 1]
							+ this.board[i][j];
					this.board[i][j] = 0;
					success = true;
					successArray[i] = true;
				} else if (this.board[i][j - 1] != 3 && this.board[i][j] != 3
						&& this.board[i][j - 1] + this.board[i][j] == 3) {
					this.board[i][j - 1] = 3;
					this.board[i][j] = 0;
					success = true;
					successArray[i] = true;
				}
			}
		}
		// Insert new tile
		if (success){
			insertTile(successArray, 'l');
			this.numberOfMovements ++;
		}
		return success;
	}

	public boolean hasMovement() {

		// Check for 0s 
		for (int i = 0; i < this.board.length; i++)
			for (int j = 0; j < this.board.length; j++)
					if (this.board[i][j] == 0)
						return true;
		
	
		for (int i = this.board.length-1; i > 0; i--) 
			for (int j = this.board.length-1; j > 0; j--) {
					if (this.board[i][j] == this.board[i - 1][j] && !(this.board[i][j] == 1 && this.board[i-1][j] == 1) && !(this.board[i][j] == 2 && this.board[i-1][j] == 2))
						return true;
					if (this.board[i][j] == this.board[i][j - 1] && !(this.board[i][j] == 1 && this.board[i][j-1] == 1) && !(this.board[i][j] == 2 && this.board[i][j-1] == 2))
						return true;
					if (this.board[i][j] + this.board[i - 1][j] == 3 && (this.board[i][j] != 3 && this.board[i-1][j] != 3))
						return true;
					if (this.board[i][j] + this.board[i][j - 1] == 3 && (this.board[i][j] != 3 && this.board[i][j-1] != 3))
						return true;
			}
		for (int i = 0; i < this.board.length - 1; i++) 
			for (int j = 0; j < this.board.length - 1; j++) {
					if (this.board[i][j] == this.board[i + 1][j] && !(this.board[i][j] == 1 && this.board[i+1][j] == 1) && !(this.board[i][j] == 2 && this.board[i+1][j] == 2))
						return true;
					if (this.board[i][j] == this.board[i][j + 1] && !(this.board[i][j] == 1 && this.board[i][j+1] == 1) && !(this.board[i][j] == 2 && this.board[i][j+1] == 2))
						return true;
					if (this.board[i][j] + this.board[i + 1][j] == 3 && (this.board[i][j] != 3 && this.board[i+1][j] != 3))
						return true;
					if (this.board[i][j] + this.board[i][j + 1] == 3 && (this.board[i][j] != 3 && this.board[i][j+1] != 3))
						return true;
			
		
			}
		return false;
	}
	
	public double getScore(){
		double totalScore = 0;
		for (int i = 0; i < this.board.length; i++) {
			for (int j = 0; j < this.board.length; j++) {
				totalScore += getScore(this.board[i][j]);
			}
		}
		return totalScore;				
	}
	
	public void setScore(){
		this.currentScore = getScore();
	}

	private double getScore(int tile) {
		switch (tile) {
		case 0:
			return 0;
		case 1:
			return 1;
		case 2:			
			return 1;
		default:
			double expo = ((Math.log10(tile/3))/(Math.log10(2)))+1;
			return Math.pow(3, expo);
		}
	}

	private void insertTile(boolean[] successArray, char direction) {
		String[] lexicographyOrder = new String[4];
		String lowestLex = "9999999999";
		int nextIndex = -1;
		switch (direction) {
		case 'u':
			// Check in the successColumn
			for (int i = 0; i < successArray.length; i++) {
				// If there was a successful movement
				if (successArray[i]) {
					lexicographyOrder[i] = toLex(i, 'u');
				}
			}
			// Check max value
			for (int i = 0; i < lexicographyOrder.length; i++) {
				if (successArray[i] && lexicographyOrder[i].length() < lowestLex.length()){
					nextIndex = i;
					lowestLex = lexicographyOrder[i];
				}
				else if (successArray[i]
						&& lowestLex.compareTo(lexicographyOrder[i]) > 0) {
					nextIndex = i;
					lowestLex = lexicographyOrder[i];
				}
			}
			// Insert new tile
			insertNewTile(nextIndex, 'u');
			break;
		case 'd':
			// Check in the successColumn
			for (int i = 0; i < successArray.length; i++) {
				// If there was a successful movement
				if (successArray[i]) {
					lexicographyOrder[i] = toLex(i, 'd');
				}
			}
			// Check max value
			for (int i = 0; i < lexicographyOrder.length; i++) {
				if (successArray[i]
						&& lowestLex.compareTo(lexicographyOrder[i]) >= 0) {
					nextIndex = i;
					lowestLex = lexicographyOrder[i];
				}
			}
			// Insert new tile
			insertNewTile(nextIndex, 'd');
			break;
		case 'r':
			// Check in the successColumn
			for (int i = 0; i < successArray.length; i++) {
				// If there was a successful movement
				if (successArray[i]) {
					lexicographyOrder[i] = toLex(i, 'r');
				}
			}
			// Check max value
			for (int i = 0; i < lexicographyOrder.length; i++) {
				if (successArray[i]
						&& lowestLex.compareTo(lexicographyOrder[i]) >= 0) {
					nextIndex = i;
					lowestLex = lexicographyOrder[i];
				}
			}
			// Insert new tile
			insertNewTile(nextIndex, 'r');
			break;
		case 'l':
			// Check in the successColumn
			for (int i = 0; i < successArray.length; i++) {
				// If there was a successful movement
				if (successArray[i]) {
					lexicographyOrder[i] = toLex(i, 'l');
				}
			}
			// Check max value
			for (int i = 0; i < lexicographyOrder.length; i++) {
				if (successArray[i]
						&& lowestLex.compareTo(lexicographyOrder[i]) > 0) {
					nextIndex = i;
					lowestLex = lexicographyOrder[i];
				}
			}
			// Insert new tile
			insertNewTile(nextIndex, 'l');
			break;
		default:
			break;
		}

	}

	private void insertNewTile(int currentIndex, char direction) {
		switch (direction) {
		case 'u':
			this.board[3][currentIndex] = inSequence.remove(0);
			break;
		case 'd':
			this.board[0][currentIndex] = inSequence.remove(0);
			break;
		case 'l':
			this.board[currentIndex][3] = inSequence.remove(0);
			break;
		case 'r':
			this.board[currentIndex][0] = inSequence.remove(0);
			break;
		default:
			break;
		}
	}

	private String toLex(int fixedIndex, char direction) {
		String lex = "";
		switch (direction) {
		case 'u':
			for (int i = board.length - 1; i >= 0; i--) {
				lex = lex + board[i][fixedIndex];
			}
			return lex;
		case 'd':
			for (int i = 0; i < board.length; i++) {
				lex = lex + board[i][fixedIndex];
			}
			return lex;
		case 'l':
			for (int i = board.length - 1; i >= 0; i--) {
				lex = lex + board[fixedIndex][i];
			}
			return lex;
		case 'r':
			for (int i = 0; i < board.length; i++) {
				lex = lex + board[fixedIndex][i];
			}
			return lex;
		default:
			break;
		}
		return null;
	}

	public void copyThrees(Threes old) {
		this.currentScore = old.currentScore;
		this.inSequence = copySequence(old.inSequence);
		this.board = copyBoard(old.board);
	}

	private int[][] copyBoard(int[][] board) {
		int[][] returnBoard = new int[4][4];
		for (int i = 0; i < returnBoard.length; i++) {
			for (int j = 0; j < returnBoard.length; j++) {
				returnBoard[i][j] = board[i][j];
			}
		}
		return returnBoard;
	}

	private ArrayList<Integer> copySequence(ArrayList<Integer> inSequence) {
		ArrayList<Integer> returnSeq = new ArrayList<Integer>();
		for (Integer integer : inSequence) {
			returnSeq.add(integer);
		}
		return returnSeq;
	}

}
