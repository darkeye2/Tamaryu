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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.tr.img.TRImage;
import com.tr.img.TRImageView;
import com.tr.img.filter.BrightnessFilter;
import com.tr.img.filter.ColorChangeFilter;
import com.tr.img.filter.ColorFilter;
import com.tr.img.filter.ColorizationFilter;
import com.tr.img.filter.ContrastFilter;
import com.tr.img.filter.EdgeDetectFilter;
import com.tr.img.filter.GrayFilter;
import com.tr.img.filter.HSLColorFilter;
import com.tr.img.filter.OverlayFilter;
import com.tr.img.filter.PixelationFilter;
import com.tr.img.filter.RGBFilter;
import com.tr.img.filter.StrokeFilter;

public class FilterTest implements ActionListener{
	private JFrame frame = new JFrame("Filter Test");
	private JPanel mainP = new JPanel();
	private JPanel menueP = new JPanel();

	private TRImageView imgView;
	private TRImage img;
	private Color background = Color.green;

	private HashMap<JButton, Integer> buttonMap = new HashMap<JButton, Integer>();

	public FilterTest() {
		// init frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800, 600));
		frame.setLayout(new BorderLayout());
		frame.add(mainP, BorderLayout.CENTER);
		frame.add(menueP, BorderLayout.EAST);

		BufferedImage i = null;
		try {
			i = ImageIO
					.read(ClassLoader
							.getSystemResource("com/tr/res/img/tam1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		img = new TRImage("TEST", 1, new ImageIcon(i));
		imgView = new TRImageView(img, 0);
		
		mainP.setLayout(new BorderLayout());
		mainP.add(imgView, BorderLayout.CENTER);
		
		mainP.setBackground(background);
		menueP.setLayout(new BoxLayout(menueP, BoxLayout.PAGE_AXIS));
		addButton(new JButton("Hintergrund Farbe"), 1);
		addButton(new JButton("Bild Ändern"), 2);
		addButton(new JButton("Reset Filter"), 3);
		addButton(new JButton("Farbfilter"), 4);
		addButton(new JButton("FF (RGB Fast)"), 5);
		addButton(new JButton("Farbfilter (RGB)"), 11);
		addButton(new JButton("Farbfilter (HSV)"), 12);
		addButton(new JButton("Farbfilter (HSL)"), 16);
		addButton(new JButton("Kontrast"), 6);
		addButton(new JButton("Helligkeit"), 7);
		addButton(new JButton("Graustufen"), 8);
		addButton(new JButton("Negativ"), 9);
		addButton(new JButton("Overlay"), 13);
		addButton(new JButton("Pixelation"), 10);
		addButton(new JButton("Edge Detect"), 14);
		addButton(new JButton("Stroke"), 15);
		
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
	
	private void resetFilter(){
		//GraphicsUtility.saveImage(GraphicsUtility.toImage(img.getImageIcon()));
		img.removeAllFilter();
		update();
	}
	
	private void overlayFilter(){
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(frame);
		
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File file = fc.getSelectedFile();
			BufferedImage i = null;
			try {
				i = ImageIO.read(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			img.addFilter(new OverlayFilter(i));
		}
		
		update();
	}
	
	private void grayFilter(){
		img.addFilter(new GrayFilter());
		update();
	}
	
	private void negationFilter(){
		img.addFilter(new com.tr.img.filter.NegationFilter());
		update();
	}
	
	private void strokeFilter(){
		img.addFilter(new StrokeFilter());
		update();
	}
	
	private void colFilter1(){
		Color c = JColorChooser.showDialog(
                frame,
                "Filter Color",
                background);
		
		img.addFilter(new ColorizationFilter(c));
		update();
	}
	
	private void colFilter2(){
		Color c = JColorChooser.showDialog(
                frame,
                "Filter Color",
                background);
		
		img.addFilter(new RGBFilter(c, false));
		update();
	}
	
	private void repFilter(){
		Color c = JColorChooser.showDialog(
                frame,
                "Filter Farbe",
                background);
		
		img.addFilter(new ColorChangeFilter(c));
		update();
	}
	
	private void hslFilter(){
		Color c = JColorChooser.showDialog(
                frame,
                "Filter Farbe",
                background);
		
		img.addFilter(new HSLColorFilter(c));
		update();
	}
	
	private void repFilter2(){
		Color c = JColorChooser.showDialog(
                frame,
                "Filter Farbe",
                background);
		
		img.addFilter(new ColorFilter(c));
		update();
	}
	
	private void brightFilter(){
		int ans = Integer.parseInt( JOptionPane.showInputDialog("Helltigkeit als Wert zwischen -255 und 255:"));
		
		img.addFilter(new BrightnessFilter(ans));
		update();
	}
	
	private void edgeFilter(){
		img.addFilter(new EdgeDetectFilter());
		update();
	}
	
	private void contrastFilter(){
		double ans = Double.parseDouble( JOptionPane.showInputDialog("Kontrast als Wert zwischen -255.0 und 255.0:"));
		
		img.addFilter(new ContrastFilter((float) ans));
		update();
	}
	
	private void pixelFilter(){
		int ans = Integer.parseInt( JOptionPane.showInputDialog("Pixelblock größe:"));
		
		img.addFilter(new PixelationFilter(ans));
		update();
	}
	
	private void selectImage() throws IOException{
		final JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(frame);
		
		if(returnVal == JFileChooser.APPROVE_OPTION){
			File file = fc.getSelectedFile();
			BufferedImage i = ImageIO.read(file);
			/*if(imgView != null){
				mainP.remove(imgView);
			}*/
			img = new TRImage("TEST", 1, new ImageIcon(i));
			imgView.setImage(img);
			//mainP.add(imgView, BorderLayout.CENTER);
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
		case 3: resetFilter(); break;
		case 4: colFilter1(); break;
		case 5: colFilter2(); break;
		case 6: contrastFilter(); break;
		case 7: brightFilter(); break;
		case 8: grayFilter(); break;
		case 9: negationFilter(); break;
		case 10: pixelFilter(); break;
		case 11: repFilter(); break;
		case 12: repFilter2(); break;
		case 13: overlayFilter(); break;
		case 14: edgeFilter(); break;
		case 15: strokeFilter(); break;
		case 16: hslFilter(); break;
		
		}
		update();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new FilterTest();
	}

}
