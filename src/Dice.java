import java.util.Random;

public class Dice {
	private static final Random rng = new Random();

	public int roll() {
		return rng.nextInt(6) + 1;
	}

}
