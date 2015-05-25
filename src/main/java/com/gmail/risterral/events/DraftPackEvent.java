package com.gmail.risterral.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.gmail.risterral.controllers.hex.HexEventsController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DraftPackEvent extends AbstractHexEvent {

    List<String> draftPackCardsNames;

    @Override
    public void setAdditionalInfo(ArrayNode additionalInfo) {
        draftPackCardsNames = new ArrayList<>();
        Iterator<JsonNode> iterator = additionalInfo.elements();
        while (iterator.hasNext()) {
            draftPackCardsNames.add(iterator.next().asText());
        }
    }

    @Override
    public void call() {
        if (HexEventsController.getInstance().isListenToDraft()) {
            HexEventsController.getInstance().setNewDraftPackCards(draftPackCardsNames);
        }
    }
}
