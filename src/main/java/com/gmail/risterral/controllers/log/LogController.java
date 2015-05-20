package com.gmail.risterral.controllers.log;

import com.gmail.risterral.controllers.gui.GUIController;
import org.apache.log4j.Logger;

import java.util.Date;

public class LogController {
    public static void log(Class clazz, Exception e, LogMessageType type, String message) {
        LogMessageDTO logMessageDTO = new LogMessageDTO(new Date() + ": " + message, type, type.getErrorDuration(), new Date().getTime());
        GUIController.getInstance().logMessage(logMessageDTO);

        if (LogMessageType.ERROR.equals(type)) {
            Logger.getLogger(clazz).error(message, e);
        }
    }
}
