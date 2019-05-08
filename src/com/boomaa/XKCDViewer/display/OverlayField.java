package com.boomaa.XKCDViewer.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/** <p>A text field with hint text while not selected.</p> */
@SuppressWarnings("serial")
public class OverlayField extends JTextField implements FocusListener {
    /** <p>The text of the hint.</p> */
    private final String hint;

    /** <p>The status of whether the hint is showing or not.</p> */
    private boolean showingHint;

    /**
     * <p>Constructs text field by extending JTextField.</p>
     * @param hint the text to display when not selected
     * @param col the number of columns of the text field
     */
    public OverlayField(final String hint, final int col) {
        super(hint, col);
        super.setForeground(Color.GRAY);
        this.hint = hint;
        this.showingHint = true;
        super.addFocusListener(this);
    }

    /** <p>Resets to show hint after changing image.</p> */
    public void reset() {
        super.setVisible(false);
        super.setText(hint);
        showingHint = true;
        super.setVisible(true);
    }

    /** <p>Hides hint when cursor is active on the text field.</p> */
    @Override
    public void focusGained(FocusEvent e) {
        if (this.getText().isEmpty()) {
            super.setText("");
            showingHint = false;
        }
    }

    /** <p>Shows hint when cursor is not active on the text field.</p> */
    @Override
    public void focusLost(FocusEvent e) {
        if (this.getText().isEmpty()) {
            super.setText(hint);
            showingHint = true;
        }
    }

    /** <p>Gets text in text field</p> */
    @Override
    public String getText() {
        return showingHint ? "" : super.getText();
    }
}