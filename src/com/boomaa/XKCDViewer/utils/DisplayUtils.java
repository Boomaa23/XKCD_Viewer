package com.boomaa.XKCDViewer.utils;

import com.boomaa.XKCDViewer.display.Login;
import com.boomaa.XKCDViewer.display.MainDisplay;
import com.boomaa.XKCDViewer.reporting.PackageMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/** <p>Assorted utils for JSON reading and image manipulation.</p> */
public class DisplayUtils {
	/**
     * <p>Retrieves web content in String form.</p>
     * @param url the url to retrieve content from.
     * @return the web content requested as a String.
	 * @throws IOException if the content cannot be retrieved.
     */
    private static StringBuilder retrieveWebContent(String url) throws IOException {
    	System.out.println(PackageMap.utils.DISPLAY_UTILS + "Web content retrieval finished");
    	InputStream is = new URL(url).openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        for (int c = br.read(); c != -1; c = br.read()) {
            sb.append((char) c);
        }
        is.close();
        return sb;
    }
	
    /**
     * <p>Reads the address of a URL of a JSON and returns it back.</p>
     * @param url the address of the JSON requested to read.
     * @return the JSON at the address in JSONObject form.
     * @throws IOException if a URL stream could not be opened or data could not be read.
     */
    public static JsonObject getJSONFromHTTP(String url) throws IOException {
    	System.out.println(PackageMap.utils.DISPLAY_UTILS + "HTTP content retrieved as JsonObject");
        return new JsonParser().parse(retrieveWebContent(url).toString()).getAsJsonObject();
    }

    /**
     * <p>Reads the address of an FTP URL of a JSON and returns it back.</p>
     * @param ftpurl the address of the JSON requested to read.
     * @return the JSON at the address in JSONObject form.
     * @throws IOException if a URL stream could not be opened or data could not be read.
     */
    public static JsonObject getJSONFromFTP(String ftpurl) throws IOException {
        try {
        	JsonObject js = new JsonParser().parse(retrieveWebContent(ftpurl).toString()).getAsJsonObject();
        	System.out.println(PackageMap.utils.DISPLAY_UTILS + "FTP content retrieved as JsonObject");
        	return js;
        } catch (IllegalStateException e) {
        	return new JsonObject();
        }
    }
    
    /**
	 * <p>Uploads passed string to FTP.</p>
	 * @param append the content to affix to the voting JSON.
	 * @param modValue the change in vote (+/-), if any.
	 * @throws IOException if anything happens to the connections.
	 */
	public static void uploadToFTP(String append, int modValue) throws IOException {
		StringBuilder sb = retrieveWebContent(Login.FTP_URL);
        
        if(modValue != 0) {
	        JsonObject json = new JsonParser().parse(sb.toString()).getAsJsonObject();
	        json.addProperty(String.valueOf(MainDisplay.DISPLAYED_XKCD_NUM), json.getAsJsonPrimitive(String.valueOf(MainDisplay.DISPLAYED_XKCD_NUM)).getAsInt() + modValue);
	        sb = new StringBuilder();
	        sb.append(json.toString());
        }
        
        OutputStream os = new URL(Login.FTP_URL).openConnection().getOutputStream();
        StatsUtils.addTransferredBytes(Login.FTP_URL, Login.FTP_URL);
        if(sb.length() >= 1) {
        	sb.replace(sb.length() - 1, sb.length(), "");
        } else {
        	sb.append("{");
        }
        sb.append(append);
        sb.append("}");
        
        InputStream iss = new ByteArrayInputStream(sb.toString().getBytes());
        byte[] buffer = new byte[8192];
        int bytesRead = -1;
        while((bytesRead = iss.read(buffer)) != -1) {
        	os.write(buffer, 0, bytesRead);
        }
        os.close();
        System.out.println(PackageMap.utils.DISPLAY_UTILS + "FTP upload completed");
	}
    
    /**
     * <p>Reads and image from a JSONObject.</p>
     * @param jsonObj the JSONObject which contains the "img" tag to get the image location.
     * @return an Image read from a URL location.
     * @throws MalformedURLException if a new URL could not be read from the "img" tag.
     */
    public static Image getImageFromJSON(JsonObject jsonObj) throws IOException {
    	StatsUtils.addTransferredBytes(jsonObj.getAsJsonPrimitive("img").getAsString());
    	System.out.println(PackageMap.utils.DISPLAY_UTILS + "Image extracted from JsonObject");
        return resizeImage(ImageIO.read(new URL(jsonObj.getAsJsonPrimitive("img").getAsString())));
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

        if (height >= screen.getHeight() - MainDisplay.FRAME_BORDER) {
            height = screen.getHeight() - MainDisplay.FRAME_BORDER;
            width -= height / (screen.getHeight() / screen.getWidth());
        }
        System.out.println(PackageMap.utils.DISPLAY_UTILS + "Image passed for potential resizing");
        return JDEC.SCALE_CHECKBOX.isSelected() && height != image.getHeight(JDEC.FRAME) ? image.getScaledInstance((int) (width), (int) (height), Image.SCALE_SMOOTH) : image;
    }

    /**
     * <p>Saves image to file from JSONObject.</p>
     * @param json the JSONObject to get the Image from and save to file.
     * @throws IOException if an image could not be found from the JSONObject.
     */
    public static void saveImage(JsonObject json) throws IOException {
        JFileChooser fileChooser = new JFileChooser("Save the displayed XKCD image");
        fileChooser.setSelectedFile(new File(json.getAsJsonPrimitive("safe_title").getAsString().replaceAll("\\s+", "_").toLowerCase() + ".jpeg"));
        if (fileChooser.showSaveDialog(JDEC.FRAME) == JFileChooser.APPROVE_OPTION) {
            ImageIO.write((RenderedImage) (ImageIO.read(new URL(json.getAsJsonPrimitive("img").getAsString()))), "jpeg", fileChooser.getSelectedFile());
            System.out.println(PackageMap.utils.DISPLAY_UTILS + "Image downloaded to local computer");
        }
    }
	
	/**
	 * <p>A more efficient wrapper for adding JComponents</p>
	 * @param panel the enclosing swing object to add the comp items to.
	 * @param comp as many JComponent objects as should be added.
	 */
	public static void addPanelComponents(JComponent panel, JComponent... comp) {
		for(JComponent jc : comp) { panel.add(jc); }
	}
}
