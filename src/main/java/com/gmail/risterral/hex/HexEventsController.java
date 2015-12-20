package com.gmail.risterral.hex;

import com.gmail.risterral.bot.BotController;
import com.gmail.risterral.bot.BotMessageType;
import com.gmail.risterral.hex.events.AbstractHexEvent;
import com.gmail.risterral.hex.events.EventParsingException;
import com.gmail.risterral.hex.events.dto.CardDTO;
import com.gmail.risterral.util.DraftController;
import com.gmail.risterral.util.log.LogController;
import com.gmail.risterral.util.log.LogMessageType;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HexEventsController {
    private static final HexEventsController instance = new HexEventsController();
    private final Logger LOG = Logger.getLogger(HexEventsController.class);

    private Thread thread = null;
    private HexListener hexListener = null;
    private List<CardDTO> draftPackCards = null;

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

    public void setNewDraftPackCards(ArrayList<CardDTO> cards) {
        if (cards == null || cards.isEmpty()) return;
        Collections.reverse(cards);
        if (draftPackCards != null && draftPackCards.equals(cards)) return;
        this.draftPackCards = cards;

        BotController.getInstance().sendMessage(prepareBotMessage(this.draftPackCards), BotMessageType.CARDS_LIST, null);
        BotController.getInstance().setListenEvents(true);
        DraftController.getInstance().setNewDraftPackCards(cards, true);
    }

    public void clearDraftPackCards(Boolean clearIfEnabled) {
        if (this.draftPackCards != null) {
            this.draftPackCards.clear();
            DraftController.getInstance().setNewDraftPackCards(new ArrayList<CardDTO>(), clearIfEnabled);
        }
    }

    public void setListenToDraft(boolean listenToDraft) {
        this.listenToDraft = listenToDraft;
        clearDraftPackCards(true);
    }

    public void cardPicked(String cardName) {
        DraftController.getInstance().pickCard(cardName, false);
    }

    private String prepareBotMessage(List<CardDTO> cards) {
        String message = "List of current cards: ";
        for (int i = 1; i <= cards.size(); i++) {
            message += i + ". [" + cards.get(i - 1).name + "] ";
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

                    if (content != null) {
                        LOG.debug("Event received: " + content);
                        try {
                            AbstractHexEvent event = AbstractHexEvent.getEvent(content);
                            event.call();
                        } catch (EventParsingException e) {
                            LogController.log(this.getClass(), e, LogMessageType.ERROR, "Unexpected event type.");
                        }
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
