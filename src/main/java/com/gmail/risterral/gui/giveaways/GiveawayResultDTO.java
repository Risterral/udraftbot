package com.gmail.risterral.gui.giveaways;

import com.gmail.risterral.util.configuration.GiveawayDTO;

public class GiveawayResultDTO {
    private String name;
    private Double chance;
    private Integer minimumNumberOfVotes;
    private Integer numberOfRecurringAttempts;
    private Boolean isSuccessful;

    public GiveawayResultDTO() {
    }

    public GiveawayResultDTO(GiveawayDTO giveawayDTO) {
        this.name = giveawayDTO.getName();
        this.chance = giveawayDTO.getChance();
        this.minimumNumberOfVotes = giveawayDTO.getMinimumNumberOfVotes();
        numberOfRecurringAttempts = 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getChance() {
        return chance;
    }

    public void setChance(Double chance) {
        this.chance = chance;
    }

    public Integer getMinimumNumberOfVotes() {
        return minimumNumberOfVotes;
    }

    public void setMinimumNumberOfVotes(Integer minimumNumberOfVotes) {
        this.minimumNumberOfVotes = minimumNumberOfVotes;
    }

    public Integer getNumberOfRecurringAttempts() {
        return numberOfRecurringAttempts;
    }

    public void setNumberOfRecurringAttempts(Integer numberOfRecurringAttempts) {
        this.numberOfRecurringAttempts = numberOfRecurringAttempts;
    }

    public Boolean getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(Boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }
}
