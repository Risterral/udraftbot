package com.gmail.risterral.bot.events;

public enum BotEvents {
    PICK_CARD("!pick", new PickBotEvent()),
    SHOW_CARD("!card", new ShowBotEvent()),
    UDRAFT_HELP("!udraft help", new UDraftHelpBotEvent()),
    UDRAFT_ABOUT("!udraft about", new UDraftAboutBotEvent()),
    UDRAFT_COMMANDS("!udraft commands", new UDraftCommandsBotEvent()),
    UDRAFT_TEST("!udraft test", new UDraftTestEvent());

    private String eventCommand;
    private IBotEvent event;

    BotEvents(String eventCommand, IBotEvent event) {
        this.eventCommand = eventCommand;
        this.event = event;
    }

    public String getEventCommand() {
        return eventCommand;
    }

    public IBotEvent getEvent() {
        return event;
    }
}
