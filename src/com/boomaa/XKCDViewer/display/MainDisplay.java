package com.boomaa.XKCDViewer.display;

import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.json.JSONException;
import org.json.JSONObject;

import com.boomaa.XKCDViewer.utils.*;

/** <p>Instigates display and houses main method</p> */
public class MainDisplay extends ActionListeners implements JDEC {
	public MainDisplay() {}
	
	/** <p>The number of pixels of the frame border.</p> */
	public static final int FRAME_BORDER = 20;
	
	/** <p>The number of the latest XKCD comic number.</p> */
	public static int LATEST_XKCD_NUM = 844;
	
	/** <p>The number of the currently displayed XKCD comic.</p> */
	public static int DISPLAYED_XKCD_NUM;

	/**
	 * <p>Displays JFrame with everything added to it. Main running method.</p>
	 * @param args default main method.
	 * @throws JSONException if the latest XKCD JSON cannot be read.
	 * @throws IOException if the URL of the latest XKCD JSON is invalid.
	 */
	public static void main(String[] args) throws JSONException, IOException {
		SCALE_CHECKBOX.setSelected(true);
		JSONObject jsonLatest = DisplayUtils.getJSONFromURL("https://xkcd.com/info.0.json");
		Image image = DisplayUtils.getImageFromJSON(jsonLatest);
		LATEST_XKCD_NUM = jsonLatest.getInt("num");
		
		FRAME.setIconImage(ImageIO.read(new File("icon.png")));
		MAIN_PANEL.setLayout(new BoxLayout(MAIN_PANEL, BoxLayout.Y_AXIS));
		FRAME.getRootPane().setDefaultButton(NUM_BTN);
		FWD_BTN.setVisible(false);
		setupFrame(image);
		setupTitle(jsonLatest);
		setupScroll();
		
		IMAGE_PANEL.add(new JLabel(new ImageIcon(image)));
		IMAGE_POPUP.add(SAVE_IMAGE);
		IMAGE_POPUP.add(BROWSE_IMAGE);
		IMAGE_POPUP.add(STATS_INSPECT);
		IMAGE_POPUP.add(THREADED_SELECT);
		addFrameElements();
		addButtonListeners();
		
		IMAGE_PANEL.setComponentPopupMenu(IMAGE_POPUP);
		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FRAME.setVisible(true);
	}
	
	/**
	 * <p>Reformats main panel and frame with new XKCD image from JSON.</p>
	 * @param numReq the XKCD image number requested.
	 */
	public static void panelRewrite(int numReq) {
		JSONObject json = null;
		try {
			json = DisplayUtils.getJSONFromURL("https://xkcd.com/" + numReq + "/info.0.json");
		} catch (JSONException | IOException e) {
			resetOnJSONError();
		}
		TEXT_INPUT.reset();
		TITLE_PANEL.removeAll();
		ERROR_PANEL.removeAll();
		setupTitle(json);
		FWD_BTN.setVisible(!(DISPLAYED_XKCD_NUM == LATEST_XKCD_NUM));
		
		IMAGE_PANEL.removeAll();
		Image imgRand = null;
		try {
			imgRand = DisplayUtils.getImageFromJSON(json);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		IMAGE_PANEL.add(new JLabel(new ImageIcon(imgRand)));

		setupFrame(imgRand);
		FRAME.revalidate();
		FRAME.repaint();
	}
	
	/**
	 * <p>Sizes frame around XKCD image.</p>
	 * @param image the image upon which sizing is based.
	 */
	private static void setupFrame(Image image) {
		int minWidth = 300;
		int width = image.getWidth(FRAME) + 2 * FRAME_BORDER;
		int height = image.getHeight(FRAME);
		int maxHeight = (int)(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight());
		
		height += 12 * FRAME_BORDER;
		height = (height >= maxHeight) ? maxHeight : height;
		width = (width < minWidth) ? minWidth : width;
		
		FRAME.setSize(width, height);
	}
	
	/**
	 * <p>Sets window title with currently displayed XKCD number.</p>
	 * @param json the XKCD JSON to pull the displayed number from.
	 */
	private static void setupTitle(JSONObject json) {
		TITLE_PANEL.add(new JLabel(json.getString("title") + " - #" + json.getInt("num")));
		FRAME.setTitle("XKCD Viewer | #" + json.getInt("num"));
		DISPLAYED_XKCD_NUM = json.getInt("num");
	}

	/** <p>Determines if a vertical scrollbar is needed and displays if so.</p> */
	private static void setupScroll() {
		JScrollPane scroll = new JScrollPane(MAIN_PANEL, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		FRAME.add(scroll);
	}

	/** <p>Adds individual items to Select and Main JPanels.</p> */
	private static void addFrameElements() {
		SCALING_PANEL.add(SCALE_CHECKBOX);
		
		SELECT_PANEL_UPPER.add(LATEST_BTN);
		SELECT_PANEL_UPPER.add(RANDOM_BTN);
		SELECT_PANEL_MIDDLE.add(TEXT_INPUT);
		SELECT_PANEL_MIDDLE.add(NUM_BTN);
		SELECT_PANEL_LOWER.add(BACK_BTN);
		SELECT_PANEL_LOWER.add(FWD_BTN);
		
		MAIN_PANEL.add(TITLE_PANEL);
		MAIN_PANEL.add(IMAGE_PANEL);
		MAIN_PANEL.add(SCALING_PANEL);
		MAIN_PANEL.add(SELECT_PANEL_UPPER);
		MAIN_PANEL.add(SELECT_PANEL_MIDDLE);
		MAIN_PANEL.add(SELECT_PANEL_LOWER);
		MAIN_PANEL.add(ERROR_PANEL);
	}

	/** <p>Adds ActionListeners on buttons.</p> */
	private static void addButtonListeners() {
		LATEST_BTN.addActionListener(new LatestSelect());
		RANDOM_BTN.addActionListener(new RandomSelect());
		NUM_BTN.addActionListener(new NumSelect());
		FWD_BTN.addActionListener(new FwdAction());
		BACK_BTN.addActionListener(new BackAction());
		SAVE_IMAGE.addActionListener(new SaveAction());
		BROWSE_IMAGE.addActionListener(new BrowseAction());
		STATS_INSPECT.addActionListener(new StatsInspectCreate());
		THREADED_SELECT.addActionListener(new ThreadSelectorAction());
		SCALE_CHECKBOX.addActionListener(new ScaleSelect());
	}
	
	/** <p>Error message display for JSON retrieval error.</p> */
	public static void resetOnJSONError() {
		ERROR_PANEL.removeAll();
		ERROR_PANEL.add(new JLabel("ERROR: No XKCD found for this number"));
		FRAME.revalidate();
		FRAME.repaint();
	}
}
