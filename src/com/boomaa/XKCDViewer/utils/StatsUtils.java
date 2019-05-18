package com.boomaa.XKCDViewer.utils;

import com.boomaa.XKCDViewer.display.MainDisplay;
import com.boomaa.XKCDViewer.utils.Listeners.HREFAction;
import net.sf.image4j.codec.ico.ICODecoder;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

/** <p>Utilities for the stats inspect and image list panels.</p> */
public class StatsUtils {
    /** <p>Temporary JSON of each window's object.</p> */
    private JSONObject json;
    /** <p>Temporary passed mainPanel of each window's object.</p> */
    private JPanel mainPanel;
    /** <p>Temporary passed frame of each window's object.</p> */
    private JFrame frame;

    /**
     * <p>Constructs panel for each window object.</p>
     * @param json JSON of current image.
     * @param mainPanel mainPanel from stats inspect/image list.
     * @param frame the frame from stats inspect/image list.
     */
    public StatsUtils(JSONObject json, JPanel mainPanel, JFrame frame) {
        this.json = json;
        this.mainPanel = mainPanel;
        this.frame = frame;
        frameInit();
    }

    /** <p>Initializes frame and sets size.</p> */
    private void frameInit() {
        try {
            frame.setIconImages(ICODecoder.read(new URL("https://xkcd.com/s/919f27.ico").openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        frame.setSize(415, 230);
    }

    /**
     * <p>Overloads String and boolean parameter addLabelPanel as false boolean.</p>
     * @param text the text to input.
     */
    public void addLabelPanel(String text) {
        addLabelPanel(text, false);
    }

    /**
     * <p>Makes new nested label in new panel for each panel item.</p>
     * @param text the text to input.
     * @param href note if text should become a hyperlink.
     */
    public void addLabelPanel(String text, boolean href) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if (href) {
            panel.add(new JLabel(text.substring(0, 11)));
            JLabel label = new JLabel(text.substring(11));
            label.addMouseListener(new HREFAction(text.substring(11)));
            label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            label.setForeground(Color.blue.darker());
            panel.add(label);
        } else {
            panel.add(new JLabel(text));
        }
        mainPanel.add(panel);
    }

    /**
     * <p>Gets size of any web resource.</p>
     * @param loc the location of the web resource to get the size of.
     * @param nested if the location is nested in a json.
     * @return size of image in bytes.
     */
    public long webResourceSize(String loc, boolean nested) {
        try {
            if (nested) {
                return new URL(json.getString(loc)).openConnection().getContentLength();
            } else {
                return new URL(loc).openConnection().getContentLength();
            }
        } catch (JSONException | IOException e) {
            return -1;
        }
    }

    /**
     * <p>Turns bytes into human-readable SI units.</p>
     * @param bytes number of bytes to transcribe.
     * @return the bytes passed in as SI units.
     */
    public String byteTranscribe(long bytes) {
        if (bytes < 1000) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1000));
        return String.format("%.1f %sB", bytes / Math.pow(1000, exp), "kMGTPE".charAt(exp - 1));
    }

    /**
     * <p>Gets local and public IP of client as well as hostname.</p>
     * @return string of hostname @ local IP (public IP).
     */
    public static String getHostIP() {
        try {
        	BufferedReader in = new BufferedReader(new InputStreamReader(new URL("http://checkip.amazonaws.com").openStream()));
            return InetAddress.getLocalHost().getHostName() + " @ " + InetAddress.getLocalHost().getHostAddress() + " (" + in.readLine() + ")";
        } catch (IOException e) {
            return "Could not find hostname or IP address";
        }
    }
    
    /**
     * <p>Increments total transferred bytes by size of passed location.</p>
     * @param loc the location to measure the size of.
     */
    public static void addTransferredBytes(String... loc) {
        try {
        	for(String s : loc) {
        		MainDisplay.TRANSFERRED_BYTES += new URL(s).openConnection().getContentLength();
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * <p>Gets response time from passed website in milliseconds.</p>
     * @param site the site to get the response time of.
     * @return the response time of the passed website in milliseconds.
     */
    public static long getResponseTime(String site) {
    	long start = System.nanoTime();
    	try {
	    	URLConnection url = new URL(site).openConnection();
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(url.getInputStream()));
			while(reader.readLine() != null) {}
	    	reader.close();
    	} catch (IOException e) {
    		e.printStackTrace();
    		return -1;
    	}
    	return (System.nanoTime() - start) / 1000000;
    }
    
    /** <p>Adds duplicate panel items for DevStats and SelectList.</p> */
    public void addGenericPanelItems() {
        addLabelPanel("Title: " + json.getString("title"));
        addLabelPanel("Image #: " + json.getInt("num"));
        addLabelPanel("Date Published: " + json.getInt("month") + "/" + json.getInt("day") + "/" + json.getInt("year"));
        addLabelPanel("Image URL: " + json.getString("img"), true);
        addLabelPanel("Image Size: " + byteTranscribe(webResourceSize("img", true)));
    }
}
