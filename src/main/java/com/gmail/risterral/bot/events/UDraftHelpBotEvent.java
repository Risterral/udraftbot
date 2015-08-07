package com.gmail.risterral.bot.events;

import com.gmail.risterral.bot.BotController;
import com.gmail.risterral.bot.BotMessageType;

public class UDraftHelpBotEvent implements IBotEvent {

    public static final String ABOUT_MESSAGE = "The bot is connected to streamers Hex client, which allows to list cards from draft.";
    public static final String PICK_COMMAND_DETAILS = "To vote for card use command !pick {index} or !pick {name}.";
    public static final String CARD_COMMAND_DETAILS = "To check card details use command !card {index} or !card {name}.";

    @Override
    public void call(String sender, String... args) {
        BotController.getInstance().sendMessage(ABOUT_MESSAGE + " " + PICK_COMMAND_DETAILS + " " + CARD_COMMAND_DETAILS, BotMessageType.BOT_INFO, null);
    }
}
