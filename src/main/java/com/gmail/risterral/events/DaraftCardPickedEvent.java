package com.gmail.risterral.events;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.gmail.risterral.controllers.hex.HexEventsController;

public class DaraftCardPickedEvent extends AbstractHexEvent {
    private String cardPicked;

    @Override
    public void setAdditionalInfo(ArrayNode additionalInfo) throws EventParsingException {
        if (additionalInfo.size() != 1) {
            throw new EventParsingException();
        }
        cardPicked = additionalInfo.get(0).asText();
    }

    @Override
    public void call() {
        HexEventsController.getInstance().cardPicked(cardPicked);
    }
}
