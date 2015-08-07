package com.gmail.risterral.hex.events.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GemDTO {

    @JsonProperty("Name")
    public String name;

    @JsonProperty("Guid")
    public GuidDTO guid;

}
