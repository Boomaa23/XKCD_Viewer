package com.boomaa.XKCDViewer.utils;

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

import com.boomaa.XKCDViewer.display.Display;

/** <p>Assorted utils for JSON reading and image manipulation.</p> */
public class JSONUtils {
	/** @deprecated No constructor needed, JSONUtils is all static. */
	public JSONUtils() {}
	
	/**
	 * <p>Reads the address of a URL of a JSON and returns it back to use.</p>
	 * @param url the address of the JSON requested to read.
	 * @return the JSON at the address in JSONObject form.
	 * @throws IOException if a URL stream could not be opened or data could not be read.
	 * @throws JSONException if a JSONObject could not be made from StringBuilder.
	 */
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
	
	/**
	 * <p>Reads and image from a JSONObject.</p>
	 * @param jsonObj the JSONObject which contains the "img" tag to get the image location.
	 * @return an Image read from a URL location.
	 * @throws MalformedURLException if a new URL could not be read from the "img" tag.
	 * @throws JSONException if the tag "img" could not be found in the JSONObject parameter.
	 * @throws IOException if the URL is invalid or could not be read from.
	 */
	public static Image getImageFromJson(JSONObject jsonObj) throws MalformedURLException, JSONException, IOException {
		return ImageIO.read(new URL(jsonObj.getString("img")));
	}
	
	/**
	 * <p>Saves image to file from JSONObject.</p>
	 * @param json the JSONObject to get the Image from and save to file.
	 * @throws MalformedURLException if an image could not be found from the passed JSONObject.
	 * @throws JSONException if the JSONObject is invalid.
	 * @throws IOException if an image could not be found from the JSONObject.
	 */
	public static void saveImage(JSONObject json) throws MalformedURLException, JSONException, IOException {
		JFileChooser fileChooser = new JFileChooser("Save the displayed XKCD image");
		fileChooser.setSelectedFile(new File("XKCD_" + Display.DISPLAYED_XKCD_NUM + ".jpeg"));
		if(fileChooser.showSaveDialog(JDEC.FRAME) == JFileChooser.APPROVE_OPTION) {
			ImageIO.write((RenderedImage)(getImageFromJson(json)), "jpeg", fileChooser.getSelectedFile());
		}
	}
}