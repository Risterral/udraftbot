package com.gmail.risterral.gui;

import com.gmail.risterral.configuration.ConfigurationController;
import com.gmail.risterral.controllers.bot.BotController;
import com.gmail.risterral.controllers.hex.HexEventsController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame {

    private JPanel mainPanel;
    private JTabbedPane tabbedPane;

    public MainWindow() throws HeadlessException {
        super("UDraft Bot");
        this.setSize(810, 630);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Integer windowLastPositionX = ConfigurationController.getInstance().getConfigurationDTO().getWindowLastPositionX();
        Integer windowLastPositionY = ConfigurationController.getInstance().getConfigurationDTO().getWindowLastPositionY();
        Integer windowLastPositionWidth = ConfigurationController.getInstance().getConfigurationDTO().getWindowLastPositionWidth();
        Integer windowLastPositionHeight = ConfigurationController.getInstance().getConfigurationDTO().getWindowLastPositionHeight();
        if (windowLastPositionX != null && windowLastPositionY != null && windowLastPositionWidth != null && windowLastPositionHeight != null) {
            this.setBounds(windowLastPositionX, windowLastPositionY, windowLastPositionWidth, windowLastPositionHeight);
        } else {
            this.setLocationRelativeTo(null);
        }

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                ConfigurationController.getInstance().getConfigurationDTO().setWindowLastPositionX(getBounds().x);
                ConfigurationController.getInstance().getConfigurationDTO().setWindowLastPositionY(getBounds().y);
                ConfigurationController.getInstance().getConfigurationDTO().setWindowLastPositionWidth(getBounds().width);
                ConfigurationController.getInstance().getConfigurationDTO().setWindowLastPositionHeight(getBounds().height);
                ConfigurationController.getInstance().saveData();

                BotController.getInstance().disconnect();
                HexEventsController.getInstance().disconnect();

                System.exit(0);
            }
        });

        mainPanel = new JPanel(new BorderLayout());

        tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        this.add(mainPanel);

        final CheckVersion checkVersion = new CheckVersion(this);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                checkVersion.checkVersion();
            }
        });
    }


    public void showWindow() {
        this.setVisible(true);
    }

    public void addToTabPane(String title, JPanel panel) {
        tabbedPane.add(title, panel);
    }

    public void addLogPanel(JPanel logPanel) {
        mainPanel.add(logPanel, BorderLayout.SOUTH);
    }
}
