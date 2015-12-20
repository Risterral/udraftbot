package com.gmail.risterral.util.log;

import com.gmail.risterral.gui.GUIController;
import org.apache.log4j.Logger;

import java.util.Date;

public class LogController {
    public static void log(Class clazz, Exception e, LogMessageType type, String message) {
        log(clazz, e, type, message, true);
    }

    public static void log(Class clazz, Exception e, LogMessageType type, String message, boolean logMessageToWindow) {
        if (logMessageToWindow) {
            LogMessageDTO logMessageDTO = new LogMessageDTO(new Date() + ": " + message, type, type.getErrorDuration(), new Date().getTime());
            GUIController.getInstance().logMessage(logMessageDTO);
        }

        if (LogMessageType.ERROR.equals(type)) {
            Logger.getLogger(clazz).error(message, e);
        }
    }
}
