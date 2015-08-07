package com.gmail.risterral.bot.events;

public enum BotEvents {
    PICK_CARD("!pick", new String[]{"random"}, new PickBotEvent(), true, "Use command !pick {index} or !pick {name} or !pick [random] to vote for a card"),
    SHOW_CARD("!card", null, new ShowBotEvent(), true, "Use command !card {index} or !card {name} to check a card details"),
    COSTS("!costs", null, new CostsBotEvent(), true, "Use this command to check resource costs from all picked cards so far"),
    RATIOS("!ratios", null, new RatiosBotEvent(), true, "Use this command to check card types from all picked cards so far"),
    THRESHOLDS("!thresholds", null, new ThresholdsBotEvent(), true, "Use this command to check card thresholds from all picked cards so far"),
    PRICE("!price", null, new PriceBotEvent(), true, "Use command !price {index} or !price {name} to check a card value"),
    UDRAFT_GIVEAWAY("!udraft giveaway", null, new GiveawayBotEvent(), true, "Use this command to check number of successful giveaways and nearest giveaway to unlock"),
    UDRAFT_HELP("!udraft help", null, new UDraftHelpBotEvent(), true, "Use this command to display basic bot help"),
    UDRAFT_ABOUT("!udraft about", null, new UDraftAboutBotEvent(), true, "Use this command to display basic bot about message"),
    UDRAFT_COMMANDS("!udraft commands", null, new UDraftCommandsBotEvent(), true, "Use this command to display bot commands"),
    UDRAFT_TEST("!udraft test", new String[]{"populate"}, new UDraftTestEvent(), false, "Use command !udraft test or !udraft test [populate] to display test list of cards");

    private final String eventCommand;
    private final String[] optionalArguments;
    private final IBotEvent event;
    private final Boolean isDefaultEnabled;
    private final String description;

    private BotEvents(String eventCommand, String[] additionalArguments, IBotEvent event, Boolean isDefaultEnabled, String description) {
        this.eventCommand = eventCommand;
        this.optionalArguments = additionalArguments;
        this.event = event;
        this.isDefaultEnabled = isDefaultEnabled;
        this.description = description;
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

    public Boolean getIsDefaultEnabled() {
        return isDefaultEnabled;
    }

    public String getDescription() {
        return description;
    }
}
