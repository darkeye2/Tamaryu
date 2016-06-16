package com.tr.test;

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

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tr.img.gameobject.TRImage;
import com.tr.img.gameobject.TRImageView;
import com.tr.util.LanguageTranslator;

public class ImageViewTest implements ActionListener{
	
	private JFrame frame = new JFrame("Image View Test");
	private JPanel mainP = new JPanel();
	private JPanel menueP = new JPanel();
	
	private TRImageView imgView;
	private Color background = Color.green;
	
	private HashMap<JButton, Integer> buttonMap = new HashMap<JButton, Integer>();
	
	public ImageViewTest(){
		//init frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800, 600));
		frame.setLayout(new BorderLayout());
		frame.add(mainP, BorderLayout.CENTER);
		frame.add(menueP, BorderLayout.EAST);
		
		BufferedImage i = null;
		try {
			i = ImageIO.read(ClassLoader.getSystemResource( "img/platzhalter_friends.png" ));
		} catch (IOException e) {
			e.printStackTrace();
		}
		TRImage tri = new TRImage("TEST", 1, new ImageIcon(i));
		imgView = new TRImageView(tri, 0);
		//imgView.setPreferredSize(new Dimension(300,300));
		mainP.setLayout(new BorderLayout());
		mainP.add(imgView, BorderLayout.CENTER);
		
		mainP.setBackground(background);
		menueP.setLayout(new BoxLayout(menueP, BoxLayout.PAGE_AXIS));
		addButton(new JButton(LanguageTranslator.getString("backgroundcolor")), 1);
		addButton(new JButton(LanguageTranslator.getString("changeimage")), 2);
		addButton(new JButton(LanguageTranslator.getString("scaling") + ": FIT"), 3);
		addButton(new JButton(LanguageTranslator.getString("scaling") + ": FILL"), 4);
		addButton(new JButton(LanguageTranslator.getString("scaling") + ": ORG"), 5);
		
		imgView.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
			    System.out.println("Is Transparent: "+imgView.isTransparent(e.getX(), e.getY()));
			}
		});
		
		
		
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
	
	private void selectImage() throws IOException{
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(frame);
		
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File file = fc.getSelectedFile();
			BufferedImage i = ImageIO.read(file);
			if(imgView != null){
				mainP.remove(imgView);
			}
			imgView.setImage(new TRImage("TEST", 1, new ImageIcon(i)));
			mainP.add(imgView, BorderLayout.CENTER);
		}
		
		update();
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
		case 1: selectColor(); break;
		case 2: try {
				selectImage();
			} catch (IOException e1) {
				e1.printStackTrace();
			} break;
		case 3: imgView.setScalingMode(0); break;
		case 4: imgView.setScalingMode(1); break;
		case 5: imgView.setScalingMode(2); break;
		}
		update();
	}
	
	
	public static void main(String[] args){
		new ImageViewTest();
	}

}
