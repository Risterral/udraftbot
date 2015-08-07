package com.gmail.risterral.gui;

import javax.swing.*;

public abstract class TabPanel extends JPanel {
    private final String tabTitle;

    public abstract void update();

    public TabPanel(String tabTitle) {
        this.tabTitle = tabTitle;
    }

    public String getTabTitle() {
        return tabTitle;
    }
}
