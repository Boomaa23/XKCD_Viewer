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
import javax.swing.JTextField;

import org.json.JSONException;
import org.json.JSONObject;

public class Main {
	private static final int FRAME_BORDER = 20;
	private static int LATEST_XKCD_NUM;
	
	private static JFrame FRAME = new JFrame("XKCD Viewer");
	private static JPanel MAIN_PANEL = new JPanel();
	private static JPanel TITLE_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private static JPanel IMAGE_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private static JPanel SELECT_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
	private static JPanel ERROR_PANEL = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
	public static void main(String[] args) throws JSONException, IOException {
		JSONObject jsonLatest = readJsonFromUrl("https://xkcd.com/info.0.json");
		Image image = getImageFromJson(jsonLatest);
		LATEST_XKCD_NUM = jsonLatest.getInt("num");
		
		JButton jBtnRandom = new JButton("Random");
		JButton jBtnNum = new JButton("Go!");
		JTextField jTextNum = new JTextField(10);
		
		MAIN_PANEL.setLayout(new BoxLayout(MAIN_PANEL, BoxLayout.Y_AXIS));
		setupFrame(image);
		setupTitle(jsonLatest);
		
		IMAGE_PANEL.add(new JLabel(new ImageIcon(image)));
		SELECT_PANEL.add(jBtnRandom);
		SELECT_PANEL.add(jTextNum);
		SELECT_PANEL.add(jBtnNum);
		
		jBtnRandom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panelRewrite((int)(Math.random() * LATEST_XKCD_NUM));
			}
		});
		
		jBtnNum.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!jTextNum.getText().isEmpty() && Integer.parseInt(jTextNum.getText()) <= LATEST_XKCD_NUM) {
					panelRewrite(Integer.parseInt(jTextNum.getText()));
					jTextNum.setText("");
				} else {
					ERROR_PANEL.removeAll();
					ERROR_PANEL.add(new JLabel("ERROR: No XKCD found for this number"));
					FRAME.revalidate();
					FRAME.repaint();
				}
			}
		});
		
		MAIN_PANEL.add(TITLE_PANEL);
		MAIN_PANEL.add(IMAGE_PANEL);
		MAIN_PANEL.add(SELECT_PANEL);
		MAIN_PANEL.add(ERROR_PANEL);
		FRAME.add(MAIN_PANEL);
		
		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FRAME.setVisible(true);
	}
	
	private static void panelRewrite(int numReq) {
		JSONObject json = null;
		try {
			json = readJsonFromUrl("https://xkcd.com/" + numReq + "/info.0.json");
		} catch (JSONException | IOException e2) {
			e2.printStackTrace();
		}
		TITLE_PANEL.removeAll();
		ERROR_PANEL.removeAll();
		setupTitle(json);
		
		IMAGE_PANEL.removeAll();
		Image imgRand = null;
		try {
			imgRand = getImageFromJson(json);
		} catch (JSONException | IOException e2) {
			e2.printStackTrace();
		}
		IMAGE_PANEL.add(new JLabel(new ImageIcon(imgRand)));
		
		setupFrame(imgRand);
		FRAME.revalidate();
		FRAME.repaint();
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
	
	private static void setupFrame(Image image) {
		FRAME.setSize(image.getWidth(FRAME) + FRAME_BORDER, image.getHeight(FRAME) + 7 * FRAME_BORDER);
		
	}
	
	private static void setupTitle(JSONObject jsonLatest) {
		TITLE_PANEL.add(new JLabel(jsonLatest.getString("title") + " - #" + jsonLatest.getInt("num")));
	}
}
