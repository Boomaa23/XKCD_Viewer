package com.boomaa.XKCDViewer.utils;

import com.boomaa.XKCDViewer.utils.MiscListeners.HREFAction;
import net.sf.image4j.codec.ico.ICODecoder;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

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
     * <p>Gets size of JSON-accessed image.</p>
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

    public String getHostIP() {
        try {
            return InetAddress.getLocalHost().getHostName() + " @ " + InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "Could not find hostname or IP address";
        }
    }
}
