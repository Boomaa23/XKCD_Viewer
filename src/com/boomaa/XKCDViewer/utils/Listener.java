package com.boomaa.XKCDViewer.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import org.json.JSONException;

import com.boomaa.XKCDViewer.display.Display;

public class Listener {
	public static FwdAction fwdAction() { return new FwdAction(); }
	public static BackAction backAction() { return new BackAction(); }
	public static NumSelect numSelect() { return new NumSelect(); }
	public static RandomSelect randomSelect() { return new RandomSelect(); }
	public static SaveAction saveAction() { return new SaveAction(); }
	
	static class FwdAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e0) {
			try {
				Display.panelRewrite(Display.DISPLAYED_XKCD_NUM + 1);
			} catch (JSONException e1) {
				Display.resetOnJSONError();
			}
		}
	}
	
	static class BackAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e0) {
			try {
				Display.panelRewrite(Display.DISPLAYED_XKCD_NUM - 1);
			} catch (JSONException e1) {
				Display.resetOnJSONError();
			}
		}
	}
	
	static class NumSelect implements ActionListener {
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
	
	static class RandomSelect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Display.panelRewrite((int)(Math.random() * Display.LATEST_XKCD_NUM));
		}
	}
	
	static class SaveAction implements ActionListener {
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
