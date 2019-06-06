package com.boomaa.XKCDViewer.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.sf.image4j.codec.ico.ICODecoder;

@SuppressWarnings("serial")
@Deprecated
public class Instructions extends JFrame {
	/** <p>Creates instructions panel and content.</p> */
	public Instructions() {
		JPanel header = new JPanel();
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel title = new JLabel("XKCD Viewer - Instructions");
	
		titlePanel.add(title);
		header.add(titlePanel);
		
		header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
		title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 18));
		JButton exitButton = new JButton("Continue");
		exitButton.addActionListener(e -> { this.dispose(); new MainDisplay(); });
		
		this.getContentPane().add(header, BorderLayout.PAGE_START);
		this.getContentPane().add(exitButton, BorderLayout.PAGE_END);
		
		formatMainText();
		formatFrame();
	}
	
	/**
	 * <p>Consolidates main text to display with String formatting.</p>
	 * @return the main instruction text as a String.
	 */
	private String getMainText() {
		String intro = "This application is a web comic viewer based on comics from XKCD.com by Randall Munroe. As he describes it himself, \"[xkcd is] A webcomic of romance, sarcasm, math, and language\". ";
		String nav = "The < and > buttons move you forward and backward one comic, whereas Random will put you at a random comic and Latest is the latest one. The field is for a number 1-[latest], and Go will put you there. Image scaling on will make the entire window fit to the size of your screen.";
		String voting = "Clicking the +1 upvotes the current comic, and -1 downvotes it. This requires an FTP account. The top voted end up on the Leaderboard.";
		String menu = "Right-clicking on the comic allows you to save the comic, open a selector of all the comics, look at the top voted comic, see a bunch of stats, look at the console ouput of the program, or login to the voting server.";
		String inet = "The three circles on the bottom right are for internet connection, xkcd.com connection, and voting server connection - green is connected, red if not.";
		return intro + "\n\n" + nav + "\n\n" + voting + "\n\n" + menu + "\n\n" + inet;
	}
	
	/** <p>Formats main text area and adds content.</p> */
	private void formatMainText() {
		JTextArea mainText = new JTextArea(getMainText());
		mainText.setWrapStyleWord(true);
		mainText.setLineWrap(true);
		mainText.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 5));
		mainText.setEditable(false);
		mainText.setBackground(new Color(240,240,240));
		this.getContentPane().add(mainText, BorderLayout.CENTER);
	}
	
	/** <p>Adds finishing touches to frame and displays.</p> */
	private void formatFrame() {
		try {
			this.setIconImages(ICODecoder.read(new URL("https://xkcd.com/s/919f27.ico").openStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setTitle("Instructions");
		this.setSize(400,475);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
