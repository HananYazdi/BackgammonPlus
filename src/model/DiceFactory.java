package model;

public class DiceFactory {
    // Static method to create a Dice instance based on type
    public static DiceInterface createDice(String type) {
    	if(type==null) {
    		return null;
    	}
    	if(type.equalsIgnoreCase("Dice")) {
    		return new Dice();
    	}
    	if(type.equalsIgnoreCase("EnhancedDice")) {
    		return new EnhancedDice();
    	}
    	if(type.equalsIgnoreCase("QuestionDice")) {
    		return new QuestionDice();
    	}
    	return null;
    }
}