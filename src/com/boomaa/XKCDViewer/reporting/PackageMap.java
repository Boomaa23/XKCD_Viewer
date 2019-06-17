package com.boomaa.XKCDViewer.reporting;

import com.boomaa.XKCDViewer.display.*;
import com.boomaa.XKCDViewer.draw.*;
import com.boomaa.XKCDViewer.threading.*;
import com.boomaa.XKCDViewer.utils.*;

/** <p>Stores names for all classes in nested interfaces (organized by package).</p> */
public interface PackageMap {
	/** <p>Name storage for classes in the display package.</p> */
	public static interface display {
		/** Stores name for {@link com.boomaa.XKCDViewer.display.DevStats} */
		final String DEV_STATS = "[" + DevStats.class.getSimpleName() + "] ";
		/** Stores name for {@link com.boomaa.XKCDViewer.display.Leaderboard} */
		final String LEADERBOARD = "[" + Leaderboard.class.getSimpleName() + "] ";
		/** Stores name for {@link com.boomaa.XKCDViewer.display.Login} */
		final String LOGIN = "[" + Login.class.getSimpleName() + "] ";
		/** Stores name for {@link com.boomaa.XKCDViewer.display.MainDisplay} */
		final String MAIN_DISPLAY = "[" + MainDisplay.class.getSimpleName() + "] ";
		/** Stores name for {@link com.boomaa.XKCDViewer.display.SelectList} */
		final String SELECT_LIST = "[" + SelectList.class.getSimpleName() + "] ";
		/** Stores name for {@link com.boomaa.XKCDViewer.display.Download} */
		final String DOWNLOAD = "[" + Download.class.getSimpleName() + "] ";
	}
	
	/** <p>Name storage for classes in the draw package.</p> */
	public static interface draw {
		/** Stores name for {@link com.boomaa.XKCDViewer.draw.InetCircles} */
		final String INET_CIRCLES = "[" + InetCircles.class.getSimpleName() + "] ";
		/** Stores name for {@link com.boomaa.XKCDViewer.draw.OverlayField} */
		final String OVERLAY_FIELD = "[" + OverlayField.class.getSimpleName() + "] ";
	}
	
	/** <p>Name storage for classes in the threading package.</p> */
	public static interface threading {
		/** Stores name for {@link com.boomaa.XKCDViewer.threading.ThreadManager} */
		final String THREAD_MANAGER = "[" + ThreadManager.class.getSimpleName() + "] ";
		/** Stores name for {@link com.boomaa.XKCDViewer.threading.TitleThread} */
		final String TITLE_THREAD = "[" + TitleThread.class.getSimpleName() + "] ";
		/** Stores name for {@link com.boomaa.XKCDViewer.threading.TTS} */
		@SuppressWarnings("deprecation")
		final String TTS = "[" + TTS.class.getSimpleName() + "] ";
	}
	
	/** <p>Name storage for classes in the utils package.</p> */
	public static interface utils {
		/** Stores name for {@link com.boomaa.XKCDViewer.utils.DisplayUtils} */
		final String DISPLAY_UTILS = "[" + DisplayUtils.class.getSimpleName() + "] ";
		/** Stores name for {@link com.boomaa.XKCDViewer.utils.JDEC} */
		final String JDEC = "[" + JDEC.class.getSimpleName() + "] ";
		/** Stores name for {@link com.boomaa.XKCDViewer.utils.Listeners} */
		final String LISTENERS = "[" + Listeners.class.getSimpleName() + "] ";
		/** Stores name for {@link com.boomaa.XKCDViewer.utils.StatsUtils} */
		final String STATS_UTILS = "[" + StatsUtils.class.getSimpleName() + "] ";
	}
	
	/** <p>Name storage for classes in the reporting package.</p> */
	public static interface reporting {
		/** Stores name for {@link com.boomaa.XKCDViewer.reporting.Console} */
		final String CONSOLE = "[" + Console.class.getSimpleName() + "] ";
		/** Stores name for {@link com.boomaa.XKCDViewer.reporting.ErrorMessages} */
		final String ERROR_MESSAGES = "[" + ErrorMessages.class.getSimpleName() + "] ";
		/** Stores name for {@link com.boomaa.XKCDViewer.reporting.PackageMap} */
		final String PACKAGE_MAP = "[" + PackageMap.class.getSimpleName() + "] ";
	}
}
