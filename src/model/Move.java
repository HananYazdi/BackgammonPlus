package model;

import java.util.ArrayList;
import java.util.Arrays;

import controller.Turn;
import view.PlayerColor;

public class Move {
	private Position from;
	private Position to;
	private static int[] board;
	private static Board b;
	private static PlayerColor color;

	public Move(Position from, Position to) {
		this.from = from;
		this.to = to;
	}

	public Position getFrom() {
		return from;
	}

	public Position getTo() {
		return to;
	}

	@Override
	public String toString() {
		return from.getPointNumber() + "-" + to.getPointNumber();
	}

	// **************************************************
	// GET ALL POSSIBLE TURNS
	// **************************************************

	private static void parseBoard(Board b, PlayerColor color) {
		Move.b = b;
		Move.color = color;
		int[] temp = new int[24];
		Move.board = new int[25];

		for (int i = 0; i < 24; i++) {
			Triangle point = b.getPoint(i + 1);
			temp[i] = point.getCount();
			if (color.equals(point.getPieceColor()))
				temp[i] *= -1;
		}

		// Reverse board for WHITE player to maintain consistent direction
		/*
		 * if (color.equals(PlayerColor.WHITE)) temp = reverse(temp);
		 */

		System.arraycopy(temp, 0, Move.board, 0, temp.length);
		Move.board[Move.board.length - 1] = b.getBar().getCount(color);
		System.out.println("board type   " + b.getClass().getSimpleName());
	}

	public static ArrayList<Turn> reducePossibleTurns(Board b, ArrayList<Turn> turns, Position from, Position to) {
		ArrayList<Turn> newTurns = new ArrayList<>();
		Move[] oldMoves;
		Move[] newMoves;

		for (Turn turn : turns) {
			if (turn.getMoves()[0].getFrom() == from && turn.getMoves()[0].getTo() == to) {
				oldMoves = turn.getMoves();
				newMoves = new Move[oldMoves.length - 1];
				System.arraycopy(oldMoves, 1, newMoves, 0, newMoves.length);
				newTurns.add(new Turn(newMoves));
			}
		}

		for (int i = 0; i < newTurns.size(); i++) {
			if (newTurns.get(i).getMoves().length == 0) {
				newTurns.remove(i);
				i--;
			}
		}

		parseBoard(b, color);
		System.out.print(Arrays.toString(board) + " : ");
		System.out.println(newTurns);

		return newTurns;
	}

	public static ArrayList<Turn> getPossibleTurns(Board b, PlayerColor c, int r1, int r2) {
		parseBoard(b, c);
		color = c;
		ArrayList<Turn> turns = new ArrayList<>();

		if (r1 == r2) {
			doubleRolls(turns, r1);

		} else {
			twoRolls(turns, r1, r2);
		}

		System.out.println(Arrays.toString(board) + " : " + turns);
		return turns;
	}

