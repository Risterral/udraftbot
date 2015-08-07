package com.gmail.risterral.gui.commands;

import com.gmail.risterral.bot.events.BotEvents;
import com.gmail.risterral.gui.TabPanel;
import com.gmail.risterral.util.configuration.ConfigurationController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class CommandsPanel extends TabPanel {
    private static final int PADDING = 25;

    private boolean isSaveOrChangeEnabled = true;

    @Override
    public void update() {

    }

    public CommandsPanel(String tabTitle) {
        super(tabTitle);
        this.setLayout(new BorderLayout());

        isSaveOrChangeEnabled = false;
        this.add(createHeader(), BorderLayout.NORTH);
        this.add(createForm(), BorderLayout.CENTER);
        isSaveOrChangeEnabled = true;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        JLabel headerTitle = new JLabel("Commands panel");
        headerTitle.setFont(headerTitle.getFont().deriveFont(16.0f));
        header.add(headerTitle);

        return header;
    }

    private JScrollPane createForm() {
        Object[] columnNames = {"Command", "Description", "Enabled"};
        final ArrayList<Object[]> data = new ArrayList<>();
        final ArrayList<String> checkboxesNames = new ArrayList<>();
        for (BotEvents botEvent : BotEvents.values()) {
            Object[] row = new Object[3];
            row[0] = " " + botEvent.getEventCommand();
            row[1] = " " + botEvent.getDescription();
            Boolean isCommandEnabled = ConfigurationController.getInstance().getConfigurationDTO().getCommands().get(botEvent.name());
            row[2] = isCommandEnabled != null ? isCommandEnabled : botEvent.getIsDefaultEnabled();
            data.add(row);
            checkboxesNames.add(botEvent.name());
        }
        DefaultTableModel model = new DefaultTableModel(data.toArray(new Object[data.size()][3]), columnNames);

        JTable table = new JTable(model) {
            private static final long serialVersionUID = 1L;

            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    default:
                        return Boolean.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }

            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (column == 2 && isSaveOrChangeEnabled) {
                    ConfigurationController.getInstance().getConfigurationDTO().getCommands().put(checkboxesNames.get(row), (boolean) aValue);
                    ConfigurationController.getInstance().saveData();
                }
                super.setValueAt(aValue, row, column);
            }
        };

        for (int i = 0; i < data.size(); i++) {
            table.setRowHeight(i, 30);
        }

        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(Color.orange);
        table.getColumnModel().getColumn(0).setMinWidth(150);
        table.getColumnModel().getColumn(0).setMaxWidth(150);
        table.getColumnModel().getColumn(2).setMinWidth(60);
        table.getColumnModel().getColumn(2).setMaxWidth(60);

        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));

        return scrollPane;
    }
}
