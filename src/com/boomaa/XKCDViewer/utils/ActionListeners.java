package com.boomaa.XKCDViewer.utils;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;

import org.json.JSONException;

import com.boomaa.XKCDViewer.display.MainDisplay;
import com.boomaa.XKCDViewer.display.SelectList;
import com.boomaa.XKCDViewer.display.StatsInspect;

/** <p>Nested ActionListener classes.</p> */
public class ActionListeners {
	/** <p>Displays the most recent XKCD image upon actionPerformed().</p> */
	public static class LatestSelect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			MainDisplay.panelRewrite(MainDisplay.LATEST_XKCD_NUM);
		}
	}
	
	/** <p>Selects and displays a random XKCD image upon actionPerformed().</p> */
	public static class RandomSelect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			MainDisplay.panelRewrite((int)(Math.random() * MainDisplay.LATEST_XKCD_NUM));
		}
	}
	
	/** <p>Navigates to and displays input XKCD imgage upon actionPerformed().</p> */
	public static class NumSelect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int numRequest = 0;
			try {
				numRequest = Integer.parseInt(JDEC.TEXT_INPUT.getText());
				if(!JDEC.TEXT_INPUT.getText().isEmpty() && numRequest <= MainDisplay.LATEST_XKCD_NUM && numRequest > 0) {
					MainDisplay.panelRewrite(numRequest);
				} else {
					MainDisplay.resetOnJSONError();
				}
			} catch (NumberFormatException e0) {
				MainDisplay.resetOnJSONError();
			}
		}
	}
	
	/** <p>Increments displayed XKCD image upon actionPerformed().</p> */
	public static class FwdAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e0) {
			try {
				MainDisplay.panelRewrite(MainDisplay.DISPLAYED_XKCD_NUM + 1);
			} catch (JSONException e1) {
				MainDisplay.resetOnJSONError();
			}
		}
	}
	
	/** <p>Decrements displayed XKCD image upon actionPerformed().</p> */
	public static class BackAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e0) {
			try {
				MainDisplay.panelRewrite(MainDisplay.DISPLAYED_XKCD_NUM - 1);
			} catch (JSONException e1) {
				MainDisplay.resetOnJSONError();
			}
		}
	}
	
	/** <p>Saves XKCD image currently displayed upon actionPerformed().</p> */
	public static class SaveAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				DisplayUtils.saveImage(DisplayUtils.getJSONFromURL("https://xkcd.com/" + MainDisplay.DISPLAYED_XKCD_NUM + "/info.0.json"));
			} catch (JSONException | IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/** <p>Opens XKCD image currently displayed in browser upon actionPerformed().</p> */
	public static class BrowseAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Desktop.getDesktop().browse(new URI(DisplayUtils.getJSONFromURL("https://xkcd.com/" + MainDisplay.DISPLAYED_XKCD_NUM + "/info.0.json").getString("img")));
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/** <p>Rewrites image as scaled or non-scaled upon actionPerformed().</p> */
	public static class ScaleSelect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			MainDisplay.panelRewrite(MainDisplay.DISPLAYED_XKCD_NUM);
		}
	}
	
	/** <p>Opens select window and initiates multithreaded title requesting.</p> */
	public static class ThreadSelectorAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			SelectList.createStatsInspect(MainDisplay.DISPLAYED_XKCD_NUM);
		}
	}
	
	/** <p>Opens stats window of currently displayed xkcd image.</p> */
	public static class StatsInspectCreate implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			new StatsInspect();
		}
	}
	
	/** <p>Discards frame upon actionPerformed().</p> */
	public static class DisposeFrameAction implements ActionListener {
		/** <p>Storage of passed in JFrame.</p> */
		private JFrame frame;
		
		/**
		 * <p>Passes in frame to dispose of.</p>
		 * @param frame the frame to de disposed of.
		 */
		public DisposeFrameAction(JFrame frame) {
			this.frame = frame;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
		}
	}
}
