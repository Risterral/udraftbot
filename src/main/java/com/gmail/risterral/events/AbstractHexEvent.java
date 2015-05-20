package com.gmail.risterral.events;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;

public abstract class AbstractHexEvent {
    protected String eventName;
    protected String username;

    public static AbstractHexEvent getEvent(String json) throws EventParsingException {
        JsonFactory factory = new JsonFactory();

        ObjectMapper mapper = new ObjectMapper(factory);
        JsonNode rootNode;
        try {
            rootNode = mapper.readTree(json);
        } catch (IOException e) {
            throw new EventParsingException("Unexpected error while parsing JSON element.", e);
        }
        if (rootNode.size() != 3) {
            throw new EventParsingException("Number of elements in root element is not equal to 3.");
        }

        AbstractHexEvent result;
        String eventName = rootNode.get(0).asText();
        try {
            Class clazz = Class.forName(AbstractHexEvent.class.getPackage().getName() + "." + eventName + "Event");
            result = (AbstractHexEvent) clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            return null;
        }

        result.setEventName(eventName);
        result.setUsername(rootNode.get(1).asText());
        result.setAdditionalInfo((ArrayNode) rootNode.get(2));

        return result;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public abstract void setAdditionalInfo(ArrayNode additionalInfo) throws EventParsingException;

    public abstract void call();
}
