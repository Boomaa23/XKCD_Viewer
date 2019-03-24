package com.boomaa.XKCDViewer.display;

import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.json.JSONException;
import org.json.JSONObject;

import com.boomaa.XKCDViewer.listeners.*;
import com.boomaa.XKCDViewer.utils.*;

import net.sf.image4j.codec.ico.ICODecoder;

public class Display {
	private static final int FRAME_BORDER = 20;
	public static int LATEST_XKCD_NUM = 844;
	public static int DISPLAYED_XKCD_NUM;
	
	public static void main(String[] args) throws JSONException, IOException {
		JSONObject jsonLatest = Utils.readJSONFromUrl("https://xkcd.com/info.0.json");
		Image image = Utils.getImageFromJson(jsonLatest);
		LATEST_XKCD_NUM = jsonLatest.getInt("num");
		
		JDEC.FRAME.setIconImages(ICODecoder.read(new URL("https://xkcd.com/s/919f27.ico").openStream()));
		JDEC.MAIN_PANEL.setLayout(new BoxLayout(JDEC.MAIN_PANEL, BoxLayout.Y_AXIS));
		JDEC.FRAME.getRootPane().setDefaultButton(JDEC.NUM_BTN);
		JDEC.FWD_BTN.setVisible(false);
		setupFrame(image);
		setupTitle(jsonLatest);
		setupScroll();
		
		JDEC.IMAGE_PANEL.add(new JLabel(new ImageIcon(image)));
		JDEC.IMAGE_POPUP.add(JDEC.SAVE_IMAGE);
		addFrameElements();
		addButtonListeners();
		
		JDEC.IMAGE_PANEL.setComponentPopupMenu(JDEC.IMAGE_POPUP);
		JDEC.FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JDEC.FRAME.setVisible(true);
	}
	
	public static void panelRewrite(int numReq) {
		JSONObject json = null;
		try {
			json = Utils.readJSONFromUrl("https://xkcd.com/" + numReq + "/info.0.json");
		} catch (JSONException | IOException e) {
			resetOnJSONError();
		}
		JDEC.TEXT_INPUT.setText("");
		JDEC.TITLE_PANEL.removeAll();
		JDEC.ERROR_PANEL.removeAll();
		setupTitle(json);
		JDEC.FWD_BTN.setVisible(!(DISPLAYED_XKCD_NUM == LATEST_XKCD_NUM));
			
		JDEC.IMAGE_PANEL.removeAll();
		Image imgRand = null;
		try {
			imgRand = Utils.getImageFromJson(json);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
		JDEC.IMAGE_PANEL.add(new JLabel(new ImageIcon(imgRand)));
		
		setupFrame(imgRand);
		JDEC.FRAME.revalidate();
		JDEC.FRAME.repaint();
	}
	
	private static void setupFrame(Image image) {
		int height = image.getHeight(JDEC.FRAME);
		int maxHeight = (int)(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight());
		if(height >= maxHeight) {
			height = maxHeight - (9 * FRAME_BORDER);
		}
		JDEC.FRAME.setSize(image.getWidth(JDEC.FRAME) + 2 * FRAME_BORDER, height + 9 * FRAME_BORDER);
	}
	
	private static void setupTitle(JSONObject json) {
		JDEC.TITLE_PANEL.add(new JLabel(json.getString("title") + " - #" + json.getInt("num")));
		JDEC.FRAME.setTitle("XKCD Viewer | #" + json.getInt("num"));
		DISPLAYED_XKCD_NUM = json.getInt("num");
	}

	private static void setupScroll() {
		JScrollPane scroll = new JScrollPane(JDEC.MAIN_PANEL, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		JDEC.FRAME.add(scroll);
	}

	private static void addFrameElements() {
		JDEC.SELECT_PANEL_UPPER.add(JDEC.RANDOM_BTN);
		JDEC.SELECT_PANEL_UPPER.add(JDEC.TEXT_INPUT);
		JDEC.SELECT_PANEL_UPPER.add(JDEC.NUM_BTN);
		JDEC.SELECT_PANEL_LOWER.add(JDEC.BACK_BTN);
		JDEC.SELECT_PANEL_LOWER.add(JDEC.FWD_BTN);
		
		JDEC.MAIN_PANEL.add(JDEC.TITLE_PANEL);
		JDEC.MAIN_PANEL.add(JDEC.IMAGE_PANEL);
		JDEC.MAIN_PANEL.add(JDEC.SELECT_PANEL_UPPER);
		JDEC.MAIN_PANEL.add(JDEC.SELECT_PANEL_LOWER);
		JDEC.MAIN_PANEL.add(JDEC.ERROR_PANEL);
	}

	private static void addButtonListeners() {
		JDEC.FWD_BTN.addActionListener(new FwdAction());
		JDEC.BACK_BTN.addActionListener(new BackAction());
		JDEC.RANDOM_BTN.addActionListener(new RandomSelect());
		JDEC.NUM_BTN.addActionListener(new NumSelect());
		JDEC.SAVE_IMAGE.addActionListener(new SaveAction());
	}
	
	public static void resetOnJSONError() {
		JDEC.ERROR_PANEL.removeAll();
		JDEC.ERROR_PANEL.add(new JLabel("ERROR: No XKCD found for this number"));
		JDEC.FRAME.revalidate();
		JDEC.FRAME.repaint();
	}
}
