package com.tr.test.genetic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ColorGeneticTest implements ActionListener{
	private JFrame frame = new JFrame("Color Genetic Test");
	private JPanel mainP = new JPanel();
	private JPanel menueP = new JPanel();
	
	private Color background = Color.DARK_GRAY;
	
	private HashMap<JButton, Integer> buttonMap = new HashMap<JButton, Integer>();
	
	public ColorGeneticTest(){
		//init frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800, 600));
		frame.setLayout(new BorderLayout());
		frame.add(mainP, BorderLayout.CENTER);
		frame.add(menueP, BorderLayout.EAST);
		
		mainP.setLayout(null);
		
		mainP.setBackground(background);
		menueP.setLayout(new BoxLayout(menueP, BoxLayout.PAGE_AXIS));
		addButton(new JButton("Hintergrund Farbe"), 1);
		
		//show frame
		frame.pack();
		frame.setVisible(true);
	}
	
	private void addButton(JButton b, int id){
		b.setAlignmentX(Component.CENTER_ALIGNMENT);
		b.addActionListener(this);
		buttonMap.put(b, id);
		menueP.add(b);
		menueP.add(Box.createRigidArea(new Dimension(0,10)));
	}
	
	
	private void selectColor(){
		background = JColorChooser.showDialog(
                frame,
                "Background Color",
                background);
		
		update();
	}
	
	
	private void update(){
		mainP.setBackground(background);
		mainP.validate();
		mainP.repaint();
		frame.validate();
		frame.repaint();
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(buttonMap.get(e.getSource())){
		case 1: selectColor(); break;
		}
		update();
	}
	
	
	public static void main(String[] args){
		new ColorGeneticTest();
	}
}
