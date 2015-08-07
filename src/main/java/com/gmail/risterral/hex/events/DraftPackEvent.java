package com.gmail.risterral.hex.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.risterral.hex.HexEventsController;
import com.gmail.risterral.hex.events.dto.CardDTO;

import java.util.ArrayList;

public class DraftPackEvent extends AbstractHexEvent {

    @JsonProperty("Cards")
    ArrayList<CardDTO> draftPackCardsNames;

    @Override
    public void call() {
        if (HexEventsController.getInstance().isListenToDraft()) {
            HexEventsController.getInstance().setNewDraftPackCards(draftPackCardsNames);
        }
    }
}
