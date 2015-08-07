package com.gmail.risterral.bot.events;

import com.gmail.risterral.util.GiveawayController;

public class GiveawayBotEvent implements IBotEvent {

    @Override
    public void call(String sender, String... args) {
        GiveawayController.getInstance().showCurrentGiveawayMessage();
    }
}
