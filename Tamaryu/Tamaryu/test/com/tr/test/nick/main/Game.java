package com.tr.test.nick.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.tr.engine.sound.AudioMaster;

public class Game
{
	public static void main (String [] args)
	{	
		AudioMaster.initialize();
		JFrame window = new JFrame("TamaRyu");
		window.setContentPane(new GameWindow());
		//window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.addWindowListener( new WindowAdapter(){
            public void windowClosing( WindowEvent windowevent ){
            	AudioMaster.killAllData();
                System.exit(0);
            }
        });
		window.setResizable(false);
		window.pack();
		window.setVisible(true);
	}
}