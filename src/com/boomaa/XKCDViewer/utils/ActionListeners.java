package com.boomaa.XKCDViewer.utils;

import com.boomaa.XKCDViewer.display.MainDisplay;
import org.json.JSONException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/** <p>Nested ActionListener classes.</p> */
public class ActionListeners {
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

    /** <p>Increments displayed XKCD image upon actionPerformed().</p> */
    public static class FwdAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e0) {
            try {
                MainDisplay.panelRewrite(MainDisplay.DISPLAYED_XKCD_NUM + 1);
            } catch (JSONException e1) {
                MainDisplay.resetOnJSONError();
            }
        }
    }

    /** <p>Decrements displayed XKCD image upon actionPerformed().</p> */
    public static class BackAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e0) {
            try {
                MainDisplay.panelRewrite(MainDisplay.DISPLAYED_XKCD_NUM - 1);
            } catch (JSONException e1) {
                MainDisplay.resetOnJSONError();
            }
        }
    }

    /** <p>Saves XKCD image currently displayed upon actionPerformed().</p> */
    public static class SaveAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                DisplayUtils.saveImage(DisplayUtils.getJSONFromURL("https://xkcd.com/" + MainDisplay.DISPLAYED_XKCD_NUM + "/info.0.json"));
            } catch (JSONException | IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    /** <p>Opens XKCD image currently displayed in browser upon actionPerformed().</p> */
    public static class BrowseAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Desktop.getDesktop().browse(new URI(DisplayUtils.getJSONFromURL("https://xkcd.com/" + MainDisplay.DISPLAYED_XKCD_NUM + "/info.0.json").getString("img")));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        }
    }
}
