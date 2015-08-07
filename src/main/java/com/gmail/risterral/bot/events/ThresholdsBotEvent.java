package com.gmail.risterral.bot.events;

import com.gmail.risterral.util.DraftController;

public class ThresholdsBotEvent implements IBotEvent {

    @Override
    public void call(String sender, String... args) {
        DraftController.getInstance().checkThresholds();
    }
}
