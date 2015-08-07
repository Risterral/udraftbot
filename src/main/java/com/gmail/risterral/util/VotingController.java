package com.gmail.risterral.util;

import com.gmail.risterral.gui.GUIController;
import com.gmail.risterral.hex.events.dto.CardDTO;

import java.util.*;

public class VotingController {
    private static final VotingController instance = new VotingController();

    private LinkedHashMap<String, Integer> allVotesForCards = new LinkedHashMap<>();
    private LinkedHashMap<String, Integer> cards = new LinkedHashMap<>();
    private LinkedHashMap<String, String> votes = new LinkedHashMap<>();

    private VotingController() {
    }

    public static VotingController getInstance() {
        return instance;
    }

    public void setNewDraftPackCards(List<CardDTO> newCardsList) {
        this.cards = new LinkedHashMap<>();
        this.votes = new LinkedHashMap<>();

        for (CardDTO card : newCardsList) {
            this.cards.put(card.name, 0);
        }

        setVotesMap();
    }

    public void voteForCard(String sender, String card) {
        if (cards.containsKey(card)) {
            if (votes.containsKey(sender)) {
                String lastCard = votes.get(sender);
                cards.put(lastCard, cards.get(lastCard) - 1);
                allVotesForCards.put(lastCard, allVotesForCards.get(lastCard) - 1);

                votes.remove(sender);
            }
            Integer cardValue = cards.get(card) + 1;
            cards.put(card, cardValue);
            votes.put(sender, card);

            allVotesForCards.put(card, allVotesForCards.containsKey(card) ? allVotesForCards.get(card) + 1 : 1);

            setVotesMap();
            GiveawayController.getInstance().updateGiveaways(allVotesForCards);
        }
    }

    public void clearAllVotes() {
        allVotesForCards.clear();
        GiveawayController.getInstance().clearGiveaways();
        GiveawayController.getInstance().updateGiveaways(allVotesForCards);
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
