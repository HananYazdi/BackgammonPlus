import java.util.Random;

public class EnhancedDice extends Dice {
	private static final Random rng = new Random();

	// Overriding the roll method to provide enhanced dice behavior
	public int roll() {
		return rng.nextInt(10) - 3; // Returns a value between -3 and 6
	}
}