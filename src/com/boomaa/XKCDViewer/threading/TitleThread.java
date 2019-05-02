package com.boomaa.XKCDViewer.threading;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import com.boomaa.XKCDViewer.display.SelectList;
import com.boomaa.XKCDViewer.utils.DisplayUtils;

/** <p></p> */
public class TitleThread implements Runnable {
	/** <p></p> */
	private boolean alive = true;
	
	/** <p></p> */
	public int start;
	
	/** <p></p> */
	private int end;
	
	
	/**
	 * <p></p>
	 * @param start
	 * @param reqpt
	 */
	public TitleThread(int start, int reqpt) {
		System.out.println(start + " | " + (start + reqpt));
		this.start = start;
		end = start + reqpt;
	}
	
	
	@Override
	/** <p></p> */
	public void run() {
		for(int i = start;i <= end;i++) {
			JSONObject json = null;
			try {
				json = DisplayUtils.getJSONFromURL("https://xkcd.com/" + i + "/info.0.json");
				SelectList.titles[i] = json.getInt("num") + " - " + json.getString("title");
			} catch (JSONException | IOException e1) {
				SelectList.titles[i] = "";
			}
		}
		alive = false;
		System.out.println(start/67 + " | DONE");
	}
	
	/**
	 * <p></p>
	 * @return
	 */
	public boolean isAlive() {
		return alive;
	}
}
