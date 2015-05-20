package com.gmail.risterral.bot.events;

import com.gmail.risterral.controllers.gui.GUIController;
import com.gmail.risterral.controllers.hex.HexEventsController;
import com.gmail.risterral.controllers.vote.VotingController;

import java.util.ArrayList;
import java.util.Random;

public class UDraftTestEvent implements IBotEvent {
    private Random randomGenerator = new Random();

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
//            test.add("Not existing card to test");
            HexEventsController.getInstance().clearDraftPackCards();
            HexEventsController.getInstance().setNewDraftPackCards(test);

            for (int i = 0; i < 100; i++) {
                VotingController.getInstance().voteForCard("sender_" + i, randomGenerator.nextInt(test.size()));
            }
        }
    }
}
