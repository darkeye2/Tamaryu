package com.tr.test.genetic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class ColorRep extends JLabel {
	private static final long serialVersionUID = 1L;

	private Color pColor = Color.black;

	public ColorRep(Color c) {
		this.setOpaque(false);
		pColor = c;
		this.setPreferredSize(new Dimension(25, 25));
	}

	public void setColor(Color c) {
		pColor = c;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// get Size
		int size = this.getSize().height;
		int r = (size - 4) / 2;
		r = Math.max(r, 1);

		// create points
		Point2D center = new Point2D.Float(2 + r, 2 + r);
		float[] dist = { 0.0f, 1.0f };
		Color[] colors = { calculateColor(r, 0), pColor };

		// paint circle
		RadialGradientPaint rgp = new RadialGradientPaint(center, r, dist,
				colors);
		g2d.setPaint(rgp);
		g2d.fillArc(2, 2, r * 2, r * 2, 0, 360);
	}

	private Color calculateColor(int r, int i) {
		double f = (255.0 * 0.5) / (float) r; // general factor
		f *= r - i;

		// color valus
		int rc = pColor.getRed(), gc = pColor.getGreen(), bc = pColor.getBlue();
		rc = Math.min(255, rc + (int) f);
		gc = Math.min(255, gc + (int) f);
		bc = Math.min(255, bc + (int) f);

		return new Color(rc, gc, bc);
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("Status Test");
		f.getContentPane().setBackground(Color.DARK_GRAY);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setPreferredSize(new Dimension(600, 600));
		f.getContentPane().add(new ColorRep(Color.green));
		f.pack();
		f.setVisible(true);
	}

}
