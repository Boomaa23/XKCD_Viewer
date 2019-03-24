package com.boomaa.xkcd_viewer.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.boomaa.xkcd_viewer.display.Display;

public class BackAction implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		Display.panelRewrite(Display.DISPLAYED_XKCD_NUM - 1);
	}
}
