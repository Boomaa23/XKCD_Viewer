package com.boomaa.XKCDViewer.utils;

import com.boomaa.XKCDViewer.display.Login;
import com.boomaa.XKCDViewer.display.MainDisplay;
import com.boomaa.XKCDViewer.display.SelectList;
import com.boomaa.XKCDViewer.reporting.ErrorMessages;
import com.boomaa.XKCDViewer.threading.ThreadManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.Timer;

/** <p>Nested ActionListener classes.</p> */
public class Listeners {
    /** <p>Navigates to and displays input XKCD image upon actionPerformed().</p> */
    public static class NumSelect implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int numRequest = 0;
            try {
                numRequest = Integer.parseInt(JDEC.TEXT_INPUT.getText());
                if (!JDEC.TEXT_INPUT.getText().isEmpty() && numRequest <= MainDisplay.LATEST_XKCD_NUM && numRequest > 0) {
                    MainDisplay.panelRewrite(numRequest);
                } else {
                    ErrorMessages.numInvalid();
                }
            } catch (NumberFormatException e0) {
                if(ThreadManager.TITLES != null && !containsNull()) {
                	for(int i = 0;i < ThreadManager.TITLES.length;i++) {
                		if(ThreadManager.TITLES[i].trim().toLowerCase()
                                .contains(JDEC.TEXT_INPUT.getText().trim().toLowerCase())) {
                			MainDisplay.panelRewrite(i);
                			return;
                		}
                	}
                	ErrorMessages.nameInvalid();
                } else {
                	ErrorMessages.namesNotLoaded();
                }
            }
        }
        
        /** 
         * <p>Checks to make sure ThreadManager.TITLES is not still filling.</p>
         * @return true if a null object is found, false if not.
         */
        private boolean containsNull() {
        	for(Object obj : ThreadManager.TITLES) {
        		if(obj == null) { return true; }
        	}
        	return false;
        }
    }

    /** <p>Saves XKCD image currently displayed upon actionPerformed().</p> */
    public static class SaveAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                DisplayUtils.saveImage(DisplayUtils.getJSONFromHTTP("https://xkcd.com/" + MainDisplay.DISPLAYED_XKCD_NUM + "/info.0.json"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /** <p>Opens XKCD image currently displayed in browser upon actionPerformed().</p> */
    public static class BrowseAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Desktop.getDesktop().browse(new URI(DisplayUtils.getJSONFromHTTP("https://xkcd.com/" + MainDisplay.DISPLAYED_XKCD_NUM + "/info.0.json").getAsJsonPrimitive("img").getAsString()));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        }
    }
    
    /** <p>Starts vote change process upon actionPerformed().</p> */
    public static class VoteAction implements ActionListener {
    	/** <p>Storage for the change in vote amount.</p> */
    	private int mod;
    	
    	/**
    	 * <p>Determines the direction of the vote.</p>
    	 * @param mod the change in vote for the operation.
    	 */
    	public VoteAction(int mod) {
    		this.mod = mod;
    	}
    	
    	@Override
        public void actionPerformed(ActionEvent e) {
			try {
	    		if(Login.FTP_URL != null) {
	    			DisplayUtils.uploadToFTP("", mod);
	    		} else {
	    			new Login(mod == 1 ? "Upvote" : "Downvote");
	    		}
			} catch (IOException e0) {
				e0.printStackTrace();
			}
    	}
    }
    
    /** <p>Opens a link to a webpage in default web browser.</p> */
    public static class HREFAction extends MouseAdapter {
        /** <p>Location of web link.</p> */
        private String href;

        /**
         * <p>Passes in href location.</p>
         * @param href location of web link, temporary.
         */
        public HREFAction(String href) {
            this.href = href;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                Desktop.getDesktop().browse(new URI(href));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        }
    }

    /** <p>Changes selector menu after the item selected is changed.</p> */
    public static class SelectItemAction implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String in = (String) e.getItem();
                SelectList.createSelectList(Integer.parseInt(in.substring(0, in.indexOf(" - "))));
                SelectList.refreshSelector();
            }
        }
    }
    
    /** <p>Displays selected xkcd image from leaderboard.</p> **/
    public static class LeaderboardAdvance extends MouseAdapter {
    	/** <p>Leaderboard frame to dispose.</p> **/
    	private final JFrame frame;
    	/** <p>Requested image number.</p> **/
    	private final int req_num;
    	
    	/**
    	 * <p>Passes in Leaderboard frame and xkcd image number.</p>
    	 * @param frame Leaderboard frame.
    	 * @param req_num Requested xkcd image number.
    	 */
    	public LeaderboardAdvance(JFrame frame, int req_num) {
    		this.frame = frame;
    		this.req_num = req_num;
    	}
    	
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		frame.dispose();
    		MainDisplay.panelRewrite(req_num);
    	}
    }
    
    @Deprecated
    /** <p>Adds the TTS checkbox to the screen if a double click is detected on the main frame.</p> */
    public static class TTSEnable extends MouseAdapter implements ActionListener {
	    /** <p>The timer to regulate click doubling.</p> */
	    private Timer timer = new Timer((int) Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval"), this);
	    
	    /** <p>Adds TTS button to scaling panel if a double click is detected.</p> */
	    public void doubleClick() {
	    	if(JDEC.SCALING_PANEL.isAncestorOf(JDEC.TTS_CHECKBOX)) {
	    		JDEC.TTS_CHECKBOX.setSelected(false);
	    		JDEC.SCALING_PANEL.remove(JDEC.TTS_CHECKBOX); 
	    	} else {
	    		JDEC.TTS_CHECKBOX.setSelected(true);
		    	JDEC.SCALING_PANEL.add(JDEC.TTS_CHECKBOX); 
	    	}
	    	JDEC.MAIN_PANEL.revalidate(); 
	    	JDEC.MAIN_PANEL.repaint();
	    }

	    @Override
	    public void mouseClicked(MouseEvent e) {
	        if (e.getClickCount() > 2) { return; }
	        if (timer.isRunning()) {
	            timer.stop();
	            doubleClick();
	        } else {
	            timer.restart();
	        }
	    }
	    
	    @Override
	    public void actionPerformed(ActionEvent e) {
	        timer.stop();
	    }
    }
}
