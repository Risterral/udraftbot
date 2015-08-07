package com.gmail.risterral.bot.events;

import com.gmail.risterral.bot.BotController;
import com.gmail.risterral.bot.BotMessageType;
import com.gmail.risterral.util.configuration.ConfigurationController;

import java.util.HashMap;

public class UDraftCommandsBotEvent implements IBotEvent {

    @Override
    public void call(String sender, String... args) {
        int i = 0;
        BotController.getInstance().sendMessage("List of allowed commands: ", BotMessageType.BOT_INFO, i++);

        HashMap<String, Boolean> commands = ConfigurationController.getInstance().getConfigurationDTO().getCommands();
        for (BotEvents botEvents : BotEvents.values()) {
            if ((commands.containsKey(botEvents.name()) && commands.get(botEvents.name())) || (!commands.containsKey(botEvents.name()) && botEvents.getIsDefaultEnabled())) {
                BotController.getInstance().sendMessage(botEvents.getEventCommand() + " - " + botEvents.getDescription(), BotMessageType.BOT_INFO, i++);
            }
        }

    }
}
