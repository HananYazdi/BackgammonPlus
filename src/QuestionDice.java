import java.util.Random;

class QuestionDice extends Dice {
	private static final Random rng = new Random();

	// מחזיר את רמת השאלה
	public String rollQuestionDice() {
		int result = rng.nextInt(3); // מחזיר 0, 1, או 2
		switch (result) {
		case 0:
			return "שאלה קלה";
		case 1:
			return "שאלה בינונית";
		case 2:
			return "שאלה קשה";
		default:
			throw new IllegalStateException("Unexpected value: " + result); // מוודא שהקוד בטוח
		}
	}
}