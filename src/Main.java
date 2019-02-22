import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

public class Main {
	private static final int FRAME_BORDER = 20;
	private static JButton jBtnRandom = new JButton("Random");
	
	public static void main(String[] args) throws JSONException, IOException {
		JSONObject jsonLatest = readJsonFromUrl("https://xkcd.com/info.0.json");
		Image image = getImageFromJson(jsonLatest);
		
		JFrame frame = new JFrame("XKCD Viewer");
		JPanel mainPanel = new JPanel();
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		setupFrame(frame, image);

		setupTitle(titlePanel, jsonLatest);
		imagePanel.add(new JLabel(new ImageIcon(image)));
		selectPanel.add(jBtnRandom);
		
		jBtnRandom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JSONObject jsonRand = null;
				try {
					jsonRand = readJsonFromUrl("https://xkcd.com/" + (int)(Math.random() * jsonLatest.getInt("num")) + "/info.0.json");
				} catch (JSONException | IOException e2) {
					e2.printStackTrace();
				}
				titlePanel.removeAll();
				setupTitle(titlePanel, jsonRand);
				
				imagePanel.removeAll();
				Image imgRand = null;
				try {
					imgRand = getImageFromJson(jsonRand);
				} catch (JSONException | IOException e2) {
					e2.printStackTrace();
				}
				imagePanel.add(new JLabel(new ImageIcon(imgRand)));
				
				setupFrame(frame, imgRand);
				
				frame.revalidate();
				frame.repaint();
			}
		});
		
		mainPanel.add(titlePanel);
		mainPanel.add(imagePanel);
		mainPanel.add(selectPanel);
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
	
	private static Image getImageFromJson(JSONObject jsonObj) throws MalformedURLException, JSONException, IOException {
		return ImageIO.read(new URL(jsonObj.getString("img")));
	}
	
	private static void setupFrame(JFrame frame, Image image) {
		frame.setSize(image.getWidth(frame) + FRAME_BORDER, image.getHeight(frame) + 6 * FRAME_BORDER);
		
	}
	
	private static void setupTitle(JPanel titlePanel, JSONObject jsonLatest) {
		titlePanel.add(new JLabel(jsonLatest.getString("title") + " - #" + jsonLatest.getInt("num")));
	}
}
