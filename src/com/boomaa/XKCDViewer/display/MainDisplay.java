package com.boomaa.XKCDViewer.display;

import com.boomaa.XKCDViewer.utils.Listeners;
import com.boomaa.XKCDViewer.utils.StatsUtils;
import com.google.gson.JsonObject;
import com.boomaa.XKCDViewer.utils.DisplayUtils;
import com.boomaa.XKCDViewer.utils.JDEC;
import net.sf.image4j.codec.ico.ICODecoder;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/** <p>Instigates display and houses main method</p> */
public class MainDisplay extends Listeners implements JDEC {
    /** <p>The number of pixels of the frame border.</p> */
    public static final int FRAME_BORDER = 20;
    /** <p>The number of the latest XKCD comic number.</p> */
    public static int LATEST_XKCD_NUM = 844;
    /** <p>The number of the currently displayed XKCD comic.</p> */
    public static int DISPLAYED_XKCD_NUM;
    /** <p>The number of bytes transferred in total.</p> */
    public static long TRANSFERRED_BYTES = 0;

    public MainDisplay() {}

    /**
     * <p>Displays JFrame with everything added to it. Main running method.</p>
     * @param args default main method.
     */
    public static void main(String[] args) {
        SCALE_CHECKBOX.setSelected(true);
        try {
	        initAllFrame();
        } catch(IOException e) {
        	MAIN_PANEL.add(new JLabel("Internet connection not found..."));
        	FRAME.add(MAIN_PANEL);
        	FRAME.setSize(300, 65);
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
        IMAGE_PANEL.add(imgTemp);
        addImagePopup();
        addFrameElements();
        addButtonListeners();

        IMAGE_PANEL.setComponentPopupMenu(IMAGE_POPUP);
        FRAME.setLocationRelativeTo(null);
        FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FRAME.setVisible(true);
    }

    /**
     * <p>Reformats main panel and frame with new XKCD image from JSON.</p>
     * @param numReq the XKCD image number requested.
     */
    public static void panelRewrite(int numReq) {
        JsonObject json = null;
        try {
            json = DisplayUtils.getJSONFromHTTP("https://xkcd.com/" + numReq + "/info.0.json");
            StatsUtils.addTransferredBytes("https://xkcd.com/" + numReq + "/info.0.json");
        } catch (IOException e) {
            resetOnJSONError();
        }
        TEXT_INPUT.reset();
        TITLE_PANEL.removeAll();
        ERROR_PANEL.removeAll();
        setupTitle(json);
        FWD_BTN.setVisible(!(DISPLAYED_XKCD_NUM == LATEST_XKCD_NUM));

        IMAGE_PANEL.removeAll();
        Image imgTemp = null;
        try {
            imgTemp = DisplayUtils.getImageFromJSON(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel imgLabel = new JLabel(new ImageIcon(imgTemp));
        imgLabel.setToolTipText(json.getAsJsonPrimitive("alt").getAsString());
        IMAGE_PANEL.add(imgLabel);

        setupFrame(imgTemp);
        FRAME.setLocationRelativeTo(null);
        FRAME.revalidate();
        FRAME.repaint();
    }

    /**
     * <p>Sizes frame around XKCD image.</p>
     * @param image the image upon which sizing is based.
     */
    private static void setupFrame(Image image) {
        int minWidth = 300;
        int width = image.getWidth(FRAME) + 2 * FRAME_BORDER;
        int height = image.getHeight(FRAME);
        int maxHeight = (int) (GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getHeight());

        height += 12 * FRAME_BORDER;
        height = (height >= maxHeight) ? maxHeight : height;
        width = (width < minWidth) ? minWidth : width;

        FRAME.setSize(width, height);
    }

    /**
     * <p>Sets window title with currently displayed XKCD number.</p>
     * @param json the XKCD JSON to pull the displayed number from.
     */
    private static void setupTitle(JsonObject json) {
        TITLE_PANEL.add(new JLabel(json.getAsJsonPrimitive("title").getAsString() + " - #" + json.getAsJsonPrimitive("num").getAsString()));
        FRAME.setTitle("XKCD Viewer | #" + json.getAsJsonPrimitive("num").getAsInt());
        DISPLAYED_XKCD_NUM = json.getAsJsonPrimitive("num").getAsInt();
    }

    /** <p>Determines if a vertical scrollbar is needed and displays if so.</p> */
    private static void setupScroll() {
        JScrollPane scroll = new JScrollPane(MAIN_PANEL,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        FRAME.add(scroll);
    }

    /** <p>Adds individual items to Select and Main JPanels.</p> */
    private static void addFrameElements() {
        SCALING_PANEL.add(SCALE_CHECKBOX);

        SELECT_PANEL_UPPER.add(LATEST_BTN);
        SELECT_PANEL_UPPER.add(RANDOM_BTN);
        SELECT_PANEL_MIDDLE.add(TEXT_INPUT);
        SELECT_PANEL_MIDDLE.add(NUM_BTN);
        SELECT_PANEL_LOWER.add(BACK_BTN);
        SELECT_PANEL_LOWER.add(FWD_BTN);

        MAIN_PANEL.add(TITLE_PANEL);
        MAIN_PANEL.add(IMAGE_PANEL);
        MAIN_PANEL.add(SCALING_PANEL);
        MAIN_PANEL.add(SELECT_PANEL_UPPER);
        MAIN_PANEL.add(SELECT_PANEL_MIDDLE);
        MAIN_PANEL.add(SELECT_PANEL_LOWER);
        MAIN_PANEL.add(INET_CIRCLES);
        
        MAIN_PANEL.add(ERROR_PANEL);
    }
    
    /** <p>Adds options to rightclick on image popup.</p> */
    private static void addImagePopup() {
    	IMAGE_POPUP.add(SAVE_IMAGE);
        IMAGE_POPUP.add(BROWSE_IMAGE);
        IMAGE_POPUP.add(SELECT_LIST);
        IMAGE_POPUP.add(LEADERBOARD);
        IMAGE_POPUP.add(DEV_STATS);
        IMAGE_POPUP.add(CONSOLE_OPEN);
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
        NUM_BTN.addActionListener(new NumSelect());
        SAVE_IMAGE.addActionListener(new SaveAction());
        BROWSE_IMAGE.addActionListener(new BrowseAction());
    }
    
    /** <p>Error message display for JSON retrieval error.</p> */
    public static void resetOnJSONError() {
        ERROR_PANEL.removeAll();
        ERROR_PANEL.add(new JLabel("ERROR: No XKCD found for this number"));
        FRAME.revalidate();
        FRAME.repaint();
    }
}
