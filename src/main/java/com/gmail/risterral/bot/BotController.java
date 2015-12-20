package com.gmail.risterral.bot;

import com.gmail.risterral.gui.GUIController;
import com.gmail.risterral.util.log.LogController;
import com.gmail.risterral.util.log.LogMessageType;

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
    private volatile ArrayList<Message> messagesToSend = new ArrayList<>();

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
                this.channel = channel.startsWith(CHAT_PREFIX) ? channel.toLowerCase() : CHAT_PREFIX + channel.toLowerCase();
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

    public void sendMessage(final String message, final BotMessageType messageType, final Integer innerPriority) {
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
                messagesToSend.add(new Message(message, messageType, innerPriority));
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
        ArrayList<Message> groupedList = new ArrayList<>();
        HashMap<BotMessageType, ArrayList<String>> priorityMap = new HashMap<>();
        for (Message message : messagesToSend) {
            if (message.getMessageType().isTryToGroup()) {
                if (priorityMap.containsKey(message.getMessageType())) {
                    ArrayList<String> messages = priorityMap.get(message.getMessageType());
                    if (!messages.contains(message.getMessage())) {
                        messages.add(message.getMessage());
                    }
                } else {
                    priorityMap.put(message.getMessageType(), new ArrayList<>(Arrays.asList(message.getMessage())));
                }
            } else {
                groupedList.add(message);
            }
        }

        for (BotMessageType messageType : priorityMap.keySet()) {
            String groupedMessage = "";
            String separator = "";
            for (String message : priorityMap.get(messageType)) {
                groupedMessage += separator + message;
                separator = " ";
            }
            groupedList.add(new Message(groupedMessage, messageType, null));
        }

        messagesToSend = groupedList;

        Collections.sort(messagesToSend, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                if (!o1.getMessageType().getPriority().equals(o2.getMessageType().getPriority()))
                    return o1.getMessageType().getPriority() - o2.getMessageType().getPriority();
                if (o1.getInnerPriority() != null && o2.getInnerPriority() != null)
                    return o1.getInnerPriority() - o2.getInnerPriority();
                if (o1.getInnerPriority() != null) return 1;
                if (o2.getInnerPriority() != null) return -1;
                return 0;
            }
        });
    }

    private class SendMessageTask extends TimerTask {

        @Override
        public void run() {
            Message messageToSend = messagesToSend.get(0);
            messagesToSend.remove(messageToSend);
            isTimerRunning = false;
            sendMessage(messageToSend.getMessage(), messageToSend.getMessageType(), messageToSend.getInnerPriority());
        }
    }

    private class Message {
        private final String message;
        private final BotMessageType messageType;
        private final Integer innerPriority;

        public Message(String message, BotMessageType messageType, Integer innerPriority) {
            this.message = message;
            this.messageType = messageType;
            this.innerPriority = innerPriority;
        }

        public String getMessage() {
            return message;
        }

        public BotMessageType getMessageType() {
            return messageType;
        }

        public Integer getInnerPriority() {
            return innerPriority;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Message)) return false;

            Message message1 = (Message) o;

            if (innerPriority != null ? !innerPriority.equals(message1.innerPriority) : message1.innerPriority != null)
                return false;
            if (message != null ? !message.equals(message1.message) : message1.message != null) return false;
            if (messageType != message1.messageType) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = message != null ? message.hashCode() : 0;
            result = 31 * result + (messageType != null ? messageType.hashCode() : 0);
            result = 31 * result + (innerPriority != null ? innerPriority.hashCode() : 0);
            return result;
        }
    }
}
