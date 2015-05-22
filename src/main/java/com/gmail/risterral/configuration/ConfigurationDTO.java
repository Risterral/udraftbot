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

    public ConfigurationDTO() {
        this.serverHostname = "";
        this.serverPort = "";
        this.botName = "";
        this.password = "";
        this.channel = "";
        this.hexListenerPort = "";
        this.isBotAccountModded = false;
        this.doNotShowAgainUpdateVersion = "";
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
}
