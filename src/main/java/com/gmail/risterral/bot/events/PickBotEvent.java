package com.gmail.risterral.bot.events;

import com.gmail.risterral.controllers.vote.VotingController;

public class PickBotEvent implements IBotEvent {

    @Override
    public void call(String sender, String... args) {
        if (args.length > 1) {
            if (args[1].matches("^\\d+$")) {
                VotingController.getInstance().voteForCard(sender, Integer.parseInt(args[1]) - 1);
            } else if (BotEvents.PICK_CARD.getOptionalArguments()[0].equalsIgnoreCase(args[1])) {
                VotingController.getInstance().voteForRandomCard(sender);
            } else {
                String card = "";
                String separator = "";
                for (int i = 1; i < args.length; i++) {
                    card += separator + args[i];
                    separator = " ";
                }
                VotingController.getInstance().voteForCard(sender, card);
            }
        }
    }
}
