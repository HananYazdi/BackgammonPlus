package model;

public class DiceStatisticsObserver implements DiceRollObserver {
    private int totalRolls = 0;
    private int sum = 0;

    @Override
    public void onDiceRolled(DiceInterface dice, int result) {
        totalRolls++;
        sum += result;
        System.out.printf("Roll #%d: %d, Average: %.2f%n", 
            totalRolls, result, (double)sum/totalRolls);
    }
}