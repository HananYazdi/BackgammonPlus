package model;
import java.util.Random;

public class QuestionDice extends Dice {
    @Override
    protected int getMinValue() {
        return 1; // מינימום בקוביה המשופרת
    }

    @Override
    protected int getMaxValue() {
        return 3; // מקסימום בקוביה המשופרת
    }
}
