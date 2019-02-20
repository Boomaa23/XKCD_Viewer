import java.awt.FlowLayout;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

public class Main {
	private static final int FRAME_BORDER = 20;

	public static void main(String[] args) throws JSONException, IOException {
		JSONObject json = readJsonFromUrl("https://xkcd.com/info.0.json");
		Image image = ImageIO.read(new URL(json.getString("img")));

		JFrame frame = new JFrame("XKCD Viewer");
		JPanel mainPanel = new JPanel();
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		frame.setSize(image.getWidth(frame) + FRAME_BORDER, image.getHeight(frame) + 60 + FRAME_BORDER);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		imagePanel.add(new JLabel(new ImageIcon(image)));
		titlePanel.add(new JLabel(json.getString("title") + " - #" + json.getInt("num")));
		
		mainPanel.add(titlePanel);
		mainPanel.add(imagePanel);
		frame.add(mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		for(int c = br.read();c != -1;c = br.read()) {
			sb.append((char) c);
		}
		is.close();
		return new JSONObject(sb.toString());
	}

}
