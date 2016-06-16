package com.tr.test;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.LayoutManager;

import javax.swing.JComponent;

import com.tr.img.gameobject.TRImageView;

public class TestPanel extends javax.swing.JPanel {
	private static final long serialVersionUID = 1L;
	private TRImageView bg;
	
	public TestPanel(TRImageView b){
		this.bg = b;
		bg.setScalingMode(TRImageView.SCALE_MODE_FILL);
		this.setLayout(null);
	}
	
	public void setLayout(LayoutManager l){
		super.setLayout(null);
	}
	
	public void paintComponent(Graphics g){
		bg.setBounds(0, 0, this.getWidth(), this.getHeight());
		bg.paintComponent(g);
		//super.paintComponent(g);
	}
	
	public void paintComponents(Graphics g){
		for(Component c : this.getComponents()){
			if(c instanceof TRImageView){
				g.translate(c.getX(), c.getY());
				((TRImageView) c).paintComponent(g);
				g.translate(-c.getX(), -c.getY());
			}else{
				c.paintAll(g);
			}
		}
	}
	
	/*public void paint(Graphics g){
		//bg.setBounds(0, 0, this.getWidth(), this.getHeight());
		//bg.paintAll(g);
		//super.paint(g);
		
	}*/

}
