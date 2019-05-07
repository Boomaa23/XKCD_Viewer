package com.boomaa.XKCDViewer.display;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import com.boomaa.XKCDViewer.utils.DisplayUtils;
import com.boomaa.XKCDViewer.threading.ThreadManager;
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
	protected static JComboBox<String> select;
	
	/** <p>Object of utils class.</p> */
	private static StatsUtils statsUtils;
	
	/** <p>Currently displayed image number.</p> */
	protected static int NUM;
	
	/** <p>Storage of all titles for all xkcd.</p> */
	public static String[] titles;
	
	/** <p>Storage of select menu change listener.</p> */
	protected static SelectItemAction item = new SelectItemAction();
	
	/** <p>Progress bar showing number of requests out of total.</p> */
	public static JProgressBar jpb = new JProgressBar();
	
	/** <p>Total number of threads finished.</p> */
	private static int finished = 0;
	
	/**
	 * <p>Constructs stats window.</p>
	 * @param num the number of the image to display.
	 */
	public static void createStatsInspect(int num) {
		SelectList.NUM = num;
		JSONInit();
		jpb.setMaximum(MainDisplay.LATEST_XKCD_NUM);
		statsUtils = new StatsUtils(json, mainPanel, frame);
		
		if(titles == null || titlesEmpty()) {
			new ThreadManager();
			mainPanel.add(jpb);
			JPanel loading = new JPanel(new FlowLayout(FlowLayout.CENTER));
			loading.add(new JLabel("Loading..."));
			mainPanel.add(loading);
		}
		frame.add(mainPanel);
		frame.setVisible(true); 
	} 
	
	/**
	 * <p>Determines if the titles array has any contents.</p>
	 * @return true if no contents are detected, false if not.
	 */
	private static boolean titlesEmpty() {
		boolean empty = true;
		for (Object ob : titles) {
		  if (ob != null) {
		    empty = false;
		    break;
		  }
		}
		return empty;
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
		JButton refresh = new JButton("Refresh");
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		close.addActionListener(new DisposeFrameAction(frame));
		view.addActionListener(new ActionListener() { 
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				MainDisplay.panelRewrite(NUM);
			}
		});
		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainPanel.removeAll();
				mainPanel.add(jpb);
				mainPanel.repaint();
				new ThreadManager();
			}
		});
		
		buttonPanel.add(view);
		buttonPanel.add(close);
		buttonPanel.add(refresh);
		mainPanel.add(buttonPanel);
	}
	
	/**
	 * <p>Updates status of loading bar and checks for finished status.</p>
	 * @param isFinished if all title threads have completed
	 */
	public static void updateBar(boolean isFinished) {
		if(isFinished) { finished++; } else { jpb.setValue(jpb.getValue() + 1); }
		if(finished >= ThreadManager.titleThreads.length && isFinished) {
			jpb.setValue(jpb.getMinimum());
			finished = 0;
			select.removeItemListener(item);
			createStatsInspect(NUM);
			selectPanelInit();
		}
	}
}
