package com.boomaa.XKCDViewer.draw;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JComponent;

import com.boomaa.XKCDViewer.utils.JDEC;
import com.boomaa.XKCDViewer.utils.StatsUtils;

@SuppressWarnings("serial")
public class InetCircles extends JComponent {
    	private final int diameter;
    	private int spacing;
    	
    	public InetCircles(final int diameter) {
    		this.diameter = diameter;
    		this.spacing = JDEC.FRAME.getWidth() - (diameter * 3);
    		setLayout(new FlowLayout(FlowLayout.RIGHT));
		}
    	
    	private void draw(Color c, Graphics g) {
    		g.setColor(c);
    		g.fillOval(spacing, 0, diameter, diameter);
    		spacing -= diameter * 1.5;
    	}
		
		@Override
		public void paintComponent(Graphics g) {
			draw(StatsUtils.getHostIP() == "Could not find hostname or IP address" ? Color.RED : Color.GREEN, g);
			draw(StatsUtils.getResponseTime("https://xkcd.com/") == -1 ? Color.RED : Color.GREEN, g);
			draw(StatsUtils.getResponseTime("http://phptest123.byethost24.com/") == -1 ? Color.RED : Color.GREEN, g);
		}
    }