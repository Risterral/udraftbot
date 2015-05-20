package com.gmail.risterral.events;

public class EventParsingException extends Exception {

    public EventParsingException() {
        super();
    }

    public EventParsingException(String message) {
        super(message);
    }

    public EventParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
