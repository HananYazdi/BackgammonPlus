package model;
import java.util.Random;

class QuestionDice extends Dice {
	private static final Random rng = new Random();

	// מחזיר את רמת השאלה
	public int rollQuestionDice() {
		return rng.nextInt(3) + 1;
	}
}
