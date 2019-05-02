package com.boomaa.XKCDViewer.threading;

import com.boomaa.XKCDViewer.display.MainDisplay;
import com.boomaa.XKCDViewer.display.SelectList;

/** <p>Manager for all title lookup threads.</p> */
public class ThreadManager extends SelectList {
	/** <p>Storage of all title threads, utilized for isAlive().</p> */
	private static TitleThread[] titleThreads = new TitleThread[16];
	
	/** <p>Runs each thread to get all titles.</p> */
	public ThreadManager() {
		int reqpt = MainDisplay.LATEST_XKCD_NUM / titleThreads.length;
		for(int i = 0;i < titleThreads.length;i++) {
			titleThreads[i] = new TitleThread(i * reqpt, reqpt);
			new Thread(titleThreads[i]).start();
		}
		while(isRunning()) { 
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		selectPanelInit();
	}
	
	/**
	 * <p>Determine if all threads have stopped or are still running.</p>
	 * @return true if any thread is still running, false if all are stopped.
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
