package com.boomaa.XKCDViewer.display;

import com.boomaa.XKCDViewer.utils.Listeners;
import com.boomaa.XKCDViewer.utils.StatsUtils;
import com.google.gson.JsonObject;
import com.boomaa.XKCDViewer.reporting.Console;
import com.boomaa.XKCDViewer.reporting.ErrorMessages;
import com.boomaa.XKCDViewer.reporting.PackageMap;
import com.boomaa.XKCDViewer.threading.TTS;
import com.boomaa.XKCDViewer.utils.DisplayUtils;
import com.boomaa.XKCDViewer.utils.JDEC;
import net.sf.image4j.codec.ico.ICODecoder;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

@SuppressWarnings("deprecation")
/** <p>Instigates display and houses main method</p> */
public class MainDisplay extends Listeners implements JDEC {
    /** <p>The number of pixels of the frame border.</p> */
    public static final int FRAME_BORDER = 300;
    /** <p>The number of the latest XKCD comic number.</p> */
    public static int LATEST_XKCD_NUM = 844;
    /** <p>The number of the currently displayed XKCD comic.</p> */
    public static int DISPLAYED_XKCD_NUM;
    /** <p>The number of bytes transferred in total.</p> */
    public static long TRANSFERRED_BYTES = 0;

    /** 
     * <p>Displays JFrame with everything added to it. Main running method.</p> 
     * @param args the default main method parameters.
     * */
    public static void main(String[] args) {
        SCALE_CHECKBOX.setSelected(true);
        try {
	        initAllFrame();
        } catch(IOException e) {
        	System.err.println(PackageMap.display.MAIN_DISPLAY + "ERROR: No internet connection");
        	MAIN_PANEL.add(new JLabel("No Internet Connection..."));
        	FRAME.add(MAIN_PANEL);
        	FRAME.setSize(300, 75);
        	FRAME.setVisible(true);
        	e.printStackTrace();
        }
    }
    
    /**
     * <p>Initializes and displays frame for first load-in.</p>
     * @throws IOException if a connectivity problem is detected.
     */
    private static void initAllFrame() throws IOException {
    	JsonObject jsonLatest = DisplayUtils.getJSONFromHTTP("https://xkcd.com/info.0.json");
        StatsUtils.addTransferredBytes("https://xkcd.com/info.0.json", "https://xkcd.com/s/919f27.ico");
        Image image = DisplayUtils.getImageFromJSON(jsonLatest);
        LATEST_XKCD_NUM = jsonLatest.getAsJsonPrimitive("num").getAsInt();

        FRAME.setIconImages(ICODecoder.read(new URL("https://xkcd.com/s/919f27.ico").openStream()));
        MAIN_PANEL.setLayout(new BoxLayout(MAIN_PANEL, BoxLayout.Y_AXIS));
        FRAME.getRootPane().setDefaultButton(NUM_BTN);
        FWD_BTN.setVisible(false);
        setupFrame(image);
        setupTitle(jsonLatest);
        setupScroll();

        JLabel imgTemp = new JLabel(new ImageIcon(image));
        imgTemp.setToolTipText(jsonLatest.getAsJsonPrimitive("alt").getAsString());
        imgTemp.addMouseListener(new TTSEnable());
        IMAGE_PANEL.add(imgTemp);
        DisplayUtils.addPanelComponents(IMAGE_POPUP, SAVE_IMAGE, DOWNLOAD, SELECT_LIST, LEADERBOARD, DEV_STATS, CONSOLE_OPEN, LOGIN);
        addFrameElements();
        addButtonListeners();

        IMAGE_PANEL.setComponentPopupMenu(IMAGE_POPUP);
        FRAME.setLocationRelativeTo(null);
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.setVisible(true);
        System.out.println(PackageMap.display.MAIN_DISPLAY + "Frame initialized correctly with components");
    }

    /**
     * <p>Reformats main panel and frame with new XKCD image from JSON.</p>
     * @param numReq the XKCD image number requested.
     */
    public static void panelRewrite(int numReq){
        JsonObject json = null; Image imgTemp = null;
        try {
            json = DisplayUtils.getJSONFromHTTP("https://xkcd.com/" + numReq + "/info.0.json");
            imgTemp = DisplayUtils.getImageFromJSON(json);
            StatsUtils.addTransferredBytes("https://xkcd.com/" + numReq + "/info.0.json");
        } catch (IOException e) {
            ErrorMessages.numInvalid();
        }
        if(IMAGE_POPUP.isAncestorOf(LOGIN) && Login.FTP_URL != null) {
        	IMAGE_POPUP.remove(LOGIN);
        	IMAGE_POPUP.repaint();
        }
        TEXT_INPUT.reset();
        DisplayUtils.removeAll(TITLE_PANEL, ERROR_PANEL, IMAGE_PANEL);
        setupTitle(json);
        
        JLabel imgLabel = new JLabel(new ImageIcon(imgTemp));
        imgLabel.setToolTipText(json.getAsJsonPrimitive("alt").getAsString());
        imgLabel.addMouseListener(new TTSEnable());
        IMAGE_PANEL.add(imgLabel);
        setupFrame(imgTemp);
        
        FWD_BTN.setVisible(!(DISPLAYED_XKCD_NUM == LATEST_XKCD_NUM));
        FRAME.setLocationRelativeTo(null);
        FRAME.revalidate();
        FRAME.repaint();
        System.out.println(PackageMap.display.MAIN_DISPLAY + "Display reset to image " + numReq + " successfully");
    }