	private static void twoRolls(ArrayList<Turn> fullList, int r1, int r2) {
		ArrayList<Turn> turns = new ArrayList<>();
		ArrayList<Move> moves1;
		ArrayList<Move> moves2;
		if (r1 > r2) {
			int temp = r1; // שמור את הערך של r1 באופן זמני
			r1 = r2; // העתק את הערך הקטן ל-r1
			r2 = temp; // העתק את הערך הגדול ל-r2
		}
		if (b.countPiecesOut(color) == 14) {
			int[] Board1 = board.clone();
			int[] Board2 = board.clone();
			moves1 = getNextMove(Board1, r1);
			for (Move move1 : moves1) {
				System.out.println("move1 special" + move1.toString());
				addTurn(turns, createTurn(move1.getFrom(), move1.getTo()));
			}
			moves2 = getNextMove(Board2, r2);
			for (Move move2 : moves2) {
				System.out.println("move2 special" + move2.toString());
				addTurn(turns, createTurn(move2.getFrom(), move2.getTo()));
			}
			for (Turn turn : turns)
				addTurn(fullList, turn);
			// return;
		}

		// First scenario: r2 then r1
		int[] tempBoard = board.clone();
		moves1 = getNextMove(tempBoard, r1);

		for (Move move1 : moves1) {
			// Create a new board for each first move to ensure clean state
			System.out.println(" board before ");
			for (int i = 0; i < tempBoard.length; i++) {
				System.out.print(tempBoard[i]);
			}
			System.out.println(" ");
			int[] board1 = makeMove(tempBoard.clone(), move1);
			System.out.println("move1 " + move1.toString());
			for (int i = 0; i < board1.length; i++) {
				System.out.print(board1[i]);
			}
			System.out.println(" ");

			moves2 = getNextMove(board1, r2);
			for (Move move2 : moves2) {
				// Create a new board for each second move
				System.out.println("move2 " + move2.toString());
				int[] board2 = makeMove(board1.clone(), move2);

				addTurn(turns, createTurn(move1.getFrom(), move1.getTo(), move2.getFrom(), move2.getTo()));
			}
		}

		// Second scenario: r1 then r2
		int[] tempBoard2 = board.clone();
		moves1 = getNextMove(tempBoard2, r2);

		for (Move move1 : moves1) {
			// Create a new board for each first move to ensure clean state
			System.out.println(" board before ");
			for (int i = 0; i < tempBoard2.length; i++) {
				System.out.print(tempBoard2[i]);
			}
			System.out.println(" ");
			int[] board1 = makeMove(tempBoard2.clone(), move1);
			System.out.println("move1 " + move1.toString());
			for (int i = 0; i < board1.length; i++) {
				System.out.print(board1[i]);
			}
			System.out.println(" ");

			moves2 = getNextMove(board1, r1);
			for (Move move2 : moves2) {
				System.out.println("move2 " + move2.toString());
				// Create a new board for each second move
				int[] board2 = makeMove(board1.clone(), move2);

				addTurn(turns, createTurn(move1.getFrom(), move1.getTo(), move2.getFrom(), move2.getTo()));
			}
		}

		// Add all generated turns to the full list
		for (Turn turn : turns)
			addTurn(fullList, turn);
	}

	private static void doubleRolls(ArrayList<Turn> fullList, int r) {
		ArrayList<Turn> turns = new ArrayList<>();
		ArrayList<Move> moves1;
		ArrayList<Move> moves2;
		ArrayList<Move> moves3;
		ArrayList<Move> moves4;

		if (b.countPiecesOut(color) > 11) {
			twoRolls(fullList, r, r);
			twoRolls(fullList, r, r);
			return;
		}

		moves1 = getNextMove(board.clone(), r);
		for (Move move1 : moves1) {
			// Create a fresh board after first move
			int[] board1 = makeMove(board.clone(), move1);

			moves2 = getNextMove(board1.clone(), r);
			for (Move move2 : moves2) {
				// Create a fresh board after second move
				int[] board2 = makeMove(board1.clone(), move2);

				moves3 = getNextMove(board2.clone(), r);
				for (Move move3 : moves3) {
					// Create a fresh board after third move
					int[] board3 = makeMove(board2.clone(), move3);

					moves4 = getNextMove(board3.clone(), r);
					for (Move move4 : moves4) {
						// Create a fresh board after fourth move
						int[] board4 = makeMove(board3.clone(), move4);

						// Create turn with all four moves
						addTurn(turns, createTurn(move1.getFrom(), move1.getTo(), move2.getFrom(), move2.getTo(),
								move3.getFrom(), move3.getTo(), move4.getFrom(), move4.getTo()));
					}
				}
			}
		}

		// Add all generated turns to the full list
		for (Turn turn : turns) {
			addTurn(fullList, turn);
		}
	}

