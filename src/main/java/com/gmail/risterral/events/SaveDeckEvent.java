package com.gmail.risterral.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.gmail.risterral.controllers.bot.BotController;
import com.gmail.risterral.controllers.bot.BotMessageType;
import com.gmail.risterral.controllers.gui.GUIController;

import java.util.ArrayList;
import java.util.Iterator;

public class SaveDeckEvent extends AbstractHexEvent {
    private String deckName;
    private String championName;
    private ArrayList<CardDTO> deckCardList;
    private ArrayList<CardDTO> reservesCardList;

    @Override
    public void setAdditionalInfo(ArrayNode additionalInfo) throws EventParsingException {
        if (additionalInfo.size() != 4) {
            throw new EventParsingException();
        }
        deckName = additionalInfo.get(0).asText();
        championName = additionalInfo.get(1).asText();

        deckCardList = new ArrayList<>();
        Iterator<JsonNode> iterator = additionalInfo.get(2).elements();
        while (iterator.hasNext()) {
            deckCardList.add(new CardDTO((ArrayNode) iterator.next()));
        }

        reservesCardList = new ArrayList<>();
        iterator = additionalInfo.get(3).elements();
        while (iterator.hasNext()) {
            reservesCardList.add(new CardDTO((ArrayNode) iterator.next()));
        }
    }

    @Override
    public void call() {
        if (GUIController.getInstance().isTestSaveDeckEventEnabled()) {
            BotController.getInstance().sendMessage("You just saved the deck (" + deckName + ")", BotMessageType.DEFAULT);
        }
    }

}
