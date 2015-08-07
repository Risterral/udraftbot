package com.gmail.risterral.hex.events.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CardDTO {

    @JsonProperty("Name")
    public String name;

    @JsonProperty("Flags")
    public String flags;

    @JsonProperty("Guid")
    public GuidDTO guid;

    @JsonProperty("Gems")
    public List<GemDTO> gems;

    public CardDTO() {
    }

    public CardDTO(String name) {
        this.name = name;
    }
}
