package com.gmail.risterral.bot;

public enum BotMessageType {
    CARDS_LIST(0, false),
    CARDS_PICKED(1, false),
    CARD_DETAILS(2, true),
    GIVEAWAY_UNLOCKED(3, false),
    GIVEAWAY_INFO(4, false),
    CARDS_STATISTICS(5, false),
    BOT_INFO(6, false),
    DEFAULT(7, true);

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
