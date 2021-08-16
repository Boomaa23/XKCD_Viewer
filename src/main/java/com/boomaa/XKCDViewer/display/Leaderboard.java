package com.boomaa.XKCDViewer.display;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.boomaa.XKCDViewer.reporting.PackageMap;
import com.boomaa.XKCDViewer.utils.DisplayUtils;
import com.boomaa.XKCDViewer.utils.StatsUtils;
import com.boomaa.XKCDViewer.utils.Listeners.LeaderboardAdvance;
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

    /** <p>JFrame that stores leaderboard.</p> */
    private JFrame frame = new JFrame("Leaderboard");

    /** <p>Creates leaderboard and checks for login.</p> */
    public Leaderboard() {
        try {
            frame.setIconImages(ICODecoder.read(new URL("https://xkcd.com/s/919f27.ico").openStream()));
            if(Login.FTP_URL == null) {
                new Login(this.getClass().getSimpleName());
                frame.dispose();
                return;
            }
            setupLeaderboard();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Login Failed", "Login", JOptionPane.ERROR_MESSAGE);
            new Login(this.getClass().getSimpleName());
            return;
        }
        frame.setSize(250, 350);
        frame.revalidate();
        frame.setVisible(true);
        System.out.println(PackageMap.display.LEADERBOARD + "Leaderboard initialized and displayed successfully");
    }

    /**
     * <p>Sets up leaderboard in 3x11 grid with rank, number, and votes.</p>
     * @throws IOException if authentication fails or resources are not available.
     */
    private void setupLeaderboard() throws IOException {
        frame.setLayout(new GridLayout(11,3));
        JsonObject voteJSON = DisplayUtils.getJSONFromFTP(Login.FTP_URL);
        StatsUtils.addTransferredBytes("https://xkcd.com/s/919f27.ico");
        if(voteJSON.size() != MainDisplay.LATEST_XKCD_NUM) { updateToLatest(voteJSON); }
        VoteStatus[] votes = new VoteStatus[voteJSON.size() + 1];
        votes[0] = new VoteStatus(-1,-1);
        for(int i = 1;i < votes.length;i++) {
            votes[i] = new VoteStatus(i, voteJSON.getAsJsonPrimitive(String.valueOf(i)).getAsInt());
        }
        Arrays.sort(votes);
        System.out.println(PackageMap.display.LEADERBOARD + "Content retrieved and sorted");
        addBorderedObjects(frame, new JLabel(" Rank"), new JLabel(" XKCD#"), new JLabel(" Votes"));
        System.out.println(PackageMap.display.LEADERBOARD + "Headers added");
        for(int i = votes.length-1; i >= votes.length - 10;i--) {
            JLabel rank = new JLabel(" " + String.valueOf(-1 * (i - votes.length)));
            JLabel number = new JLabel(" " + votes[i].num);
            JLabel vote = new JLabel(" " + votes[i].votes);
            final int curr_num = Integer.valueOf(votes[i].num);
            MouseAdapter listen = new LeaderboardAdvance(frame, curr_num);
            addBorderedObjects(frame, addMouseListeners(listen, rank, number, vote));
        }
        System.out.println(PackageMap.display.LEADERBOARD + "Display set up completed");
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
        for(int i = voteJSON.size() + 1;i <= MainDisplay.LATEST_XKCD_NUM;i++) {
            if(i != 1) {
                sb.append("," + "\"" + i + "\":0");
            } else {
                sb.append("\"1\": 0");
            }
        }
        DisplayUtils.uploadToFTP(sb.toString(), 0);
        System.out.println(PackageMap.display.LEADERBOARD + "Remote JSON updated to latest xkcd number index");
    }
}
