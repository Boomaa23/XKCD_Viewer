package com.boomaa.XKCDViewer.utils;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

/** <p>Text-to-Speech on individual threads with passed String text.</p> */
public class TTS implements Runnable {
	/** <p>The voice in which to say the TTS message.</p> */
	private Voice voice = VoiceManager.getInstance().getVoice("kevin16");
	/** <p>The message to speak when run.</p> */
	private String message;
	
	/**
	 * <p>Sets text to speak for TTS.</p>
	 * @param message the text to speak.
	 */
	public TTS(String message) {
		this.message = message;
	}
	
	@Override
	public void run() {
		voice.allocate();
		voice.speak(this.message);
		voice.deallocate();
	}
}
