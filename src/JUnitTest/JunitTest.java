package JUnitTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import controller.Game;
import model.Board;
import model.Dice;
import model.EnhancedDice;
import model.Level;
import model.QuestionDice;
import view.PlayerColor;

public class JunitTest {
	private Game game;
	private Dice dice;
	private QuestionDice questionDice;
	private EnhancedDice enhancedDice;

	@Before
	public void setUp() {
		game = new Game(Level.HARD); // or other levels
		game.getPlayers();
		game.start();
		dice = new Dice();
		questionDice = new QuestionDice();
		enhancedDice = new EnhancedDice();
	}

	// בדיקה 1: לוודא שהמשחק מתחיל עם לוח תקין
	@Test
	public void testGameInitialization() {

		Board board = game.getBoard();
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
		// בדיקה שהלוח החדש מכיל את המספר הנכון של אבנים
		int totalWhite = 0;
		int totalBlack = 0;
		Board board = game.getBoard();
		totalWhite += board.countPieces(PlayerColor.WHITE);
		totalBlack += board.countPieces(PlayerColor.BLACK);

		assertEquals(15, totalWhite);
		assertEquals(15, totalBlack);
	}

	@Test
	public void testDiceRollRange() {
		// בדיקה שהקוביות מחזירות מספרים בטווח החוקי
		int value1 = dice.roll();
		assertTrue(value1 >= 1 && value1 <= 6);
	}

	@Test
	public void testDiceRollRangeForQuestionDice() {
		// בדיקה שהקוביות מחזירות מספרים בטווח החוקי
		int value1 = questionDice.rollQuestionDice();
		assertTrue(value1 >= 1 && value1 <= 3);
	}

	@Test
	public void testDiceRollRangeForEnhancedDice() {
		// בדיקה שהקוביות מחזירות מספרים בטווח החוקי
		int value1 = enhancedDice.rollEnhancedDice();
		assertTrue(value1 >= -3 && value1 <= 6);
	}

}
