package com.boomaa.XKCDViewer.reporting;

import javax.swing.JLabel;

import com.boomaa.XKCDViewer.utils.JDEC;

/** <p>Common error messages to display to user on JDEC.ERROR_PANEL.</p> */
public class ErrorMessages {
    /** <p>Error for unaccessible or badly formatted xkcd number.</p> */
    public static void numInvalid() {
        System.err.println(PackageMap.reporting.ERROR_MESSAGES + "No XKCD found for this number");
        errorDisplay("No XKCD found for this number");
    }

    /** <p>Error for unaccessible or invalid xkcd name.</p> */
    public static void nameInvalid() {
        System.err.println(PackageMap.reporting.ERROR_MESSAGES + "No XKCD found for this name");
        errorDisplay("No XKCD found for this name");
    }

    /** <p>Error for names not loaded - unsearchable b/c all null.</p> */
    public static void namesNotLoaded() {
        System.err.println(PackageMap.reporting.ERROR_MESSAGES + "XKCD names not loaded");
        errorDisplay("XKCD names not loaded");
    }

    /**
     * <p>Error message display for passed error.</p>
     *  @param error the error message to display.
     */
    private static void errorDisplay(String error) {
        JDEC.TEXT_INPUT.reset();
        JDEC.ERROR_PANEL.removeAll();
        JDEC.ERROR_PANEL.add(new JLabel("ERROR: " + error));
        JDEC.FRAME.revalidate();
        JDEC.FRAME.repaint();
    }
}
