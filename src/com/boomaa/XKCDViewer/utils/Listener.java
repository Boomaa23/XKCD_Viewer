package com.boomaa.XKCDViewer.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import org.json.JSONException;

import com.boomaa.XKCDViewer.display.Display;

/** <p>Nested ActionListener classes with getters</p> */
public class Listener {
	/** @deprecated No constructor needed, Listener is a wrapper class for other listeners. */
	public Listener() {}
	
	
	/** 
	 * <p>Getter for forward action class.</p>
	 * @return static nested FwdAction class 
	 */
	public static FwdAction fwdAction() { return new FwdAction(); }
	
	/** 
	 * <p>Getter for back action class.</p>
	 * @return static nested BackAction class 
	 */
	public static BackAction backAction() { return new BackAction(); }
	
	/** 
	 * <p>Getter for num select class.</p>
	 * @return static nested NumSelect class 
	 */
	public static NumSelect numSelect() { return new NumSelect(); }
	
	/** 
	 * <p>Getter for random select class.</p>
	 * @return static nested RandomSelect class 
	 */
	public static RandomSelect randomSelect() { return new RandomSelect(); }
	
	/** 
	 * <p>Getter for save action class.</p>
	 * @return static nested SaveAction class 
	 */
	public static SaveAction saveAction() { return new SaveAction(); }
	
	
	/** <p>Increments displayed XKCD image upon actionPerformed()</p> */
	static class FwdAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e0) {
			try {
				Display.panelRewrite(Display.DISPLAYED_XKCD_NUM + 1);
			} catch (JSONException e1) {
				Display.resetOnJSONError();
			}
		}
	}
	
	/** <p>Decrements displayed XKCD image upon actionPerformed()</p> */
	static class BackAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e0) {
			try {
				Display.panelRewrite(Display.DISPLAYED_XKCD_NUM - 1);
			} catch (JSONException e1) {
				Display.resetOnJSONError();
			}
		}
	}
	
	/** <p>Navigates to and displays input XKCD imgage upon actionPerformed()</p> */
	static class NumSelect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int numRequest = 0;
			try {
				numRequest = Integer.parseInt(JDEC.TEXT_INPUT.getText());
				if(!JDEC.TEXT_INPUT.getText().isEmpty() && numRequest <= Display.LATEST_XKCD_NUM && numRequest > 0) {
					Display.panelRewrite(numRequest);
				} else {
					Display.resetOnJSONError();
				}
			} catch (NumberFormatException e0) {
				Display.resetOnJSONError();
			}
		}
	}
	
	/** <p>Selects and displays a random XKCD image upon actionPerformed()</p> */
	static class RandomSelect implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Display.panelRewrite((int)(Math.random() * Display.LATEST_XKCD_NUM));
		}
	}
	
	/** <p>Saves XKCD image currently displayed upon actionPerformed()</p> */
	static class SaveAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				JSONUtils.saveImage(JSONUtils.readJSONFromUrl("https://xkcd.com/" + Display.DISPLAYED_XKCD_NUM + "/info.0.json"));
			} catch (JSONException | IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
