package com.boomaa.XKCDViewer.utils;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

/** <p><b>J</b>ava <b>D</b>isplay <b>E</b>lement <b>C</b>onstants.</p> */
public class JDEC {
	public JDEC() {}
	
	/** <p>JFrame displayed which everything is added to.</p> */
	public static JFrame FRAME = new JFrame("XKCD Viewer");
	
	
	/** <p>Main JPanel which accompanies the same area as the FRAME.</p> */
	public static JPanel MAIN_PANEL = new JPanel();
	
	/** <p>Shows currently displayed XKCD number and title at top.</p> */
	public static JPanel TITLE_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	/** <p>Displays selected XKCD Image.</p> */
	public static JPanel IMAGE_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	/** <p>Houses random button, input field, and input selector.</p> */
	public static JPanel SELECT_PANEL_UPPER = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	/** <p>Houses forwards and backwards buttons.</p> */
	public static JPanel SELECT_PANEL_LOWER = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	/** <p>Space reserved for potential errors below select panels.</p> */
	public static JPanel ERROR_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	
	/** <p>Increments XKCD displayed number.</p> */
	public static JButton FWD_BTN = new JButton(" > ");
	
	/** <p>Decrements XKCD displayed number.</p> */
	public static JButton BACK_BTN = new JButton(" < ");
	
	/** <p>Sends currently input XKCD number to be displayed.</p> */
	public static JButton NUM_BTN = new JButton("Go!");
	
	/** <p>Displays a random XKCD image.</p> */
	public static JButton RANDOM_BTN = new JButton("Random");
	
	
	/** <p>Input for specific number XKCD image to be displayed.</p> */
	public static JTextField TEXT_INPUT = new JTextField(10);
	
	/** <p>Right-click dialog box while over image to save.</p> */
	public static JPopupMenu IMAGE_POPUP = new JPopupMenu();
	
	/** <p>Menu item of popup box to save XKCD image.</p> */
	public static JMenuItem SAVE_IMAGE = new JMenuItem("Save Image");
}
