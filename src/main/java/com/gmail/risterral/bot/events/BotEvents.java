package com.gmail.risterral.bot.events;

public enum BotEvents {
    PICK_CARD("!pick", new String[] {"random"}, new PickBotEvent()),
    SHOW_CARD("!card", null, new ShowBotEvent()),
    UDRAFT_HELP("!udraft help", null, new UDraftHelpBotEvent()),
    UDRAFT_ABOUT("!udraft about", null, new UDraftAboutBotEvent()),
    UDRAFT_COMMANDS("!udraft commands", null, new UDraftCommandsBotEvent()),
    UDRAFT_TEST("!udraft test", new String[] {"populate"}, new UDraftTestEvent());

    private String eventCommand;
    private String[] optionalArguments;
    private IBotEvent event;

    private BotEvents(String eventCommand, String[] additionalArguments, IBotEvent event) {
        this.eventCommand = eventCommand;
        this.optionalArguments = additionalArguments;
        this.event = event;
    }

    public String getEventCommand() {
        return eventCommand;
    }

    public String[] getOptionalArguments() {
        return optionalArguments;
    }

    public IBotEvent getEvent() {
        return event;
    }
}
