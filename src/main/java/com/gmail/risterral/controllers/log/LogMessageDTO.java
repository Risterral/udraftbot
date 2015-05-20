package com.gmail.risterral.controllers.log;

public class LogMessageDTO {
    private String message;
    private LogMessageType type;
    private Integer duration;
    private Long logTime;

    public LogMessageDTO(String message, LogMessageType type, Integer duration, Long logTime) {
        this.message = message;
        this.type = type;
        this.duration = duration;
        this.logTime = logTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LogMessageType getType() {
        return type;
    }

    public void setType(LogMessageType type) {
        this.type = type;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getLogTime() {
        return logTime;
    }

    public void setLogTime(Long logTime) {
        this.logTime = logTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogMessageDTO)) return false;

        LogMessageDTO that = (LogMessageDTO) o;

        if (duration != null ? !duration.equals(that.duration) : that.duration != null) return false;
        if (logTime != null ? !logTime.equals(that.logTime) : that.logTime != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (logTime != null ? logTime.hashCode() : 0);
        return result;
    }
}
