package com.gmail.risterral.gui.giveaways;

import com.gmail.risterral.gui.GUIController;
import com.gmail.risterral.gui.TabPanel;
import com.gmail.risterral.gui.common.ButtonColumn;
import com.gmail.risterral.util.configuration.ConfigurationController;
import com.gmail.risterral.util.configuration.GiveawayDTO;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GiveawaysPanel extends TabPanel {
    private static final int PADDING = 25;

    private boolean isSaveOrChangeEnabled = true;
    private GridBagConstraints inputConstraints = null;
    private GridBagConstraints labelConstraints = null;
    private JTextField giveawayMessage;
    private ArrayList<Object[]> data;
    private DefaultTableModel tableModel;
    private JTable table;

    @Override
    public void update() {

    }

    public GiveawaysPanel(String tabTitle) {
        super(tabTitle);
        this.setLayout(new BorderLayout());
        this.initConstrains();

        isSaveOrChangeEnabled = false;
        this.add(createHeader(), BorderLayout.NORTH);
        this.add(createForm(), BorderLayout.CENTER);
        giveawayMessage.setText(ConfigurationController.getInstance().getConfigurationDTO().getGiveawayMessage());
        isSaveOrChangeEnabled = true;
    }

    public void updateGiveaways() {
        isSaveOrChangeEnabled = false;
        data.clear();
        for (GiveawayDTO giveawayDTO : ConfigurationController.getInstance().getConfigurationDTO().getGiveaways()) {
            Object[] giveaway = new Object[7];
            giveaway[0] = giveawayDTO.getName();
            giveaway[1] = giveawayDTO.getChance();
            giveaway[2] = giveawayDTO.getMinimumNumberOfVotes();
            giveaway[3] = giveawayDTO.getRecurring();
            giveaway[4] = giveawayDTO.getRemoveAfterAppearing();
            giveaway[5] = giveawayDTO.getEnabled();
            giveaway[6] = "images/delete.png";
            data.add(giveaway);
        }

        Integer numberOfRows = tableModel.getRowCount();
        for (int i = 0; i < numberOfRows; i++) {
            tableModel.removeRow(0);
        }

        for (int i = 0; i < data.size(); i++) {
            tableModel.addRow(data.get(i));
            table.setRowHeight(i, 30);
        }
        isSaveOrChangeEnabled = true;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        JLabel headerTitle = new JLabel("Giveaways configuration panel");
        headerTitle.setFont(headerTitle.getFont().deriveFont(16.0f));
        header.add(headerTitle);

        return header;
    }

    private JPanel createForm() {
        Object[] columnNames = {"Giveaway name", "Chance to unlock [0-100]", "Number of votes to unlock", "Recurring", "Remove after unlocking", "Enabled", ""};
        data = new ArrayList<>();
        for (GiveawayDTO giveaway : ConfigurationController.getInstance().getConfigurationDTO().getGiveaways()) {
            Object[] row = new Object[7];
            row[0] = giveaway.getName();
            row[1] = giveaway.getChance();
            row[2] = giveaway.getMinimumNumberOfVotes();
            row[3] = giveaway.getRecurring();
            row[4] = giveaway.getRemoveAfterAppearing();
            row[5] = giveaway.getEnabled();
            row[6] = "images/delete.png";
            data.add(row);
        }

        tableModel = new DefaultTableModel(data.toArray(new Object[data.size()][7]), columnNames);

        table = new JTable(tableModel) {
            private static final long serialVersionUID = 1L;

            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    case 2:
                        return String.class;
                    case 3:
                        return Boolean.class;
                    case 4:
                        return Boolean.class;
                    case 5:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                super.setValueAt(aValue, row, column);
                data.get(row)[column] = aValue;
                saveData();
            }
        };

        for (int i = 0; i < data.size(); i++) {
            table.setRowHeight(i, 30);
        }

        Action delete = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTable table = (JTable) e.getSource();
                int modelRow = Integer.valueOf(e.getActionCommand());
                ((DefaultTableModel) table.getModel()).removeRow(modelRow);
                data.remove(modelRow);
                saveData();
            }
        };

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        table.getColumnModel().getColumn(1).setCellEditor(new NumberCellEditor(new JTextField(), 0., 100.));
        table.getColumnModel().getColumn(2).setCellEditor(new NumberCellEditor(new JTextField(), 0., null));

        new ButtonColumn(table, delete, 6);

        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(Color.orange);
        table.getColumnModel().getColumn(3).setMinWidth(80);
        table.getColumnModel().getColumn(3).setMaxWidth(80);
        table.getColumnModel().getColumn(4).setMinWidth(140);
        table.getColumnModel().getColumn(4).setMaxWidth(140);
        table.getColumnModel().getColumn(5).setMinWidth(60);
        table.getColumnModel().getColumn(5).setMaxWidth(60);
        table.getColumnModel().getColumn(6).setMinWidth(30);
        table.getColumnModel().getColumn(6).setMaxWidth(30);

        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        JScrollPane scrollPane = new JScrollPane(table);

        final JCheckBox enableGiveawaysCheckBox = new JCheckBox("Enable giveaways");
        enableGiveawaysCheckBox.setSelected(ConfigurationController.getInstance().getConfigurationDTO().getEnableGiveaways());
        enableGiveawaysCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConfigurationController.getInstance().getConfigurationDTO().setEnableGiveaways(enableGiveawaysCheckBox.isSelected());
                ConfigurationController.getInstance().saveData();

                GUIController.getInstance().updateGiveawaysResultPanel(enableGiveawaysCheckBox.isSelected());
            }
        });

        JPanel giveawayMessageForm = new JPanel(new GridBagLayout());
        giveawayMessageForm.setBorder(new EmptyBorder(PADDING, 0, PADDING, PADDING));
        giveawayMessage = createFormInput(giveawayMessageForm, "Successful giveaway message");
        createSpacer(giveawayMessageForm, new Dimension(0, 5));
        createFormLabel(giveawayMessageForm, "{giveaway_name} - parameter replaced with giveaway name value.");
        createFormLabel(giveawayMessageForm, "{giveaway_chance} - parameter replaced with giveaway chance value.");
        createFormLabel(giveawayMessageForm, "{giveaway_minimum_number_of_votes} - parameter replaced with giveaway minimum number of votes value.");

        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new BorderLayout());
        checkBoxPanel.add(enableGiveawaysCheckBox, BorderLayout.WEST);
        checkBoxPanel.add(giveawayMessageForm, BorderLayout.SOUTH);
        checkBoxPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JButton connectButton = new JButton("Add new giveaway");
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.requestFocus();
                isSaveOrChangeEnabled = false;
                tableModel.addRow(new Object[]{"", 0, 0, true, true, true, ""});
                data.add(new Object[]{"", 0, 0, true, true, true, ""});
                isSaveOrChangeEnabled = true;
                table.setRowHeight(data.size() - 1, 30);
                table.editCellAt(data.size() - 1, 0);
            }
        });

        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout());
        upperPanel.add(checkBoxPanel, BorderLayout.NORTH);
        upperPanel.add(connectButton, BorderLayout.EAST);
        upperPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BorderLayout());
        formPanel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
        formPanel.add(upperPanel, BorderLayout.NORTH);
        formPanel.add(scrollPane, BorderLayout.CENTER);

        return formPanel;
    }

    private void saveData() {
        if (isSaveOrChangeEnabled) {
            ArrayList<GiveawayDTO> giveaways = new ArrayList<>();
            for (Object[] row : data) {
                String name = row[0] != null ? row[0].toString() : "";
                Double chance = row[1] != null ? Double.valueOf(row[1].toString()) : 0.;
                Integer minimumNumberOFVotes = row[2] != null ? Integer.valueOf(row[2].toString()) : 0;
                if (!name.isEmpty() && chance > 0 && minimumNumberOFVotes > 0) {
                    giveaways.add(new GiveawayDTO(name, chance, minimumNumberOFVotes, (boolean) row[3], (boolean) row[4], (boolean) row[5]));
                }
            }

            ConfigurationController.getInstance().getConfigurationDTO().setGiveaways(giveaways);
            ConfigurationController.getInstance().saveData();
        }
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

    private JTextField createFormInput(JPanel form, String title) {
        GridBagLayout layout = (GridBagLayout) form.getLayout();

        JLabel titleLabel = new JLabel(title + ": ");
        layout.setConstraints(titleLabel, labelConstraints);
        form.add(titleLabel);

        JTextField inputField = new JTextField();
        inputField.getDocument().addDocumentListener(new InputChangeListener());

        layout.setConstraints(inputField, inputConstraints);
        form.add(inputField);

        return inputField;
    }

    private void createFormLabel(JPanel form, String value) {
        GridBagLayout layout = (GridBagLayout) form.getLayout();
        JLabel titleLabel = new JLabel(value);
        layout.setConstraints(titleLabel, inputConstraints);
        form.add(titleLabel);
    }


    private void createSpacer(JPanel form, Dimension dimension) {
        GridBagLayout layout = (GridBagLayout) form.getLayout();
        Component component = Box.createRigidArea(dimension);
        layout.setConstraints(component, inputConstraints);
        form.add(component);
    }

    private static class NumberCellEditor extends DefaultCellEditor {
        private static final Border red = new LineBorder(Color.red);
        private static final Border black = new LineBorder(Color.black);

        private final JTextField textField;
        private final Double start;
        private final Double end;

        public NumberCellEditor(JTextField textField, Double start, Double end) {
            super(textField);
            this.textField = textField;
            this.start = start;
            this.end = end;
            this.textField.setHorizontalAlignment(JTextField.RIGHT);
        }

        @Override
        public boolean stopCellEditing() {
            try {
                double v = Double.valueOf(textField.getText());
                if ((start != null && v < start) || (end != null && v > end)) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                textField.setBorder(red);
                return false;
            }
            return super.stopCellEditing();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            textField.setBorder(black);
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }
    }

    private class InputChangeListener implements DocumentListener {

        private void saveGiveawayMessage() {
            if (isSaveOrChangeEnabled && giveawayMessage.getText() != null && !giveawayMessage.getText().isEmpty()) {
                ConfigurationController.getInstance().getConfigurationDTO().setGiveawayMessage(giveawayMessage.getText());
                ConfigurationController.getInstance().saveData();
            }
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            saveGiveawayMessage();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            saveGiveawayMessage();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            saveGiveawayMessage();
        }
    }

}
