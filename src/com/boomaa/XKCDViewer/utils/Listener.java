package com.boomaa.XKCDViewer.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import org.json.JSONException;

import com.boomaa.XKCDViewer.display.Display;

/** <p>Nested ActionListener classes with getters.</p> */
public class Listener {
	public Listener() {}
	
	/** <p>Displays the most recent XKCD image upon actionPerformed().</p> */
	public class LatestSelect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Display.panelRewrite(Display.LATEST_XKCD_NUM);
		}
	}
	
	/** <p>Selects and displays a random XKCD image upon actionPerformed().</p> */
	public class RandomSelect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Display.panelRewrite((int)(Math.random() * Display.LATEST_XKCD_NUM));
		}
	}
	
	/** <p>Navigates to and displays input XKCD imgage upon actionPerformed().</p> */
	public class NumSelect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int numRequest = 0;
			try {
				numRequest = Integer.parseInt(JDEC.TEXT_INPUT.getText());
				if(!JDEC.TEXT_INPUT.getText().isEmpty() && numRequest <= Display.LATEST_XKCD_NUM && numRequest > 0) {
					Display.panelRewrite(numRequest);
				} else {
					Display.resetOnJSONError();
				}
			} catch (NumberFormatException e0) {
				Display.resetOnJSONError();
			}
		}
	}
	
	/** <p>Increments displayed XKCD image upon actionPerformed().</p> */
	public class FwdAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e0) {
			try {
				Display.panelRewrite(Display.DISPLAYED_XKCD_NUM + 1);
			} catch (JSONException e1) {
				Display.resetOnJSONError();
			}
		}
	}
	
	/** <p>Decrements displayed XKCD image upon actionPerformed().</p> */
	public class BackAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e0) {
			try {
				Display.panelRewrite(Display.DISPLAYED_XKCD_NUM - 1);
			} catch (JSONException e1) {
				Display.resetOnJSONError();
			}
		}
	}
	
	/** <p>Saves XKCD image currently displayed upon actionPerformed().</p> */
	public class SaveAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				JSONUtils.saveImage(JSONUtils.readJSONFromUrl("https://xkcd.com/" + Display.DISPLAYED_XKCD_NUM + "/info.0.json"));
			} catch (JSONException | IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
