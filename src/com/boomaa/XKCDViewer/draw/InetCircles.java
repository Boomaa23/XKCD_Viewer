package com.boomaa.XKCDViewer.draw;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JComponent;

import com.boomaa.XKCDViewer.utils.JDEC;
import com.boomaa.XKCDViewer.utils.StatsUtils;

/** <p>Draws circles corresponding to the status of various web services.</p> */
@SuppressWarnings("serial")
public class InetCircles extends JComponent {
	/** <p>The diameter of each circle.</p> */
	private final int diameter;
	/** <p>The X position of the center of each circle.</p> */
	private int spacing;
	
	/**
	 * <p>Creates circle panel layout and establishes diameter.</p>
	 * @param diameter the diameter of all circles.
	 */
	public InetCircles(final int diameter) {
		this.diameter = diameter;
		setLayout(new FlowLayout(FlowLayout.RIGHT));
	}
	
	/**
	 * <p>Draws circles and decrements spacing for next circle.</p>
	 * @param c Color for circle to be drawn in.
	 * @param g Graphics object to draw on.
	 */
	private void circle(Color c, Graphics g) {
		g.setColor(c);
		g.fillOval(spacing, 0, diameter, diameter);
		spacing -= (diameter * 1.5);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		spacing = (int) (JDEC.FRAME.getWidth() - (diameter * 3)); 
		circle(StatsUtils.getHostIP() == "Could not find hostname or IP address" ? Color.RED : Color.GREEN, g);
		circle(StatsUtils.getResponseTime("https://xkcd.com/") == -1 ? Color.RED : Color.GREEN, g);
		circle(StatsUtils.getResponseTime("http://phptest123.byethost24.com/") == -1 ? Color.RED : Color.GREEN, g);
	}
}