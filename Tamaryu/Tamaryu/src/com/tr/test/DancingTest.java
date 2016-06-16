package com.tr.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tr.img.gameobject.TRImage;
import com.tr.img.gameobject.TRImageView;
import com.tr.img.animation.TRAnimation;
import com.tr.img.animation.TRAnimationView;
import com.tr.img.animation.TRComplexAnimationView;
import com.tr.img.filter.HSLColorFilter;
import com.tr.img.mng.ImageLoader;
import com.tr.util.GraphicsUtility;
import com.tr.util.LanguageTranslator;

public class DancingTest extends JFrame implements ActionListener {
	private JFrame frame;
	private TestPanel mainP;
	private JPanel menueP = new JPanel();

	private ImageLoader il = new ImageLoader();
	private Color background = Color.green;
	TimerTask tt;

	// animation components
	private TRAnimationView headmView, bodyView, tailView, larmView, rarmView,
			llegView, rlegView, mouthView, hornView, eyeView;
	private TRAnimation headDefault, bodyDefault, tailDefault, rarmDefault,
			larmDefault, rlegDefault, llegDefault, mouthDefault, hornDefault,
			eyeDefault, eyeClosed, eyeBlink;
	private TRComplexAnimationView headView = new TRComplexAnimationView("head");
	
	private TRImageView bg;

	private TRComplexAnimationView aniView = new TRComplexAnimationView();

	private HashMap<JButton, Integer> buttonMap = new HashMap<JButton, Integer>();

