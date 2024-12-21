import static org.junit.Assert.*;
import org.junit.Test;

import controller.Game;
import model.Board;
import model.Dice;
import model.Level;
import model.Move;
import model.Player;
import model.QuestionDice;
public class Test2 {

	/*@Test
	public void test() {//check move logic
		Game game=new Game(Level.EASY);
		game.start();
		Board b=game.getBoard();	//	.setInitialBoard();
		//b.setInitialBoard();
		game.setRolls(new int[] { 3, 5 });

		game.getBoard().makeMove(b.getSelectedPosition(), null);
		 //assertEquals(30, sumPieces);  // Check if the sum equals 30
	}*/
	@Test
	public void testPlayer()
	{
		Game game=new Game(Level.EASY);
		game.start();
		Player a=game.getActivePlayer();
		System.out.println(a);
		game.switchActivePlayer();
		System.out.println(game.getActivePlayer());
		assertEquals(a, game.getActivePlayer());  // Check if ac
	}
	@Test
	public void testDice()
	{
		Dice d= new Dice();
		int value=d.roll();
		assertTrue("Value " + value + " is not within the range 1 to 6",value>= 1 && value <= 6);	
	}
	@Test
	public void testDiceQuestion()
	{
		QuestionDice d= new QuestionDice();
		int value=d.rollQuestionDice();
		assertTrue("Value " + value + " is not within the range 1 to 3",value>= 1 && value <= 3);	
	}

}
