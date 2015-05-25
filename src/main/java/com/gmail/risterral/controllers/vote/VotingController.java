package com.gmail.risterral.controllers.vote;

import com.gmail.risterral.controllers.bot.BotController;
import com.gmail.risterral.controllers.bot.BotMessageType;
import com.gmail.risterral.controllers.gui.GUIController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class VotingController {
    private static final VotingController instance = new VotingController();

    private static final String IMAGE_PREFIX = "https://hextcg.com/wp-content/themes/hex/images/autocard/";
    private static final String IMAGE_EXTENSION = ".png";

    private Random randomGenerator = new Random();
    private LinkedHashMap<String, Integer> cards;
    private LinkedHashMap<String, String> votes;

    private VotingController() {
    }

    public static VotingController getInstance() {
        return instance;
    }

    public void setNewDraftPackCards(List<String> cards) {
        this.cards = new LinkedHashMap<>();
        this.votes = new LinkedHashMap<>();

        for (String card : cards) {
            this.cards.put(card, 0);
        }

        setVotesMap();
    }

    public void voteForCard(String sender, Integer cardIndex) {
        if (cardIndex >= 0 && cards != null && cardIndex < cards.size()) {
            voteForCard(sender, new ArrayList<>(cards.keySet()).get(cardIndex));
        }
    }

    public void voteForCard(String sender, String card) {
        if (cards.containsKey(card)) {
            if (votes.containsKey(sender)) {
                String lastCard = votes.get(sender);
                Integer cardValue = cards.get(lastCard) - 1;
                cards.put(lastCard, cardValue);
                votes.remove(sender);
            }
            Integer cardValue = cards.get(card) + 1;
            cards.put(card, cardValue);
            votes.put(sender, card);

            setVotesMap();
        }
    }

    public void voteForRandomCard(String sender) {
        if (cards != null) {
            voteForCard(sender, new ArrayList<>(cards.keySet()).get(randomGenerator.nextInt(cards.size())));
        }
    }

    public void showCard(Integer cardIndex) {
        if (cardIndex >= 0 && cardIndex < cards.size()) {
            showCard(new ArrayList<>(cards.keySet()).get(cardIndex));
        }
    }

    public void showCard(String cardName) {
        try {
            BotController.getInstance().sendMessage(cardName + ": " + IMAGE_PREFIX + URLEncoder.encode(cardName, "UTF-8").replace("+", "%20").replace("%E2%80%99", "%27").replace("%3A", "").replace(".", "") + IMAGE_EXTENSION, BotMessageType.CARD_DETAILS);
        } catch (UnsupportedEncodingException ignored) {
        }
    }

    private void setVotesMap() {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(cards.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return b.getValue().compareTo(a.getValue());
            }
        });
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : entries) {
            if (entry.getValue() > 0) sortedMap.put(entry.getKey(), entry.getValue());
        }

        GUIController.getInstance().setVotesMap(sortedMap);
    }
}
