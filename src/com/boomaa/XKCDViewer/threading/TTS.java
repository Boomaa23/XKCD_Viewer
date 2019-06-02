package com.boomaa.XKCDViewer.threading;

import com.boomaa.XKCDViewer.utils.PackageMap;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory;

/** <p>Text-to-Speech on individual threads with passed String text.</p> */
public class TTS implements Runnable {
	/** <p>The voice in which to say the TTS message.</p> */
	private Voice voice;
	/** <p>The message to speak when run.</p> */
	private String message;
	
	/**
	 * <p>Sets text to speak for TTS.</p>
	 * @param message the text to speak.
	 */
	public TTS(String message) {
		voice = new KevinVoiceDirectory().getVoices()[1];
		this.message = message;
	}
	
	@Override
	public void run() {
		System.out.println(PackageMap.threading.TTS + "TTS active");
		voice.allocate();
		voice.speak(this.message);
		voice.deallocate();
	}
}
