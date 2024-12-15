package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import controller.TriangleListener;
import view.Palette;
import view.PlayerColor;

public class Triangle extends JComponent implements Position {
	private final Board board;
	private final int pointNumber;
	private final Polygon polygon;
	private PlayerColor pieceColour;
	private int pieceCount;
	private boolean hasQuestionMark; // Flag to determine if the triangle should show a question mark to do the
										// special logo in the triangle
	private boolean hasSurpriseMark; // Flag to determine if the triangle should show a Surprise mark to do the
										// special logo in the triangle

	public Triangle(int pieceCount, PlayerColor pieceColour, int number, final Board board) {
		this.board = board;
		pointNumber = number;
		this.pieceColour = pieceColour;
		this.pieceCount = pieceCount;
		hasQuestionMark = false;
		hasSurpriseMark = false;

		this.board.add(this);
		setBounds(Board.getGeometry().getPointRectangle(pointNumber));
		polygon = getTriangle();

		addMouseListener(new TriangleListener());
	}

	public boolean isHasSurpriseMark() {
		return hasSurpriseMark;
	}

	public void setHasSurpriseMark(boolean hasSurpriseMark) {
		this.hasSurpriseMark = hasSurpriseMark;
	}

	public boolean isHasQuestionMark() {
		return hasQuestionMark;
	}

	public void setHasQuestionMark(boolean hasQuestionMark) {
		this.hasQuestionMark = hasQuestionMark;
	}

	public int getCount() {
		return pieceCount;
	}

	@Override
	public PlayerColor getPieceColor() {
		return pieceColour;
	}

	public int getPointNumber() {
		return pointNumber;
	}

	@Override
	public Board getBoard() {
		return board;
	}

	public void setColor(PlayerColor color) {
		pieceColour = color;
	}

	public void setCountAndColor(int count, PlayerColor color) {
		pieceCount = count;
		pieceColour = color;
	}

	public void addPiece(PlayerColor color) {
		if (pieceCount == 0)
			setColor(color);

		if (pieceCount == 1 && color != pieceColour && pieceColour != null) {
			board.getBar().addPiece(color.getOtherColor());
			pieceColour = color;
		} else
			pieceCount = pieceCount + 1;
	}

	public void removePiece(PlayerColor color) {
		if (pieceCount == 0)
			return;
		pieceCount = pieceCount - 1;
		if (pieceCount == 0)
			pieceColour = null;
	}

	// **************************************************
	// GRAPHICS
	// **************************************************

	@Override
	public void addHighlight(Color color) {
		setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, color));
		repaint();
	}

	@Override
	public void removeHighlight() {
		setBorder(null);
		repaint();
	}

	@Override
	protected void paintComponent(final Graphics g) {
		final Color fg = this.isDarkColoured() ? Palette.getDarkTriangleColour() : Palette.getLightTriangleColour();
		final Graphics2D g2 = (Graphics2D) g;
		g2.setColor(fg);
		g2.fillPolygon(this.polygon);
		this.paintPieces(g2);

		if (this.hasQuestionMark == true) {// for the special question
			paintQuestionMark(g2);
		}
		if (this.hasSurpriseMark == true) {// for the special surprise
			paintSuprpriseMark(g2);
		}

	}

	protected void paintPieces(final Graphics2D g2) {
		if (this.pieceCount == 0)
			return;

		final Color diskColour = Palette.getPieceColor(pieceColour);
		final Color textColour = Palette.getPieceTextColor(pieceColour);
		final int diskCount = Math.min(5, pieceCount);
		final int multiplier = 1 + pieceCount - diskCount;
		for (int i = 1; i <= diskCount; i++) {
			this.paintPiece(g2, diskColour, textColour, i, (i > 1) ? 1 : multiplier);
		}
	}

	protected void paintPiece(final Graphics2D g2, final Color diskColour, final Color textColour, final int location,
			final int count) {
		final BoardGeometry geometry = Board.getGeometry();
		g2.setColor(Palette.getPieceOutlineColor());
		final Ellipse2D disk = geometry.diskAt(location, this.pointsDown());
		g2.draw(disk);
		g2.setColor(diskColour);
		g2.fill(disk);
		if (count > 1) {
			g2.setColor(textColour);
			g2.drawString(String.format("\u00d7%d", count), (int) (disk.getX() + 3.0),
					(int) (disk.getY() + 3.0 + disk.getHeight() / 2.0));
		}
	}

	// Graphics2D for question
	protected void paintQuestionMark(Graphics2D g2) {

		// Load the question mark image
		Image questionMarkImage = Toolkit.getDefaultToolkit()
				.getImage(Triangle.class.getResource("/images/red-question-mark-removebg-preview.png"));
		// Loop through the array of places (board positions where the question mark
		// should be drawn)
		if (this.getY() < 320)
			g2.drawImage(questionMarkImage, 0, 0, 50, 50, this); // Resized to 50x50 for example
		else
			g2.drawImage(questionMarkImage, 0, 250, 50, 50, this); // Resized to 50x50 for example
	}

	// Graphics2D for SuprpriseMark
	protected void paintSuprpriseMark(Graphics2D g2) {

		// Load the question mark image
		Image questionMarkImage = Toolkit.getDefaultToolkit()
				.getImage(Triangle.class.getResource("/images/surprise-removebg-preview.png"));
		// Loop through the array of places (board positions where the question mark
		// should be drawn)
		if (this.getY() < 320)
			g2.drawImage(questionMarkImage, 0, 0, 50, 50, this); // Resized to 50x50 for example
		else
			g2.drawImage(questionMarkImage, 0, 250, 50, 50, this); // Resized to 50x50 for example
	}

	private Polygon getTriangle() {
		final int x1 = 0;
		final int x2 = x1 + this.getWidth();
		final int xm = (x1 + x2) / 2;
		final int y1 = 0;
		final int y2 = y1 + this.getHeight();
		final Polygon answer = new Polygon();

		if (this.pointsDown()) {
			answer.addPoint(x1, y1);
			answer.addPoint(x2, y1);
			answer.addPoint(xm, y2);
		} else {
			answer.addPoint(x1, y2);
			answer.addPoint(x2, y2);
			answer.addPoint(xm, y1);
		}

		return answer;
	}

	public boolean pointsUp() {
		return this.pointNumber <= 12;
	}

	public boolean pointsDown() {
		return !this.pointsUp();
	}

	public boolean isDarkColoured() {
		return this.pointNumber % 2 == 1;
	}
}
