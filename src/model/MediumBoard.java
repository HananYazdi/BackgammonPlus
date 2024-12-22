package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import controller.Game;

public class MediumBoard extends Board {

	public MediumBoard(Game game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	protected void drawQuestionDice(Graphics g, int value, int x, int y) {
		// Draw die background (white)
		g.setColor(Color.RED);
		g.fillRect(x, y, 40, 40);
		// Draw die outline
		g.setColor(Color.BLACK);
		g.drawRect(x, y, 40, 40);

		// Draw dots based on the value of the die
		g.setColor(Color.BLACK);
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

		}
	}

	@Override
	protected void paintComponent(final Graphics g) {
		if (g == null)
			return;
		final Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);

		// Add null checks
		if (game == null || game.getRolls() == null) {
			return;
		}

		int die1;
		if (game.getRolls().length == 1) {
			die1 = game.getRolls()[0];
			drawDice(g, die1, 250, 300);
		}

		int die2;
		int die3 = game.getQuestionRoll();
		if (game.getRolls().length == 2) {
			die2 = game.getRolls()[1];
			drawDice(g, die2, 670, 300);
		}

		drawQuestionDice(g, die3, 730, 300);
	}

}
