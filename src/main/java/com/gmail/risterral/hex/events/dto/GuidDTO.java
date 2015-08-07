package com.gmail.risterral.hex.events.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GuidDTO {

    @JsonProperty("m_Guid")
    public String guid;

    @Override
    public String toString() {
        return guid;
    }
}
