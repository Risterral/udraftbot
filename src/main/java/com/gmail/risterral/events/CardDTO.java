package com.gmail.risterral.events;

import com.fasterxml.jackson.databind.node.ArrayNode;

public class CardDTO {
    private String cardName;
    private String gemName;


    public CardDTO(ArrayNode node) {
        if (node.size() > 0) {
            cardName = node.get(0).asText();
        }
        if (node.size() > 1) {
            gemName = node.get(1).asText();
        }
    }

    public String getCardName() {
        return cardName;
    }

    public String getGemName() {
        return gemName;
    }
}
