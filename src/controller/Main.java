package controller;

import model.Level;

public class Main {
	public static void main(String[] args) {
		Game game = new Game(Level.EASY);
		game.start();
	}
}
