package com.boomaa.XKCDViewer.reporting;

import net.sf.image4j.codec.ico.ICODecoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** <p>A Swing OutputStream transposer for standard out and standard error.</p> */
public class Console extends OutputStream {
    /** <p>An array of a single byte for write().</p> */
    private byte[] oneByte;
    /** <p>The textfield appender instance.</p> */
    private Appender appender;

    /**
     * <p>Constructs a console given a swing output.</p>
     * @param textArea The Swing text area to display the outputstream on.
     */
    public Console(JTextArea textArea) {
        oneByte = new byte[1];
        appender = new Appender(textArea);
    }

    /** <p>Clears appender.</p> */
    public synchronized void clear() {
        if (appender != null) {
            appender.clear();
        }
    }

    /** <p>Sets appender to null - effectively closes stream.</p> */
    public synchronized void close() {
        appender = null;
    }

    /** <p>Move to next synchronized thread cycle.</p> */
    public synchronized void flush() {
    }

    /**
     * <p>Write a byte to the stream.</p>
     * @param val the value of the byte to write.
     */
    public synchronized void write(int val) {
        oneByte[0] = (byte) val;
        write(oneByte, 0, 1);
    }

    /**
     * <p>Synchronized method wrapper for standard OutputStream write.</p>
     * @param ba the byte array to write to stream
     */
    public synchronized void write(byte[] ba) {
        write(ba, 0, ba.length);
    }

    /**
     * <p>Main write method to OutputStream.</p>
     * @param ba the set of data to write in byte array form.
     * @param str the string offset for new String.
     * @param len the length of the output String.
     */
    public synchronized void write(byte[] ba, int str, int len) {
        if(appender != null) {
            appender.append(bytesToString(ba, str, len));
        }
    }

    /**
     * <p>A conversion method for bytes to String given parameters.</p>
     * @param ba the set of data to write in byte array form.
     * @param str the string offset for new String.
     * @param len the length of the output String.
     * @return a String representation of the passed byte[] value.
     */
    private static String bytesToString(byte[] ba, int str, int len) {
        try {
            return new String(ba, str, len,"UTF-8");
        } catch (UnsupportedEncodingException thr) {
            return new String(ba, str, len);
        }
    }

    /** <p>Builds Swing frame and makes console instance the output device.</p> */
    public static void build() {
        JFrame frame = new JFrame("XKCD Developer Console");
        try {
            frame.setTitle(frame.getTitle() + " @ " + InetAddress.getLocalHost().getHostName());
            frame.setIconImages(ICODecoder.read(new URL("https://xkcd.com/s/919f27.ico").openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JTextArea textArea = new JTextArea(15, 45);
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textArea.setCaretPosition(textArea.getDocument().getLength());
                textArea.update(textArea.getGraphics());
            }
        });
        textArea.setEditable(false);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));
                System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
                System.out.println(PackageMap.reporting.CONSOLE + "System.out and System.err reset back to IDE/command console");
            }
        });
        PrintStream console = new PrintStream(new Console(textArea));
        System.setOut(console);
        System.setErr(console);

        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(new JScrollPane(textArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        frame.setVisible(true);
        System.out.println(PackageMap.reporting.CONSOLE + "System.out and System.err set to standalone console");
    }

    /** <p>The appender to append data to output.</p> */
    public static class Appender implements Runnable {
        /** <p>Standard \n end of line (EOL) character.</p> */
        static private final String EOL1 = "\n";
        /** <p>System-specific end of line (EOL) character.</p> */
        static private final String EOL2 = System.getProperty("line.separator", EOL1);

        /** <p>Text area to output to.</p> */
        private final JTextArea textArea;
        /** <p>Maximum number of scrollback-able lines in text area.</p> */
        private final int maxLines = 2000;
        /** <p>Lengths of appended values.</p> */
        private final LinkedList<Integer> lengths;
        /** <p>Appended values</p> */
        private final List<String> values;

        /** <p>Current length of value to be appended.</p> */
        private int curLength;
        /** <p>If the text area appended should be cleared.</p> */
        private boolean clear;
        /** <p>If this process is going to be queued.</p> */
        private boolean queue;

        /**
         * <p>Constructs an appender based on given Swing text area.</p>
         * @param textArea the output space Swing text area.
         */
        Appender(JTextArea textArea) {
            this.textArea = textArea;
            lengths = new LinkedList<>();
            values = new ArrayList<>();

            curLength = 0;
            clear = false;
            queue = true;
        }

        /**
         * <p>Appends String value to text area.</p>
         * @param val the value to be appended.
         */
        public synchronized void append(String val) {
            values.add(val);
            if (queue) {
                queue = false;
                EventQueue.invokeLater(this);
            }
        }

        /** <p>Clears entire text area including instance vars.</p> */
        public synchronized void clear() {
            clear = true;
            curLength = 0;
            lengths.clear();
            values.clear();
            if (queue) {
                queue = false;
                EventQueue.invokeLater(this);
            }
        }

        /** <p>Runs action to append applicable text values.</p> */
        public synchronized void run() {
            if (clear) {
                textArea.setText("");
            }
            for (String val : values) {
                curLength += val.length();
                if (val.endsWith(EOL1) || val.endsWith(EOL2)) {
                    if (lengths.size() >= maxLines) {
                        textArea.replaceRange("", 0, lengths.removeFirst()); }
                    lengths.addLast(curLength);
                    curLength = 0;
                }
                textArea.append(val);
            }
            values.clear();
            clear = false;
            queue = true;
        }
    }
}