	public DancingTest() {
		frame = this;new TimerTask(){

			@Override
			public void run() {
				blink();
				GraphicsUtility.getTimer().schedule(tt, Math.round( Math.random()*500+300));
				
			}};
		// init frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800, 600));
		frame.setLayout(new BorderLayout());
		frame.add(menueP, BorderLayout.EAST);

		// init path for image loader
		il.addPath("img");
		try {
			bg = new TRImageView(il.load("savanna_bg_night.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mainP = new TestPanel(bg);

		frame.add(mainP, BorderLayout.CENTER);
		

		loadAnimation();

		mainP.setLayout(new BorderLayout());
		mainP.add(aniView, BorderLayout.CENTER);

		mainP.setBackground(background);
		menueP.setLayout(new BoxLayout(menueP, BoxLayout.PAGE_AXIS));
		addButton(new JButton(LanguageTranslator.getString("background")), 4);
		addButton(new JButton(LanguageTranslator.getString("color")), 1);
		addButton(new JButton(LanguageTranslator.getString("eyecolor")), 6);
		addButton(new JButton(LanguageTranslator.getString("closeeyes")), 2);
		addButton(new JButton(LanguageTranslator.getString("blink")), 3);
		addButton(new JButton(LanguageTranslator.getString("openeyes")), 5);
		addButton(new JButton(LanguageTranslator.getString("wavehand")), 7);
		
		// show frame
		frame.pack();
		frame.setVisible(true);

		GraphicsUtility.getTimer().schedule(new TimerTask() {

			@Override
			public void run() {
				update();

			}
		}, 0, 15);
	}

	private void loadAnimation() {
		try {
			bodyDefault = new TRAnimation(new TRImage[]{il.load("dancing1.png"),il.load("dancing2.png")}, 3);
			tailDefault = new TRAnimation(il.loadAll("sprite01_tail_1"), 5);
			eyeDefault = new TRAnimation(
					new TRImage[] { il.load("sprite01_eyes_1") }, 5);
			eyeClosed = new TRAnimation(
					new TRImage[] { il.load("sprite01_eyes_2") }, 5);
			eyeBlink = new TRAnimation(new TRImage[] {
					il.load("sprite01_eyes_1"), il.load("sprite01_eyes_2"),
					il.load("sprite01_eyes_1") }, 15, false);

			tailView = new TRAnimationView("tail");
			tailView.addAnimation("default", tailDefault, true);
			tailView.setBounds(400, 420, 200, 180);
			tailView.setPosition(300, 360);

			eyeView = new TRAnimationView("eye");
			eyeView.addAnimation("closed", eyeClosed);
			eyeView.addAnimation("blink", eyeBlink);
			eyeView.addAnimation("default", eyeDefault, true);
			eyeView.setBounds(150, 250, 190, 100);
			eyeView.setPosition(160, 80);

			bodyView = new TRAnimationView("body");
			bodyView.addAnimation("default", bodyDefault, true);
			bodyView.setBounds(0, 0, 600, 600);

			aniView.setSize(800, 800);
			aniView.add(tailView);
			aniView.add(bodyView);
			aniView.add(eyeView);
			
			aniView.setScale(0.5f);
			aniView.setBounds(300, 200, 600, 600);
			aniView.setBorder(BorderFactory.createLineBorder(Color.red));
			System.out.println(aniView.getBounds(null));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tt = createDT();
			
		GraphicsUtility.getTimer().schedule(tt, Math.round( Math.random()*500+300));
	}
	
	public TimerTask createDT(){
		tt = new TimerTask(){

			@Override
			public void run() {
				blink();
				GraphicsUtility.getTimer().schedule(createDT(), Math.round( Math.random()*800+600));
				
			}};
			
		return tt;
	}

	public void closeEyes() {
		eyeView.loadAnimation("closed", true);
	}

	public void openEyes() {
		eyeView.loadAnimation("default", true);
	}

	public void blink() {
		eyeView.loadAnimation("blink", true);
	}



	public void setColor(Color c) {
		aniView.removeAllFilter();
		aniView.addFilter(new HSLColorFilter(c));
		
		//aniView.getView("head").get("eye").removeAllFilter();
		((TRComplexAnimationView) aniView.getView("head")).getView("eye").get("default").getImage(0).removeAllFilter();
		((TRComplexAnimationView) aniView.getView("head")).getView("eye").get("blink").getImage(0).removeAllFilter();
		((TRComplexAnimationView) aniView.getView("head")).getView("eye").get("blink").getImage(2).removeAllFilter();
		
		/*headDefault.removeAllFilter();
		headDefault.addFilter(new HSLColorFilter(c));
		tailDefault.removeAllFilter();
		tailDefault.addFilter(new HSLColorFilter(c));
		rlegDefault.removeAllFilter();
		rlegDefault.addFilter(new HSLColorFilter(c));
		llegDefault.removeAllFilter();
		llegDefault.addFilter(new HSLColorFilter(c));
		rarmDefault.removeAllFilter();
		rarmDefault.addFilter(new HSLColorFilter(c));
		larmDefault.removeAllFilter();
		larmDefault.addFilter(new HSLColorFilter(c));
		bodyDefault.removeAllFilter();
		bodyDefault.addFilter(new HSLColorFilter(c));
		mouthDefault.removeAllFilter();
		mouthDefault.addFilter(new HSLColorFilter(c));*/
	}

	public void setEyeColor(Color c) {
		((TRComplexAnimationView) aniView.getView("head")).getView("eye").get("default").getImage(0).removeAllFilter();
		((TRComplexAnimationView) aniView.getView("head")).getView("eye").get("blink").getImage(0).removeAllFilter();
		((TRComplexAnimationView) aniView.getView("head")).getView("eye").get("blink").getImage(2).removeAllFilter();
		((TRComplexAnimationView) aniView.getView("head")).getView("eye").get("default").getImage(0).addFilter(new HSLColorFilter(c));
		((TRComplexAnimationView) aniView.getView("head")).getView("eye").get("blink").getImage(0).addFilter(new HSLColorFilter(c));
		((TRComplexAnimationView) aniView.getView("head")).getView("eye").get("blink").getImage(2).addFilter(new HSLColorFilter(c));

		/*eyeView.removeAllFilter();
		eyeView.addFilter(new HSLColorFilter(c));*/
	}

	public void changeEyeColor() {
		Color c = JColorChooser.showDialog(frame, "Drachen Farbe", background);
		setEyeColor(c);

		update();
	}

	public void changeColor() {
		Color c = JColorChooser.showDialog(frame, "Drachen Farbe", background);
		setColor(c);

		update();
	}

	private void addButton(JButton b, int id) {
		b.setAlignmentX(Component.CENTER_ALIGNMENT);
		b.addActionListener(this);
		buttonMap.put(b, id);
		menueP.add(b);
		menueP.add(Box.createRigidArea(new Dimension(0, 10)));
	}

	private void selectColor() {
		background = JColorChooser.showDialog(frame, "Background Color",
				background);

		update();
	}

	private void update() {
		mainP.setBackground(background);
		mainP.validate();
		mainP.repaint();
		frame.validate();
		frame.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (buttonMap.get(e.getSource())) {
		case 1:
			changeColor();
			break;
		case 2:
			closeEyes();
			break;
		case 3:
			blink();
			break;
		case 5:
			openEyes();
			break;
		case 6:
			changeEyeColor();
			break;
		case 7:
			//wink();
			break;
		case 4:
			selectColor();
			break;
		}
		update();
	}
	
	public void paintComponent(Graphics g){
		bg.paintComponent(g);
		super.paintComponents(g);
	}
	

	public static void main(String[] args) {
		new DancingTest();
	}
}
