package model;

public interface DiceRollObserver {
    void onDiceRolled(DiceInterface dice, int result);
}