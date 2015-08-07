package com.gmail.risterral.util.configuration;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigurationDTO {
    private String serverHostname;
    private String serverPort;
    private String botName;
    private String password;
    private String channel;
    private String hexListenerPort;
    private Boolean isBotAccountModded;
    private String doNotShowAgainUpdateVersion;
    private Boolean useCustomHtmlDraftPanel;
    private Integer windowLastPositionX;
    private Integer windowLastPositionY;
    private Integer windowLastPositionWidth;
    private Integer windowLastPositionHeight;
    private HashMap<String, Boolean> commands;
    private ArrayList<AliasDTO> aliases;
    private Boolean enableGiveaways;
    private String giveawayMessage;
    private ArrayList<GiveawayDTO> giveaways;
    private HashMap<String, WindowPopupDTO> windowsPopups;

    public ConfigurationDTO() {
        this.serverHostname = "";
        this.serverPort = "";
        this.botName = "";
        this.password = "";
        this.channel = "";
        this.hexListenerPort = "";
        this.isBotAccountModded = false;
        this.doNotShowAgainUpdateVersion = "";
        this.useCustomHtmlDraftPanel = false;
        this.windowLastPositionX = null;
        this.windowLastPositionY = null;
        this.windowLastPositionWidth = null;
        this.windowLastPositionHeight = null;
        this.commands = new HashMap<>();
        this.aliases = new ArrayList<>();
        this.enableGiveaways = false;
        this.giveawayMessage = "NEW GIVEAWAY HAS BEEN UNLOCKED - {giveaway_name} giveaway!";
        this.giveaways = new ArrayList<>();
        this.windowsPopups = new HashMap<>();
    }

    public String getServerHostname() {
        return serverHostname;
    }

    public void setServerHostname(String serverHostname) {
        this.serverHostname = serverHostname;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getHexListenerPort() {
        return hexListenerPort;
    }

    public void setHexListenerPort(String hexListenerPort) {
        this.hexListenerPort = hexListenerPort;
    }

    public Boolean getIsBotAccountModded() {
        return isBotAccountModded;
    }

    public void setIsBotAccountModded(Boolean isBotAccountModded) {
        this.isBotAccountModded = isBotAccountModded;
    }

    public String getDoNotShowAgainUpdateVersion() {
        return doNotShowAgainUpdateVersion;
    }

    public void setDoNotShowAgainUpdateVersion(String doNotShowAgainUpdateVersion) {
        this.doNotShowAgainUpdateVersion = doNotShowAgainUpdateVersion;
    }

    public Boolean getUseCustomHtmlDraftPanel() {
        return useCustomHtmlDraftPanel;
    }

    public void setUseCustomHtmlDraftPanel(Boolean useCustomHtmlDraftPanel) {
        this.useCustomHtmlDraftPanel = useCustomHtmlDraftPanel;
    }

    public Integer getWindowLastPositionX() {
        return windowLastPositionX;
    }

    public void setWindowLastPositionX(Integer windowLastPositionX) {
        this.windowLastPositionX = windowLastPositionX;
    }

    public Integer getWindowLastPositionY() {
        return windowLastPositionY;
    }

    public void setWindowLastPositionY(Integer windowLastPositionY) {
        this.windowLastPositionY = windowLastPositionY;
    }

    public Integer getWindowLastPositionWidth() {
        return windowLastPositionWidth;
    }

    public void setWindowLastPositionWidth(Integer windowLastPositionWidth) {
        this.windowLastPositionWidth = windowLastPositionWidth;
    }

    public Integer getWindowLastPositionHeight() {
        return windowLastPositionHeight;
    }

    public void setWindowLastPositionHeight(Integer windowLastPositionHeight) {
        this.windowLastPositionHeight = windowLastPositionHeight;
    }

    public HashMap<String, Boolean> getCommands() {
        return commands;
    }

    public void setCommands(HashMap<String, Boolean> commands) {
        this.commands = commands;
    }

    public ArrayList<AliasDTO> getAliases() {
        return aliases;
    }

    public void setAliases(ArrayList<AliasDTO> aliases) {
        this.aliases = aliases;
    }

    public Boolean getEnableGiveaways() {
        return enableGiveaways;
    }

    public void setEnableGiveaways(Boolean enableGiveaways) {
        this.enableGiveaways = enableGiveaways;
    }

    public String getGiveawayMessage() {
        return giveawayMessage;
    }

    public void setGiveawayMessage(String giveawayMessage) {
        this.giveawayMessage = giveawayMessage;
    }

    public ArrayList<GiveawayDTO> getGiveaways() {
        return giveaways;
    }

    public void setGiveaways(ArrayList<GiveawayDTO> giveaways) {
        this.giveaways = giveaways;
    }

    public HashMap<String, WindowPopupDTO> getWindowsPopups() {
        return windowsPopups;
    }

    public void setWindowsPopups(HashMap<String, WindowPopupDTO> windowsPopups) {
        this.windowsPopups = windowsPopups;
    }
}
