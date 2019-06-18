package com.boomaa.XKCDViewer.display;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.boomaa.XKCDViewer.utils.DisplayUtils;

@SuppressWarnings("serial")
/** <p>A downloader to download a bunch of xkcd images.</p> */
public class Download extends JFrame {
	/** <p>Creates all items on frame.</p> */
	public Download() {
		this.setSize(300, 300);
		JPanel header = new JPanel();
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel title = new JLabel("Download Images");
		JTextArea nums = new JTextArea(10,20);
	
		mainPanel.add(nums);
		titlePanel.add(title);
		header.add(titlePanel);
		header.add(mainPanel);
		
		header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
		JButton downloadButton = new JButton("Download");
		downloadButton.addActionListener(e -> { imageDownloadHandler(nums.getText()); });
		
		this.getContentPane().add(header, BorderLayout.PAGE_START);
		this.getContentPane().add(downloadButton, BorderLayout.PAGE_END);
		this.setVisible(true);
	}
	
	/**
	 * <p>Instigates download of each image w/ array handling/processing.</p>
	 * @param downloadStrs the CSV formatted string of images to download.
	 */
	private void imageDownloadHandler(String downloadStrs) {
		String[] toDownload = downloadStrs.split(", ");
		for(String download : toDownload) {
			try {
				DisplayUtils.saveImage(DisplayUtils.getJSONFromHTTP("https://xkcd.com/" + download.trim() + "/info.0.json"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
