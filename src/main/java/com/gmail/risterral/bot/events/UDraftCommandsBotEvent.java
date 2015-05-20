package com.gmail.risterral.bot.events;

import com.gmail.risterral.controllers.bot.BotController;
import com.gmail.risterral.controllers.bot.BotMessageType;

public class UDraftCommandsBotEvent implements IBotEvent {

    public static final String LIST_OF_COMMANDS = "!pick {index}, !pick {name}, !card {index}, !card {name}, !udraft help, !udraft about, !udraft commands";

    @Override
    public void call(String sender, String... args) {
        BotController.getInstance().sendMessage("List of allowed commands: " + LIST_OF_COMMANDS, BotMessageType.BOT_INFO);
    }
}
