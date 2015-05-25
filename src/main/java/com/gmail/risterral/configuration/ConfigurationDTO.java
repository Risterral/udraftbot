package com.gmail.risterral.configuration;

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
}
