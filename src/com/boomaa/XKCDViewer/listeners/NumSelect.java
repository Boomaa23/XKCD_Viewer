package com.boomaa.XKCDViewer.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.boomaa.XKCDViewer.display.Display;
import com.boomaa.XKCDViewer.utils.JDEC;

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
