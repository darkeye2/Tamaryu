package com.tr.test.nick.main;

import javax.swing.JFrame;

public class Game
{
	public static void main (String [] args)
	{
		JFrame window = new JFrame("TamaRyu");
		window.setContentPane(new GameWindow());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}

}
