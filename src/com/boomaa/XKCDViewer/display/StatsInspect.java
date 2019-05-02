package com.boomaa.XKCDViewer.display;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import com.boomaa.XKCDViewer.utils.DisplayUtils;
import com.boomaa.XKCDViewer.utils.StatsUtils;

/** <p>Displays stats of currently displayed XKCD in new frame.</p> */
public class StatsInspect {
	/** <p>A temporary storage of the currently displayed JSON. </p> */
	private JSONObject json;
	
	/** <p>Stats display frame.</p> */
	public JFrame frame = new JFrame("XKCD Stats Inspector");
	
	/** <p>Main panel of stats display.</p> */
	private JPanel mainPanel = new JPanel();
	
	/** <p>Object of utils class.</p> */
	private StatsUtils statsUtils;
	
	/** <p>Constructs stats window.</p> */
	public StatsInspect() {
		JSONInit();
		frameInit();
		statsUtils = new StatsUtils(json, mainPanel);
		statsUtils.addPanelItems();
		setupCloseButton();
		
		frame.add(mainPanel);
		frame.setVisible(true);
	} 
	
	/** <p>Initializes frame and sets size.</p> */
	private void frameInit() {
		try {
			frame.setIconImage(ImageIO.read(new File("icon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		frame.setSize(415,220);
	}
	
	/** <p>Initializes JSON from URL.</p> */
	private void JSONInit() {
		try {
			json = DisplayUtils.getJSONFromURL("https://xkcd.com/" + MainDisplay.DISPLAYED_XKCD_NUM + "/info.0.json");
		} catch (JSONException | IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/** <p>Adds button at bottom of stats frame to close window.</p> */
	public void setupCloseButton() {
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
}
