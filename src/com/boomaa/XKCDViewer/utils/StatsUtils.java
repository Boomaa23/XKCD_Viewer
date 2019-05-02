package com.boomaa.XKCDViewer.utils;

import java.awt.FlowLayout;
import java.io.IOException;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

/** <p>Utilities for the stats inspect and image list panels.</p> */
public class StatsUtils {
	/** <p>Temporary JSON of each window's object.</p> */
	private JSONObject json;
	
	/** <p>Temporary passed mainPanel of each window's object.</p> */
	private JPanel mainPanel;
	
	/**
	 * <p>Constructs panel for each window object.</p>
	 * @param json JSON of current image.
	 * @param mainPanel mainPanel from stats inspect/image list.
	 */
	public StatsUtils(JSONObject json, JPanel mainPanel) {
		this.json = json;
		this.mainPanel = mainPanel;
	}
	
	/** <p>Adds each statistic item to the main panel.</p> */
	public void addPanelItems() {
		addLabelPanel("Title: " + json.getString("title"));
		addLabelPanel("Image #: " + json.getInt("num"));
		addLabelPanel("Date Published: " + json.getInt("month") + "/" + json.getInt("day") + "/" + json.getInt("year"));
		addLabelPanel("Image URL: " + json.getString("img"));
		addLabelPanel("Image Size: " + byteTranscribe(imageSize()));
	}
	
	/**
	 * <p>Makes new nested label in new panel for each panel item.</p>
	 * @param text the text to input.
	 */
	private void addLabelPanel(String text) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(new JLabel(text));
		mainPanel.add(panel);
	}

	/**
	 * <p>Gets size of JSON-accessed image.</p>
	 * @return size of image in bytes.
	 */
	public long imageSize() {
		try {
			return new URL(json.getString("img")).openConnection().getContentLength();
		} catch (JSONException | IOException e) {
			return -1;
		}
	}
	
	/**
	 * <p>Turns bytes into human-readable SI units.</p>
	 * @param bytes number of bytes to transcribe.
	 * @return the bytes passed in as SI units.
	 */
	public String byteTranscribe(long bytes) {
	    if (bytes < 1000) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(1000));
	    return String.format("%.1f %sB", bytes / Math.pow(1000, exp), "kMGTPE".charAt(exp-1));
	}
}