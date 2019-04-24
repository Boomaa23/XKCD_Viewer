package com.boomaa.XKCDViewer.utils;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/** <p><b>J</b>ava <b>D</b>isplay <b>E</b>lement <b>C</b>onstants.</p> */
public class JDEC {
	public JDEC() {}
	
	/** <p>JFrame displayed which everything is added to.</p> */
	protected static JFrame FRAME = new JFrame("XKCD Viewer");
	
	
	/** <p>Main JPanel which accompanies the same area as the FRAME.</p> */
	protected static JPanel MAIN_PANEL = new JPanel();
	
	/** <p>Shows currently displayed XKCD number and title at top.</p> */
	protected static JPanel TITLE_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	/** <p>Shows checkbox for image scaling.</p> */
	protected static JPanel SCALING_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
	
	/** <p>Displays selected XKCD Image.</p> */
	protected static JPanel IMAGE_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	/** <p>Houses latest and random button </p> */
	protected static JPanel SELECT_PANEL_UPPER = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	/** <p>Houses input field, and input selector.</p> */
	protected static JPanel SELECT_PANEL_MIDDLE = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	/** <p>Houses forwards and backwards buttons.</p> */
	protected static JPanel SELECT_PANEL_LOWER = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	/** <p>Space reserved for potential errors below select panels.</p> */
	protected static JPanel ERROR_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	
	/** <p>Increments XKCD displayed number.</p> */
	protected static JButton FWD_BTN = new JButton(" > ");
	
	/** <p>Decrements XKCD displayed number.</p> */
	protected static JButton BACK_BTN = new JButton(" < ");
	
	/** <p>Sends currently input XKCD number to be displayed.</p> */
	protected static JButton NUM_BTN = new JButton("Go!");
	
	/** <p>Displays a random XKCD image.</p> */
	protected static JButton RANDOM_BTN = new JButton("Random");
	
	/** <p>Displays the most recent XKCD image.</p> */
	protected static JButton LATEST_BTN = new JButton("Latest");
	
	
	/** <p>Input for specific number XKCD image to be displayed.</p> */
	protected static HintTextField TEXT_INPUT = new HintTextField(" XKCD Number", 10);
	
	/** <p>A check box to toggle image scaling.</p> */
	protected static JCheckBox SCALE_CHECKBOX = new JCheckBox("Image Scaling");
	
	/** <p>Right-click dialog box while over image to save.</p> */
	protected static JPopupMenu IMAGE_POPUP = new JPopupMenu();
	
	/** <p>Menu item of popup box to save XKCD image.</p> */
	protected static JMenuItem SAVE_IMAGE = new JMenuItem("Save Image");
	
	/** <p>Menu item of popup box to open image URL in browser.</p> */
	protected static JMenuItem OPEN_IMAGE = new JMenuItem("Open Image in Browser");
}
