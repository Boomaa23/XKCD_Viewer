package com.boomaa.XKCDViewer.display;

import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.boomaa.XKCDViewer.utils.DisplayUtils;

import net.sf.image4j.codec.ico.ICODecoder;

/** <p>Authenticates FTP credentials for voting/leaderboard systems.</p> */
public class Login {
	/** <p>Root URL for the FTP server, along with retrieved credentials.</p> */
	public static String FTP_URL;
	/** <p>Frame containing login elements in mainPanel.</p> */
	private JFrame frame = new JFrame("Login");
	/** <p>Panel containing login elements.</p> */
	private JPanel loginPanel = new JPanel();
	/** <p>The name of the class redirecting to the login page.</p> */
	private String lastClass = "";
	
	/**
	 * <p>Authenticates FTP credentials through GUI.</p>
	 * @param lastClass the class that was closed to get to login.
	 */
	public Login(String lastClass) {
		this.lastClass = lastClass;
		try {
			frame.setIconImages(ICODecoder.read(new URL("https://xkcd.com/s/919f27.ico").openStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setSize(250, 125);
		setupFTPLogin();
	}

	/** <p>Adds login panel items and listeners.</p> */
	private void setupFTPLogin() {
		JButton submit = new JButton("Submit");
		frame.getRootPane().setDefaultButton(submit);
		submit.addActionListener(e -> { 
			try {	
				StringBuilder sb = new StringBuilder();
				for(char c : ((JPasswordField) (loginPanel.getComponent(3))).getPassword()) { sb.append(c); }
				FTP_URL = "ftp://" + ((JTextField) (loginPanel.getComponent(1))).getText() + ":"  + sb.toString() + "@ftpupload.net/htdocs/XKCD/votes.json";
				reopenClosedClass();
				frame.dispose();
			} catch (IllegalArgumentException e0) {
				setupFTPLogin();
			}
		});
		DisplayUtils.addPanelComponents(loginPanel, new JLabel("FTP Username: "), new JTextField("b24_21343661", 10), new JLabel("FTP Password: "), new JPasswordField(10), submit);
		frame.add(loginPanel);
		frame.setVisible(true);
		((JPasswordField) (loginPanel.getComponent(3))).requestFocus();
	}
	
	/** <p>Opens a new instance of the linking class to get to the login page.</p> */
	private void reopenClosedClass() {
		try {
			switch(lastClass) {
				case "Leaderboard": new Leaderboard(); break;
				case "Upvote": DisplayUtils.uploadToFTP("", 1); break;
				case "Downvote": DisplayUtils.uploadToFTP("", -1); break;
				default: break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
