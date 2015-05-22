package com.gmail.risterral.gui.config;

import com.gmail.risterral.bot.events.BotEvents;
import com.gmail.risterral.configuration.ConfigurationController;
import com.gmail.risterral.configuration.ConfigurationDTO;
import com.gmail.risterral.controllers.bot.BotController;
import com.gmail.risterral.controllers.hex.HexEventsController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedHashMap;

public class ConfigurationPanel extends JPanel {
    private static final int PADDING = 15;
    private static final int PADDING_RIGHT = 100;

    private GridBagConstraints inputConstraints = null;
    private GridBagConstraints labelConstraints = null;
    private boolean isSaveEnabled = true;

    private JTextField serverHostname;
    private JTextField serverPort;
    private JTextField botName;
    private JTextField password;
    private JTextField channel;
    private JTextField hexListenerPort;

    private JCheckBox isBotAccountModded;
    private JCheckBox enableTestCommandCheckBox;
    private JCheckBox enableTestSaveDeckEventCheckBox;

    private JButton connectButton;
    private JButton disconnectButton;

    public ConfigurationPanel() {
        super();
        this.setLayout(new BorderLayout());
        this.initConstrains();

        this.add(createHeader(), BorderLayout.NORTH);
        this.add(createForm(), BorderLayout.CENTER);
        this.add(createButtonsPanel(), BorderLayout.EAST);

        isSaveEnabled = false;
        serverHostname.setText(ConfigurationController.getInstance().getConfigurationDTO().getServerHostname());
        serverPort.setText(ConfigurationController.getInstance().getConfigurationDTO().getServerPort());
        botName.setText(ConfigurationController.getInstance().getConfigurationDTO().getBotName());
        password.setText(ConfigurationController.getInstance().getConfigurationDTO().getPassword());
        channel.setText(ConfigurationController.getInstance().getConfigurationDTO().getChannel());
        hexListenerPort.setText(ConfigurationController.getInstance().getConfigurationDTO().getHexListenerPort());
        isBotAccountModded.setSelected(ConfigurationController.getInstance().getConfigurationDTO().getIsBotAccountModded());
        isSaveEnabled = true;

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

    public Boolean isBotAccountModded() {
        return isBotAccountModded.isSelected();
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

        isBotAccountModded = createFormCheckBox(form, "Does bot account have a mod?", true);
        enableTestCommandCheckBox = createFormCheckBox(form, "Enable test draw command ( " + BotEvents.UDRAFT_TEST.getEventCommand() + " [" + BotEvents.UDRAFT_TEST.getOptionalArguments()[0] + "] )", false);
        enableTestSaveDeckEventCheckBox = createFormCheckBox(form, "Enable test save deck Hex event", false);

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


    private JCheckBox createFormCheckBox(JPanel form, String title, boolean saveOnChange) {
        GridBagLayout layout = (GridBagLayout) form.getLayout();
        JCheckBox checkBoxField = new JCheckBox(title);

        if (saveOnChange) {
            checkBoxField.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    saveData();
                }
            });
        }
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

    private void saveData() {
        if (isSaveEnabled) {
            ConfigurationController.getInstance().getConfigurationDTO().setServerHostname(serverHostname.getText());
            ConfigurationController.getInstance().getConfigurationDTO().setServerPort(serverPort.getText());
            ConfigurationController.getInstance().getConfigurationDTO().setBotName(botName.getText());
            ConfigurationController.getInstance().getConfigurationDTO().setPassword(password.getText());
            ConfigurationController.getInstance().getConfigurationDTO().setChannel(channel.getText());
            ConfigurationController.getInstance().getConfigurationDTO().setHexListenerPort(hexListenerPort.getText());
            ConfigurationController.getInstance().getConfigurationDTO().setIsBotAccountModded(isBotAccountModded.isSelected());
            ConfigurationController.getInstance().saveData();
        }
    }

    private class InputChangeListener implements DocumentListener {

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
