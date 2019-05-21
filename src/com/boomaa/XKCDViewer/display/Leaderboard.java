package com.boomaa.XKCDViewer.display;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.boomaa.XKCDViewer.utils.DisplayUtils;
import com.boomaa.XKCDViewer.utils.StatsUtils;
import com.google.gson.JsonObject;

import net.sf.image4j.codec.ico.ICODecoder;

/** <p>Displays leaderboard of 10 top scoring XKCDs.</p> */
public class Leaderboard {
	/** <p>Storage class for each XKCD's votes and number.</p> */
	public static class VoteStatus implements Comparable<VoteStatus> {
		/** <p>Storage of votes and number.</p> */
		public final String num, votes;
		
		/**
		 * <p>Creates placeholder with number and votes.</p>
		 * @param num the xkcd number that the votes are for.
		 * @param votes the number of votes for that xkcd number.
		 */
		public VoteStatus(final int num, final int votes) {
			this.num = String.valueOf(num);
			this.votes = String.valueOf(votes);
		}
		
		@Override
		public int compareTo(VoteStatus vs) {
			return votes.compareTo(vs.votes);
		}
	}
	
	/** <p>URL for FTP uploads - HTTP doesn't work with some sites because of not having JS.</p> */
	private String FTP_URL;
	/** <p>Displayed leaderboard frame.</p> */
	private JFrame frame = new JFrame("Leaderboard");
	/** <p>The panel that stores the login page.</p> */
	private JPanel loginPanel = new JPanel();
	
	
	/** <p>Creates leaderboard at login stage with full listener implementation.</p> */
	public Leaderboard() {
		try {
			frame.setIconImages(ICODecoder.read(new URL("https://xkcd.com/s/919f27.ico").openStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setSize(250, 350);
		frame.setVisible(true);
		setupFTPLogin();
	}
	
	/** <p>Sets up login before leaderboard, initializes FTP authentication.</p> */
	private void setupFTPLogin() {
		JButton submit = new JButton("Submit");
		frame.getRootPane().setDefaultButton(submit);
		submit.addActionListener(e -> { 
			try {	
				StringBuilder sb = new StringBuilder();
				for(char c : ((JPasswordField) (loginPanel.getComponent(3))).getPassword()) { sb.append(c); }
				FTP_URL = "ftp://" + ((JTextField) (loginPanel.getComponent(1))).getText() + ":"  + sb.toString() + "@ftpupload.net/htdocs/XKCD/votes.json";
				frame.remove(loginPanel); 
				setupLeaderboard();
			} catch (IllegalArgumentException e0) {
				setupFTPLogin();
			}
		});
		addLoginComponents(new JLabel("FTP Username: "), new JTextField("b24_21343661", 10), new JLabel("FTP Password: "), new JPasswordField(10), submit);
		frame.add(loginPanel);
		frame.setVisible(true);
		((JPasswordField) (loginPanel.getComponent(3))).requestFocus();
	}
	
	/** <p>Sets up leaderboard in 3x11 grid with rank, number, and votes.</p> */
	private void setupLeaderboard() {
		try {
			frame.setLayout(new GridLayout(11,3));
			JsonObject voteJSON = DisplayUtils.getJSONFromFTP(FTP_URL);
			StatsUtils.addTransferredBytes("https://xkcd.com/s/919f27.ico", FTP_URL);
			if(voteJSON.size() != MainDisplay.LATEST_XKCD_NUM) { updateToLatest(voteJSON); }
			VoteStatus[] votes = new VoteStatus[voteJSON.size() + 1];
			votes[0] = new VoteStatus(-1,-1);
			for(int i = 1;i < votes.length;i++) {
				votes[i] = new VoteStatus(i, voteJSON.getAsJsonPrimitive(String.valueOf(i)).getAsInt());
			}
			Arrays.sort(votes);
			addBorderedObjects(frame, new JLabel("Rank"), new JLabel("XKCD#"), new JLabel("Votes"));
			for(int i = votes.length-1; i >= votes.length - 10;i--) {
				JLabel rank = new JLabel(String.valueOf(-1 * (i - votes.length)));
				JLabel number = new JLabel(votes[i].num);
				JLabel vote = new JLabel(votes[i].votes);
				final int curr_num = Integer.valueOf(votes[i].num);
				MouseAdapter listen = new MouseAdapter() {
					@Override
			        public void mouseClicked(MouseEvent e) {
			    		frame.dispose();
			    		MainDisplay.panelRewrite(curr_num);
			    	}
				};
				addBorderedObjects(frame, addMouseListeners(listen, rank, number, vote));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		frame.revalidate();
		frame.setVisible(true);
	}
	
	/**
	 * <p>A more efficient wrapper for adding JComponents</p>
	 * @param comp as many JComponent objects as should be added.
	 */
	private void addLoginComponents(JComponent... comp) {
		for(JComponent jc : comp) { loginPanel.add(jc); }
	}
	
	/**
	 * <p>Adds mouse listeners of the passed listener to the JLabels.</p>
	 * @param listen the passed instance of the mouse listener.
	 * @param labels any number of JLabels to add mouse listeners to.
	 * @return the array of all JLabels passed with mouse listeners.
	 */
	private JLabel[] addMouseListeners(MouseAdapter listen, JLabel... labels) {
		for(JLabel label : labels) {
			label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			label.addMouseListener(listen);
		}
		return labels;
	}
	
	/**
	 * <p>Adds labels to frame with a border.</p>
	 * @param frame the JFrame to add the JLabels to.
	 * @param labels all the JLabels to add.
	 */
	private void addBorderedObjects(JFrame frame, JLabel... labels) {
		for(JLabel label : labels) {
			label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
			frame.add(label);
		}
	}
	
	/**
	 * <p>Updates JSON on vote server with latest xkcd numbers.</p>
	 * @param voteJSON the current JSON contents.
	 * @throws IOException if the FTP upload fails.
	 */
	private void updateToLatest(JsonObject voteJSON) throws IOException {
		StringBuilder sb = new StringBuilder();
		for(int i = voteJSON.size() + 1;i < MainDisplay.LATEST_XKCD_NUM;i++) {
			sb.append(", " + "\"" + i + "\": 0");
		}
		DisplayUtils.uploadToFTP(sb.toString(), FTP_URL);
	}
}
