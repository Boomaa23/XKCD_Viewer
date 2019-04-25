package com.boomaa.XKCDViewer.display;

import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import com.boomaa.XKCDViewer.utils.JSONUtils;

public class StatsFrame {
	private JSONObject json;
	private JFrame frame;
	private JPanel mainPanel;
	
	public StatsFrame(int x, int y) {
		init();
		addPanelItems();
		setupFrame(x, y);
		display();
	} 
	
	private void init() {
		try {
			json = JSONUtils.getJSONFromURL("https://xkcd.com/" + MainFrame.DISPLAYED_XKCD_NUM + "/info.0.json");
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		frame = new JFrame("XKCD Stats | #" + json.getInt("num"));
		mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	}
	
	private void addPanelItems() {
		addLabelPanel("Number: " + json.getInt("num"));
		addLabelPanel("Title: " + json.getString("title"));
		addLabelPanel("Date Published: " + json.getInt("month") + "/" + json.getInt("day") + "/" + json.getInt("year"));
		addLabelPanel("Image URL: " + json.getString("img"));
	}
	
	private void addLabelPanel(String text) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(new JLabel(text));
		mainPanel.add(panel);
	}
	
	private void setupFrame(int x, int y) {
		frame.setSize(x, y);
	}
	
	private void display() {
		frame.add(mainPanel);
		frame.setVisible(true);
	}
}
