package com.boomaa.xkcd_viewer.display;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.json.JSONException;
import org.json.JSONObject;

import com.boomaa.xkcd_viewer.listeners.*;
import com.boomaa.xkcd_viewer.utils.Utils;

import net.sf.image4j.codec.ico.ICODecoder;

public class Display {
	private static final int FRAME_BORDER = 20;
	public static int LATEST_XKCD_NUM = 844;
	public static int DISPLAYED_XKCD_NUM;
	
	public static JFrame FRAME = new JFrame("XKCD Viewer");
	private static JPanel MAIN_PANEL = new JPanel();
	private static JPanel TITLE_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private static JPanel IMAGE_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private static JPanel SELECT_PANEL_UPPER = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private static JPanel SELECT_PANEL_LOWER = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private static JPanel ERROR_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public static JTextField TEXT_INPUT = new JTextField(10);
	private static JButton FWD_BTN = new JButton(" > ");
	
	public static void main(String[] args) throws JSONException, IOException {
		FRAME.setIconImages(ICODecoder.read(new URL("https://xkcd.com/s/919f27.ico").openStream()));
		
		JSONObject jsonLatest = Utils.readJSONFromUrl("https://xkcd.com/info.0.json");
		Image image = Utils.getImageFromJson(jsonLatest);
		LATEST_XKCD_NUM = jsonLatest.getInt("num");
		
		JButton randomBtn = new JButton("Random");
		JButton numBtn = new JButton("Go!");
		JButton backBtn = new JButton(" < ");
		
		JPopupMenu imagePopup = new JPopupMenu();
		JMenuItem saveImage = new JMenuItem("Save Image");
		
		MAIN_PANEL.setLayout(new BoxLayout(MAIN_PANEL, BoxLayout.Y_AXIS));
		setupFrame(image);
		setupTitle(jsonLatest);
		FWD_BTN.setVisible(false);
		
		JScrollPane scroll = new JScrollPane(MAIN_PANEL, 
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		FRAME.add(scroll);
		FRAME.getRootPane().setDefaultButton(numBtn);
		
		IMAGE_PANEL.add(new JLabel(new ImageIcon(image)));
		SELECT_PANEL_UPPER.add(randomBtn);
		SELECT_PANEL_UPPER.add(TEXT_INPUT);
		SELECT_PANEL_UPPER.add(numBtn);
		SELECT_PANEL_LOWER.add(backBtn);
		SELECT_PANEL_LOWER.add(FWD_BTN);
		imagePopup.add(saveImage);
		
		FWD_BTN.addActionListener(new FwdAction());
		backBtn.addActionListener(new BackAction());
		randomBtn.addActionListener(new RandomSelect());
		numBtn.addActionListener(new NumSelect());
		saveImage.addActionListener(new SaveAction());
		
		MAIN_PANEL.add(TITLE_PANEL);
		MAIN_PANEL.add(IMAGE_PANEL);
		MAIN_PANEL.add(SELECT_PANEL_UPPER);
		MAIN_PANEL.add(SELECT_PANEL_LOWER);
		MAIN_PANEL.add(ERROR_PANEL);

		IMAGE_PANEL.setComponentPopupMenu(imagePopup);
		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FRAME.setVisible(true);
	}
	
	public static void panelRewrite(int numReq) {
		JSONObject json = null;
		try {
			json = Utils.readJSONFromUrl("https://xkcd.com/" + numReq + "/info.0.json");
		} catch (JSONException | IOException e2) {
			e2.printStackTrace();
		}
		TEXT_INPUT.setText("");
		TITLE_PANEL.removeAll();
		ERROR_PANEL.removeAll();
		setupTitle(json);
		FWD_BTN.setVisible(!(DISPLAYED_XKCD_NUM == LATEST_XKCD_NUM));
			
		IMAGE_PANEL.removeAll();
		Image imgRand = null;
		try {
			imgRand = Utils.getImageFromJson(json);
		} catch (JSONException | IOException e2) {
			e2.printStackTrace();
		}
		IMAGE_PANEL.add(new JLabel(new ImageIcon(imgRand)));
		
		setupFrame(imgRand);
		FRAME.revalidate();
		FRAME.repaint();
	}

	private static void setupFrame(Image image) {
		int height = image.getHeight(FRAME);
		int maxHeight = (int)(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight());
		if(height >= maxHeight) {
			height = maxHeight - (9 * FRAME_BORDER);
		}
		FRAME.setSize(image.getWidth(FRAME) + 2 * FRAME_BORDER, height + 9 * FRAME_BORDER);
	}
	
	private static void setupTitle(JSONObject json) {
		TITLE_PANEL.add(new JLabel(json.getString("title") + " - #" + json.getInt("num")));
		FRAME.setTitle("XKCD Viewer | #" + json.getInt("num"));
		DISPLAYED_XKCD_NUM = json.getInt("num");
	}
	
	public static void resetOnJSONError() {
		ERROR_PANEL.removeAll();
		ERROR_PANEL.add(new JLabel("ERROR: No XKCD found for this number"));
		FRAME.revalidate();
		FRAME.repaint();
	}
}
