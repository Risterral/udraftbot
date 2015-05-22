package com.gmail.risterral.gui;

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
        this.setSize(800, 600);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);

                BotController.getInstance().disconnect();
                HexEventsController.getInstance().disconnect();
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
