import java.util.ArrayList;

public class Player {
	private PlayerColor playerColor;
	private String name;
	private int score;
	private Game game;

	public Player(PlayerColor c, String name, Game game) {
		playerColor = c;
		this.name = name;
		score = 0;
		this.game = game;
	}

	public PlayerColor getColor() {
		return playerColor;
	}

	public String getName() {
		return name;
	}

	public int firstRoll() {
		Dice regularDice = new Dice();
		return regularDice.roll();
	}

	public int[] RollTurn() {
		int dice[] = new int[2];
		Dice regularDice = new Dice();
		dice[0] = regularDice.roll();
		dice[1] = regularDice.roll();
		// SelectMove(dice1,dice2);
		return dice;
	}

	public int RollQuestionTurn() {
		QuestionDice QuestionDice = new QuestionDice();
		return QuestionDice.rollQuestionDice();
	}

	public int[] RollEnhancedDiceTurn() {

		int dice[] = new int[2];
		EnhancedDice EnhancedDice = new EnhancedDice();
		dice[0] = EnhancedDice.rollEnhancedDice();
		dice[1] = EnhancedDice.rollEnhancedDice();
		return dice;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int x) {
		score = score + x;
	}

	public void selectMove(ArrayList<Turn> turns) {
	}

}
