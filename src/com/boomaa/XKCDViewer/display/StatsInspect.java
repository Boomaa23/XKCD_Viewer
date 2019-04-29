package com.boomaa.XKCDViewer.display;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import com.boomaa.XKCDViewer.utils.DisplayUtils;

/** <p>Displays stats of currently displayed XKCD in new frame.</p> */
public class StatsInspect {
	/** <p>A temporary storage of the currently displayed JSON. </p> */
	private JSONObject json;
	
	/** <p>Stats display frame.</p> */
	private JFrame frame;
	
	/** <p>Main panel of stats display.</p> */
	private JPanel mainPanel;
	
	
	/** <p>Constructs stats window.</p> */
	public StatsInspect() {
		init();
		addPanelItems();
		setupCloseButton();
		
		frame.add(mainPanel);
		frame.setVisible(true);
	} 
	
	/** <p>Initializes JSON, frame, and main panel with sizing and base layout.</p> */
	private void init() {
		try {
			json = DisplayUtils.getJSONFromURL("https://xkcd.com/" + MainDisplay.DISPLAYED_XKCD_NUM + "/info.0.json");
			frame = new JFrame("XKCD Stats | #" + json.getInt("num"));
			frame.setIconImage(ImageIO.read(new File("icon.png")));
			mainPanel = new JPanel();
			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
			frame.setSize(415,205);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/** <p>Adds each statistic item to the main panel.</p> */
	private void addPanelItems() {
		addLabelPanel("Title: " + json.getString("title"));
		addLabelPanel("Image #: " + json.getInt("num"));
		addLabelPanel("Date Published: " + json.getInt("month") + "/" + json.getInt("day") + "/" + json.getInt("year"));
		addLabelPanel("Image URL: " + json.getString("img"));
		addLabelPanel("Image Size: " + byteTranscribe(imageSize()));
	}
	
	/** <p>Adds button at bottom of stats frame to close window.</p> */
	private void setupCloseButton() {
		JButton close = new JButton("Close");
		JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		closePanel.add(close);
		mainPanel.add(closePanel);
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
	private long imageSize() {
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
	public static String byteTranscribe(long bytes) {
	    if (bytes < 1000) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(1000));
	    return String.format("%.1f %sB", bytes / Math.pow(1000, exp), "kMGTPE".charAt(exp-1));
	}
}
