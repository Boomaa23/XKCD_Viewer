package com.boomaa.xkcd_viewer.utils;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

/* Class name stands for (J)ava (D)isplay (E)lement (C)onstants */
public class JDEC {
	public static JFrame FRAME = new JFrame("XKCD Viewer");
	
	public static JPanel MAIN_PANEL = new JPanel();
	public static JPanel TITLE_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public static JPanel IMAGE_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public static JPanel SELECT_PANEL_UPPER = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public static JPanel SELECT_PANEL_LOWER = new JPanel(new FlowLayout(FlowLayout.CENTER));
	public static JPanel ERROR_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	public static JButton FWD_BTN = new JButton(" > ");
	public static JButton RANDOM_BTN = new JButton("Random");
	public static JButton NUM_BTN = new JButton("Go!");
	public static JButton BACK_BTN = new JButton(" < ");
	
	public static JTextField TEXT_INPUT = new JTextField(10);
	public static JPopupMenu IMAGE_POPUP = new JPopupMenu();
	public static JMenuItem SAVE_IMAGE = new JMenuItem("Save Image");
}
