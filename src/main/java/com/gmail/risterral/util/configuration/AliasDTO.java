package com.gmail.risterral.util.configuration;

public class AliasDTO {
    private String pattern;
    private String value;
    private Boolean enabled;

    public AliasDTO() {
        this.pattern = "";
        this.value = "";
        this.enabled = true;
    }

    public AliasDTO(String pattern, String value, Boolean enabled) {
        this.pattern = pattern;
        this.value = value;
        this.enabled = enabled;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
