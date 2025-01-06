package model;

public interface Observable {
    void addObserver(DiceRollObserver observer);
    void removeObserver(DiceRollObserver observer);
}