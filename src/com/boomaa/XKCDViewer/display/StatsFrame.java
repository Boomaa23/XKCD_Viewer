package com.boomaa.XKCDViewer.display;

import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import com.boomaa.XKCDViewer.utils.JSONUtils;

/** <p>A separate JFrame to dipsplay image statistics.</p> */
public class StatsFrame {
	/** <p>JSON of currently displayed image.</p> */
	private JSONObject json;
	
	/** <p>Stats frame.</p> */
	private JFrame frame;
	
	/** <p>Main panel of stats frame.</p> */
	private JPanel mainPanel;
	
	
	/**
	 * <p>Constructor to create and display a stats frame.</p>
	 * @param x width of frame
	 * @param y height of frame
	 */
	public StatsFrame(int x, int y) {
		init();
		addPanelItems();
		frame.setSize(x, y);
		display();
	} 
	
	/** </p>Gets JSON from current URL and creates frame structure.</p> */
	private void init() {
		try {
			json = JSONUtils.getJSONFromURL("https://xkcd.com/" + MainFrame.DISPLAYED_XKCD_NUM + "/info.0.json");
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		frame = new JFrame("XKCD Stats | #" + json.getInt("num"));
		mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	}
	
	/** <p>Add all stats items to panels.</p> */
	private void addPanelItems() {
		addLabelPanel("Number: " + json.getInt("num"));
		addLabelPanel("Title: " + json.getString("title"));
		addLabelPanel("Date Published: " + json.getInt("month") + "/" + json.getInt("day") + "/" + json.getInt("year"));
		addLabelPanel("Image URL: " + json.getString("img"));
	}
	
	/**
	 * <p>Adds one stat item to a new panel on the main panel.</p>
	 * @param text what to display on the JLabel
	 */
	private void addLabelPanel(String text) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(new JLabel(text));
		mainPanel.add(panel);
	}
	
	/** <p>Displays mainPanel on frame and sets visible.</p> */
	private void display() {
		frame.add(mainPanel);
		frame.setVisible(true);
	}
}
