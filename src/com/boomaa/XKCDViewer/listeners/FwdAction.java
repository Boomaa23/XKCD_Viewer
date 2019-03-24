package com.boomaa.XKCDViewer.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.json.JSONException;

import com.boomaa.XKCDViewer.display.Display;

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
