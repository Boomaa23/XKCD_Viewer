package com.boomaa.XKCDViewer.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.boomaa.XKCDViewer.display.Display;

public class RandomSelect implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		Display.panelRewrite((int)(Math.random() * Display.LATEST_XKCD_NUM));
	}
}
