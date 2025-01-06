package model;

import java.util.ArrayList;

import controller.Game;
import controller.Turn;
import view.PlayerColor;

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

	public void setName(String name) {
		this.name = name;
	}

	public int firstRoll() {
		DiceFactory DiceFactory = new DiceFactory();
		DiceInterface regularDice = DiceFactory.createDice("Dice");
		ObservableDice observableDice = new ObservableDice(regularDice);
		DiceStatisticsObserver stats = new DiceStatisticsObserver();
		observableDice.addObserver(stats);
		//Dice regularDice = new Dice();
		return observableDice.roll();
	}

	public int[] RollTurn() {
		int dice[] = new int[2];
		DiceInterface regularDice = DiceFactory.createDice("Dice");
		ObservableDice observableDice = new ObservableDice(regularDice);
		DiceStatisticsObserver stats = new DiceStatisticsObserver();
		observableDice.addObserver(stats);
		//Dice regularDice = new Dice();
		dice[0] = observableDice.roll();
		dice[1] = observableDice.roll();
		// SelectMove(dice1,dice2);
		return dice;
	}

	public int RollQuestionTurn() {
		DiceInterface QuestionDice = DiceFactory.createDice("QuestionDice");
		ObservableDice observableDice = new ObservableDice(QuestionDice);
		DiceStatisticsObserver stats = new DiceStatisticsObserver();

		observableDice.addObserver(stats);
		//QuestionDice QuestionDice = new QuestionDice();
		return observableDice.roll();
	}

	public int[] RollEnhancedDiceTurn() {

		int dice[] = new int[2];
		
		DiceInterface EnhancedDice = DiceFactory.createDice("EnhancedDice");
		ObservableDice observableDice = new ObservableDice(EnhancedDice);
		DiceStatisticsObserver stats = new DiceStatisticsObserver();

		observableDice.addObserver(stats);
		//EnhancedDice EnhancedDice = new EnhancedDice();
		dice[0] = observableDice.roll();
		dice[1] = observableDice.roll();
		return dice;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int x) {
		score = x;
	}

	public void selectMove(ArrayList<Turn> turns) {
	}

}
