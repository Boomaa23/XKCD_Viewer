package com.boomaa.XKCDViewer.display;

import java.awt.FlowLayout;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import com.boomaa.XKCDViewer.utils.DisplayUtils;
import com.boomaa.XKCDViewer.utils.Listeners.DisposeFrameAction;
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
		statsUtils = new StatsUtils(json, mainPanel, frame);
		statsUtils.addPanelItems();
		setupCloseButton();
		
		frame.add(mainPanel);
		frame.setVisible(true);
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
		close.addActionListener(new DisposeFrameAction(frame));
		closePanel.add(close);
		mainPanel.add(closePanel);
	}
}
