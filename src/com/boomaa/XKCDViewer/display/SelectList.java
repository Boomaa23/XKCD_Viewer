package com.boomaa.XKCDViewer.display;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import com.boomaa.XKCDViewer.utils.DisplayUtils;
import com.boomaa.XKCDViewer.utils.Listeners.DisposeFrameAction;
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
	private static JComboBox<String> select = new JComboBox<String>();
	
	/** <p>Object of utils class.</p> */
	private static StatsUtils statsUtils;
	
	/** <p>Currently displayed image number.</p> */
	private static int num;
	
	/** <p>Storage of all titles for all xkcd.</p> */
	public static String[] titles = new String[MainDisplay.LATEST_XKCD_NUM+1];
	
	/**
	 * <p>Constructs stats window.</p>
	 * @param num the number of the image to display.
	 */
	public static void createStatsInspect(int num) {
		SelectList.num = num;
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
			json = DisplayUtils.getJSONFromURL("https://xkcd.com/" + num + "/info.0.json");
		} catch (JSONException | IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/** <p>Adds title items from title array to select menu and onto mainPanel.</p> */
	public static void selectPanelInit() {
		for(int i = titles.length-1;i >= 1;i--) {
			select.addItem(titles[i]);
		}
		select.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					frame.dispose();
					String in = (String) e.getItem();
					createStatsInspect(Integer.parseInt(in.substring(0, in.indexOf(" - "))));
					refreshSelector();
				}
			}
		});
		refreshSelector();
	}
	
	/** <p>Refresh display after select num changes.</p> */
	private static void refreshSelector() {
		mainPanel.removeAll();
		mainPanel.add(select);
		JSONInit();
		statsUtils.addPanelItems();
		setupButtons();
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
				MainDisplay.panelRewrite(num);
			}
		});
		
		buttonPanel.add(close);
		buttonPanel.add(view);
		mainPanel.add(buttonPanel);
	}
}
