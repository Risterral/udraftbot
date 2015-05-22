package com.gmail.risterral.controllers.bot;

import com.gmail.risterral.bot.TwitchBot;
import com.gmail.risterral.controllers.gui.GUIController;
import com.gmail.risterral.controllers.log.LogController;
import com.gmail.risterral.controllers.log.LogMessageType;

import java.util.*;

public class BotController {
    private static final BotController instance = new BotController();
    private static final String OAUTH_PREFIX = "oauth:";
    private static final String CHAT_PREFIX = "#";
    private static final int MESSAGE_SEND_TIME_LIMIT_WITH_MOD = 500;
    private static final int MESSAGE_SEND_TIME_LIMIT_WITHOUT_MOD = 1500;

    private TwitchBot bot = null;
    private String channel;
    private Long lastMessageSendTime;
    private volatile boolean isTimerRunning = false;
    private volatile ArrayList<Pair<String, BotMessageType>> messagesToSend = new ArrayList<>();

    private BotController() {
    }

    public static BotController getInstance() {
        return instance;
    }

    public boolean connect(String serverHostname, Integer serverPort, String botName, String password, String channel) {
        try {
            if (bot != null) {
                bot.terminate();
                bot = null;
            }
            bot = new TwitchBot(botName);
            bot.setVerbose(true);
            bot.connect(serverHostname, serverPort, password.startsWith(OAUTH_PREFIX) ? password : OAUTH_PREFIX + password);

            if (bot.isConnected() && bot.isLoginSuccessful()) {
                this.channel = channel.startsWith(CHAT_PREFIX) ? channel : CHAT_PREFIX + channel;
                bot.joinChannel(this.channel);
                lastMessageSendTime = new Date().getTime();
                LogController.log(this.getClass(), null, LogMessageType.SUCCESS, "Successfully logged in to server.");
                return true;
            } else if (!bot.isLoginSuccessful()) {
                LogController.log(this.getClass(), null, LogMessageType.ERROR, "Error, login unsuccessful.");
                return false;
            } else {
                LogController.log(this.getClass(), null, LogMessageType.ERROR, "Error, cannot connect to server.");
                bot.quitServer();
                return false;
            }
        } catch (Exception e) {
            LogController.log(this.getClass(), e, LogMessageType.ERROR, "Unexpected error while connecting to server.");
            return false;
        }
    }

    public void disconnect() {
        if (bot != null) {
            bot.terminate();
            bot = null;
            LogController.log(this.getClass(), null, LogMessageType.SUCCESS, "Disconnected from server.");
        }
    }

    public void sendMessage(final String message, final BotMessageType messageType) {
        if (bot != null) {
            Integer messageSendTimeLimit = GUIController.getInstance().isBotAccountModded() ? MESSAGE_SEND_TIME_LIMIT_WITH_MOD : MESSAGE_SEND_TIME_LIMIT_WITHOUT_MOD;
            Long currentTime = new Date().getTime();
            if (!isTimerRunning && currentTime - lastMessageSendTime > messageSendTimeLimit) {
                bot.sendMessage(channel, message);
                lastMessageSendTime = new Date().getTime();
                if (!messagesToSend.isEmpty()) {
                    isTimerRunning = true;
                    new Timer().schedule(new SendMessageTask(), messageSendTimeLimit);
                }
            } else {
                messagesToSend.add(new Pair<>(message, messageType));
                organizeMessages();
                if (!isTimerRunning) {
                    isTimerRunning = true;
                    new Timer().schedule(new SendMessageTask(), messageSendTimeLimit - currentTime + lastMessageSendTime);
                }
            }
        }
    }

    public void setListenEvents(boolean listenEvents) {
        bot.setListenEvents(listenEvents);
    }

    private synchronized void organizeMessages() {
        ArrayList<Pair<String, BotMessageType>> groupedList = new ArrayList<>();
        HashMap<BotMessageType, ArrayList<String>> priorityMap = new HashMap<>();
        for (Pair<String, BotMessageType> pair : messagesToSend) {
            if (pair.getValue().isTryToGroup()) {
                if (priorityMap.containsKey(pair.getValue())) {
                    ArrayList<String> messages = priorityMap.get(pair.getValue());
                    if (!messages.contains(pair.getKey())) {
                        messages.add(pair.getKey());
                    }
                } else {
                    priorityMap.put(pair.getValue(), new ArrayList<>(Arrays.asList(pair.getKey())));
                }
            } else {
                groupedList.add(pair);
            }
        }

        for (BotMessageType messageType : priorityMap.keySet()) {
            String groupedMessage = "";
            String separator = "";
            for (String message : priorityMap.get(messageType)) {
                groupedMessage += separator + message;
                separator = " ";
            }
            groupedList.add(new Pair<>(groupedMessage, messageType));
        }

        messagesToSend = groupedList;

        Collections.sort(messagesToSend, new Comparator<Pair<String, BotMessageType>>() {
            @Override
            public int compare(Pair<String, BotMessageType> o1, Pair<String, BotMessageType> o2) {
                return o1.getValue().getPriority() - o2.getValue().getPriority();
            }
        });
    }

    private class SendMessageTask extends TimerTask {

        @Override
        public void run() {
            Pair<String, BotMessageType> messageToSend = messagesToSend.get(0);
            messagesToSend.remove(messageToSend);
            isTimerRunning = false;
            sendMessage(messageToSend.getKey(), messageToSend.getValue());
        }
    }
}
