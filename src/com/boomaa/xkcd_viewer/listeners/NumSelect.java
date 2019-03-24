package com.boomaa.xkcd_viewer.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.boomaa.xkcd_viewer.display.Display;
import com.boomaa.xkcd_viewer.utils.JDEC;

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
