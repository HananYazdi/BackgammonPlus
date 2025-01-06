// ObservableDice.java
package model;

import java.util.ArrayList;
import java.util.List;

public class ObservableDice implements DiceInterface, Observable {
    private final DiceInterface dice;
    private final List<DiceRollObserver> observers = new ArrayList<>();

    public ObservableDice(DiceInterface dice) {
        this.dice = dice;
    }

    @Override
    public int roll() {
        int result = dice.roll();
        notifyObservers(result);
        return result;
    }

    @Override
    public void addObserver(DiceRollObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(DiceRollObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(int result) {
        for (DiceRollObserver observer : observers) {
            observer.onDiceRolled(dice, result);
        }
    }
}