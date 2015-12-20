package com.gmail.risterral.bot.events;

public enum BotEvents {
    PICK_CARD("!pick", new PickBotEvent(), true, "Use command !pick {index} or !pick {name} to vote for a card"),
    PICK_RANDOM_CARD("!pick random", new PickRandomBotEvent(), true, "Use this command to vote for a random card"),
    PICK_VALUE_CARD("!pick #value", new PickValueBotEvent(), true, "Use this command to vote for the most valuable card"),
    SHOW_CARD("!card", new ShowBotEvent(), true, "Use command !card {index} or !card {name} to check a card details"),
    COSTS("!costs", new CostsBotEvent(), true, "Use this command to check resource costs from all picked cards so far"),
    RATIOS("!ratios", new RatiosBotEvent(), true, "Use this command to check card types from all picked cards so far"),
    THRESHOLDS("!thresholds", new ThresholdsBotEvent(), true, "Use this command to check card thresholds from all picked cards so far"),
    PRICE("!price", new PriceBotEvent(), true, "Use command !price {index} or !price {name} to check a card value"),
    UDRAFT_GIVEAWAY("!udraft giveaway", new GiveawayBotEvent(), true, "Use this command to check number of successful giveaways and nearest giveaway to unlock"),
    UDRAFT_HELP("!udraft help", new UDraftHelpBotEvent(), true, "Use this command to display basic bot help"),
    UDRAFT_ABOUT("!udraft about", new UDraftAboutBotEvent(), true, "Use this command to display basic bot about message"),
    UDRAFT_COMMANDS("!udraft commands", new UDraftCommandsBotEvent(), true, "Use this command to display bot commands"),
    UDRAFT_TEST("!udraft test", new UDraftTestEvent(), false, "Use this command to display test list of cards"),
    UDRAFT_TEST_POPULATE("!udraft test populate", new UDraftTestPopulateEvent(), false, "Use this command to display test list of cards and put randomly 10 votes");

    private final String eventCommand;
    private final IBotEvent event;
    private final Boolean isDefaultEnabled;
    private final String description;

    BotEvents(String eventCommand, IBotEvent event, Boolean isDefaultEnabled, String description) {
        this.eventCommand = eventCommand;
        this.event = event;
        this.isDefaultEnabled = isDefaultEnabled;
        this.description = description;
    }

    public String getEventCommand() {
        return eventCommand;
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
