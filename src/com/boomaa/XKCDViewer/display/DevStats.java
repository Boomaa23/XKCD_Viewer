package com.boomaa.XKCDViewer.display;

import com.boomaa.XKCDViewer.utils.DisplayUtils;
import com.boomaa.XKCDViewer.utils.StatsUtils;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/** <p>Displays stats of currently displayed XKCD in new frame.</p> */
public class DevStats {
    /** <p>Stats display frame.</p> */
	private JFrame frame = new JFrame("XKCD Developer Statistics");
    /** <p>A temporary storage of the currently displayed JSON. </p> */
    private JsonObject json;
    /** <p>Main panel of stats display.</p> */
    private JPanel mainPanel = new JPanel();
    /** <p>Object of utils class.</p> */
    private StatsUtils statsUtils;

    /** <p>Constructs stats window.</p> */
    public DevStats() {
        JSONInit();
        statsUtils = new StatsUtils(json, mainPanel, frame);
        addPanelItems();
        setupCloseButton();
        
        frame.setSize(450, 350);
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    /** <p>Initializes JSON from URL.</p> */
    private void JSONInit() {
        try {
            json = DisplayUtils.getJSONFromHTTP("https://xkcd.com/" + MainDisplay.DISPLAYED_XKCD_NUM + "/info.0.json");
            StatsUtils.addTransferredBytes("https://xkcd.com/" + MainDisplay.DISPLAYED_XKCD_NUM + "/info.0.json");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /** <p>Adds button at bottom of stats frame to close window.</p> */
    public void setupCloseButton() {
        JButton close = new JButton("Close");
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        close.addActionListener(e -> { frame.dispose(); });
        closePanel.add(close);
        mainPanel.add(closePanel);
    }

    /** <p>Adds each statistic item to the main panel.</p> */
    private void addPanelItems() {
        statsUtils.addGenericPanelItems();
        statsUtils.addLabelPanel("JSON URL: " + " https://xkcd.com/" + MainDisplay.DISPLAYED_XKCD_NUM + "/info.0.json", true);
        statsUtils.addLabelPanel("JSON Size: " + statsUtils.byteTranscribe(statsUtils.webResourceSize("https://xkcd.com/" + MainDisplay.DISPLAYED_XKCD_NUM + "/info.0.json", false)));
        statsUtils.addLabelPanel("IP Address: " + StatsUtils.getHostIP());
        statsUtils.addLabelPanel("Data Transferred: " + statsUtils.byteTranscribe(MainDisplay.TRANSFERRED_BYTES));
        statsUtils.addLabelPanel("Response Time:  " + StatsUtils.getResponseTime("https://xkcd.com/") + "ms");
    }
}
