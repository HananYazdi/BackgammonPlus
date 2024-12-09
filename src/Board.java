import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Board extends JPanel {
	private final Game game;
	private final Triangle[] points;
	private final Bar bar;
	private final Bar bearOff;
	private Position selectedPosition;

	public Board(Game game) {
		super(null, true);
		this.game = game;
		points = new Triangle[24];
		bar = new Bar(this, 1);
		bearOff = new Bar(this, 0);
		add(bar);
		add(bearOff);

		setSize(getGeometry().getBoardWidth(), getGeometry().getBoardHeight());
		setMaximumSize(new Dimension(getGeometry().getBoardWidth(), getGeometry().getBoardHeight()));
		setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));
		setBackground(Palette.getBoardBackgroundColour());

		for (int i = 0; i < points.length; i++) {
			points[i] = new Triangle(0, null, i + 1, this);
		}
	}

	public static BoardGeometry getGeometry() {
		return BoardGeometry.getBoardGeometry();
	}

	public Game getGame() {
		return game;
	}

	public Bar getBar() {
		return bar;
	}

	public Bar getBearOff() {
		return bearOff;
	}

	public Triangle getPoint(int num) {
		return points[num - 1];
	}

	public void setSelectedPosition(Position p) {
		selectedPosition = p;
	}

	public Position getSelectedPosition() {
		return selectedPosition;
	}

	public void setInitialBoard() {
		for (int i = 0; i < points.length; i++) {
			switch (i) {
			case 0:
				points[i].setCountAndColor(5, PlayerColor.BLACK);
				break;
			case 4:
				points[i].setCountAndColor(3, PlayerColor.WHITE);
				break;
			case 12:
				points[i].setCountAndColor(2, PlayerColor.WHITE);
				break;
			case 6:
				points[i].setCountAndColor(5, PlayerColor.WHITE);
				break;
			case 11:
				points[i].setCountAndColor(2, PlayerColor.BLACK);
				break;
			case 17:
				points[i].setCountAndColor(5, PlayerColor.BLACK);
				break;
			case 19:
				points[i].setCountAndColor(3, PlayerColor.BLACK);
				break;
			case 23:
				points[i].setCountAndColor(5, PlayerColor.WHITE);
				break;
			default:
				points[i].setCountAndColor(0, null);
			}
		}
	}

	public boolean checkHome(PlayerColor c) { // from liams score increment code

		if (c == PlayerColor.BLACK) {
			for (int x = 0; x <= 11; x++) {

				if (points[x].getCount() > 0 && points[x].getPieceColor() == PlayerColor.BLACK) {

					return false;
				}
			}
			for (int x = 18; x <= 23; x++) {

				if (points[x].getCount() > 0 && points[x].getPieceColor() == PlayerColor.BLACK) {

					return false;
				}
			}

		}

		if (c == PlayerColor.WHITE) {
			for (int x = 0; x <= 5; x++) {

				if (points[x].getCount() > 0 && points[x].getPieceColor() == PlayerColor.WHITE) {

					return false;
				}
			}
			for (int x = 12; x <= 23; x++) {

				if (points[x].getCount() > 0 && points[x].getPieceColor() == PlayerColor.WHITE) {

					return false;
				}
			}
		}
		return true;
	}

	public int countPieces(PlayerColor c) {
		int count = bar.getCount(c);

		for (Triangle point : points) {
			if (point.getPieceColor() == c)
				count += point.getCount();
		}

		return count;
	}

	public int countPiecesOut(PlayerColor c) {
		int count = bearOff.getCount(c);
		return count;
	}

	public boolean countPiecesAtHome(PlayerColor c) {
		if (c == PlayerColor.WHITE) {
			for (int x = 0; x <= 23; x++) {

				if (points[x].getCount() != 0 && points[x].getPieceColor() == PlayerColor.WHITE) {

					return false;
				}
			}
		}
		if (c == PlayerColor.BLACK) {
			for (int x = 0; x <= 23; x++) {

				if (points[x].getCount() != 0 && points[x].getPieceColor() == PlayerColor.BLACK) {

					return false;
				}
			}
		}
		return true;
	}

	public void makeMove(Position from, Position to) {
		ArrayList<Turn> turns = game.getPossibleTurns();

		boolean flag = true;

		for (Turn turn : turns) {
			if (turn.getMoves()[0].getFrom() == from && turn.getMoves()[0].getTo() == to)
				flag = false;
		}
		if (flag)
			return;

		int[] rolls = getGame().getRolls();

		ArrayList<Integer> temp = new ArrayList<>();

		boolean moveFound = false;
		for (int i = 0; i < rolls.length; i++) {

			int rollDifference = Math.abs(from.getPointNumber() - to.getPointNumber());

			if (getGame().getActivePlayer().getColor().equals(PlayerColor.WHITE)) {

				if (rollDifference > 6) {

					rollDifference = to.getPointNumber() - from.getPointNumber() + 24;

				}
				if (from.getPointNumber() == 25 || from.getPointNumber() == 0) {
					rollDifference = to.getPointNumber() - 12;

				}
				if (to.getPointNumber() == 25 || to.getPointNumber() == 0) {
					// rollDifference = rolls[i];
					if (from.getPointNumber() == 12) {
						if (points[6].getCount() == 0 && points[7].getCount() == 0 && points[8].getCount() == 0
								&& points[9].getCount() == 0 && points[10].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 1) {
									rollDifference = 1;
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 1;
						}

					}
					if (from.getPointNumber() == 11) {
						if (points[6].getCount() == 0 && points[7].getCount() == 0 && points[8].getCount() == 0
								&& points[9].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 2) {
									rollDifference = 2;
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 2;
						}
					}
					if (from.getPointNumber() == 10) {
						if (points[6].getCount() == 0 && points[7].getCount() == 0 && points[8].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 3) {
									rollDifference = 3;
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 3;
						}
					}
					if (from.getPointNumber() == 9) {
						if (points[6].getCount() == 0 && points[7].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 4) {
									rollDifference = 4;
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 4;
						}
					}
					if (from.getPointNumber() == 8) {
						if (points[6].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 5) {
									rollDifference = 5;
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 5;
						}
					}
					if (from.getPointNumber() == 7) {
						rollDifference = 6;
					}
				}

			} else {
				if (rollDifference > 6) {
					rollDifference = 24 - (to.getPointNumber() - from.getPointNumber());

				}
				if (from.getPointNumber() == 0 || from.getPointNumber() == 25) {
					rollDifference = 13 - to.getPointNumber();

				}
				if (to.getPointNumber() == 25 || to.getPointNumber() == 0) {
					// rollDifference = rolls[i];

					if (from.getPointNumber() == 13) {
						if (points[17].getCount() == 0 && points[16].getCount() == 0 && points[15].getCount() == 0
								&& points[14].getCount() == 0 && points[13].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 1) {
									rollDifference = 1;
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 1;
						}
					}
					if (from.getPointNumber() == 14) {
						if (points[17].getCount() == 0 && points[16].getCount() == 0 && points[15].getCount() == 0
								&& points[14].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 2) {
									rollDifference = 2;
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 2;
						}
					}
					if (from.getPointNumber() == 15) {
						if (points[17].getCount() == 0 && points[16].getCount() == 0 && points[15].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 3) {
									rollDifference = 3;
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 3;
						}
					}
					if (from.getPointNumber() == 16) {
						if (points[17].getCount() == 0 && points[16].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 4) {
									rollDifference = 4;
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 4;
						}
					}
					if (from.getPointNumber() == 17) {
						if (points[17].getCount() == 0) {
							if (i + 1 < rolls.length) {
								if (rolls[i + 1] == 5) {
									rollDifference = 5;
								}
							} else {
								rollDifference = rolls[i];
							}
							// rollDifference = rolls[i];
						} else {
							rollDifference = 5;
						}
					}
					if (from.getPointNumber() == 18) {
						rollDifference = 6;
					}
				}

			}

			if (rolls[i] == rollDifference && !moveFound) {
				moveFound = true; // אחרי שמצאנו את הצעד הראשון, נשאיר את שאר הצעדים
			} else {
				temp.add(rolls[i]);
			}
		}

		// המרת temp חזרה למערך אם יש צורך
		int[] updatedRolls = new int[temp.size()];
		for (int i = 0; i < temp.size(); i++) {
			updatedRolls[i] = temp.get(i);
		}

		System.out.println("Updated rolls: " + Arrays.toString(updatedRolls));
		// System.out.println("Updated board: " + game.getBoard().toString());

		// עדכון הגלגלים החדשים
		game.setRolls(updatedRolls);

		// עדכון הלוח (הזזת החלקים)
		from.removePiece(getGame().getActivePlayer().getColor());
		to.addPiece(getGame().getActivePlayer().getColor());

		// עדכון הצעדים האפשריים

		game.setPossibleTurns(Move.reducePossibleTurns(this, turns, from, to));

		// הצגת הלוח מחדש
		repaint();
	}

	// **************************************************
	// GRAPHICS
	// **************************************************

	public void highlightMoves(Color color) {
		ArrayList<Turn> turns = getGame().getPossibleTurns();
//		System.out.println("highlightMoves" + turns.toString());
		for (Turn turn : turns) {
//			System.out.println("turn" + turn.toString());
//			System.out.println("selectedPosition" + selectedPosition.getPointNumber());
//			System.out.println("getFrom()" + turn.getMoves()[0].getFrom().getPointNumber());
			if (turn.getMoves()[0].getFrom().getPointNumber() == selectedPosition.getPointNumber())

				turn.getMoves()[0].getTo().addHighlight(color);
		}
	}

	public void clearHighlights() {
		for (Triangle t : points)
			t.removeHighlight();
		bar.removeHighlight();
		bearOff.removeHighlight();
		repaint();
	}

	@Override
	protected void paintComponent(final Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
	}
}