import java.util.Random;

public class EnhancedDice extends Dice {
	private static final Random rng = new Random();

	// Overriding the roll method to provide enhanced dice behavior
	public int rollEnhancedDice() {
		int number;

		do {
			number = rng.nextInt(10) - 3; // טווח המספרים מ-(-3) עד 6
		} while (number == 0);
		return number;
	}
}