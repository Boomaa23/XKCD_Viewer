package com.boomaa.XKCDViewer.display;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.json.JSONObject;

import com.boomaa.XKCDViewer.utils.DisplayUtils;
import com.boomaa.XKCDViewer.utils.StatsUtils;

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
		
		public int compareTo(VoteStatus vs) {
			return votes.compareTo(vs.votes);
		}
	}
	
	/** <p>URL for FTP uploads - HTTP doesn't work with some sites because of not having JS.</p> */
	private final String FTP_URL = "ftp://b24_21343661:admin@ftpupload.net/htdocs/XKCD/votes.json";
	
	/** <p>Creates leaderboard with 3 columns for rank, number, and votes.</p> */
	public Leaderboard() {
		JFrame frame = new JFrame("Leaderboard");
		frame.setSize(250, 350);
		frame.setVisible(true);
		try {
			frame.setIconImages(ICODecoder.read(new URL("https://xkcd.com/s/919f27.ico").openStream()));
			frame.setLayout(new GridLayout(11,3));
			JSONObject voteJSON = DisplayUtils.getJSONFromFTP(FTP_URL);
			StatsUtils.addTransferredBytes("https://xkcd.com/s/919f27.ico", FTP_URL);
			if(voteJSON.length() != MainDisplay.LATEST_XKCD_NUM) { updateToLatest(voteJSON); }
			VoteStatus[] votes = new VoteStatus[voteJSON.length()+1];
			votes[0] = new VoteStatus(-1,-1);
			for(int i = 1;i < votes.length;i++) {
				votes[i] = new VoteStatus(i, voteJSON.getInt(String.valueOf(i)));
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
	private void updateToLatest(JSONObject voteJSON) throws IOException {
		StringBuilder sb = new StringBuilder();
		for(int i = voteJSON.length()+1;i < MainDisplay.LATEST_XKCD_NUM;i++) {
			sb.append(", " + "\"" + i + "\": 0");
		}
		uploadToFTP(sb.toString());
	}
	
	/**
	 * <p>Uploads passed string to FTP.</p>
	 * @param append the content to affix to the voting JSON.
	 * @throws IOException if anything happens to the connections.
	 */
	private void uploadToFTP(String append) throws IOException {
    	InputStream is = new URL(FTP_URL).openConnection().getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        for (int c = br.read(); c != -1; c = br.read()) {
            sb.append((char) c);
        }
        is.close(); br.close();
        
        OutputStream os = new URL(FTP_URL).openConnection().getOutputStream();
        StatsUtils.addTransferredBytes(FTP_URL, FTP_URL);
        sb.replace(sb.length() - 1, sb.length(), "");
        sb.append(append);
        sb.append("}");
        
        InputStream iss = new ByteArrayInputStream(sb.toString().getBytes());
        byte[] buffer = new byte[8192];
        int bytesRead = -1;
        while((bytesRead = iss.read(buffer)) != -1) {
        	os.write(buffer, 0, bytesRead);
        }
        os.close();
        
	}
}
