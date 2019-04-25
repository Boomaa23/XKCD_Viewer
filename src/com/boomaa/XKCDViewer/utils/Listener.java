package com.boomaa.XKCDViewer.utils;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONException;

import com.boomaa.XKCDViewer.display.MainFrame;
import com.boomaa.XKCDViewer.display.StatsFrame;

/** <p>Nested ActionListener classes with getters.</p> */
public class Listener {
	public Listener() {}
	
	/** <p>Displays the most recent XKCD image upon actionPerformed().</p> */
	public class LatestSelect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			MainFrame.panelRewrite(MainFrame.LATEST_XKCD_NUM);
		}
	}
	
	/** <p>Selects and displays a random XKCD image upon actionPerformed().</p> */
	public class RandomSelect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			MainFrame.panelRewrite((int)(Math.random() * MainFrame.LATEST_XKCD_NUM));
		}
	}
	
	/** <p>Navigates to and displays input XKCD imgage upon actionPerformed().</p> */
	public class NumSelect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int numRequest = 0;
			try {
				numRequest = Integer.parseInt(JDEC.TEXT_INPUT.getText());
				if(!JDEC.TEXT_INPUT.getText().isEmpty() && numRequest <= MainFrame.LATEST_XKCD_NUM && numRequest > 0) {
					MainFrame.panelRewrite(numRequest);
				} else {
					MainFrame.resetOnJSONError();
				}
			} catch (NumberFormatException e0) {
				MainFrame.resetOnJSONError();
			}
		}
	}
	
	/** <p>Increments displayed XKCD image upon actionPerformed().</p> */
	public class FwdAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e0) {
			try {
				MainFrame.panelRewrite(MainFrame.DISPLAYED_XKCD_NUM + 1);
			} catch (JSONException e1) {
				MainFrame.resetOnJSONError();
			}
		}
	}
	
	/** <p>Decrements displayed XKCD image upon actionPerformed().</p> */
	public class BackAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e0) {
			try {
				MainFrame.panelRewrite(MainFrame.DISPLAYED_XKCD_NUM - 1);
			} catch (JSONException e1) {
				MainFrame.resetOnJSONError();
			}
		}
	}
	
	/** <p>Saves XKCD image currently displayed upon actionPerformed().</p> */
	public class SaveAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				JSONUtils.saveImage(JSONUtils.getJSONFromURL("https://xkcd.com/" + MainFrame.DISPLAYED_XKCD_NUM + "/info.0.json"));
			} catch (JSONException | IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/** <p>Opens XKCD image currently displayed in browser upon actionPerformed().</p> */
	public class OpenAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Desktop.getDesktop().browse(new URI(JSONUtils.getJSONFromURL("https://xkcd.com/" + MainFrame.DISPLAYED_XKCD_NUM + "/info.0.json").getString("img")));
			} catch (IOException | URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/** <p>Rewrites image as scaled or non-scaled upon actionPerformed().</p> */
	public class ScaleSelect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			MainFrame.panelRewrite(MainFrame.DISPLAYED_XKCD_NUM);
		}
	}
	
	/** <p>Opens stats frame 400x150 upon actionPerformed().</p> */
	public class StatsInspect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			new StatsFrame(400, 125);
		}
	}
}
