package com.boomaa.XKCDViewer.utils;

import com.boomaa.XKCDViewer.draw.InetCircles;
import com.boomaa.XKCDViewer.draw.OverlayField;

import javax.swing.*;
import java.awt.*;

/** <p><b>J</b>ava <b>D</b>isplay <b>E</b>lement <b>C</b>onstants.</p> */
public interface JDEC {
    /** <p>JFrame displayed which everything is added to.</p> */
    JFrame FRAME = new JFrame("XKCD Viewer");

    /** <p>Main JPanel which accompanies the same area as the FRAME.</p> */
    JPanel MAIN_PANEL = new JPanel();
    /** <p>Shows currently displayed XKCD number and title at top.</p> */
    JPanel TITLE_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
    /** <p>Shows checkbox for image scaling.</p> */
    JPanel SCALING_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    /** <p>Displays selected XKCD Image.</p> */
    JPanel IMAGE_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
    /** <p>Houses latest and random button </p> */
    JPanel SELECT_PANEL_UPPER = new JPanel(new FlowLayout(FlowLayout.CENTER));
    /** <p>Houses input field, and input selector.</p> */
    JPanel SELECT_PANEL_MIDDLE = new JPanel(new FlowLayout(FlowLayout.CENTER));
    /** <p>Houses forwards and backwards buttons.</p> */
    JPanel SELECT_PANEL_LOWER = new JPanel(new FlowLayout(FlowLayout.CENTER));
    /** <p>Space reserved for potential errors below select panels.</p> */
    JPanel ERROR_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
    /** <p>Space for upvote and downvote buttons.</p> */
    JPanel VOTING_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));

    /** <p>Increments XKCD displayed number.</p> */
    JButton FWD_BTN = new JButton(" > ");
    /** <p>Decrements XKCD displayed number.</p> */
    JButton BACK_BTN = new JButton(" < ");
    /** <p>Sends currently input XKCD number to be displayed.</p> */
    JButton NUM_BTN = new JButton("Go!");
    /** <p>Displays a random XKCD image.</p> */
    JButton RANDOM_BTN = new JButton("Random");
    /** <p>Displays the most recent XKCD image.</p> */
    JButton LATEST_BTN = new JButton("Latest");
    /** <p>Upvotes the displayed XKCD image.</p> */
    JButton UPVOTE_BTN = new JButton("+1");
    /** <p>Downvotes the displayed XKCD image.</p> */
    JButton DOWNVOTE_BTN = new JButton("-1");

    /** <p>Right-click dialog box while over image to save.</p> */
    JPopupMenu IMAGE_POPUP = new JPopupMenu();
    /** <p>Menu item of popup box to save XKCD image.</p> */
    JMenuItem SAVE_IMAGE = new JMenuItem("Save Image");
    /** <p>Menu item of popup box to open image URL in browser.</p> */
    JMenuItem BROWSE_IMAGE = new JMenuItem("Open Image in Browser");
    /** <p>Menu item of popup box to open stats window.</p> */
    JMenuItem DEV_STATS = new JMenuItem("Developer Stats");
    /** <p>Menu item of popup box to open stats window.</p> */
    JMenuItem CONSOLE_OPEN = new JMenuItem("Developer Console");
    /** <p>Menu item of popup box to open multithreaded selection window.</p> */
    JMenuItem SELECT_LIST = new JMenuItem("Image List/Selector");
    /** <p>Menu item of popup box to download multiple xkcd images.</p> **/
    JMenuItem DOWNLOAD = new JMenuItem("Download Images");
    /** <p>Menu item of popup box to open ExplainXKCD explanation.</p> **/
    JMenuItem EXPLAIN = new JMenuItem("ExplainXKCD");
    /** <p>Leaderboard of top voted xkcds.</p> */
    JMenuItem LEADERBOARD = new JMenuItem("Leaderboard");
    /** <p>Opens new FTP authentication window.</p> */
    JMenuItem LOGIN = new JMenuItem("Login");
    
    /** <p>Input for specific number XKCD image to be displayed.</p> */
    OverlayField TEXT_INPUT = new OverlayField(" XKCD # or Name", 10);
    /** <p>A check box to toggle image scaling.</p> */
    JCheckBox SCALE_CHECKBOX = new JCheckBox("Image Scaling");
    /** <p>A check box to toggle text-to-speech.</p> */
    JCheckBox TTS_CHECKBOX = new JCheckBox("TTS");
    /** <p>Pre-drawn circles for internet connectivity.</p> */
    InetCircles INET_CIRCLES = new InetCircles(8);
}
