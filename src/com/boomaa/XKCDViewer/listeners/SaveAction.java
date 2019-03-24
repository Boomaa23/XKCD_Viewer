package com.boomaa.XKCDViewer.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import org.json.JSONException;

import com.boomaa.XKCDViewer.display.Display;
import com.boomaa.XKCDViewer.utils.Utils;

public class SaveAction implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Utils.saveImage(Utils.readJSONFromUrl("https://xkcd.com/" + Display.DISPLAYED_XKCD_NUM + "/info.0.json"));
		} catch (JSONException | IOException e1) {
			e1.printStackTrace();
		}
	}
}
