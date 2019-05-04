package com.boomaa.XKCDViewer.display;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import com.boomaa.XKCDViewer.utils.DisplayUtils;
import com.boomaa.XKCDViewer.utils.ActionListeners.DisposeFrameAction;
import com.boomaa.XKCDViewer.utils.MiscListeners.SelectItemAction;
import com.boomaa.XKCDViewer.utils.StatsUtils;

/** <p>Displays a selection screen of all possible JSON images with stats.</p> */
public class SelectList {
	/** <p>A temporary storage of the currently displayed JSON. </p> */
	private static JSONObject json;
	
	/** <p>Stats display frame.</p> */
	public static JFrame frame = new JFrame("XKCD Image Selector");
	
	/** <p>Main panel of stats display.</p> */
	private static JPanel mainPanel = new JPanel();
	
	/** <p>Display for all image titles.</p> */
	protected static JComboBox<String> select = new JComboBox<String>();
	
	/** <p>Object of utils class.</p> */
	private static StatsUtils statsUtils;
	
	/** <p>Currently displayed image number.</p> */
	protected static int NUM;
	
	/** <p>Storage of all titles for all xkcd.</p> */
	public static String[] titles = new String[MainDisplay.LATEST_XKCD_NUM+1];
	
	/** <p>Storage of select menu change listener.</p> */
	protected static SelectItemAction item = new SelectItemAction();
	
	/**
	 * <p>Constructs stats window.</p>
	 * @param num the number of the image to display.
	 */
	public static void createStatsInspect(int num) {
		SelectList.NUM = num;
		JSONInit();
		statsUtils = new StatsUtils(json, mainPanel, frame);
		statsUtils.addPanelItems();
		setupButtons();
		
		frame.add(mainPanel);
		frame.setVisible(true);
	} 
	
	/** <p>Reads JSON from URL.</p> */
	private static void JSONInit() {
		try {
			json = DisplayUtils.getJSONFromURL("https://xkcd.com/" + NUM + "/info.0.json");
		} catch (JSONException | IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/** <p>Adds title items from title array to select menu and onto mainPanel.</p> */
	public static void selectPanelInit() {
		for(int i = titles.length-1;i >= 1;i--) {
			select.addItem(titles[i]);
		}
		select.setSelectedIndex(titles.length-NUM-1);
		select.addItemListener(item);
		refreshSelector();
	}
	
	/** <p>Refresh display after select num changes.</p> */
	public static void refreshSelector() {
		mainPanel.removeAll();
		mainPanel.add(select);
		JSONInit();
		statsUtils.addPanelItems();
		setupButtons();
		frame.setVisible(true);
	}
	
	/** <p>Adds button at bottom of stats frame to close window.</p> */
	private static void setupButtons() {
		JButton close = new JButton("Close");
		JButton view = new JButton("View");
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		close.addActionListener(new DisposeFrameAction(frame));
		view.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				MainDisplay.panelRewrite(NUM);
			}
		});
		
		buttonPanel.add(close);
		buttonPanel.add(view);
		mainPanel.add(buttonPanel);
	}
}
