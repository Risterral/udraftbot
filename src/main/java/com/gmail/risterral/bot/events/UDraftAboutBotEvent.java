package com.gmail.risterral.bot.events;

import com.gmail.risterral.controllers.bot.BotController;
import com.gmail.risterral.controllers.bot.BotMessageType;

public class UDraftAboutBotEvent implements IBotEvent {

    public static final String ABOUT_MESSAGE = "Bot was created to allow streamers bigger interaction with viewers while playing draft in Hex. Bot version: 1.9.4. Created by Risterral.";

    @Override
    public void call(String sender, String... args) {
        BotController.getInstance().sendMessage(ABOUT_MESSAGE, BotMessageType.BOT_INFO);
    }
}
