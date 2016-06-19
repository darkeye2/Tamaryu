package com.tr.test2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tr.img.gameobject.TRImage;
import com.tr.img.mng.ImageLoader;
import com.tr.test.ImageViewTest;
import com.tr.util.GraphicsUtility;
import com.tr.util.LanguageTranslator;

public class BackgroundTest implements ActionListener{
	private JFrame frame = new JFrame("Background Test");
	private JPanel mainP = new JPanel();
	private JPanel menueP = new JPanel();
	
	private TestBackground imgView;
	private Color background = Color.green;
	
	private HashMap<JButton, Integer> buttonMap = new HashMap<JButton, Integer>();
	
	public BackgroundTest(){
		//init frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800, 600));
		frame.setLayout(new BorderLayout());
		frame.add(mainP, BorderLayout.CENTER);
		frame.add(menueP, BorderLayout.EAST);

		
		imgView = new TestBackground();
			
		//imgView.setPreferredSize(new Dimension(300,300));
		mainP.setLayout(new BorderLayout());
		mainP.add(imgView, BorderLayout.CENTER);
		
		mainP.setBackground(Color.black);
		menueP.setLayout(new BoxLayout(menueP, BoxLayout.PAGE_AXIS));
		
		imgView.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
			    System.out.println("Is Transparent: "+imgView.isTransparent(e.getX(), e.getY()));
			}
		});
		
		updateBG();
		
		
		//show frame
		frame.pack();
		frame.setVisible(true);
	}
	
	public void updateBG(){
		
		GraphicsUtility.getTimer().schedule(new TimerTask(){

			@Override
			public void run() {
				imgView.setTime(0, 0, 0);
				update();
			}
			
		}, 20, 20);
	}
	
	
	private void update(){
		mainP.setBackground(background);
		mainP.validate();
		mainP.repaint();
		frame.validate();
		frame.repaint();
		imgView.setBackground(Color.red);
		imgView.repaint();
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(buttonMap.get(e.getSource())){
		}
		update();
	}
	
	
	public static void main(String[] args){
		new BackgroundTest();
	}
}
