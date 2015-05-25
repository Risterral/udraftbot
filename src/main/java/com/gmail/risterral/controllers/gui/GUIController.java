package com.gmail.risterral.controllers.gui;

import com.gmail.risterral.controllers.log.LogMessageDTO;
import com.gmail.risterral.gui.MainWindow;
import com.gmail.risterral.gui.config.ConfigurationPanel;
import com.gmail.risterral.gui.draft.DraftPanel;
import com.gmail.risterral.gui.log.LogPanel;

import java.util.LinkedHashMap;

public class GUIController {
    private static final GUIController instance = new GUIController();

    private MainWindow mainWindow;
    private ConfigurationPanel configurationPanel;
    private DraftPanel draftPanel;
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

    public Boolean isBotAccountModded() {
        return configurationPanel.isBotAccountModded();
    }

    public Boolean isUseCustomHtmlDraftPanel() {
        return configurationPanel.isUseCustomHtmlDraftPanel();
    }

    public Boolean isTestCommandEnabled() {
        return configurationPanel.isTestCommandEnabled();
    }

    public Boolean isTestSaveDeckEventEnabled() {
        return configurationPanel.isTestSaveDeckEventEnabled();
    }

    private void init() {
        mainWindow = new MainWindow();
        configurationPanel = new ConfigurationPanel();
        draftPanel = new DraftPanel(configurationPanel.isUseCustomHtmlDraftPanel());
        logPanel = new LogPanel();

        mainWindow.addToTabPane("Configuration", configurationPanel);
        mainWindow.addToTabPane("Hex draft", draftPanel);
        mainWindow.addLogPanel(logPanel);
    }
}
