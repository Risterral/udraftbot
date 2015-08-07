package com.gmail.risterral.gui;

import com.gmail.risterral.gui.aliases.AliasesPanel;
import com.gmail.risterral.gui.commands.CommandsPanel;
import com.gmail.risterral.gui.config.ConfigurationPanel;
import com.gmail.risterral.gui.draft.DraftPanel;
import com.gmail.risterral.gui.giveaways.DraftResultsPanel;
import com.gmail.risterral.gui.giveaways.GiveawayResultDTO;
import com.gmail.risterral.gui.giveaways.GiveawaysPanel;
import com.gmail.risterral.gui.log.LogPanel;
import com.gmail.risterral.util.log.LogMessageDTO;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GUIController {
    private static final GUIController instance = new GUIController();

    private MainWindow mainWindow;
    private ConfigurationPanel configurationPanel;
    private CommandsPanel commandsPanel;
    private AliasesPanel aliasesPanel;
    private DraftPanel draftPanel;
    private GiveawaysPanel giveawaysPanel;
    private DraftResultsPanel draftResultsPanel;
    private LogPanel logPanel;

    private GUIController() {
        init();
    }

    public static GUIController getInstance() {
        return instance;
    }

    public void showWindow() {
        mainWindow.showWindow();
    }

    public void setVotesMap(LinkedHashMap<String, Integer> votesMap) {
        draftPanel.setVotesMap(votesMap);
    }

    public void logMessage(LogMessageDTO logMessage) {
        logPanel.logMessage(logMessage);
    }

    public void setDraftPanelView(boolean isUseCustomHtmlDraftPanel) {
        draftPanel.setView(isUseCustomHtmlDraftPanel);
    }

    public void setConnectButtonEnabled(boolean enabled) {
        configurationPanel.setConnectButtonEnabled(enabled);
    }

    public void setListenToDraft(boolean isListening) {
        draftPanel.setListenToDraft(isListening);
    }

    public Boolean isBotAccountModded() {
        return configurationPanel.isBotAccountModded();
    }

    public Boolean isUseCustomHtmlDraftPanel() {
        return configurationPanel.isUseCustomHtmlDraftPanel();
    }

    public Boolean isTestSaveDeckEventEnabled() {
        return configurationPanel.isTestSaveDeckEventEnabled();
    }

    public void updateGiveawaysPanel() {
        giveawaysPanel.updateGiveaways();
    }

    public void updateGiveawaysResultPanel(Boolean isGiveawaysEnabled) {
        draftResultsPanel.updateGiveawaysResultPanel(isGiveawaysEnabled);
    }

    public void updateGiveawaysResultPanel(Integer numberOfAllVotes, String mostPopularCard, Integer votesPerMostPopularCard, ArrayList<GiveawayResultDTO> unlockedGiveaways) {
        draftResultsPanel.updateGiveawaysResultPanel(numberOfAllVotes, mostPopularCard, votesPerMostPopularCard, unlockedGiveaways);
    }

    public void addPanelToMainWindow(TabPanel panel) {
        mainWindow.addToTabPane(panel.getTabTitle(), panel);
    }

    private void init() {
        mainWindow = new MainWindow();
        configurationPanel = new ConfigurationPanel("Configuration");
        commandsPanel = new CommandsPanel("Commands");
        aliasesPanel = new AliasesPanel("Aliases");
        draftPanel = new DraftPanel("Hex draft", configurationPanel.isUseCustomHtmlDraftPanel());
        giveawaysPanel = new GiveawaysPanel("Giveaways");
        draftResultsPanel = new DraftResultsPanel("Draft results");
        logPanel = new LogPanel();

        mainWindow.addToTabPane(configurationPanel.getTabTitle(), configurationPanel);
        mainWindow.addToTabPane(commandsPanel.getTabTitle(), commandsPanel);
        mainWindow.addToTabPane(aliasesPanel.getTabTitle(), aliasesPanel);
        mainWindow.addToTabPane(draftPanel.getTabTitle(), draftPanel);
        mainWindow.addToTabPane(giveawaysPanel.getTabTitle(), giveawaysPanel);
        mainWindow.addToTabPane(draftResultsPanel.getTabTitle(), draftResultsPanel);
        mainWindow.addLogPanel(logPanel);
    }
}
