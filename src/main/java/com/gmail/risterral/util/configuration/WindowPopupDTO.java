package com.gmail.risterral.util.configuration;

public class WindowPopupDTO {
    private Integer windowLastPositionX;
    private Integer windowLastPositionY;
    private Integer windowLastPositionWidth;
    private Integer windowLastPositionHeight;
    private Boolean isVisible;

    public WindowPopupDTO() {
        this.windowLastPositionX = null;
        this.windowLastPositionY = null;
        this.windowLastPositionWidth = 900;
        this.windowLastPositionHeight = 650;
        this.isVisible = true;
    }

    public WindowPopupDTO(Integer windowLastPositionX, Integer windowLastPositionY) {
        this.windowLastPositionX = windowLastPositionX;
        this.windowLastPositionY = windowLastPositionY;
        this.windowLastPositionWidth = 900;
        this.windowLastPositionHeight = 650;
        this.isVisible = true;
    }

    public WindowPopupDTO(Integer windowLastPositionX, Integer windowLastPositionY, Integer windowLastPositionWidth, Integer windowLastPositionHeight) {
        this.windowLastPositionX = windowLastPositionX;
        this.windowLastPositionY = windowLastPositionY;
        this.windowLastPositionWidth = windowLastPositionWidth;
        this.windowLastPositionHeight = windowLastPositionHeight;
        this.isVisible = true;
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

    public Boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WindowPopupDTO)) return false;

        WindowPopupDTO that = (WindowPopupDTO) o;

        if (isVisible != null ? !isVisible.equals(that.isVisible) : that.isVisible != null) return false;
        if (windowLastPositionHeight != null ? !windowLastPositionHeight.equals(that.windowLastPositionHeight) : that.windowLastPositionHeight != null)
            return false;
        if (windowLastPositionWidth != null ? !windowLastPositionWidth.equals(that.windowLastPositionWidth) : that.windowLastPositionWidth != null)
            return false;
        if (windowLastPositionX != null ? !windowLastPositionX.equals(that.windowLastPositionX) : that.windowLastPositionX != null)
            return false;
        if (windowLastPositionY != null ? !windowLastPositionY.equals(that.windowLastPositionY) : that.windowLastPositionY != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = windowLastPositionX != null ? windowLastPositionX.hashCode() : 0;
        result = 31 * result + (windowLastPositionY != null ? windowLastPositionY.hashCode() : 0);
        result = 31 * result + (windowLastPositionWidth != null ? windowLastPositionWidth.hashCode() : 0);
        result = 31 * result + (windowLastPositionHeight != null ? windowLastPositionHeight.hashCode() : 0);
        result = 31 * result + (isVisible != null ? isVisible.hashCode() : 0);
        return result;
    }
}
