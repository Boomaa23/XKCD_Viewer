package com.boomaa.XKCDViewer.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.io.IOException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import com.boomaa.XKCDViewer.utils.MiscListeners.HREFAction;

import net.sf.image4j.codec.ico.ICODecoder;

/** <p>Utilities for the stats inspect and image list panels.</p> */
public class StatsUtils {
	/** <p>Temporary JSON of each window's object.</p> */
	private JSONObject json;
	
	/** <p>Temporary passed mainPanel of each window's object.</p> */
	private JPanel mainPanel;
	
	/** <p>Temporary passed frame of each window's object.</p> */
	private JFrame frame;

	/**
	 * <p>Constructs panel for each window object.</p>
	 * @param json JSON of current image.
	 * @param mainPanel mainPanel from stats inspect/image list.
	 * @param frame the frame from stats inspect/image list.
	 */
	public StatsUtils(JSONObject json, JPanel mainPanel, JFrame frame) {
		this.json = json;
		this.mainPanel = mainPanel;
		this.frame = frame;
		frameInit();
	}
	
	/** <p>Initializes frame and sets size.</p> */
	private void frameInit() {
		try {
			frame.setIconImages(ICODecoder.read(new URL("https://xkcd.com/s/919f27.ico").openStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		frame.setSize(415,230);
	}
	
	/** <p>Adds each statistic item to the main panel.</p> */
	public void addPanelItems() {
		addLabelPanel("Title: " + json.getString("title"));
		addLabelPanel("Image #: " + json.getInt("num"));
		addLabelPanel("Date Published: " + json.getInt("month") + "/" + json.getInt("day") + "/" + json.getInt("year"));
		addLabelPanel("Image URL: " + json.getString("img"), true);
		addLabelPanel("Image Size: " + byteTranscribe(imageSize()));
	}
	
	/**
	 * <p>Overloads String and boolean parameter addLabelPanel as false boolean.</p>
	 * @param text the text to input.
	 */
	private void addLabelPanel(String text) {
		addLabelPanel(text, false);
	}
	
	/**
	 * <p>Makes new nested label in new panel for each panel item.</p>
	 * @param text the text to input.
	 * @param href note if text should become a hyperlink.
	 */
	private void addLabelPanel(String text, boolean href) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		if(href) {
			panel.add(new JLabel(text.substring(0, 11)));
			JLabel label = new JLabel(text.substring(11));
			label.addMouseListener(new HREFAction(text.substring(11)));
			label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			label.setForeground(Color.blue.darker());
			panel.add(label);
		} else {
			panel.add(new JLabel(text));
		}
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
	private String byteTranscribe(long bytes) {
	    if (bytes < 1000) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(1000));
	    return String.format("%.1f %sB", bytes / Math.pow(1000, exp), "kMGTPE".charAt(exp-1));
	}
}
