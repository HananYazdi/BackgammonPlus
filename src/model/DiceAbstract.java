package model;

import java.util.Random;

public abstract class DiceAbstract implements DiceInterface{
	private static final Random rng = new Random();

    // Template Method - מגדיר את שלד תהליך ה-roll
    public final int roll() {
        int min = getMinValue();
        int max = getMaxValue();
        int number;

        do {
            number = generateRandomNumber(min, max);
        } while (!isValid(number)); // מבטיח שהמספר עומד בתנאים הנדרשים

        return number;
    }

    // מתודות מופשטות להגדרת טווח המספרים
    protected abstract int getMinValue();

    protected abstract int getMaxValue();

    // מתודה אופציונלית למחלקות שרוצות להחיל כללים נוספים
    protected boolean isValid(int number) {
        return true; // ברירת מחדל: כל מספר חוקי
    }

    // לוגיקה משותפת לכל הקוביות - יצירת מספר אקראי בטווח
    private int generateRandomNumber(int min, int max) {
        return rng.nextInt(max - min + 1) + min;
    }

}
