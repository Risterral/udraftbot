package com.gmail.risterral.bot.events;

public interface IBotEvent {
    void call(String sender, String... args);
}
