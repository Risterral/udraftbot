package com.gmail.risterral.bot;

import com.gmail.risterral.bot.events.BotEvents;
import com.gmail.risterral.gui.GUIController;
import com.gmail.risterral.hex.HexEventsController;
import com.gmail.risterral.util.configuration.ConfigurationController;
import com.gmail.risterral.util.log.LogController;
import com.gmail.risterral.util.log.LogMessageType;
import org.jibble.pircbot.PircBot;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TwitchBot extends PircBot {
    public static final int MAXIMUM_NUMBER_OF_RECONNECT_ATEPMTS = 5;
    private volatile boolean running = true;
    private boolean isLoginSuccessful;
    private boolean listenEvents;
    private int reconnectAttempts = 0;
    private Timer reconnectTimer = null;

    public TwitchBot(String name) {
        this.setName(name);
        this.isLoginSuccessful = true;
        this.listenEvents = true;
    }

    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        for (BotEvents botEvent : BotEvents.values()) {
            if (message.toUpperCase().startsWith(botEvent.getEventCommand().toUpperCase())) {
                Boolean isCommandEnabled = ConfigurationController.getInstance().getConfigurationDTO().getCommands().get(botEvent.name());
                if ((isCommandEnabled != null && isCommandEnabled) || (isCommandEnabled == null && botEvent.getIsDefaultEnabled())) {
                    botEvent.getEvent().call(sender, message.trim().split("\\s+"));
                }
            }
        }
    }

    @Override
    protected void onDisconnect() {
        if (running) {
            LogController.log(this.getClass(), null, LogMessageType.ERROR, "Error, disconnected from server. Trying to reconnect.");
            try {
                reconnect();
            } catch (Exception e) {
                reconnectAttempts = 1;
                reconnectTimer = new Timer(5000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        LogController.log(this.getClass(), null, LogMessageType.ERROR, "Trying to reconnect.");
                        try {
                            reconnect();
                        } catch (Exception ignored) {
                            if (++reconnectAttempts >= MAXIMUM_NUMBER_OF_RECONNECT_ATEPMTS) {
                                LogController.log(this.getClass(), null, LogMessageType.ERROR, "Exceeded maximum number of reconnect attempts.");
                                HexEventsController.getInstance().disconnect();
                                GUIController.getInstance().setConnectButtonEnabled(true);
                                reconnectTimer.stop();
                            }
                        }
                    }
                });
                reconnectTimer.start();
            }
        } else {
            super.onDisconnect();
        }
    }

    @Override
    public void log(String s) {
        if (s.contains("Login unsuccessful") || s.contains("Error logging in")) {
            this.isLoginSuccessful = false;
            this.running = false;
        }
        super.log(s);
    }

    public void terminate() {
        this.running = false;
        this.disconnect();
    }

    public boolean isLoginSuccessful() {
        return isLoginSuccessful;
    }

    public void setListenEvents(boolean listenEvents) {
        this.listenEvents = listenEvents;
    }
}
