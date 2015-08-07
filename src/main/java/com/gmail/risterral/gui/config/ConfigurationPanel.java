package com.gmail.risterral.gui.config;

import com.gmail.risterral.bot.BotController;
import com.gmail.risterral.gui.GUIController;
import com.gmail.risterral.gui.TabPanel;
import com.gmail.risterral.gui.common.IntegerDocumentFilter;
import com.gmail.risterral.hex.HexEventsController;
import com.gmail.risterral.util.configuration.ConfigurationController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ConfigurationPanel extends TabPanel {
    private static final int PADDING = 15;
    private static final int PADDING_RIGHT = 100;

    private GridBagConstraints inputConstraints = null;
    private GridBagConstraints labelConstraints = null;
    private boolean isSaveOrChangeEnabled = true;

    private JTextField serverHostname;
    private JTextField serverPort;
    private JTextField botName;
    private JTextField password;
    private JTextField channel;
    private JTextField hexListenerPort;

    private JCheckBox isBotAccountModdedCheckBox;
    private JCheckBox useCustomHtmlDraftPanelCheckBox;
    private JCheckBox enableTestSaveDeckEventCheckBox;

    private JButton connectButton;
    private JButton disconnectButton;

    private JButton startListeningToDraftButton;
    private JButton stopListeningToDraftButton;

    @Override
    public void update() {

    }

    public ConfigurationPanel(String tabTitle) {
        super(tabTitle);
        this.setLayout(new BorderLayout());
        this.initConstrains();

        this.add(createHeader(), BorderLayout.NORTH);
        this.add(createForm(), BorderLayout.CENTER);
        this.add(createButtonsPanel(), BorderLayout.EAST);

        isSaveOrChangeEnabled = false;
        serverHostname.setText(ConfigurationController.getInstance().getConfigurationDTO().getServerHostname());
        serverPort.setText(ConfigurationController.getInstance().getConfigurationDTO().getServerPort());
        botName.setText(ConfigurationController.getInstance().getConfigurationDTO().getBotName());
        password.setText(ConfigurationController.getInstance().getConfigurationDTO().getPassword());
        channel.setText(ConfigurationController.getInstance().getConfigurationDTO().getChannel());
        hexListenerPort.setText(ConfigurationController.getInstance().getConfigurationDTO().getHexListenerPort());
        isBotAccountModdedCheckBox.setSelected(ConfigurationController.getInstance().getConfigurationDTO().getIsBotAccountModded());
        useCustomHtmlDraftPanelCheckBox.setSelected(ConfigurationController.getInstance().getConfigurationDTO().getUseCustomHtmlDraftPanel());
        isSaveOrChangeEnabled = true;

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

        startListeningToDraftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HexEventsController.getInstance().setListenToDraft(true);
                GUIController.getInstance().setListenToDraft(true);
                stopListeningToDraftButton.setEnabled(true);
                startListeningToDraftButton.setEnabled(false);
            }
        });

        stopListeningToDraftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HexEventsController.getInstance().setListenToDraft(false);
                GUIController.getInstance().setListenToDraft(false);
                startListeningToDraftButton.setEnabled(true);
                stopListeningToDraftButton.setEnabled(false);
            }
        });
    }

    public void setConnectButtonEnabled(boolean enabled) {
        connectButton.setEnabled(enabled);
        disconnectButton.setEnabled(!enabled);

        startListeningToDraftButton.setEnabled(!enabled);
        stopListeningToDraftButton.setEnabled(false);
        HexEventsController.getInstance().setListenToDraft(false);

        serverHostname.setEnabled(enabled);
        serverPort.setEnabled(enabled);
        botName.setEnabled(enabled);
        password.setEnabled(enabled);
        channel.setEnabled(enabled);
        hexListenerPort.setEnabled(enabled);
    }

    public Boolean isBotAccountModded() {
        return isBotAccountModdedCheckBox.isSelected();
    }

    public Boolean isUseCustomHtmlDraftPanel() {
        return useCustomHtmlDraftPanelCheckBox.isSelected();
    }

    public Boolean isTestSaveDeckEventEnabled() {
        return enableTestSaveDeckEventCheckBox.isSelected();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();

        JLabel headerTitle = new JLabel("Configuration panel");
        headerTitle.setFont(headerTitle.getFont().deriveFont(16.0f));
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

        isBotAccountModdedCheckBox = createFormCheckBox(form, "Does bot account have a mod?", true, false);
        useCustomHtmlDraftPanelCheckBox = createFormCheckBox(form, "Use custom html draft panel", true, true);
        enableTestSaveDeckEventCheckBox = createFormCheckBox(form, "Enable test save deck Hex event", false, false);

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

        startListeningToDraftButton = new JButton("Start listening to draft");
        startListeningToDraftButton.setAlignmentX(CENTER_ALIGNMENT);
        startListeningToDraftButton.setEnabled(false);

        stopListeningToDraftButton = new JButton("Stop listening to draft");
        stopListeningToDraftButton.setAlignmentX(CENTER_ALIGNMENT);
        stopListeningToDraftButton.setEnabled(false);

        buttonsPanel.add(connectButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(disconnectButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        buttonsPanel.add(startListeningToDraftButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        buttonsPanel.add(stopListeningToDraftButton);

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


    private JCheckBox createFormCheckBox(JPanel form, String title, final boolean saveOnChange, final boolean updateDraftPanelOnChange) {
        GridBagLayout layout = (GridBagLayout) form.getLayout();
        JCheckBox checkBoxField = new JCheckBox(title);

        if (saveOnChange || updateDraftPanelOnChange) {
            checkBoxField.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (saveOnChange) {
                        saveData();
                    }
                    if (updateDraftPanelOnChange && isSaveOrChangeEnabled) {
                        GUIController.getInstance().setDraftPanelView(useCustomHtmlDraftPanelCheckBox.isSelected());
                    }
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
        if (isSaveOrChangeEnabled) {
            ConfigurationController.getInstance().getConfigurationDTO().setServerHostname(serverHostname.getText());
            ConfigurationController.getInstance().getConfigurationDTO().setServerPort(serverPort.getText());
            ConfigurationController.getInstance().getConfigurationDTO().setBotName(botName.getText());
            ConfigurationController.getInstance().getConfigurationDTO().setPassword(password.getText());
            ConfigurationController.getInstance().getConfigurationDTO().setChannel(channel.getText());
            ConfigurationController.getInstance().getConfigurationDTO().setHexListenerPort(hexListenerPort.getText());
            ConfigurationController.getInstance().getConfigurationDTO().setIsBotAccountModded(isBotAccountModdedCheckBox.isSelected());
            ConfigurationController.getInstance().getConfigurationDTO().setUseCustomHtmlDraftPanel(useCustomHtmlDraftPanelCheckBox.isSelected());
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
