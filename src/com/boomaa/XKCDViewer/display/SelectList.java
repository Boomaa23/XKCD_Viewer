package com.boomaa.XKCDViewer.display;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import com.boomaa.XKCDViewer.utils.DisplayUtils;
import com.boomaa.XKCDViewer.utils.StatsUtils;

/** <p>Displays stats of currently displayed XKCD in new frame.</p> */
public class SelectList {
	/** <p>A temporary storage of the currently displayed JSON. </p> */
	private static JSONObject json;
	
	/** <p>Stats display frame.</p> */
	public static JFrame frame = new JFrame("XKCD Image Selector");
	
	/** <p>Main panel of stats display.</p> */
	private static JPanel mainPanel = new JPanel();
	
	/** <p></p> */
	private static JComboBox<String> select = new JComboBox<String>();
	
	/** <p></p> */
	private static StatsUtils statsUtils;
	
	/** <p></p> */
	private static int num;
	
	/** <p></p> */
	public static String[] titles;
	
	/** <p>Constructs stats window.</p> */
	public static void createStatsInspect(int num) {
		titles = new String[MainDisplay.LATEST_XKCD_NUM+1];
		SelectList.num = num;
		JSONInit();
		frameInit();
		statsUtils = new StatsUtils(json, mainPanel);
		statsUtils.addPanelItems();
		setupButtons();
		
		frame.add(mainPanel);
		frame.setVisible(true);
	} 
	
	/** <p></p> */
	private static void frameInit() {
		try {
			frame.setIconImage(ImageIO.read(new File("icon.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		frame.setSize(415,230);
	}
	
	/** <p></p> */
	private static void JSONInit() {
		try {
			json = DisplayUtils.getJSONFromURL("https://xkcd.com/" + num + "/info.0.json");
		} catch (JSONException | IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/** <p></p> */
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
					mainPanel.removeAll();
					mainPanel.add(select);
					JSONInit();
					statsUtils.addPanelItems();
					setupButtons();
				}
			}
		});
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
		
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
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
