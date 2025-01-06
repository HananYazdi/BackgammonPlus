package model;
import java.util.Random;

public class EnhancedDice extends Dice {
    @Override
    protected int getMinValue() {
        return -3; // מינימום בקוביה משופרת
    }

    @Override
    protected int getMaxValue() {
        return 6; // מקסימום בקוביה משופרת
    }

    @Override
    protected boolean isValid(int number) {
        return number != 0; // מספר חוקי לא יכול להיות 0
    }
}