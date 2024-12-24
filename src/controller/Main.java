package controller;

import javax.swing.JFrame;

import model.Level;
import view.*;
public class Main {
	public static void main(String[] args) {
		Game game = new Game(Level.HARD);
		game.start();
		
		
	}
}
