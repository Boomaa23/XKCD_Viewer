package com.boomaa.XKCDViewer.display;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import net.sf.image4j.codec.ico.ICODecoder;

/** <p>Houses output tracker and Swing transposer for System.out console.</p> */
public class Console {
	/** <p>Tracks System.out inputs via OutputStream.</p> */
	public static class OutTracker extends OutputStream {
		/** <p>Holder for data on screen.</p> */
		private JTextArea textArea;
		/** <p>StringBuilder to house each data line.</p> */
		private StringBuilder sb = new StringBuilder();
		/** <p>Prefix after hostname and before data.</p> */
		private String title;
		
		/**
		 * <p>Constructs OutTracker with textArea and prefix.</p>
		 * @param textArea the displayed text field to push data to.
		 * @param prefix the hostname before the data.
		 */
		public OutTracker(JTextArea textArea, String prefix) {
		      this.textArea = textArea;
		      this.title = prefix;
		      sb.append(prefix + "> ");
		   }
		
		@Override
		public void write(int b) throws IOException {
			if (b == '\r')
		         return;
			if (b == '\n') {
				final String text = sb.toString() + "\n";
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						textArea.append(text);
					}
				});
				sb.setLength(0);
				sb.append(title + "> ");
				return;
			}
			
			sb.append((char) b);
		}
	}
	
	/** <p>Creates console window with outputs.</p> */
	public Console() {
		JFrame console = new JFrame("XKCD Developer Console");
		try {
            console.setIconImages(ICODecoder.read(new URL("https://xkcd.com/s/919f27.ico").openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	JTextArea textArea = new JTextArea(15, 45);
 	   	OutTracker outStream = null;
		try {
			outStream = new OutTracker(textArea, InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
        
        console.setSize(600, 400);
        console.setLayout(new BorderLayout());
        console.getContentPane().add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        System.setOut(new PrintStream(outStream));
        console.pack();
        console.setLocationRelativeTo(null);
        console.setVisible(true);
	}
}