	private static boolean isLegalMove(int point, int roll) {
		if (color.equals(PlayerColor.BLACK)) {
			int destPoint = point - roll;
//			if (destPoint == 24) {
//				return false;
//			}
			if (destPoint >= 24) {
				destPoint = destPoint - 24;
			}
			if (destPoint < 0) {
				// Check if bearing off is legal
				destPoint += 24;
			}
			if (destPoint < 12 && (point <= 17 && point >= 12)) {
				if (b.checkHome(color)) {
					System.out.println("כולם בבית ");

					return true;
				} else {
					return false;
				}

			}

			System.out.println("point " + point);
			System.out.println("roll " + roll);
			System.out.println("board[destPoint] " + board[destPoint]);
			// Cannot move to point with more than one opponent piece
			if (board[destPoint] > 1) {
				return false;
			}

		} else { // WHITE
			int destPoint = point + roll;
//			if (destPoint == 24) {
//				return false;
//			}
			if (destPoint < 0) {
				destPoint = destPoint + 24;
			}

			if (destPoint > 23) {
				// Check if bearing off is legal
				destPoint -= 24;
			}

			// Cannot move to point with more than one opponent piece

			if (destPoint > 11 && destPoint < 18 && (point <= 11 && point >= 6)) {
				if (b.checkHome(color)) {
					System.out.println("כולם בבית ");
					return true;
				} else {
					return false;
				}

			}
			System.out.println("point " + point);
			System.out.println("roll " + roll);
			System.out.println("board[destPoint] " + board[destPoint]);
			System.out.println("[destPoint] " + destPoint);
			if (board[destPoint] > 1)
				return false;
		}
		return true;
	}

	private static ArrayList<Move> getBearOffMoves(int[] board, int roll, PlayerColor color) {
		ArrayList<Move> moves = new ArrayList<>();

		// וודא שכל האבנים נמצאות בבית
		if (!checkHome(board)) {
			return moves;
		}

		int startIndex = color == PlayerColor.BLACK ? 12 : 6;
		int endIndex = color == PlayerColor.BLACK ? 17 : 11;

		// בדוק אם ניתן להוציא אבן בדיוק עם המספר שנגלל
		for (int i = startIndex; i <= endIndex; i++) {
			if (board[i] < 0) {
				int distance = color == PlayerColor.BLACK ? Math.abs(11 - i) : Math.abs(i - 12);
				if (distance == roll) {
					moves.add(new Move(intToPosition(i), b.getBearOff()));
					return moves;
				}
			}
		}

		// אם לא ניתן להוציא, נסה להזיז אבן בתוך הבית
		for (int i = startIndex; i <= endIndex; i++) {
			if (board[i] < 0 && isLegalMove(i, roll)) {
				int destPoint = color == PlayerColor.BLACK ? Math.abs(i - roll) : Math.abs(i + roll);

				// וודא שההזחה נשארת בתחום הבית
				if ((color == PlayerColor.BLACK && destPoint >= 12)
						|| (color == PlayerColor.WHITE && destPoint <= 11)) {
					moves.add(new Move(intToPosition(i), intToPosition(destPoint)));
					// return moves;
				} else {
					if ((color == PlayerColor.BLACK && destPoint < 12)
							|| (color == PlayerColor.WHITE && destPoint > 11)) {
						moves.add(new Move(intToPosition(i), b.getBearOff()));
						// return moves;
					}
				}
			}
		}

		return moves;
	}

	private static boolean checkHome(int[] board) {
		if (color == PlayerColor.BLACK) {
			for (int x = 0; x <= 11; x++) {

				if (board[x] < 0) {

					return false;
				}
			}
			for (int x = 18; x <= 23; x++) {

				if (board[x] < 0) {

					return false;
				}
			}

		}

		if (color == PlayerColor.WHITE) {
			for (int x = 0; x <= 5; x++) {

				if (board[x] < 0) {

					return false;
				}
			}
			for (int x = 12; x <= 23; x++) {

				if (board[x] < 0) {

					return false;
				}
			}
		}
		return true;
	}

