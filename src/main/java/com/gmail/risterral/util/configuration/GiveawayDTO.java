package com.gmail.risterral.util.configuration;

public class GiveawayDTO {
    private String name;
    private Double chance;
    private Integer minimumNumberOfVotes;
    private Boolean recurring;
    private Boolean removeAfterAppearing;
    private Boolean enabled;

    public GiveawayDTO() {
        this.name = "";
        this.chance = 0.;
        this.minimumNumberOfVotes = 0;
        this.recurring = true;
        this.removeAfterAppearing = true;
        this.enabled = true;
    }

    public GiveawayDTO(String name, Double chance, Integer minimumNumberOfVotes, Boolean recurring, Boolean removeAfterAppearing, Boolean enabled) {
        this.name = name;
        this.chance = chance;
        this.minimumNumberOfVotes = minimumNumberOfVotes;
        this.recurring = recurring;
        this.removeAfterAppearing = removeAfterAppearing;
        this.enabled = enabled;
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

    public Boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(Boolean recurring) {
        this.recurring = recurring;
    }

    public Boolean getRemoveAfterAppearing() {
        return removeAfterAppearing;
    }

    public void setRemoveAfterAppearing(Boolean removeAfterAppearing) {
        this.removeAfterAppearing = removeAfterAppearing;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
