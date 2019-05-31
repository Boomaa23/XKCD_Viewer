package com.boomaa.XKCDViewer.display;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.boomaa.XKCDViewer.utils.DisplayUtils;

import net.sf.image4j.codec.ico.ICODecoder;

/** <p>Houses output tracker and Swing transposer for System.out console.</p> */
@SuppressWarnings("serial")
public class Console extends JFrame {
	/** <p>Tracks System.out inputs via OutputStream.</p> */
	public static class OutTracker extends OutputStream {
		/** <p>Holder for data on screen.</p> */
		private JTextArea textArea;
		/** <p>StringBuilder to house each data line.</p> */
		private StringBuilder sb = new StringBuilder();
		
		/**
		 * <p>Constructs OutTracker with textArea.</p>
		 * @param textArea the displayed text field to push data to.
		 */
		public OutTracker(JTextArea textArea) {
		    this.textArea = textArea;
		}
		
		@Override
		public void write(int b) throws IOException {
			textArea.setCaretPosition(textArea.getDocument().getLength());
            textArea.update(textArea.getGraphics());
			if (b == '\n') {
				final String text = sb.toString() + "\n";
				textArea.append(text);
			}
			sb.append((char) b);
		}
	}
	
	/** <p>Creates console window with outputs.</p> */
	public Console() {
		setTitle("XKCD Developer Console");
		try {
			setTitle(getTitle() + " @ " + InetAddress.getLocalHost().getHostName());
			setIconImages(ICODecoder.read(new URL("https://xkcd.com/s/919f27.ico").openStream()));
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
 	   	OutTracker outStream = new OutTracker(textArea);
        
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err)));
				System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
				System.out.println("[" + DisplayUtils.class.getSimpleName() + "] System.out and System.err reset back to IDE/command console");
			}
		});
 	   	
        setSize(600, 400);
        setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(textArea, 
        		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        System.setOut(new PrintStream(outStream));
        System.setErr(new PrintStream(outStream));
        setVisible(true);
        System.out.println("[" + DisplayUtils.class.getSimpleName() + "] System.out and System.err set to standalone console");
	}
}
