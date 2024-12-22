package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import controller.Game;

public class HardBoard extends MediumBoard {

	public HardBoard(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	private void drawEnhancedDice(Graphics g, int value, int x, int y) {
		// Draw die background (green)
		g.setColor(Color.green);
		g.fillRect(x, y, 40, 40);

		// Draw die outline
		g.setColor(Color.BLACK);
		g.drawRect(x, y, 40, 40);

		// Handle value display
		g.setColor(Color.BLACK);
		if (value < 0) {
			// Draw a red background for negative values
			g.setColor(Color.BLUE);
			g.fillRect(x, y, 40, 40);
			g.setColor(Color.BLACK);
			g.drawRect(x, y, 40, 40);
			switch (value) {
			case -1:
				g.fillOval(x + 18, y + 18, 4, 4);
				break;
			case -2:
				g.fillOval(x + 10, y + 10, 4, 4);
				g.fillOval(x + 26, y + 26, 4, 4);
				break;
			case -3:
				g.fillOval(x + 10, y + 10, 4, 4);
				g.fillOval(x + 18, y + 18, 4, 4);
				g.fillOval(x + 26, y + 26, 4, 4);
				break;
			}

		} else {
			// Draw dots for values 1-6
			switch (value) {
			case 1:
				g.fillOval(x + 18, y + 18, 4, 4);
				break;
			case 2:
				g.fillOval(x + 10, y + 10, 4, 4);
				g.fillOval(x + 26, y + 26, 4, 4);
				break;
			case 3:
				g.fillOval(x + 10, y + 10, 4, 4);
				g.fillOval(x + 18, y + 18, 4, 4);
				g.fillOval(x + 26, y + 26, 4, 4);
				break;
			case 4:
				g.fillOval(x + 10, y + 10, 4, 4);
				g.fillOval(x + 10, y + 26, 4, 4);
				g.fillOval(x + 26, y + 10, 4, 4);
				g.fillOval(x + 26, y + 26, 4, 4);
				break;
			case 5:
				g.fillOval(x + 10, y + 10, 4, 4);
				g.fillOval(x + 10, y + 26, 4, 4);
				g.fillOval(x + 26, y + 10, 4, 4);
				g.fillOval(x + 26, y + 26, 4, 4);
				g.fillOval(x + 18, y + 18, 4, 4);
				break;
			case 6:
				g.fillOval(x + 10, y + 10, 4, 4);
				g.fillOval(x + 10, y + 18, 4, 4);
				g.fillOval(x + 10, y + 26, 4, 4);
				g.fillOval(x + 26, y + 10, 4, 4);
				g.fillOval(x + 26, y + 18, 4, 4);
				g.fillOval(x + 26, y + 26, 4, 4);
				break;
			default:
				// If the value is out of bounds, show a question mark
				g.drawString("?", x + 15, y + 25);
			}
		}
	}

	// @Override
	protected void paintComponent(final Graphics g) {
		final Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);

		// int die1 = game.getRolls()[0];
		int die1;
		if (game.getRolls().length == 1) {
			die1 = game.getRolls()[0];
			drawEnhancedDice(g, die1, 250, 300);
		}
		int die2;
		int die3 = game.getQuestionRoll();
		if (game.getRolls().length == 2) {
			die2 = game.getRolls()[1];
			drawEnhancedDice(g, die2, 670, 300);
			die1 = game.getRolls()[0];
			drawEnhancedDice(g, die1, 250, 300);
		} else {

		}

		// drawEnhancedDice(g, die1, 250, 300);
		drawQuestionDice(g, die3, 730, 300);

	}

}
