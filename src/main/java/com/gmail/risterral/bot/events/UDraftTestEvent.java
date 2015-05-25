package com.gmail.risterral.bot.events;

import com.gmail.risterral.controllers.gui.GUIController;
import com.gmail.risterral.controllers.hex.HexEventsController;
import com.gmail.risterral.controllers.vote.VotingController;

import java.util.ArrayList;

public class UDraftTestEvent implements IBotEvent {

    @Override
    public void call(String sender, String... args) {
        if (GUIController.getInstance().isTestCommandEnabled()) {
            final java.util.List<String> test = new ArrayList<>();
            test.add("Crackling Rot");
            test.add("Army of the Arcane Cinder");
            test.add("Zeedu");
            test.add("Zakiir");
            test.add("Zombie");
            test.add("Zombie Plague");
            test.add("Wrathwood Master Moss");
            test.add("Wakizashi Ambusher");
            test.add("Te'talca, Orc Gladiator");
            test.add("Baby Yeti");
            test.add("Fish Hands");
            test.add("Jank Bot");
            test.add("Kismet's Reverie");
            test.add("Soothing Breeze");
            test.add("Not existing card");
            HexEventsController.getInstance().clearDraftPackCards();
            HexEventsController.getInstance().setNewDraftPackCards(test);

            if (args.length > 2 && BotEvents.UDRAFT_TEST.getOptionalArguments()[0].equalsIgnoreCase(args[2])) {
                for (int i = 0; i < 10; i++) {
                    VotingController.getInstance().voteForRandomCard("sender_" + i);
                }
            }
        }
    }
}
