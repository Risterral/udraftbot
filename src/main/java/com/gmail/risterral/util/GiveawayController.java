package com.gmail.risterral.util;

import com.gmail.risterral.bot.BotController;
import com.gmail.risterral.bot.BotMessageType;
import com.gmail.risterral.gui.GUIController;
import com.gmail.risterral.gui.giveaways.GiveawayResultDTO;
import com.gmail.risterral.util.configuration.ConfigurationController;
import com.gmail.risterral.util.configuration.GiveawayDTO;

import java.util.*;

public class GiveawayController {
    private static final GiveawayController instance = new GiveawayController();

    private Random randomGenerator = new Random();
    private Integer numberOfAllVotes = 0;
    private ArrayList<GiveawayResultDTO> giveawaysUnlocked = new ArrayList<>();

    private GiveawayController() {
    }

    public static GiveawayController getInstance() {
        return instance;
    }

    public void updateGiveaways(LinkedHashMap<String, Integer> allVotes) {
        if (!allVotes.isEmpty()) {
            List<Map.Entry<String, Integer>> entries = new ArrayList<>(allVotes.entrySet());
            Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                    return b.getValue().compareTo(a.getValue());
                }
            });
            numberOfAllVotes = 0;
            for (Map.Entry<String, Integer> entry : entries) {
                numberOfAllVotes += entry.getValue();
            }

            if (ConfigurationController.getInstance().getConfigurationDTO().getEnableGiveaways()) {
                ArrayList<GiveawayDTO> giveawaysToRemove = new ArrayList<>();
                for (GiveawayDTO giveawayDTO : ConfigurationController.getInstance().getConfigurationDTO().getGiveaways()) {
                    if (!giveawayDTO.getEnabled()) continue;
                    GiveawayResultDTO giveawayResultDTO = isGiveawaysAlreadyUnlocked(giveawayDTO);
                    if (giveawayResultDTO == null) {

                        if (giveawayDTO.getMinimumNumberOfVotes() <= numberOfAllVotes) {
                            GiveawayResultDTO newGiveaway = new GiveawayResultDTO(giveawayDTO);
                            newGiveaway.setIsSuccessful(randomGenerator.nextDouble() * 100 <= giveawayDTO.getChance());
                            giveawaysUnlocked.add(newGiveaway);

                            if (newGiveaway.getIsSuccessful()) {
                                BotController.getInstance().sendMessage(prepareGiveawayMessage(giveawayDTO), BotMessageType.GIVEAWAY_UNLOCKED, null);

                                if (giveawayDTO.getRemoveAfterAppearing()) {
                                    giveawaysToRemove.add(giveawayDTO);
                                }
                            }
                        }
                    } else {
                        if (!giveawayDTO.getRecurring() || giveawayResultDTO.getIsSuccessful()) continue;
                        if (numberOfAllVotes / giveawayResultDTO.getMinimumNumberOfVotes() > giveawayResultDTO.getNumberOfRecurringAttempts()) {
                            giveawayResultDTO.setNumberOfRecurringAttempts(giveawayResultDTO.getNumberOfRecurringAttempts() + 1);
                            giveawayResultDTO.setIsSuccessful(randomGenerator.nextDouble() * 100 <= giveawayResultDTO.getChance());

                            if (giveawayResultDTO.getIsSuccessful()) {
                                BotController.getInstance().sendMessage(prepareGiveawayMessage(giveawayDTO), BotMessageType.GIVEAWAY_UNLOCKED, null);

                                if (giveawayDTO.getRemoveAfterAppearing()) {
                                    giveawaysToRemove.add(giveawayDTO);
                                }
                            }
                        }
                    }
                }
                if (!giveawaysToRemove.isEmpty()) {
                    ArrayList<GiveawayDTO> giveawaysList = new ArrayList<>();
                    for (GiveawayDTO giveawayDTO : ConfigurationController.getInstance().getConfigurationDTO().getGiveaways()) {
                        if (!giveawaysToRemove.contains(giveawayDTO)) {
                            giveawaysList.add(giveawayDTO);
                        }
                    }
                    ConfigurationController.getInstance().getConfigurationDTO().setGiveaways(giveawaysList);
                    ConfigurationController.getInstance().saveData();
                    GUIController.getInstance().updateGiveawaysPanel();
                }
            }

            GUIController.getInstance().updateGiveawaysResultPanel(numberOfAllVotes, entries.get(0).getKey(), entries.get(0).getValue(), giveawaysUnlocked);
        } else {
            clearGiveaways();
            GUIController.getInstance().updateGiveawaysResultPanel(0, "", 0, new ArrayList<GiveawayResultDTO>());
        }

    }

    public void showCurrentGiveawayMessage() {
        if (ConfigurationController.getInstance().getConfigurationDTO().getEnableGiveaways()) {
            Integer numberOfSuccessfulGiveaways = 0;
            for (GiveawayResultDTO giveawayResultDTO : giveawaysUnlocked) {
                if (giveawayResultDTO.getIsSuccessful()) {
                    numberOfSuccessfulGiveaways++;
                }
            }

            Integer numberOfVotesNeededToClosestGiveaway = Integer.MAX_VALUE;
            String giveawayName = "";
            for (GiveawayDTO giveawayDTO : ConfigurationController.getInstance().getConfigurationDTO().getGiveaways()) {
                if (!giveawayDTO.getEnabled()) continue;

                GiveawayResultDTO giveawayResultDTO = isGiveawaysAlreadyUnlocked(giveawayDTO);
                if (giveawayResultDTO == null) {
                    if (giveawayDTO.getMinimumNumberOfVotes() - numberOfAllVotes < numberOfVotesNeededToClosestGiveaway) {
                        numberOfVotesNeededToClosestGiveaway = giveawayDTO.getMinimumNumberOfVotes() - numberOfAllVotes;
                        giveawayName = giveawayDTO.getName();
                    }
                } else {
                    if (!giveawayDTO.getRecurring() || giveawayResultDTO.getIsSuccessful()) continue;
                    if (giveawayResultDTO.getMinimumNumberOfVotes() * (giveawayResultDTO.getNumberOfRecurringAttempts() + 1) - numberOfAllVotes < numberOfVotesNeededToClosestGiveaway) {
                        numberOfVotesNeededToClosestGiveaway = giveawayResultDTO.getMinimumNumberOfVotes() * (giveawayResultDTO.getNumberOfRecurringAttempts() + 1) - numberOfAllVotes;
                        giveawayName = giveawayResultDTO.getName();
                    }
                }
            }

            BotController.getInstance().sendMessage("Number of giveaways unlocked so far: " + numberOfSuccessfulGiveaways, BotMessageType.GIVEAWAY_INFO, null);
            if (numberOfVotesNeededToClosestGiveaway != Integer.MAX_VALUE) {
                String votesString = numberOfVotesNeededToClosestGiveaway != 1 ? numberOfVotesNeededToClosestGiveaway + " more votes" : "1 more vote";
                BotController.getInstance().sendMessage("The nearest giveaway is " + giveawayName + " giveaway. You need " + votesString + " for chance to unlock it!", BotMessageType.GIVEAWAY_INFO, null);
            } else {
                BotController.getInstance().sendMessage("There are not giveaways set to chase", BotMessageType.GIVEAWAY_INFO, null);
            }
        }
    }

    public void clearGiveaways() {
        giveawaysUnlocked.clear();
        numberOfAllVotes = 0;
    }

    private GiveawayResultDTO isGiveawaysAlreadyUnlocked(GiveawayDTO giveawayDTO) {
        for (GiveawayResultDTO giveawayResultDTO : giveawaysUnlocked) {
            if (giveawayResultDTO.getName().equals(giveawayDTO.getName()) &&
                    giveawayResultDTO.getChance().equals(giveawayDTO.getChance()) &&
                    giveawayResultDTO.getMinimumNumberOfVotes().equals(giveawayDTO.getMinimumNumberOfVotes())) {
                return giveawayResultDTO;
            }
        }
        return null;
    }

    private String prepareGiveawayMessage(GiveawayDTO dto) {
        return ConfigurationController.getInstance().getConfigurationDTO().getGiveawayMessage()
                .replace("{giveaway_name}", dto.getName())
                .replace("{giveaway_chance}", dto.getChance().toString())
                .replace("{giveaway_minimum_number_of_votes}", dto.getMinimumNumberOfVotes().toString());
    }
}
