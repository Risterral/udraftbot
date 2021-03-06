package com.gmail.risterral.bot.events;

import com.gmail.risterral.util.DraftController;

public class ShowBotEvent implements IBotEvent {

    @Override
    public void call(String sender, String... args) {
        if (args.length > 1) {
            if (args[1].matches("^\\d+$")) {
                DraftController.getInstance().showCard(Integer.parseInt(args[1]) - 1);
            } else {
                String card = "";
                String separator = "";
                for (int i = 1; i < args.length; i++) {
                    card += separator + args[i];
                    separator = " ";
                }
                DraftController.getInstance().showCard(card, true);
            }
        }
    }
}
