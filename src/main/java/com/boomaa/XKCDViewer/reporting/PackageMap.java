package com.boomaa.XKCDViewer.reporting;

import com.boomaa.XKCDViewer.display.*;
import com.boomaa.XKCDViewer.draw.*;
import com.boomaa.XKCDViewer.threading.*;
import com.boomaa.XKCDViewer.utils.*;

/** <p>Stores names for all classes in nested interfaces (organized by package).</p> */
public interface PackageMap {
    /** <p>Name storage for classes in the display package.</p> */
    interface display {
        /** Stores name for {@link com.boomaa.XKCDViewer.display.DevStats} */
        String DEV_STATS = "[" + DevStats.class.getSimpleName() + "] ";
        /** Stores name for {@link com.boomaa.XKCDViewer.display.Leaderboard} */
        String LEADERBOARD = "[" + Leaderboard.class.getSimpleName() + "] ";
        /** Stores name for {@link com.boomaa.XKCDViewer.display.Login} */
        String LOGIN = "[" + Login.class.getSimpleName() + "] ";
        /** Stores name for {@link com.boomaa.XKCDViewer.display.MainDisplay} */
        String MAIN_DISPLAY = "[" + MainDisplay.class.getSimpleName() + "] ";
        /** Stores name for {@link com.boomaa.XKCDViewer.display.SelectList} */
        String SELECT_LIST = "[" + SelectList.class.getSimpleName() + "] ";
        /** Stores name for {@link com.boomaa.XKCDViewer.display.Download} */
        String DOWNLOAD = "[" + Download.class.getSimpleName() + "] ";
        /** Stores name for {@link com.boomaa.XKCDViewer.display.ExplainXKCD} */
        String EXPLAIN_XKCD = "[" + ExplainXKCD.class.getSimpleName() + "] ";
    }

    /** <p>Name storage for classes in the draw package.</p> */
    interface draw {
        /** Stores name for {@link com.boomaa.XKCDViewer.draw.InetCircles} */
        String INET_CIRCLES = "[" + InetCircles.class.getSimpleName() + "] ";
        /** Stores name for {@link com.boomaa.XKCDViewer.draw.OverlayField} */
        String OVERLAY_FIELD = "[" + OverlayField.class.getSimpleName() + "] ";
    }

    /** <p>Name storage for classes in the threading package.</p> */
    interface threading {
        /** Stores name for {@link com.boomaa.XKCDViewer.threading.ThreadManager} */
        String THREAD_MANAGER = "[" + ThreadManager.class.getSimpleName() + "] ";
        /** Stores name for {@link com.boomaa.XKCDViewer.threading.TitleThread} */
        String TITLE_THREAD = "[" + TitleThread.class.getSimpleName() + "] ";
        /** Stores name for {@link com.boomaa.XKCDViewer.threading.TTS} */
        @SuppressWarnings("deprecation")
        String TTS = "[" + TTS.class.getSimpleName() + "] ";
    }

    /** <p>Name storage for classes in the utils package.</p> */
    interface utils {
        /** Stores name for {@link com.boomaa.XKCDViewer.utils.DisplayUtils} */
        String DISPLAY_UTILS = "[" + DisplayUtils.class.getSimpleName() + "] ";
        /** Stores name for {@link com.boomaa.XKCDViewer.utils.JDEC} */
        String JDEC = "[" + JDEC.class.getSimpleName() + "] ";
        /** Stores name for {@link com.boomaa.XKCDViewer.utils.Listeners} */
        String LISTENERS = "[" + Listeners.class.getSimpleName() + "] ";
        /** Stores name for {@link com.boomaa.XKCDViewer.utils.StatsUtils} */
        String STATS_UTILS = "[" + StatsUtils.class.getSimpleName() + "] ";
        /** Stores name for {@link com.boomaa.XKCDViewer.utils.HTMLToPlainText} */
        String HTML_PLAINTEXT = "[" + HTMLToPlainText.class.getSimpleName() + "] ";
    }

    /** <p>Name storage for classes in the reporting package.</p> */
    interface reporting {
        /** Stores name for {@link com.boomaa.XKCDViewer.reporting.Console} */
        String CONSOLE = "[" + Console.class.getSimpleName() + "] ";
        /** Stores name for {@link com.boomaa.XKCDViewer.reporting.ErrorMessages} */
        String ERROR_MESSAGES = "[" + ErrorMessages.class.getSimpleName() + "] ";
        /** Stores name for {@link com.boomaa.XKCDViewer.reporting.PackageMap} */
        String PACKAGE_MAP = "[" + PackageMap.class.getSimpleName() + "] ";
    }
}
