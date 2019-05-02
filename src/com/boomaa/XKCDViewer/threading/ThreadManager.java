package com.boomaa.XKCDViewer.threading;

import com.boomaa.XKCDViewer.display.MainDisplay;
import com.boomaa.XKCDViewer.display.SelectList;

/** <p></p> */
public class ThreadManager extends SelectList {
	/** <p></p> */
	private static TitleThread[] titleThreads = new TitleThread[32];
	
	/** <p></p> */
	public ThreadManager() {
		titles = new String[MainDisplay.DISPLAYED_XKCD_NUM+1];
		int reqpt = MainDisplay.LATEST_XKCD_NUM / titleThreads.length;
		for(int i = 0;i < titleThreads.length;i++) {
			titleThreads[i] = new TitleThread(i * reqpt, reqpt);
			new Thread(titleThreads[i]).start();
		}
		while(isRunning()) { 
			System.out.print("."); 
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		selectPanelInit();
	}
	
	/**
	 * <p></p>
	 * @return
	 */
	public static boolean isRunning() {
		for(TitleThread t : titleThreads) {
			if(t == null || t.isAlive() == true) {
				return true;
			}
		}
		return false;
	}
}
