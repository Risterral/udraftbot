package com.gmail.risterral.bot.events;

import com.gmail.risterral.hex.HexEventsController;
import com.gmail.risterral.hex.events.dto.CardDTO;
import com.gmail.risterral.util.DraftController;
import com.gmail.risterral.util.VotingController;

import java.util.ArrayList;

public class UDraftTestEvent implements IBotEvent {

    @Override
    public void call(String sender, String... args) {
        final ArrayList<CardDTO> test = new ArrayList<>();
        test.add(new CardDTO("Crackling Rot"));
        test.add(new CardDTO("Army of the Arcane Cinder"));
        test.add(new CardDTO("Zeedu"));
        test.add(new CardDTO("Zakiir"));
        test.add(new CardDTO("Construction Plans: War Hulk"));
        test.add(new CardDTO("Zombie Plague"));
        test.add(new CardDTO("Wrathwood Master Moss"));
        test.add(new CardDTO("To the Skies!"));
        test.add(new CardDTO("Te'talca, Orc Gladiator"));
        test.add(new CardDTO("Baby Yeti"));
        test.add(new CardDTO("Fish Hands"));
        test.add(new CardDTO("Jank Bot"));
        test.add(new CardDTO("Kismet's Reverie"));
        test.add(new CardDTO("Soothing Breeze"));
        test.add(new CardDTO("Not existing card"));
        VotingController.getInstance().clearAllVotes();
        HexEventsController.getInstance().clearDraftPackCards(true);
        HexEventsController.getInstance().setNewDraftPackCards(test);

        if (args.length > 2 && BotEvents.UDRAFT_TEST.getOptionalArguments()[0].equalsIgnoreCase(args[2])) {
            for (int i = 0; i < 10; i++) {
                DraftController.getInstance().voteForRandomCard("sender_" + i);
            }
        }
    }
}
