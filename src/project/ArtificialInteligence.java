package project;

import java.util.ArrayList;

public class ArtificialInteligence {
	private static final double NANO_TO_SEC = (long) 0.0000000001;
	private Threes game;
	private Threes[] games;
	private boolean[] successMovement;
	private String saveSeq;
	private final String SAVE_NAME = "output.txt";
	private String saveMsg;
	private long startTotalTime = 0;
	private long endTotalTime = 0;

	private ArrayList<String> sequenceDFS;
	private ArrayList<Double> scoreDFS;
	private int myCount = 0;

	public ArtificialInteligence() {
		this.game = new Threes();
		this.saveSeq = "";
		this.saveMsg = "";
		this.startTotalTime = 0;
		this.endTotalTime = 0;
	}

	public String toString(String name) {
		String print = name + "\n";

		print += this.game.toString();
		print += "Game score:			" + this.game.getScore() + "\n";
		print += "Game has movement:	" + this.game.hasMovement() + "\n";
		print += "Game has inSequence:	" + !this.game.inSequence.isEmpty()
				+ "\n";
		print += "Number of Movements:	" + this.game.numberOfMovements + "\n";
		print += "Number of Mapping:	" + this.game.numberOfMapping + "\n";
		print += "Result Sequence:		" + this.saveSeq + "\n";
		print += "Time Elapsed:			" + ( this.endTotalTime - this.startTotalTime)
				* (NANO_TO_SEC) + "\n";

		return print;
	}

	public void start(String fileName) {
		for (int i = 2; i < 4; i++) {
			loadGame(fileName);
			executeGameMovements(i);
			saveGame(SAVE_NAME, Double.toString(this.game.getScore()));
			finishGame();
		}
//		 loadGame(fileName);
//		 executeGameMovements(3);
//		 saveGame(SAVE_NAME,Double.toString(this.game.getScore()));
//		 finishGame();
	}

	private void finishGame() {
		this.game = new Threes();
		this.saveSeq = "";
		this.saveMsg = "";
		this.startTotalTime = 0;
		this.endTotalTime = 0;
	}

	private void loadGame(String fileName) {
		game.readFile(fileName);
	}

	private void saveGame(String SAVE_NAME, String score) {
		String msg = this.saveMsg + "\n" + score + "Sequence: \n"
				+ this.saveSeq;
		game.saveFile(SAVE_NAME, msg);
	}

	private void executeGameMovements(int approach) {
		switch (approach) {
		case 1:
			this.startTotalTime = System.currentTimeMillis();
			excuteMovementsDepthFirstMod();
			this.endTotalTime = System.currentTimeMillis();			
			System.out.println(this.toString("Greedy"));
			break;
		case 2:
			System.out.println(System.currentTimeMillis());
			long b = System.currentTimeMillis();
			excuteMovementsBF();
			long a = System.currentTimeMillis();
			System.out.println(a-b);
			System.out.println(this.toString("Brute Force"));
			break;
		case 3:
			this.startTotalTime = System.currentTimeMillis();
			executeMovementsPP();
			this.endTotalTime = System.currentTimeMillis();
			System.out.println(this.toString(myCount + "Prince of Persia"));
			break;

		default:
			break;
		}
	}

	private void executeMovementsPP() {
		
		startVariables();

		// Set limits of the interaction
		int limit 	= 5;
			
		while (this.game.hasMovement()){
			if (this.game.inSequence.size() < limit){
	
				
				
				String currentSeq = chooseHighestGame();
				moveGame(currentSeq.substring(1, currentSeq.length()));
				this.saveSeq += currentSeq.substring(1, currentSeq.length());
				break;
			}
			startThree(this.game,limit,"");
			
			String currentSeq = chooseHighestGame();
			this.saveSeq += currentSeq.substring(0, 1);		
			// Move root
			moveGame(currentSeq.substring(0, 1));
			this.game.numberOfMapping ++;
		}

	}
	
	// v.1.1
	// --------------------------------------------------------------------------------------------------------------------------------------------------
	private void excuteMovementsBF() {
		startVariables();

		// Set limits of the interaction
		int limit = 5;
		int size = this.game.inSequence.size();

		for (int i = 0; i < size / limit && this.game.hasMovement(); i++) {

			startThree(this.game, limit, "");

			String aux = chooseHighestGame();
			this.saveSeq = this.saveSeq + aux;

			// Move root
			moveGame(aux);
			this.game.numberOfMapping++;
		}

		if (size % limit != 0) {
			startThree(this.game, size % limit, "");

			String aux = chooseHighestGame();
			this.saveSeq = this.saveSeq + aux;
			// Move root
			moveGame(aux);
			this.game.numberOfMapping++;
		}
		this.saveSeq = this.saveSeq.substring(0, this.game.numberOfMovements);
		saveGame("BF_" + SAVE_NAME, "");
	}

	private void moveGame(String sequence) {
		for (int i = 0; i < sequence.length(); i++)
			switch (sequence.charAt(i)) {
			case 'U':
				this.game.up();
				break;
			case 'R':
				this.game.right();
				break;
			case 'D':
				this.game.down();
				break;
			case 'L':
				this.game.left();
				break;
			default:
				this.game = null;
				break;
			}
	}

