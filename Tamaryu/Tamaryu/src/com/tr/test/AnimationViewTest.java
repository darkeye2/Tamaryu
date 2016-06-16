package com.tr.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tr.img.gameobject.TRImage;
import com.tr.util.LanguageTranslator;
import com.tr.img.animation.TRAnimation;
import com.tr.img.animation.TRAnimationView;

public class AnimationViewTest implements ActionListener{

	private JFrame frame = new JFrame("Animation View Test");
	private JPanel mainP = new JPanel();
	private JPanel menueP = new JPanel();
	
	private TRAnimationView aniView;
	private Color background = Color.green;
	
	private HashMap<JButton, Integer> buttonMap = new HashMap<JButton, Integer>();
	
	public AnimationViewTest(){
		//init frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800, 600));
		frame.setLayout(new BorderLayout());
		frame.add(mainP, BorderLayout.CENTER);
		frame.add(menueP, BorderLayout.EAST);

		try {
			createAnimation();
			aniView.loadAnimation("MOVE_UP", true);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		mainP.setLayout(new BorderLayout());
		mainP.add(aniView, BorderLayout.CENTER);
		
		mainP.setBackground(background);
		menueP.setLayout(new BoxLayout(menueP, BoxLayout.PAGE_AXIS));
		addButton(new JButton(LanguageTranslator.getString("backgroundcolor")), 1);
		addButton(new JButton(LanguageTranslator.getString("start")), 2);
		addButton(new JButton(LanguageTranslator.getString("stop")), 3);
		addButton(new JButton(LanguageTranslator.getString("pause")), 4);
		addButton(new JButton(LanguageTranslator.getString("scaling") + ": FIT"), 5);
		addButton(new JButton(LanguageTranslator.getString("scaling") + ": FILL"), 6);
		addButton(new JButton(LanguageTranslator.getString("scaling") + ": ORG"), 7);
		addButton(new JButton(LanguageTranslator.getString("moveup")), 8);
		addButton(new JButton(LanguageTranslator.getString("movedown")), 9);
		addButton(new JButton(LanguageTranslator.getString("moveright")), 10);
		addButton(new JButton(LanguageTranslator.getString("moveleft")), 11);
		
		aniView.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
			    System.out.println("Is Transparent: "+aniView.isTransparent(e.getX(), e.getY()));
			}
		});
		
		
		
		//show frame
		frame.pack();
		frame.setVisible(true);
	}
	
	private void createAnimation() throws IOException{
		String p = "img/";
		
		//move down animation
		TRImage[] moveDown = new TRImage[4];
		moveDown[0] = new TRImage("TEST", 1, new ImageIcon(ImageIO.read(ClassLoader.getSystemResource( p+"moveDown_1.png" ))));
		moveDown[1] = new TRImage("TEST", 1, new ImageIcon(ImageIO.read(ClassLoader.getSystemResource( p+"moveDown_2.png" ))));
		moveDown[2] = new TRImage("TEST", 1, new ImageIcon(ImageIO.read(ClassLoader.getSystemResource( p+"moveDown_3.png" ))));
		moveDown[3] = new TRImage("TEST", 1, new ImageIcon(ImageIO.read(ClassLoader.getSystemResource( p+"moveDown_2.png" ))));
		
		//move left animation
		TRImage[] moveLeft = new TRImage[4];
		moveLeft[0] = new TRImage("TEST", 1, new ImageIcon(ImageIO.read(ClassLoader.getSystemResource( p+"moveLeft_1.png" ))));
		moveLeft[1] = new TRImage("TEST", 1, new ImageIcon(ImageIO.read(ClassLoader.getSystemResource( p+"moveLeft_2.png" ))));
		moveLeft[2] = new TRImage("TEST", 1, new ImageIcon(ImageIO.read(ClassLoader.getSystemResource( p+"moveLeft_3.png" ))));
		moveLeft[3] = new TRImage("TEST", 1, new ImageIcon(ImageIO.read(ClassLoader.getSystemResource( p+"moveLeft_2.png" ))));
			
		//move right animation
		TRImage[] moveRight = new TRImage[4];
		moveRight[0] = new TRImage("TEST", 1, new ImageIcon(ImageIO.read(ClassLoader.getSystemResource( p+"moveRight_1.png" ))));
		moveRight[1] = new TRImage("TEST", 1, new ImageIcon(ImageIO.read(ClassLoader.getSystemResource( p+"moveRight_2.png" ))));
		moveRight[2] = new TRImage("TEST", 1, new ImageIcon(ImageIO.read(ClassLoader.getSystemResource( p+"moveRight_3.png" ))));
		moveRight[3] = new TRImage("TEST", 1, new ImageIcon(ImageIO.read(ClassLoader.getSystemResource( p+"moveRight_2.png" ))));
		
		//move down animation
		TRImage[] moveUp = new TRImage[4];
		moveUp[0] = new TRImage("TEST", 1, new ImageIcon(ImageIO.read(ClassLoader.getSystemResource( p+"moveUp_1.png" ))));
		moveUp[1] = new TRImage("TEST", 1, new ImageIcon(ImageIO.read(ClassLoader.getSystemResource( p+"moveUp_2.png" ))));
		moveUp[2] = new TRImage("TEST", 1, new ImageIcon(ImageIO.read(ClassLoader.getSystemResource( p+"moveUp_3.png" ))));
		moveUp[3] = new TRImage("TEST", 1, new ImageIcon(ImageIO.read(ClassLoader.getSystemResource( p+"moveUp_2.png" ))));
		
		TRAnimation downAni = new TRAnimation(moveDown, 10);
		TRAnimation upAni = new TRAnimation(moveUp, 10);
		TRAnimation rightAni = new TRAnimation(moveRight, 10);
		TRAnimation leftAni = new TRAnimation(moveLeft, 10);
		
		aniView = new TRAnimationView();
		aniView.addAnimation("MOVE_DOWN", downAni);
		aniView.addAnimation("MOVE_UP", upAni);
		aniView.addAnimation("MOVE_RIGHT", rightAni);
		aniView.addAnimation("MOVE_LEFT", leftAni);
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
		aniView.setBackground(Color.red);
		aniView.repaint();
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(buttonMap.get(e.getSource())){
		case 1: selectColor(); break;
		case 2: aniView.startAnimation(); break;
		case 3: aniView.stopAnimation(); break;
		case 4: aniView.pauseAnimation(); break;
		case 5: aniView.setScalingMode(0); break;
		case 6: aniView.setScalingMode(1); break;
		case 7: aniView.setScalingMode(2); break;
		case 8: aniView.loadAnimation("MOVE_UP", true); break;
		case 9: aniView.loadAnimation("MOVE_DOWN", true); break;
		case 10: aniView.loadAnimation("MOVE_RIGHT", true); break;
		case 11: aniView.loadAnimation("MOVE_LEFT", true); break;
		}
		update();
	}
	
	
	public static void main(String[] args){
		new AnimationViewTest();
	}

}
