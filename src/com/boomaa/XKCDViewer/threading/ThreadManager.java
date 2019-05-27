package com.boomaa.XKCDViewer.threading;

import com.boomaa.XKCDViewer.display.MainDisplay;
import com.boomaa.XKCDViewer.display.SelectList;

import javax.swing.*;

/** <p>Manager for all title lookup threads.</p> */
public class ThreadManager extends SelectList {
    /** <p>Storage of all title threads, utilized for isAlive().</p> */
    public static TitleThread[] TITLE_THREADS = new TitleThread[Runtime.getRuntime().availableProcessors() * 4];
    
    /** <p>Runs each thread to get all titles.</p> */
    public ThreadManager() {
        SelectList.TITLES = new String[MainDisplay.LATEST_XKCD_NUM + 1];
        SelectList.select = new JComboBox<String>();
        int reqpt = MainDisplay.LATEST_XKCD_NUM / TITLE_THREADS.length;
        for (int i = 0; i < TITLE_THREADS.length; i++) {
            TITLE_THREADS[i] = new TitleThread(i * reqpt, reqpt + 1);
            new Thread(TITLE_THREADS[i]).start();
        }
    }
}
