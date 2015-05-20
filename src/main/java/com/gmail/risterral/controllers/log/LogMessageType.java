package com.gmail.risterral.controllers.log;

public enum LogMessageType {
    SUCCESS(10000),
    ERROR(20000);

    private final Integer errorDuration;

    LogMessageType(Integer errorDuration) {
        this.errorDuration = errorDuration;
    }

    public Integer getErrorDuration() {
        return errorDuration;
    }
}
