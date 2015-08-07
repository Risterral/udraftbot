package com.gmail.risterral.hex.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.risterral.hex.HexEventsController;
import com.gmail.risterral.hex.events.dto.CardDTO;

public class DraftCardPickedEvent extends AbstractHexEvent {

    @JsonProperty("Card")
    public CardDTO cardPicked;

    @Override
    public void call() {
        if (HexEventsController.getInstance().isListenToDraft()) {
            HexEventsController.getInstance().cardPicked(cardPicked.name);
        }
    }
}
