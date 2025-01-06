package model;
import java.util.Random;

public class Dice extends DiceAbstract {
    @Override
    protected int getMinValue() {
        return 1; // מינימום בקוביה רגילה
    }

    @Override
    protected int getMaxValue() {
        return 6; // מקסימום בקוביה רגילה
    }

}
