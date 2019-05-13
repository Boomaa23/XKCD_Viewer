package com.boomaa.XKCDViewer.display;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JComponent;

import com.boomaa.XKCDViewer.utils.JDEC;

@SuppressWarnings("serial")
public class DrawCircles extends JComponent {
    	private Color[] colors;
    	private final int diameter;
    	
    	public DrawCircles(final int diameter, final Color... colors) {
    		this.diameter = diameter; 
    		this.colors = colors;
    		setLayout(new FlowLayout(FlowLayout.RIGHT));
		}
    	
    	public void changeStatus(Color... colorChanges) {
    		if(colors.length == colorChanges.length) {
    			for(int i = 0;i < colorChanges.length;i++) {
        			if(colorChanges[i] == null) {
        				colorChanges[i] = colors[i];
        			}
        		}
    			colors = colorChanges;
    		}
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