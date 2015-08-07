package com.gmail.risterral.util;

import com.gmail.risterral.bot.BotController;
import com.gmail.risterral.bot.BotMessageType;
import com.gmail.risterral.hex.events.dto.CardDTO;
import com.gmail.risterral.util.configuration.AliasDTO;
import com.gmail.risterral.util.configuration.ConfigurationController;
import com.gmail.risterral.util.statistics.CardsStatisticsController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

public class DraftController {
    private static final DraftController instance = new DraftController();

    private static final String IMAGE_PREFIX = "https://hextcg.com/wp-content/themes/hex/images/autocard/";
    private static final String IMAGE_EXTENSION = ".png";

    private Random randomGenerator = new Random();
    private boolean isDraftEnded = false;
    private ArrayList<String> cardsPicked = new ArrayList<>();
    private ArrayList<CardDTO> cards;

    private DraftController() {
    }

    public static DraftController getInstance() {
        return instance;
    }

    public void setNewDraftPackCards(ArrayList<CardDTO> cards, Boolean clearIfEnabled) {
        if (isDraftEnded && clearIfEnabled) {
            cardsPicked.clear();
            VotingController.getInstance().clearAllVotes();
            isDraftEnded = false;
        }
        this.cards = cards;
        VotingController.getInstance().setNewDraftPackCards(cards);
    }

    public void voteForCard(String sender, Integer cardIndex) {
        if (cardIndex >= 0 && cards != null && cardIndex < cards.size()) {
            voteForCard(sender, cards.get(cardIndex).name, false);
        }
    }

    public void voteForCard(String sender, String cardName, boolean searchForAlias) {
        if (searchForAlias) {
            cardName = searchForAlias(cardName);
        }
        VotingController.getInstance().voteForCard(sender, cardName);
    }

    public void voteForRandomCard(String sender) {
        if (cards != null) {
            voteForCard(sender, cards.get(randomGenerator.nextInt(cards.size())).name, false);
        }
    }

    public void showCard(Integer cardIndex) {
        if (cardIndex >= 0 && cardIndex < cards.size()) {
            showCard(cards.get(cardIndex).name, false);
        }
    }

    public void showCard(String cardName, boolean searchForAlias) {
        try {
            if (searchForAlias) {
                cardName = searchForAlias(cardName);
            }
            BotController.getInstance().sendMessage(cardName + ": " + IMAGE_PREFIX + URLEncoder.encode(cardName, "UTF-8").replace("+", "%20").replace("%E2%80%99", "%27").replace("%3A", "").replace("%21", "").replace(".", "") + IMAGE_EXTENSION, BotMessageType.CARD_DETAILS, null);
        } catch (UnsupportedEncodingException ignored) {
        }
    }

    public void pickCard(Integer cardIndex) {
        if (cardIndex >= 0 && cardIndex < cards.size()) {
            pickCard(cards.get(cardIndex).name, false);
        }
    }

    public void pickCard(String cardName, boolean searchForAlias) {
        if (searchForAlias) {
            cardName = searchForAlias(cardName);
        }
        cardsPicked.add(cardName);
        BotController.getInstance().sendMessage("Card picked: " + cardName, BotMessageType.CARDS_PICKED, null);
    }

    public void checkCosts() {
        CardsStatisticsController.getInstance().checkCosts(cardsPicked);
    }

    public void checkRatios() {
        CardsStatisticsController.getInstance().checkRatios(cardsPicked);
    }

    public void checkThresholds() {
        CardsStatisticsController.getInstance().checkThresholds(cardsPicked);
    }

    public void checkPrice(Integer cardIndex) {
        if (cardIndex >= 0 && cardIndex < cards.size()) {
            checkPrice(cards.get(cardIndex).name, false);
        }
    }

    public void checkPrice(String cardName, boolean searchForAlias) {
        if (searchForAlias) {
            cardName = searchForAlias(cardName);
        }
        CardsStatisticsController.getInstance().checkPrice(cardName);
    }

    public void setDraftEnded(boolean isDraftEnded) {
        this.isDraftEnded = isDraftEnded;
    }

    private String searchForAlias(String value) {
        for (AliasDTO alias : ConfigurationController.getInstance().getConfigurationDTO().getAliases()) {
            if (!alias.getEnabled()) continue;
            if (alias.getPattern().toLowerCase().startsWith("regexp=")) {
                String regexpPattern = alias.getPattern().substring(7);
                if (value.matches(regexpPattern)) {
                    return alias.getValue();
                }
            } else {
                if (alias.getPattern().equals(value)) {
                    return alias.getValue();
                }
            }
        }

        return value;
    }
}
