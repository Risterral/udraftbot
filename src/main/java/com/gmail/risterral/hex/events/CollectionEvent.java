package com.gmail.risterral.hex.events;

import com.gmail.risterral.hex.HexEventsController;
import com.gmail.risterral.util.DraftController;

public class CollectionEvent extends AbstractHexEvent {

    @Override
    public void call() {
        HexEventsController.getInstance().clearDraftPackCards(false);
        DraftController.getInstance().setDraftEnded(true);
    }

}
