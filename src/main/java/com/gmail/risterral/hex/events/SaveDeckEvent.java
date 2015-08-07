package com.gmail.risterral.hex.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.risterral.bot.BotController;
import com.gmail.risterral.bot.BotMessageType;
import com.gmail.risterral.gui.GUIController;

public class SaveDeckEvent extends AbstractHexEvent {

    @JsonProperty("Name")
    public String deckName;

    @JsonProperty("Champion")
    public String championName;

    @Override
    public void call() {
        if (GUIController.getInstance().isTestSaveDeckEventEnabled()) {
            BotController.getInstance().sendMessage("You have just saved the deck (" + deckName + ")", BotMessageType.DEFAULT, null);
        }
    }
}