	private static ArrayList<Move> getNextMove(int[] board, int roll) {
		ArrayList<Move> moves = new ArrayList<>();

		// If pieces on bar, must move them first
		if (board[24] != 0 && !checkHome(board)) {
			int startPos = color == PlayerColor.BLACK ? (roll > 0 ? 12 - roll : 11 + Math.abs(roll))
					: (roll > 0 ? 11 + roll : 12 - Math.abs(roll));

			if (board[startPos] <= 1) {
				moves.add(new Move(intToPosition(24), intToPosition(startPos)));
			}
			return moves;
		}

		if (checkHome(board)) {
			moves = getBearOffMoves(board, roll, color);
			if (!moves.isEmpty()) {
				return moves;
			}
		}

		if (color.equals(PlayerColor.BLACK)) {

			// Start from furthest point and work towards home
			for (int i = 11; i >= 0; i--) {
				if (board[i] < 0 && isLegalMove(i, roll)) {
					int destPoint = i - roll;
					if (destPoint < 0) {
						destPoint += 24;
					}

					moves.add(new Move(intToPosition(i), intToPosition(destPoint)));
				}
			}
			for (int i = 23; i >= 12; i--) {
				if (board[i] < 0 && isLegalMove(i, roll)) {
					int destPoint = i - roll;
					if (destPoint > 23) {
						// Check if bearing off is legal
						destPoint -= 24;
						moves.add(new Move(intToPosition(i), intToPosition(destPoint)));
					}
					if (destPoint >= 12) {
						moves.add(new Move(intToPosition(i), intToPosition(destPoint)));
					}
				}
			}

		} else {

			// Start from furthest point and work towards home
			for (int i = 12; i <= 23; i++) {

				if (board[i] < 0 && isLegalMove(i, roll)) {
					int destPoint = i + roll;
					if (destPoint > 23) {
						// Check if bearing off is legal
						destPoint -= 24;
					}
					moves.add(new Move(intToPosition(i), intToPosition(destPoint)));

				}
			}
			for (int i = 0; i <= 11; i++) {
				if (board[i] < 0 && isLegalMove(i, roll)) {
					int destPoint = i + roll;
					if (destPoint < 0) {
						destPoint = destPoint + 24;
					}
					moves.add(new Move(intToPosition(i), intToPosition(destPoint)));
//					if (destPoint <= 11) {
//						moves.add(new Move(intToPosition(i), intToPosition(destPoint)));
//					}

				}
			}
		}

		return moves;
	}

	/**
	 * private static ArrayList<Move> getNextMove(int[] board, int roll) {
	 * ArrayList<Move> moves = new ArrayList<>();
	 * 
	 * // If pieces on bar, must move them first if (board[24] != 0 &&
	 * !b.checkHome(color)) { int startPos = color == PlayerColor.BLACK ? 12 - roll
	 * : 11 + roll; if (board[startPos] <= 1) moves.add(new Move(intToPosition(24),
	 * intToPosition(startPos))); return moves; }
	 * 
	 * 
	 * if (color.equals(PlayerColor.BLACK)) {
	 * 
	 * if (b.checkHome(color)) { if (board[roll + 11] < 0) { moves.add(new
	 * Move(intToPosition(roll + 11), b.getBearOff())); return moves; } } // Start
	 * from furthest point and work towards home for (int i = 11; i >= 0; i--) { if
	 * (board[i] < 0 && isLegalMove(i, roll)) { int destPoint = i - roll; if
	 * (destPoint < 0) { destPoint += 24; }
	 * 
	 * moves.add(new Move(intToPosition(i), intToPosition(destPoint))); } } for (int
	 * i = 23; i >= 12; i--) { if (board[i] < 0 && isLegalMove(i, roll)) { int
	 * destPoint = i - roll; if (b.checkHome(color)) {
	 * 
	 * if (destPoint < 12) { moves.add(new Move(intToPosition(i), b.getBearOff()));
	 * 
	 * } else { moves.add(new Move(intToPosition(i), intToPosition(destPoint))); }
	 * 
	 * } else { moves.add(new Move(intToPosition(i), intToPosition(destPoint))); }
	 * 
	 * } }
	 * 
	 * } else {
	 * 
	 * if (b.checkHome(color)) { if (board[12 - roll] < 0) { moves.add(new
	 * Move(intToPosition(12 - roll), b.getBearOff())); return moves; } } // Start
	 * from furthest point and work towards home for (int i = 12; i <= 23; i++) {
	 * 
	 * if (board[i] < 0 && isLegalMove(i, roll)) { int destPoint = i + roll; if
	 * (destPoint > 23) { // Check if bearing off is legal destPoint -= 24; }
	 * moves.add(new Move(intToPosition(i), intToPosition(destPoint)));
	 * 
	 * } } for (int i = 0; i <= 11; i++) { if (board[i] < 0 && isLegalMove(i, roll))
	 * { int destPoint = i + roll; if (b.checkHome(color)) {
	 * 
	 * if (destPoint > 11) { moves.add(new Move(intToPosition(i), b.getBearOff()));
	 * } else { moves.add(new Move(intToPosition(i), intToPosition(destPoint)));
	 * 
	 * }
	 * 
	 * } else { moves.add(new Move(intToPosition(i), intToPosition(destPoint)));
	 * 
	 * }
	 * 
	 * } } }
	 * 
	 * return moves; }
	 **/

