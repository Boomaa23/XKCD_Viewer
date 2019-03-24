package com.boomaa.xkcd_viewer.utils;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import org.json.JSONException;
import org.json.JSONObject;

import com.boomaa.xkcd_viewer.display.Display;

public class Utils {
	public static JSONObject readJSONFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		for(int c = br.read();c != -1;c = br.read()) {
			sb.append((char) c);
		}
		is.close();
		return new JSONObject(sb.toString());
	}
	
	public static Image getImageFromJson(JSONObject jsonObj) throws MalformedURLException, JSONException, IOException {
		return ImageIO.read(new URL(jsonObj.getString("img")));
	}
	
	public static void saveImage(JSONObject json) throws MalformedURLException, JSONException, IOException {
		JFileChooser fileChooser = new JFileChooser("Save the displayed XKCD image");
		fileChooser.setSelectedFile(new File("XKCD_" + Display.DISPLAYED_XKCD_NUM + ".jpeg"));
		if(fileChooser.showSaveDialog(JDEC.FRAME) == JFileChooser.APPROVE_OPTION) {
			ImageIO.write((RenderedImage)(getImageFromJson(json)), "jpeg", fileChooser.getSelectedFile());
		}
	}
}
