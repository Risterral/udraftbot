package com.gmail.risterral.hex.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "Message")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LoginEvent.class, name = "Login"),
        @JsonSubTypes.Type(value = SaveDeckEvent.class, name = "SaveDeck"),
        @JsonSubTypes.Type(value = DraftPackEvent.class, name = "DraftPack"),
        @JsonSubTypes.Type(value = DraftCardPickedEvent.class, name = "DraftCardPicked"),
        @JsonSubTypes.Type(value = CollectionEvent.class, name = "Collection")})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractHexEvent {

    @JsonProperty("User")
    public String user;

    public static AbstractHexEvent getEvent(String json) throws EventParsingException {
        try {
            ObjectMapper mapper = new ObjectMapper(new JsonFactory());
            return mapper.readValue(json.replaceAll("\\[\\]", "null"), AbstractHexEvent.class);
        } catch (IOException e) {
            throw new EventParsingException("Unexpected event type.", e);
        }
    }

    public abstract void call();
}