	private static Turn createTurn(Position from1, Position to1, Position from2, Position to2) {
		return new Turn(new Move[] { new Move(from1, to1), new Move(from2, to2) });
	}

	private static Turn createTurn(Position from1, Position to1) {
		return new Turn(new Move[] { new Move(from1, to1) });
	}

	private static Turn createTurn(Position from1, Position to1, Position from2, Position to2, Position from3,
			Position to3, Position from4, Position to4) {
		return new Turn(
				new Move[] { new Move(from1, to1), new Move(from2, to2), new Move(from3, to3), new Move(from4, to4) });
	}

	private static Position intToPosition(int pos) {
		if (pos == 24)
			return b.getBar();
		if (pos == -1)
			return b.getBearOff();

		return b.getPoint(pos + 1);
	}

	private static int PositionToInt(Position pos) {
		if (pos.equals(b.getBar()))
			return 24;
		if (pos.equals(b.getBearOff()))
			return -1;

		if (color == PlayerColor.BLACK)
			return 24 - pos.getPointNumber();
		return pos.getPointNumber() - 1;
	}

	private static int[] makeMove(int[] board, int from, int to) {
		int[] newBoard = board;
//		System.out.println("board: " + Arrays.toString(newBoard));
//		if (from >= 0) {
//			newBoard[from]--;
//		}
//		if (to >= 0) {
//			if (newBoard[to] == -1)
//				newBoard[to]++;
//			newBoard[to]++;
//		}

		if (from == 0 || from == 25) {
			from = 24;
			newBoard[from] = newBoard[from] - 1;
		} else {
			from = from - 1;
			newBoard[from] = newBoard[from] + 1;
		}

		if (to == 0) {
			to = 0;
			// newBoard[to] = newBoard[to] - 1;
		} else {
			to = to - 1;
			newBoard[to] = newBoard[to] - 1;
		}
		// from = from - 1;
//		to = to - 1;
//
//		newBoard[to] = newBoard[to] - 1;
		return newBoard;
	}

	private static int[] makeMove(int[] board, Move move) {
		return makeMove(board, parseNumbers(move.toString())[0], parseNumbers(move.toString())[1]);
	}

	public static int[] parseNumbers(String input) {
		// בדיקה אם הקלט ריק או null
		if (input == null || input.isEmpty()) {
			throw new IllegalArgumentException("Input cannot be null or empty");
		}

		// פיצול המחרוזת לפי המקף
		String[] parts = input.split("-");
		if (parts.length != 2) {
			throw new IllegalArgumentException("Input must be in the format 'number1-number2'");
		}

		try {
			// המרה למספרים שלמים
			int num1 = Integer.parseInt(parts[0].trim());
			int num2 = Integer.parseInt(parts[1].trim());
			return new int[] { num1, num2 };
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Both parts of the input must be valid integers", e);
		}
	}

	private static void addTurn(ArrayList<Turn> turns, Turn turn) {
		for (Turn t : turns) {
			if (t.equals(turn))
				return;
		}

		turns.add(turn);
	}

	private static int getHighestPoint() {
		int highest = 0;
		for (int i = 0; i < board.length; i++) {
			if (board[i] > 0)
				highest = i;
		}
		return highest;
	}

	private static int[] reverse(int[] arr) {
		int[] temp = new int[arr.length];

		for (int i = 0; i < arr.length; i++) {
			temp[i] = arr[arr.length - i - 1];
		}
		return temp;
	}
}