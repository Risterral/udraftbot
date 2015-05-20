package com.gmail.risterral.controllers.bot;

public enum BotMessageType {
    CARDS_LIST(0, false),
    CARDS_PICKED(1, false),
    CARD_DETAILS(2, true),
    BOT_INFO(3, false),
    DEFAULT(4, true);

    private final Integer priority;
    private final boolean tryToGroup;

    BotMessageType(Integer priority, boolean tryToGroup) {
        this.priority = priority;
        this.tryToGroup = tryToGroup;
    }

    public Integer getPriority() {
        return priority;
    }

    public boolean isTryToGroup() {
        return tryToGroup;
    }
}
