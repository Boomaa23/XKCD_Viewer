package com.boomaa.XKCDViewer.draw;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.Arrays;

import javax.swing.JComponent;

import com.boomaa.XKCDViewer.utils.JDEC;
import com.boomaa.XKCDViewer.utils.StatsUtils;

@SuppressWarnings("serial")
public class DrawCircles extends JComponent {
    	private Color[] colors;
    	private final int diameter;
    	
    	public DrawCircles(final int diameter) {
    		this.diameter = diameter; 
    		this.colors = setStatus();
    		setLayout(new FlowLayout(FlowLayout.RIGHT));
		}
    	
    	private Color[] setStatus() {
    		Color[] colors = new Color[3];
    		Arrays.fill(colors, Color.RED);
    		colors[2] = StatsUtils.getHostIP() == "Could not find hostname or IP address" ? Color.RED : Color.GREEN;
    		colors[1] = StatsUtils.getResponseTime("https://xkcd.com/") == -1 ? Color.RED : Color.GREEN;
    		colors[0] = StatsUtils.getResponseTime("http://phptest123.byethost24.com/") == -1 ? Color.RED : Color.GREEN;
    		return colors;
    	}
		
		@Override
		public void paintComponent(Graphics g) {
			int x = JDEC.FRAME.getWidth() - (diameter * colors.length);
			for(Color c : colors) {
				g.setColor(c);
				g.fillOval(x, 0, diameter, diameter);
				x -= diameter * 1.5;
			}
		}
    }