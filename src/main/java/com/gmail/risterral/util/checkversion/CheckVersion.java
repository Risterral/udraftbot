package com.gmail.risterral.util.checkversion;

import com.gmail.risterral.util.configuration.ConfigurationController;
import com.gmail.risterral.util.log.LogController;
import com.gmail.risterral.util.log.LogMessageType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;

public class CheckVersion {
    private static final String CURRENT_UDRAFT_VERSION = "2.0.3";
    private static final String UDRAFT_VERSION_URL = "https://raw.githubusercontent.com/Risterral/udraftbot/master/udraft.version";
    private static final String UDRAFT_DOWNLOAD_LINK = "http://forums.cryptozoic.com/showthread.php?t=43201";

    private final JFrame frame;

    public CheckVersion(JFrame frame) {
        this.frame = frame;
    }

    public void checkVersion() {
        try {
            Thread worker = new Thread(new CheckVersionThread());
            worker.start();
        } catch (Exception e) {
            LogController.log(CheckVersion.class, new Exception(), LogMessageType.ERROR, "Unexpected error while checking version");
        }
    }

    private class CheckVersionThread implements Runnable {
        @Override
        public void run() {
            try {
                URL url = new URL(UDRAFT_VERSION_URL);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                String version;
                while ((version = in.readLine()) != null && !version.isEmpty()) {
                    break;
                }
                in.close();
                if (version != null && CURRENT_UDRAFT_VERSION.compareTo(version) < 0 && ConfigurationController.getInstance().getConfigurationDTO().getDoNotShowAgainUpdateVersion().compareTo(version) < 0) {
                    JCheckBox checkbox = new JCheckBox("Do not show this message again.");
                    String message = "There is new version of UDraft Bot. To download it visit:";
                    JLabel label = new JLabel(UDRAFT_DOWNLOAD_LINK);

                    Font font = label.getFont();
                    Map attributes = font.getAttributes();
                    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                    label.setFont(font.deriveFont(attributes));

                    label.setForeground(Color.blue);
                    label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent event) {
                            if (Desktop.isDesktopSupported()) {
                                try {
                                    Desktop.getDesktop().browse(new URI(UDRAFT_DOWNLOAD_LINK));
                                } catch (IOException | URISyntaxException e) {
                                    LogController.log(CheckVersion.class, e, LogMessageType.ERROR, "Unexpected error while checking version");
                                }
                            } else {
                                LogController.log(CheckVersion.class, new Exception(), LogMessageType.ERROR, "Unexpected error while checking version");
                            }
                        }
                    });

                    Object[] params = {message, label, checkbox};
                    JOptionPane.showMessageDialog(frame, params, "New version", JOptionPane.INFORMATION_MESSAGE);
                    if (checkbox.isSelected()) {
                        ConfigurationController.getInstance().getConfigurationDTO().setDoNotShowAgainUpdateVersion(version);
                        ConfigurationController.getInstance().saveData();
                    }
                }
            } catch (UnknownHostException e) {
                LogController.log(CheckVersion.class, new Exception(), LogMessageType.ERROR, "Cannot connect to version server");
            } catch (Exception e) {
                LogController.log(CheckVersion.class, new Exception(), LogMessageType.ERROR, "Unexpected error while checking version");
            }
        }
    }
}
