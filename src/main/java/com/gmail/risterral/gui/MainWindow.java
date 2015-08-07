package com.gmail.risterral.gui;

import com.gmail.risterral.bot.BotController;
import com.gmail.risterral.hex.HexEventsController;
import com.gmail.risterral.util.checkversion.CheckVersion;
import com.gmail.risterral.util.configuration.ConfigurationController;
import com.gmail.risterral.util.configuration.WindowPopupDTO;
import com.gmail.risterral.util.statistics.HexEntitiesController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame {

    private JPanel mainPanel;
    private TabbedPane tabbedPane;

    public MainWindow() throws HeadlessException {
        super("UDraft Bot");
        this.setSize(900, 650);
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

                for (String popupWindowTitle : tabbedPane.getPopupFrames().keySet()) {
                    Frame popupWindowFrame = tabbedPane.getPopupFrames().get(popupWindowTitle);
                    WindowPopupDTO popupDTO = new WindowPopupDTO(popupWindowFrame.getBounds().x, popupWindowFrame.getBounds().y, popupWindowFrame.getBounds().width, popupWindowFrame.getBounds().height);
                    ConfigurationController.getInstance().getConfigurationDTO().getWindowsPopups().put(popupWindowTitle, popupDTO);
                }

                ConfigurationController.getInstance().saveData();

                BotController.getInstance().disconnect();
                HexEventsController.getInstance().disconnect();

                System.exit(0);
            }
        });

        mainPanel = new JPanel(new BorderLayout());

        tabbedPane = new TabbedPane(this);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        this.add(mainPanel);

        final CheckVersion checkVersion = new CheckVersion(this);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                HexEntitiesController.getInstance().prepareCardsData();
                checkVersion.checkVersion();
            }
        });
    }


    public void showWindow() {
        this.setVisible(true);
    }

    public void addToTabPane(String title, TabPanel panel) {
        if (ConfigurationController.getInstance().getConfigurationDTO().getWindowsPopups().containsKey(title)) {
            WindowPopupDTO windowPopupDTO = ConfigurationController.getInstance().getConfigurationDTO().getWindowsPopups().get(title);
            if (windowPopupDTO.getIsVisible()) {
                addPopupWindow(title, windowPopupDTO, panel);
                return;
            }
        }
        tabbedPane.add(title, panel);
    }

    public void addLogPanel(JPanel logPanel) {
        mainPanel.add(logPanel, BorderLayout.SOUTH);
    }

    private void addPopupWindow(final String title, final WindowPopupDTO windowPopupDTO, final TabPanel panel) {
        final JFrame frame = new JFrame(title);
        frame.setSize(windowPopupDTO.getWindowLastPositionWidth(), windowPopupDTO.getWindowLastPositionHeight());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setBounds(windowPopupDTO.getWindowLastPositionX(), windowPopupDTO.getWindowLastPositionY(), windowPopupDTO.getWindowLastPositionWidth(), windowPopupDTO.getWindowLastPositionHeight());

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                windowPopupDTO.setWindowLastPositionX(frame.getX());
                windowPopupDTO.setWindowLastPositionY(frame.getY());
                windowPopupDTO.setWindowLastPositionWidth(frame.getWidth());
                windowPopupDTO.setWindowLastPositionHeight(frame.getHeight());
                windowPopupDTO.setIsVisible(false);
                ConfigurationController.getInstance().getConfigurationDTO().getWindowsPopups().put(title, windowPopupDTO);
                ConfigurationController.getInstance().saveData();

                GUIController.getInstance().addPanelToMainWindow(panel);
                tabbedPane.getPopupFrames().remove(title);
            }
        });
        frame.getContentPane().add(panel);
        frame.setVisible(true);
        panel.update();

        tabbedPane.getPopupFrames().put(title, frame);
    }
}
