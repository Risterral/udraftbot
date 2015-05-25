package com.gmail.risterral.controllers.hex;

import com.gmail.risterral.controllers.bot.BotController;
import com.gmail.risterral.controllers.bot.BotMessageType;
import com.gmail.risterral.controllers.log.LogController;
import com.gmail.risterral.controllers.log.LogMessageType;
import com.gmail.risterral.controllers.vote.VotingController;
import com.gmail.risterral.events.AbstractHexEvent;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HexEventsController {
    private static final HexEventsController instance = new HexEventsController();
    private final Logger LOG = Logger.getLogger(HexEventsController.class);

    private Thread thread = null;
    private HexListener hexListener = null;
    private List<String> draftPackCards = null;

    private boolean listenToDraft = false;

    private HexEventsController() {

    }

    public static HexEventsController getInstance() {
        return instance;
    }

    public boolean connect(Integer port) {
        try {
            final ServerSocket serverSocket = new ServerSocket(port);
            hexListener = new HexListener(serverSocket);
            thread = new Thread(hexListener);
            thread.start();
            LogController.log(this.getClass(), null, LogMessageType.SUCCESS, "Successfully started listening to hex client at port " + port + ".");
            return true;
        } catch (Exception e) {
            LogController.log(this.getClass(), e, LogMessageType.ERROR, "Unexpected error while starting listening to hex client.");
            return false;
        }
    }

    public void disconnect() {
        if (thread != null) {
            try {
                hexListener.terminate();
                thread.join();
                thread = null;
                LogController.log(this.getClass(), null, LogMessageType.SUCCESS, "Stopped listening to hex client.");
            } catch (IOException | InterruptedException ignored) {
            }
        }
    }

    public void setNewDraftPackCards(List<String> cards) {
        if (draftPackCards != null && draftPackCards.equals(cards)) return;

        this.draftPackCards = cards;

        BotController.getInstance().sendMessage(prepareBotMessage(this.draftPackCards), BotMessageType.CARDS_LIST);
        BotController.getInstance().setListenEvents(true);
        VotingController.getInstance().setNewDraftPackCards(cards);
    }

    public void clearDraftPackCards() {
        if (this.draftPackCards != null) {
            this.draftPackCards.clear();
            VotingController.getInstance().setNewDraftPackCards(new ArrayList<String>());
        }
    }

    public void setListenToDraft(boolean listenToDraft) {
        this.listenToDraft = listenToDraft;
        clearDraftPackCards();
    }

    public void cardPicked(String cardName) {
        BotController.getInstance().sendMessage("Card picked: " + cardName, BotMessageType.CARDS_PICKED);
    }

    private String prepareBotMessage(List<String> cards) {
        String message = "List of current cards: ";
        for (int i = 1; i <= cards.size(); i++) {
            message += i + ". [" + cards.get(i - 1) + "] ";
        }
        return message;
    }

    public boolean isListenToDraft() {
        return listenToDraft;
    }

    private class HexListener implements Runnable {
        private volatile boolean running = true;
        private final ServerSocket serverSocket;

        public HexListener(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        public void terminate() throws IOException {
            running = false;
            serverSocket.close();
        }

        @Override
        public void run() {
            try {
                Socket socket;
                while (running && (socket = serverSocket.accept()) != null) {
                    HexSocket hexSocket = new HexSocket(socket);

                    String content = hexSocket.getContent(true);

                    LOG.debug("Event received: " + content);

                    AbstractHexEvent event = AbstractHexEvent.getEvent(content);

                    if (event != null) {
                        event.call();
                    }
                }
            } catch (Exception e) {
                if (running) {
                    LogController.log(this.getClass(), e, LogMessageType.ERROR, "Unexpected error while listening events from hex client.");
                }
            }
        }
    }
}
