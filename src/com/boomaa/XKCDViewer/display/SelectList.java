package com.boomaa.XKCDViewer.display;

import com.boomaa.XKCDViewer.threading.ThreadManager;
import com.boomaa.XKCDViewer.utils.DisplayUtils;
import com.boomaa.XKCDViewer.utils.Listeners.SelectItemAction;
import com.boomaa.XKCDViewer.utils.StatsUtils;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/** <p>Displays a selection screen of all possible JSON images with stats.</p> */
public class SelectList {
    /** <p>Stats display frame.</p> */
    private static JFrame frame = new JFrame("XKCD Image Selector");
    /** <p>Storage of all titles for all xkcd.</p> */
    public static String[] TITLES;
    /** <p>Progress bar showing number of requests out of total.</p> */
    private static JProgressBar jpb = new JProgressBar();
    /** <p>Display for all image titles.</p> */
    protected static JComboBox<String> select;
    /** <p>Currently displayed image number.</p> */
    private static int num;
    /** <p>Storage of select menu change listener.</p> */
    private static SelectItemAction item = new SelectItemAction();
    /** <p>A temporary storage of the currently displayed JSON. </p> */
    private static JsonObject json;
    /** <p>Main panel of stats display.</p> */
    private static JPanel mainPanel = new JPanel();
    /** <p>Object of utils class.</p> */
    private static StatsUtils statsUtils;
    /** <p>Total number of threads finished.</p> */
    private static int finished = 0;

    /**
     * <p>Constructs stats window.</p>
     * @param num the number of the image to display.
     */
    public static void createSelectList(int num) {
        SelectList.num = num;
        JSONInit();
        jpb.setMaximum(MainDisplay.LATEST_XKCD_NUM);
        statsUtils = new StatsUtils(json, mainPanel, frame);

        if (TITLES == null || titlesEmpty()) {
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
        for (Object ob : TITLES) {
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
            json = DisplayUtils.getJSONFromHTTP("https://xkcd.com/" + num + "/info.0.json");
            StatsUtils.addTransferredBytes("https://xkcd.com/" + num + "/info.0.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** <p>Adds title items from title array to select menu and onto mainPanel.</p> */
    public static void selectPanelInit() {
        for(int i = TITLES.length - 1; i >= 1; i--) {
            select.addItem(i + " - " + TITLES[i]);
        }
        select.setSelectedIndex(TITLES.length - num - 1);
        select.addItemListener(item);
        refreshSelector();
    }

    /** <p>Refresh display after select num changes.</p> */
    public static void refreshSelector() {
        mainPanel.removeAll();
        mainPanel.add(select);
        JSONInit();
        statsUtils.addGenericPanelItems();
        setupButtons();
        frame.setVisible(true);
    }
    
    /** <p>Adds button at bottom of stats frame to close window.</p> */
    private static void setupButtons() {
        JButton close = new JButton("Close");
        JButton view = new JButton("View");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        close.addActionListener(e -> { frame.dispose(); });
        view.addActionListener(e -> {
            frame.dispose();
            MainDisplay.panelRewrite(num);
        });

        buttonPanel.add(view);
        buttonPanel.add(close);
        mainPanel.add(buttonPanel);
    }

    /**
     * <p>Updates status of loading bar and checks for finished status.</p>
     * @param isFinished if all title threads have completed
     */
    public static void updateBar(boolean isFinished) {
        if (isFinished) {
            finished++;
        } else {
            jpb.setValue(jpb.getValue() + 1);
        }
        if (finished >= ThreadManager.TITLE_THREADS.length && isFinished) {
            jpb.setValue(jpb.getMinimum());
            finished = 0;
            select.removeItemListener(item);
            createSelectList(num);
            selectPanelInit();
        }
    }
}
