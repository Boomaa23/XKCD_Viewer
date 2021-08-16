package com.boomaa.XKCDViewer.display;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.boomaa.XKCDViewer.reporting.PackageMap;
import com.boomaa.XKCDViewer.utils.HTMLToPlainText;

/** <p>A popup window displaying the ExplainXKCD for the currently displayed XKCD image.</p> */
public class ExplainXKCD {
    /** <p>The main display frame.</p> */
    private final JFrame frame = new JFrame("ExplainXKCD | " + MainDisplay.DISPLAYED_XKCD_NUM);
    /** <p>The main panel to add components to.</p> */
    private final JPanel mainPanel = new JPanel();
    /** <p>The width of the frame and text.</p> */
    private final int width = 500;
    /** <p>The height of the frame and text.</p> */
    private final int height = 700;

    /** <p>Pulls ExplainXKCD website html, parses for #Explanation through #Transcript, and formats into plaintext.</p> */
    public ExplainXKCD() {
        try {
            Elements body = Jsoup.connect("https://explainxkcd.com/wiki/index.php/" + MainDisplay.DISPLAYED_XKCD_NUM).get().select(".mw-parser-output");
            StringBuilder parsed = new StringBuilder();
            HTMLToPlainText formatter = new HTMLToPlainText();
            for(Element e : body) { parsed.append(formatter.getPlainText(e)); }
            System.out.println(PackageMap.display.EXPLAIN_XKCD + "Web content retrieved and formatted");

            String text = parsed.toString().substring(parsed.toString().indexOf("Explanation"), parsed.toString().indexOf("Transcript")).replaceAll("\\[edit\\]", "");
            System.out.println(PackageMap.display.EXPLAIN_XKCD + "Request body trimmed to explanation");
            JTextArea area = new JTextArea(text);
            area.setSize(width, height);
            area.setLineWrap(true);
            area.setEditable(false);

            JScrollPane scroll = new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.getVerticalScrollBar().setUnitIncrement(20);
            mainPanel.add(area);
            frame.add(scroll);
            frame.setIconImage(ImageIO.read(new URL("https://www.explainxkcd.com/wiki/images/0/04/16px-BlackHat_head.png")));
            mainPanel.setBackground(Color.WHITE);
            frame.setSize(width, height);
            frame.setVisible(true);
            System.out.println(PackageMap.display.EXPLAIN_XKCD + "Explanation displayed on frame successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
