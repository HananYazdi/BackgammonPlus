import static org.junit.Assert.*;

import org.junit.Test;

import controller.Game;
import model.Board;
import model.Level;

public class Tests {

	@Test
	public void test() {//check has 30  board instruments
		Game game=new Game(Level.EASY);
		game.start();
		Board b=game.getBoard();	//	.setInitialBoard();
		//b.setInitialBoard();
		int sumPieces = 0;
		        //sumPieces=+Board.countPieces(PlayerColor.BLACK);
		for (int i = 0; i < b.getPoints().length; i++) {
		     sumPieces +=  b.getPoints()[i].getCount();
		 }
		 assertEquals(30, sumPieces);  // Check if the sum equals 30
	}

}
