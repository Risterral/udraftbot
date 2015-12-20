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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardDTO)) return false;

        CardDTO cardDTO = (CardDTO) o;

        if (name != null ? !name.equals(cardDTO.name) : cardDTO.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
