package com.boomaa.XKCDViewer.threading;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.boomaa.XKCDViewer.display.MainDisplay;
import com.boomaa.XKCDViewer.display.SelectList;
import com.boomaa.XKCDViewer.utils.DisplayUtils;

/** <p>Thread for getting titles of XKCDs.</p> */
public class TitleThread implements Runnable {
	/** <p>The starting index to request titles of.</p> */
	public int start;
	
	/** <p>The ending index to request titles to.</p> */
	private int end;
	
	/**
	 * <p>Constructs a thread with passed start and finish indexes.</p>
	 * @param start the start index of titles.
	 * @param reqpt the number of requests to complete.
	 */
	public TitleThread(int start, int reqpt) {
		this.start = start;
		end = start + reqpt;
		if(end > MainDisplay.LATEST_XKCD_NUM - reqpt) {
			end = MainDisplay.LATEST_XKCD_NUM;
		}
	}
	
	@Override
	/** <p>Gets title requests and adds to titles array.</p> */
	public void run() {
		for(int i = start;i <= end;i++) {
			SelectList.updateBar(false);
			JSONObject json = null;
			try {
				json = DisplayUtils.getJSONFromURL("https://xkcd.com/" + i + "/info.0.json");
				SelectList.titles[i] = json.getInt("num") + " - " + json.getString("title");
			} catch (JSONException | IOException e1) {
				SelectList.titles[i] = i + " - NO IMAGE FOUND";
			}
		}
		SelectList.updateBar(true);
	}
}
