package com.boomaa.XKCDViewer.utils;

import com.boomaa.XKCDViewer.display.Login;
import com.boomaa.XKCDViewer.display.MainDisplay;
import com.boomaa.XKCDViewer.display.SelectList;

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

/** <p>Nested ActionListener classes.</p> */
public class Listeners {
    /** <p>Navigates to and displays input XKCD imgage upon actionPerformed().</p> */
    public static class NumSelect implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int numRequest = 0;
            try {
                numRequest = Integer.parseInt(JDEC.TEXT_INPUT.getText());
                if (!JDEC.TEXT_INPUT.getText().isEmpty() && numRequest <= MainDisplay.LATEST_XKCD_NUM && numRequest > 0) {
                    MainDisplay.panelRewrite(numRequest);
                } else {
                    MainDisplay.resetOnJSONError();
                }
            } catch (NumberFormatException e0) {
                MainDisplay.resetOnJSONError();
            }
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
}