	private String chooseHighestGame() {
		Double max = Double.MIN_VALUE;
		String currentSeq = "";

		for (int i = 0; i < scoreDFS.size(); i++)
			if (scoreDFS.get(i) > max) {
				max = scoreDFS.get(i);
				currentSeq = sequenceDFS.get(i);
			}

		return currentSeq;
	}

	private void startVariables() {
		scoreDFS = new ArrayList<Double>();
		sequenceDFS = new ArrayList<String>();
	}

	private void startThree(Threes currentGame, int level, String currentSeq) {
		if (level > 0) {
			currentGame.nextOptions = makeFourMovement(currentGame);
			startThree(currentGame.nextOptions[0], level - 1, currentSeq + "U");
			startThree(currentGame.nextOptions[1], level - 1, currentSeq + "R");
			startThree(currentGame.nextOptions[2], level - 1, currentSeq + "D");
			startThree(currentGame.nextOptions[3], level - 1, currentSeq + "L");
		} else {
			scoreDFS.add(currentGame.getScore());
			sequenceDFS.add(currentSeq);
			myCount++;
		}
	}

	private Threes[] makeFourMovement(Threes node) {
		Threes[] nextMovements = new Threes[4];
		boolean[] successNextMovement = new boolean[4];
		nextMovements = setGames(node);
		// Do for 4 ways
		if (nextMovements[0].up()) {
			successNextMovement[0] = true;
			nextMovements[0].setScore();
		}
		if (nextMovements[1].right()) {
			successNextMovement[1] = true;
			nextMovements[1].setScore();
		}
		if (nextMovements[2].down()) {
			successNextMovement[2] = true;
			nextMovements[2].setScore();
		}
		if (nextMovements[3].left()) {
			successNextMovement[3] = true;
			nextMovements[3].setScore();
		}
		return nextMovements;
	}

	// v.1.0--------------------------------------------------------------------------------------------------------------------------------------------------------

	private void excuteMovementsDepthFirstMod() {
		while (game.hasMovement()) {
			// 0 - Up / 1 - Right / 2 - Down / 3 - Left
			if (game.inSequence.isEmpty())
				break;
			games = new Threes[4];
			successMovement = new boolean[4];
			games = setGames(game);

			String maxMovement = "0";
			int directionMovement = -1;

			makeForMovements();

			for (int i = 0; i < games.length; i++)
				if (successMovement[i]) {
					String currentMovement = makeFourMovements(games[i]);
					if (currentMovement.split(" ")[0].compareTo(maxMovement) > 0) {
						maxMovement = currentMovement.split(" ")[0];
						directionMovement = i;
					}
				}
			makeChoice(directionMovement);
		}

		if (game.hasMovement())
			saveMsg = "Game over: inSequence is empty";
		else
			saveMsg = ("Game over: There is not any more movements");

		game.setScore();
	}

	private void makeForMovements() {
		// Do for 4 ways
		if (games[0].up()) {
			successMovement[0] = true;
			games[0].setScore();
		} else
			games[0] = null;
		if (games[1].right()) {
			successMovement[1] = true;
			games[1].setScore();
		} else
			games[1] = null;
		if (games[2].down()) {
			successMovement[2] = true;
			games[2].setScore();
		} else
			games[2] = null;
		if (games[3].left()) {
			successMovement[3] = true;
			games[3].setScore();
		} else
			games[3] = null;
	}

	private String makeFourMovements(Threes node) {
		Threes[] nextMovements = new Threes[4];
		boolean[] successNextMovement = new boolean[4];
		nextMovements = setGames(node);
		// Do for 4 ways
		if (nextMovements[0].up()) {
			successNextMovement[0] = true;
			nextMovements[0].setScore();
		}
		if (nextMovements[1].right()) {
			successNextMovement[1] = true;
			nextMovements[1].setScore();
		}
		if (nextMovements[2].down()) {
			successNextMovement[2] = true;
			nextMovements[2].setScore();
		}
		if (nextMovements[3].left()) {
			successNextMovement[3] = true;
			nextMovements[3].setScore();
		}

		return maxValueOfNextMovements(nextMovements, successNextMovement);
	}

	private String maxValueOfNextMovements(Threes[] nextMovements,
			boolean[] successNextMovement) {
		double maxScore = Double.MIN_VALUE;
		int movement = -1;
		int count = 0;
		for (int i = 0; i < nextMovements.length; i++) {
			if (successNextMovement[i]
					&& nextMovements[i].getScore() > maxScore) {
				maxScore = nextMovements[i].getScore();
				movement = count;
			}
			count++;
		}
		return "" + maxScore + " " + movement;
	}

	private void makeChoice(int index) {
		if (index == 0) {
			game.up();
			saveSeq = saveSeq + 'U';
		} else if (index == 1) {
			game.right();
			saveSeq = saveSeq + 'R';
		} else if (index == 2) {
			game.down();
			saveSeq = saveSeq + 'D';
		} else {
			game.left();
			saveSeq = saveSeq + 'L';
		}
	}

	private Threes[] setGames(Threes currentGame) {
		Threes[] possibleGames = new Threes[4];
		for (int i = 0; i < possibleGames.length; i++) {
			possibleGames[i] = new Threes();
			possibleGames[i].copyThrees(currentGame);
			possibleGames[i].inSequence.add(0);
			possibleGames[i].inSequence.add(0);
			possibleGames[i].inSequence.add(0);
		}
		return possibleGames;
	}
}
