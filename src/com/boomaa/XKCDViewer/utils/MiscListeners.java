package com.boomaa.XKCDViewer.utils;

import java.awt.Desktop;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.boomaa.XKCDViewer.display.SelectList;

/** <p>All of the non-Action listeners.</p> */
public class MiscListeners {
	/** <p>Opens a link to a webpage in default web browser.</p> */
	public static class HREFAction extends MouseAdapter {
		/** <p>Location of web link.</p> */
		private String href;
		
		/**
		 * <p>Passes in href location.</p>
		 * @param href location of web link, temporary.
		 */
		public HREFAction(String href) {
			this.href = href;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
		    try {
		        Desktop.getDesktop().browse(new URI(href));
		    } catch (IOException | URISyntaxException e1) {
		        e1.printStackTrace();
		    }
		}
	}
	
	/** <p>Changes selector menu after the item selected is changed.</p> */
	public static class SelectItemAction implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(e.getStateChange() == ItemEvent.SELECTED) {
				String in = (String) e.getItem();
				SelectList.createStatsInspect(Integer.parseInt(in.substring(0, in.indexOf(" - "))));
				SelectList.refreshSelector();
			}
		}
	}
}
