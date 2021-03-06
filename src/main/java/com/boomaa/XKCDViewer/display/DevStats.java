package com.boomaa.XKCDViewer.display;

import com.boomaa.XKCDViewer.reporting.PackageMap;
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
        
        frame.setSize(450, 375);
        frame.add(mainPanel);
        frame.setVisible(true);
        System.out.println(PackageMap.display.DEV_STATS + "All statistic information displaying correctly");
    }

    /** <p>Initializes JSON from URL.</p> */
    private void JSONInit() {
        try {
            json = DisplayUtils.getJSONFromHTTP("https://xkcd.com/" + MainDisplay.DISPLAYED_XKCD_NUM + "/info.0.json");
            StatsUtils.addTransferredBytes("https://xkcd.com/" + MainDisplay.DISPLAYED_XKCD_NUM + "/info.0.json");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println(PackageMap.display.DEV_STATS + "JSON initialized correctly");
    }

    /** <p>Adds button at bottom of stats frame to close window.</p> */
    public void setupCloseButton() {
        JButton close = new JButton("Close");
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        close.addActionListener(e -> { frame.dispose(); });
        closePanel.add(close);
        mainPanel.add(closePanel);
        System.out.println(PackageMap.display.DEV_STATS + "Close button setup and listened");
    }

    /** <p>Adds each statistic item to the main panel.</p> */
    private void addPanelItems() {
    	statsUtils.addLabelPanel("Image URL: " + json.getAsJsonPrimitive("img").getAsString(), true);
    	statsUtils.addLabelPanel("Image Size: " + statsUtils.byteTranscribe(statsUtils.webResourceSize("img", true)));
        statsUtils.addLabelPanel("JSON URL: " + " https://xkcd.com/" + MainDisplay.DISPLAYED_XKCD_NUM + "/info.0.json", true);
        statsUtils.addLabelPanel("JSON Size: " + statsUtils.byteTranscribe(statsUtils.webResourceSize("https://xkcd.com/" + MainDisplay.DISPLAYED_XKCD_NUM + "/info.0.json", false)));
        statsUtils.addLabelPanel("IP Address: " + StatsUtils.getHostIP());
        statsUtils.addLabelPanel("Data Transferred: " + statsUtils.byteTranscribe(MainDisplay.TRANSFERRED_BYTES));
        statsUtils.addLabelPanel("Memory: " + statsUtils.byteTranscribe(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " (used) / " + 
        		statsUtils.byteTranscribe(Runtime.getRuntime().totalMemory()) + " (allocated) / " + statsUtils.byteTranscribe(Runtime.getRuntime().maxMemory()) + " (max)");
        statsUtils.addLabelPanel("XCKD Response Time:  " + StatsUtils.getResponseTime("https://xkcd.com/") + "ms");
        if(Login.FTP_URL != null) {
        	statsUtils.addLabelPanel("Voting Response Time: " + StatsUtils.getResponseTime(Login.FTP_URL) + "ms");
        }
        System.out.println(PackageMap.display.DEV_STATS + "Statistic item adding finished");
    }
}
