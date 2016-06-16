package com.tr.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.tr.img.gameobject.TRImage;
import com.tr.img.gameobject.TRImageView;
import com.tr.img.animation.TRAnimation;
import com.tr.img.animation.TRAnimationView;
import com.tr.img.mng.ImageLoader;
import com.tr.util.GraphicsUtility;
import com.tr.util.LanguageTranslator;

public class EyeLoaderTest implements ActionListener{
	private JFrame frame = new JFrame("ImageLoader Test");
	private JPanel mainP = new JPanel();
	private JPanel menueP = new JPanel();
	
	private ImageLoader il = new ImageLoader();
	private TRAnimationView aniView;
	private TRAnimation ani;
	private TRImageView imgView;
	private Color background = Color.green;
	
	private HashMap<JButton, Integer> buttonMap = new HashMap<JButton, Integer>();
	
	public EyeLoaderTest(){
		//init frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800, 600));
		frame.setLayout(new BorderLayout());
		frame.add(mainP, BorderLayout.CENTER);
		frame.add(menueP, BorderLayout.EAST);
		
		//init path for image loader
		il.addPath("img");

		loadSprite("sprite01_eyes", false);
		blink();
		
		mainP.setLayout(new BorderLayout());
		mainP.add(aniView, BorderLayout.CENTER);
		
		mainP.setBackground(background);
		menueP.setLayout(new BoxLayout(menueP, BoxLayout.PAGE_AXIS));
		addButton(new JButton(LanguageTranslator.getString("background")), 4);
		addButton(new JButton(LanguageTranslator.getString("loadimage")), 1);
		addButton(new JButton(LanguageTranslator.getString("gifanimation")), 2);
		addButton(new JButton(LanguageTranslator.getString("sheetanimation")), 3);
		addButton(new JButton(LanguageTranslator.getString("loadall")), 5);
		
		//show frame
		frame.pack();
		frame.setVisible(true);
	}
	
	private void blink(){
		ani.reset();
		TimerTask r = new TimerTask(){

			@Override
			public void run() {
				blink();
			}};
			
		GraphicsUtility.getTimer().schedule(r, Math.round(Math.random()*5000+500));
	}
	
	private void loadImg(String string) {
		try {
			TRImage b = il.load(string);
			if(imgView == null){
				imgView = new TRImageView();
			}
			imgView.setImage(b);
			clearStage();
			mainP.add(imgView, BorderLayout.CENTER);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadGif(){
		try {
			TRAnimation ani = new TRAnimation(il.loadAll("knight"), 25);
			if(aniView == null){
				aniView = new TRAnimationView();
			}
			if(!aniView.loadAnimation("knight", true)){
				aniView.addAnimation("knight", ani);
				aniView.loadAnimation("knight", true);
			}
			clearStage();
			mainP.add(aniView, BorderLayout.CENTER);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadSprite(String name, boolean auto){
		try {
			ani = new TRAnimation(il.loadAll(name), 15);
			ani.setLoop(auto);
			if(aniView == null){
				aniView = new TRAnimationView();
			}
			if(!aniView.loadAnimation("link", true)){
				aniView.addAnimation("link", ani);
				aniView.loadAnimation("link", true);
			}
			clearStage();
			mainP.add(aniView, BorderLayout.CENTER);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadAll(){
		String name = JOptionPane.showInputDialog("Name des als Resource vorhanden Bildes:");
		if(il.exists(name));
		try {
			TRImage[] images = il.loadAll(name);
			if(images != null && images.length > 0){
				if(images.length == 1){
					if(imgView == null){
						imgView = new TRImageView();
					}
					imgView.setImage(images[0]);
					clearStage();
					mainP.add(imgView, BorderLayout.CENTER);
				}else{
					TRAnimation ani = new TRAnimation(images, 10);
					if(aniView == null){
						aniView = new TRAnimationView();
					}
					String an = "ani_"+name;
					if(!aniView.loadAnimation(an, true)){
						aniView.addAnimation(an, ani);
						aniView.loadAnimation(an, true);
					}
					clearStage();
					mainP.add(aniView, BorderLayout.CENTER);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void clearStage(){
		if(aniView != null && aniView.getParent() == mainP)
			mainP.remove(aniView);
		if(imgView != null && imgView.getParent() == mainP)
			mainP.remove(imgView);
	}
	
	private void setImage(){
		String name = JOptionPane.showInputDialog("Name des als Resource vorhanden Bildes:");
		loadImg(name);
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
		case 4: selectColor(); break;
		case 1: setImage(); break;
		case 2: loadGif(); break;
		case 3: loadSprite("link", true); break;
		case 5: loadAll(); break;
		}
		update();
	}
	
	
	public static void main(String[] args){
		new EyeLoaderTest();
	}
}
