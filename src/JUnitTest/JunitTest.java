package JUnitTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import controller.Game;
import model.Board;
import model.Dice;
import model.EnhancedDice;
import model.Level;
import model.QuestionDice;
import view.PlayerColor;

public class JunitTest {

	// בדיקה 1: לוודא שהמשחק מתחיל עם לוח תקין
	@Test
	public void testGameInitialization() {
		Game game = new Game(Level.HARD, "p1", "p2"); // or other levels
		Board board = game.getBoard();
		board.setInitialBoard();
		assertEquals(5, board.getPoints()[0].getCount());
		assertEquals(PlayerColor.BLACK, board.getPoints()[0].getPieceColor());

		assertEquals(3, board.getPoints()[4].getCount());
		assertEquals(PlayerColor.WHITE, board.getPoints()[4].getPieceColor());

		assertEquals(5, board.getPoints()[6].getCount());
		assertEquals(PlayerColor.WHITE, board.getPoints()[6].getPieceColor());

		assertEquals(2, board.getPoints()[11].getCount());
		assertEquals(PlayerColor.BLACK, board.getPoints()[11].getPieceColor());

		assertEquals(2, board.getPoints()[12].getCount());
		assertEquals(PlayerColor.WHITE, board.getPoints()[12].getPieceColor());

		assertEquals(5, board.getPoints()[17].getCount());
		assertEquals(PlayerColor.BLACK, board.getPoints()[17].getPieceColor());

		assertEquals(3, board.getPoints()[19].getCount());
		assertEquals(PlayerColor.BLACK, board.getPoints()[19].getPieceColor());

		assertEquals(5, board.getPoints()[23].getCount());
		assertEquals(PlayerColor.WHITE, board.getPoints()[23].getPieceColor());
	}

	@Test
	public void testNewBoardHasCorrectCheckerCount() {
		Game game = new Game(Level.HARD, "p1", "p2");
		Board board = game.getBoard();
		board.setInitialBoard();
		int totalWhite = 0;
		int totalBlack = 0;
		totalWhite += board.countPieces(PlayerColor.WHITE);
		totalBlack += board.countPieces(PlayerColor.BLACK);
		assertEquals(15, totalWhite);
		assertEquals(15, totalBlack);

	}

	@Test
	public void testDiceRollRange() {
		Dice dice = new Dice();
		// בדיקה שהקוביות מחזירות מספרים בטווח החוקי
		int value1 = dice.roll();
		assertTrue(value1 >= 1 && value1 <= 6);
	}

	@Test
	public void testDiceRollRangeForQuestionDice() {
		QuestionDice questionDice = new QuestionDice();
		// בדיקה שהקוביות מחזירות מספרים בטווח החוקי
		int value1 = questionDice.roll();
		assertTrue(value1 >= 1 && value1 <= 3);
	}

	@Test
	public void testDiceRollRangeForEnhancedDice() {
		EnhancedDice enhancedDice = new EnhancedDice();
		// בדיקה שהקוביות מחזירות מספרים בטווח החוקי
		int value1 = enhancedDice.roll();
		assertTrue(value1 >= -3 && value1 <= 6);
	}

//	@Test
//	public void testPointsHaveValidColorsOrAreEmpty() {
//		// בדיקה גנרית שמוודאת שכל נקודה בלוח במקום
//		Game game = new Game(Level.HARD, "p1", "p2"); // or other levels
//		Board board = game.getBoard();
//		board.setInitialBoard();
//		for (int i = 0; i < board.getPoints().length; i++) {
//			int count = board.getPoints()[i].getCount();
//			PlayerColor color = board.getPoints()[i].getPieceColor();
//			if (count > 0) {
//				assertTrue(color == PlayerColor.BLACK || color == PlayerColor.WHITE);
//			}
//		}
//	}

}
