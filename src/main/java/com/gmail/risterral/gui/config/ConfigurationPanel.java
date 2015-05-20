package com.gmail.risterral.gui.config;

import com.gmail.risterral.bot.events.BotEvents;
import com.gmail.risterral.controllers.bot.BotController;
import com.gmail.risterral.controllers.hex.HexEventsController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

public class ConfigurationPanel extends JPanel {
    private static final int PADDING = 15;
    private static final int PADDING_RIGHT = 100;

    private GridBagConstraints inputConstraints = null;
    private GridBagConstraints labelConstraints = null;

    private ConfigurationFile configurationFile;

    private JTextField serverHostname;
    private JTextField serverPort;
    private JTextField botName;
    private JTextField password;
    private JTextField channel;
    private JTextField hexListenerPort;

    private JCheckBox enableTestCommandCheckBox;
    private JCheckBox enableTestSaveDeckEventCheckBox;

    private JButton connectButton;
    private JButton disconnectButton;

    public ConfigurationPanel() {
        super();
        this.setLayout(new BorderLayout());
        this.initConstrains();

        configurationFile = new ConfigurationFile();

        this.add(createHeader(), BorderLayout.NORTH);
        this.add(createForm(), BorderLayout.CENTER);
        this.add(createButtonsPanel(), BorderLayout.EAST);

        LinkedHashMap<String, String> data = configurationFile.getData();
        if (data != null) {
            serverHostname.setText(data.containsKey("serverHostname") ? data.get("serverHostname") : "");
            serverPort.setText(data.containsKey("serverPort") ? data.get("serverPort") : "");
            botName.setText(data.containsKey("botName") ? data.get("botName") : "");
            password.setText(data.containsKey("password") ? data.get("password") : "");
            channel.setText(data.containsKey("channel") ? data.get("channel") : "");
            hexListenerPort.setText(data.containsKey("hexListenerPort") ? data.get("hexListenerPort") : "");
        }

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean botConnectionResult = BotController.getInstance().connect(serverHostname.getText(), Integer.parseInt(serverPort.getText()), botName.getText(), password.getText(), channel.getText());
                if (botConnectionResult) {
                    boolean hexConnectionResult = HexEventsController.getInstance().connect(Integer.parseInt(hexListenerPort.getText()));
                    setConnectButtonEnabled(!hexConnectionResult);
                } else {
                    setConnectButtonEnabled(true);
                }
            }
        });

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BotController.getInstance().disconnect();
                HexEventsController.getInstance().disconnect();

                setConnectButtonEnabled(true);
            }
        });
    }

    public void setConnectButtonEnabled(boolean enabled) {
        connectButton.setEnabled(enabled);
        disconnectButton.setEnabled(!enabled);

        serverHostname.setEnabled(enabled);
        serverPort.setEnabled(enabled);
        botName.setEnabled(enabled);
        password.setEnabled(enabled);
        channel.setEnabled(enabled);
        hexListenerPort.setEnabled(enabled);
    }

    public Boolean isTestCommandEnabled() {
        return enableTestCommandCheckBox.isSelected();
    }

    public Boolean isTestSaveDeckEventEnabled() {
        return enableTestSaveDeckEventCheckBox.isSelected();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();

        JLabel headerTitle = new JLabel("Configuration panel");
        header.add(headerTitle);

        return header;
    }

    private JPanel createForm() {
        JPanel outerLayout = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING_RIGHT));

        serverHostname = createFormInput(form, "Server hostname", null, false, false);
        serverPort = createFormInput(form, "Server port", null, false, true);

        createSpacer(form, new Dimension(0, 10));

        botName = createFormInput(form, "Bot name", null, false, false);
        password = createFormInput(form, "Server password", null, true, false);
        channel = createFormInput(form, "Channel", null, false, false);

        createSpacer(form, new Dimension(0, 10));

        hexListenerPort = createFormInput(form, "Hex listener port", null, false, true);

        createSpacer(form, new Dimension(0, 20));

        enableTestCommandCheckBox = createFormCheckBox(form, "Enable test draw command ( " + BotEvents.UDRAFT_TEST.getEventCommand() + " )");

        enableTestSaveDeckEventCheckBox = createFormCheckBox(form, "Enable test save deck Hex event");

        outerLayout.add(form, BorderLayout.NORTH);
        return outerLayout;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
        buttonsPanel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        connectButton = new JButton("Connect");
        connectButton.setAlignmentX(CENTER_ALIGNMENT);
        connectButton.setEnabled(false);

        disconnectButton = new JButton("Disconnect");
        disconnectButton.setEnabled(false);
        disconnectButton.setAlignmentX(CENTER_ALIGNMENT);

        buttonsPanel.add(connectButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(disconnectButton);

        return buttonsPanel;
    }

    private JTextField createFormInput(JPanel form, String title, Integer width, boolean passwordType, boolean onlyDigits) {
        GridBagLayout layout = (GridBagLayout) form.getLayout();

        JLabel titleLabel = new JLabel(title + ": ");
        layout.setConstraints(titleLabel, labelConstraints);
        form.add(titleLabel);

        JTextField inputField;
        if (passwordType) {
            inputField = new JPasswordField();
        } else {
            inputField = new JTextField();
        }
        if (onlyDigits) {
            PlainDocument doc = (PlainDocument) inputField.getDocument();
            doc.setDocumentFilter(new IntegerDocumentFilter());
        }
        inputField.getDocument().addDocumentListener(new InputChangeListener());

        if (width != null) {
            Dimension inputFieldSize = inputField.getPreferredSize();
            inputFieldSize.width = width;
            inputField.setPreferredSize(inputFieldSize);
            JPanel inputFieldPanel = new JPanel();
            inputFieldPanel.setLayout(new BorderLayout());
            inputFieldPanel.add(inputField, BorderLayout.WEST);

            layout.setConstraints(inputFieldPanel, inputConstraints);
            form.add(inputFieldPanel);
        } else {
            layout.setConstraints(inputField, inputConstraints);
            form.add(inputField);
        }

        return inputField;
    }


    private JCheckBox createFormCheckBox(JPanel form, String title) {
        GridBagLayout layout = (GridBagLayout) form.getLayout();
        JCheckBox checkBoxField = new JCheckBox(title);
        layout.setConstraints(checkBoxField, inputConstraints);
        form.add(checkBoxField);
        return checkBoxField;
    }

    private void createSpacer(JPanel form, Dimension dimension) {
        GridBagLayout layout = (GridBagLayout) form.getLayout();

        Component component = Box.createRigidArea(dimension);
        layout.setConstraints(component, inputConstraints);
        form.add(component);
    }

    private void initConstrains() {
        inputConstraints = new GridBagConstraints();
        inputConstraints.fill = GridBagConstraints.HORIZONTAL;
        inputConstraints.anchor = GridBagConstraints.NORTHWEST;
        inputConstraints.weightx = 1.0;
        inputConstraints.gridwidth = GridBagConstraints.REMAINDER;
        inputConstraints.insets = new Insets(1, 1, 1, 1);

        labelConstraints = (GridBagConstraints) inputConstraints.clone();
        labelConstraints.weightx = 0.0;
        labelConstraints.gridwidth = 1;
    }

    private void validateForm() {
        connectButton.setEnabled(!serverHostname.getText().isEmpty() &&
                !serverPort.getText().isEmpty() &&
                !botName.getText().isEmpty() &&
                !password.getText().isEmpty() &&
                !channel.getText().isEmpty() &&
                !hexListenerPort.getText().isEmpty());
    }

    private class InputChangeListener implements DocumentListener {

        private void saveData() {
            LinkedHashMap<String, String> dataMap = new LinkedHashMap<>();
            if (!serverHostname.getText().isEmpty()) dataMap.put("serverHostname", serverHostname.getText());
            if (!serverPort.getText().isEmpty()) dataMap.put("serverPort", serverPort.getText());
            if (!botName.getText().isEmpty()) dataMap.put("botName", botName.getText());
            if (!password.getText().isEmpty()) dataMap.put("password", password.getText());
            if (!channel.getText().isEmpty()) dataMap.put("channel", channel.getText());
            if (!hexListenerPort.getText().isEmpty()) dataMap.put("hexListenerPort", hexListenerPort.getText());
            configurationFile.saveData(dataMap);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            validateForm();
            saveData();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validateForm();
            saveData();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            validateForm();
            saveData();
        }
    }
}
