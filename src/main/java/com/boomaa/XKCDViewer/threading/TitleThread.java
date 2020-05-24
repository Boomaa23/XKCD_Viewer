package com.boomaa.XKCDViewer.threading;

import com.boomaa.XKCDViewer.display.MainDisplay;
import com.boomaa.XKCDViewer.display.SelectList;
import com.boomaa.XKCDViewer.reporting.PackageMap;
import com.boomaa.XKCDViewer.utils.DisplayUtils;
import com.boomaa.XKCDViewer.utils.StatsUtils;
import com.google.gson.JsonObject;

import java.io.IOException;

/** <p>Thread for getting titles of XKCDs.</p> */
public class TitleThread implements Runnable {
    /** <p>The starting index to request titles of.</p> */
    public int start;
    /** <p>The ending index to request titles to.</p> */
    private int end;
    /** <p>The sequentially ordered ID of this thread.</p> */
    private int num;

    /**
     * <p>Constructs a thread with passed start and finish indexes.</p>
     * @param num the arbitrary sequential ID of the thread.
     * @param start the start index of titles.
     * @param reqpt the number of requests to complete.
     */
    public TitleThread(int num, int start, int reqpt) {
    	this.num = num;
        this.start = start;
        end = start + reqpt;
        if (end > MainDisplay.LATEST_XKCD_NUM - reqpt) {
            end = MainDisplay.LATEST_XKCD_NUM;
        }
    }

    @Override
    public void run() {
    	System.out.println(PackageMap.threading.TITLE_THREAD + "Starting title thread " + num + " from " + start + " to " + end);
        for (int i = start; i <= end; i++) {
            SelectList.updateBar(false);
            try {
                JsonObject json = DisplayUtils.getJSONFromHTTP("https://xkcd.com/" + i + "/info.0.json");
                StatsUtils.addTransferredBytes("https://xkcd.com/" + i + "/info.0.json");
                SelectList.JSONS[i] = json;
                SelectList.TITLES[i] = json.getAsJsonPrimitive("num").getAsInt() + " - " + json.getAsJsonPrimitive("title").getAsString();
            } catch (IOException e1) {
                SelectList.JSONS[i] = null;
                SelectList.TITLES[i] = i + " - NO IMAGE FOUND";
            }
        }
        SelectList.updateBar(true);
        System.out.println(PackageMap.threading.TITLE_THREAD + "Title thread " + num + " from " + start + " to " + end + " completed");
    }
}
