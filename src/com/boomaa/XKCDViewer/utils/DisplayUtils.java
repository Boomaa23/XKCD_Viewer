package com.boomaa.XKCDViewer.utils;

import com.boomaa.XKCDViewer.display.MainDisplay;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/** <p>Assorted utils for JSON reading and image manipulation.</p> */
public class DisplayUtils {
    public DisplayUtils() {}

    /**
     * <p>Reads the address of a URL of a JSON and returns it back.</p>
     * @param url the address of the JSON requested to read.
     * @return the JSON at the address in JSONObject form.
     * @throws IOException if a URL stream could not be opened or data could not be read.
     */
    public static JSONObject getJSONFromHTTP(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        for (int c = br.read(); c != -1; c = br.read()) {
            sb.append((char) c);
        }
        is.close();
        return new JSONObject(sb.toString());
    }
    
    /**
     * <p>Reads the address of an FTP URL of a JSON and returns it back.</p>
     * @param ftpurl the address of the JSON requested to read.
     * @return the JSON at the address in JSONObject form.
     * @throws IOException if a URL stream could not be opened or data could not be read.
     */
    public static JSONObject getJSONFromFTP(String ftpurl) throws IOException {
    	InputStream is = new URL(ftpurl).openConnection().getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        for (int c = br.read(); c != -1; c = br.read()) {
            sb.append((char) c);
        }
        is.close(); br.close();
        return new JSONObject(sb.toString());
    }
    
    /**
     * <p>Reads and image from a JSONObject.</p>
     * @param jsonObj the JSONObject which contains the "img" tag to get the image location.
     * @return an Image read from a URL location.
     * @throws MalformedURLException if a new URL could not be read from the "img" tag.
     */
    public static Image getImageFromJSON(JSONObject jsonObj) throws IOException {
    	StatsUtils.addTransferredBytes(jsonObj.getString("img"));
        return resizeImage(ImageIO.read(new URL(jsonObj.getString("img"))));
    }

    /**
     * <p>Resizes images to fit on screens without scrolling.</p>
     * @param image the originally pulled image to resize
     * @return the resized image proportional to the screen size if the scaling image is checked
     */
    public static Image resizeImage(Image image) {
        double width = image.getWidth(JDEC.FRAME);
        double height = image.getHeight(JDEC.FRAME);
        Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        if (height >= screen.getHeight() - 12 * MainDisplay.FRAME_BORDER) {
            height = (screen.getHeight()) - (12 * MainDisplay.FRAME_BORDER);
            width -= height / (screen.getHeight() / screen.getWidth());
        }
        return JDEC.SCALE_CHECKBOX.isSelected() && height != image.getHeight(JDEC.FRAME) ? image.getScaledInstance((int) (width), (int) (height), Image.SCALE_SMOOTH) : image;
    }

    /**
     * <p>Saves image to file from JSONObject.</p>
     * @param json the JSONObject to get the Image from and save to file.
     * @throws IOException if an image could not be found from the JSONObject.
     */
    public static void saveImage(JSONObject json) throws IOException {
        JFileChooser fileChooser = new JFileChooser("Save the displayed XKCD image");
        fileChooser.setSelectedFile(new File(json.getString("safe_title").replaceAll("\\s+", "_").toLowerCase() + ".jpeg"));
        if (fileChooser.showSaveDialog(JDEC.FRAME) == JFileChooser.APPROVE_OPTION) {
            ImageIO.write((RenderedImage) (ImageIO.read(new URL(json.getString("img")))), "jpeg", fileChooser.getSelectedFile());
        }
    }
    
    /**
	 * <p>Uploads passed string to FTP.</p>
	 * @param append the content to affix to the voting JSON.
	 * @param ftpUrl the url to send the appended string to.
	 * @throws IOException if anything happens to the connections.
	 */
	public static void uploadToFTP(String append, String ftpUrl) throws IOException {
    	InputStream is = new URL(ftpUrl).openConnection().getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        for (int c = br.read(); c != -1; c = br.read()) {
            sb.append((char) c);
        }
        is.close(); br.close();
        
        OutputStream os = new URL(ftpUrl).openConnection().getOutputStream();
        StatsUtils.addTransferredBytes(ftpUrl, ftpUrl);
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
