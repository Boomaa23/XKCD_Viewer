package com.boomaa.XKCDViewer.display;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONObject;

import com.boomaa.XKCDViewer.utils.DisplayUtils;

public class Leaderboard {
	public Leaderboard() {
		JFrame frame = new JFrame();
		frame.setLayout(new GridLayout(10,3));
		try {
			JSONObject voteJSON = DisplayUtils.getJSONFromFTP("XKCD/votes.json");
			if(voteJSON.length() != MainDisplay.LATEST_XKCD_NUM) { updateToLatest(voteJSON); }
			int[] votes = new int[voteJSON.length()+1];
			for(int i = 1;i < voteJSON.length();i++) {
				votes[i] = voteJSON.getInt(String.valueOf(i));
			}
			Arrays.sort(votes);
			for(int i = votes.length-1; i >= votes.length - 10;i--) {
				addBorderedObjects(frame, new JLabel(String.valueOf(-1 * (i - votes.length))), 
						new JLabel("WIP"), new JLabel(String.valueOf(votes[i])));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		frame.setSize(200, 350);
		frame.setVisible(true);
	}
	
	private void addBorderedObjects(JFrame frame, JLabel... object) {
		for(JLabel obj : object) {
			obj.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			frame.add(obj);
		}
	}
	
	private void updateToLatest(JSONObject voteJSON) throws MalformedURLException, IOException {
		StringBuilder sb = new StringBuilder();
		for(int i = voteJSON.length()+1;i < MainDisplay.LATEST_XKCD_NUM;i++) {
			sb.append(", " + "\"" + i + "\": 0");
		}
		uploadToFTP(sb.toString());
	}
	
	private void uploadToFTP(String append) throws MalformedURLException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("ftpurl.txt")));
    	String line = br.readLine();
    	System.out.println(line);
    	InputStream is = new URL(line + "/htdocs/XKCD/votes.json").openConnection().getInputStream();
		br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        for (int c = br.read(); c != -1; c = br.read()) {
            sb.append((char) c);
        }
        is.close(); br.close();
        OutputStream os = new URL(line + "/htdocs/XKCD/votes.json").openConnection().getOutputStream();
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