    /**
     * <p>Sizes frame around XKCD image.</p>
     * @param image the image upon which sizing is based.
     */
    private static void setupFrame(Image image) {
        int minWidth = 300;
        int width = image.getWidth(FRAME) + FRAME_BORDER / 10;
        int height = image.getHeight(FRAME);
        int maxHeight = (int) (GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight());

        height += FRAME_BORDER;
        height = (height >= maxHeight) ? maxHeight : height;
        width = (width < minWidth) ? minWidth : width;

        FRAME.setSize(width, height);
        System.out.println(PackageMap.display.MAIN_DISPLAY + "Frame size determined and set");
    }

    /**
     * <p>Sets window title with currently displayed XKCD number.</p>
     * @param json the XKCD JSON to pull the displayed number from.
     */
	private static void setupTitle(JsonObject json) {
        TITLE_PANEL.add(new JLabel(json.getAsJsonPrimitive("title").getAsString() + " - #" + json.getAsJsonPrimitive("num").getAsString()));
        if(TTS_CHECKBOX.isSelected()) { new Thread(new TTS(json.getAsJsonPrimitive("title").getAsString())).start(); }
        FRAME.setTitle("XKCD Viewer | #" + json.getAsJsonPrimitive("num").getAsInt());
        DISPLAYED_XKCD_NUM = json.getAsJsonPrimitive("num").getAsInt();
        System.out.println(PackageMap.display.MAIN_DISPLAY + "Title setup finished");
    }

    /** <p>Determines if a vertical scrollbar is needed and displays if so.</p> */
    private static void setupScroll() {
        JScrollPane scroll = new JScrollPane(MAIN_PANEL, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        FRAME.add(scroll);
        System.out.println(PackageMap.display.MAIN_DISPLAY + "Scroll bar setup finished");
    }

    /** <p>Adds individual items to Select and Main JPanels.</p> */
    private static void addFrameElements() {
    	DisplayUtils.addPanelComponents(SCALING_PANEL, SCALE_CHECKBOX);
        DisplayUtils.addPanelComponents(VOTING_PANEL, new JLabel("Vote: "), UPVOTE_BTN, DOWNVOTE_BTN);

        DisplayUtils.addPanelComponents(SELECT_PANEL_UPPER, LATEST_BTN, RANDOM_BTN);
        DisplayUtils.addPanelComponents(SELECT_PANEL_MIDDLE, TEXT_INPUT, NUM_BTN);
        DisplayUtils.addPanelComponents(SELECT_PANEL_LOWER, BACK_BTN, FWD_BTN);
        
        DisplayUtils.addPanelComponents(MAIN_PANEL, TITLE_PANEL, IMAGE_PANEL, SCALING_PANEL, VOTING_PANEL,
        		SELECT_PANEL_UPPER, SELECT_PANEL_MIDDLE, SELECT_PANEL_LOWER, INET_CIRCLES, ERROR_PANEL);
        System.out.println(PackageMap.display.MAIN_DISPLAY + "All panel items added to respective super panels");
    }
    
    /** <p>Adds ActionListeners on buttons.</p> */
    private static void addButtonListeners() {
        LATEST_BTN.addActionListener(e -> { MainDisplay.panelRewrite(MainDisplay.LATEST_XKCD_NUM); });
        RANDOM_BTN.addActionListener(e -> { MainDisplay.panelRewrite((int) (Math.random() * MainDisplay.LATEST_XKCD_NUM)); });
        SELECT_LIST.addActionListener(e -> { SelectList.createSelectList(MainDisplay.DISPLAYED_XKCD_NUM); });
        SCALE_CHECKBOX.addActionListener(e -> { MainDisplay.panelRewrite(MainDisplay.DISPLAYED_XKCD_NUM); });
        FWD_BTN.addActionListener(e -> { MainDisplay.panelRewrite(MainDisplay.DISPLAYED_XKCD_NUM + 1); });
        BACK_BTN.addActionListener(e -> { MainDisplay.panelRewrite(MainDisplay.DISPLAYED_XKCD_NUM - 1); });
        DEV_STATS.addActionListener(e -> { new DevStats(); });
        CONSOLE_OPEN.addActionListener(e -> { new Console(); });
        LEADERBOARD.addActionListener(e -> { new Leaderboard(); });
        DOWNLOAD.addActionListener(e -> { new Download(); });
        LOGIN.addActionListener(e -> { new Login("Main"); });
        UPVOTE_BTN.addActionListener(new VoteAction(1));
        DOWNVOTE_BTN.addActionListener(new VoteAction(-1));
        NUM_BTN.addActionListener(new NumSelect());
        SAVE_IMAGE.addActionListener(new SaveAction());
        BROWSE_IMAGE.addActionListener(new BrowseAction());
        MAIN_PANEL.addMouseListener(new TTSEnable());
        System.out.println(PackageMap.display.MAIN_DISPLAY + "All button listeners added");
    }
}
