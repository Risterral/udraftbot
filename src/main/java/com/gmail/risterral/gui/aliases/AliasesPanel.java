package com.gmail.risterral.gui.aliases;

import com.gmail.risterral.gui.TabPanel;
import com.gmail.risterral.gui.common.ButtonColumn;
import com.gmail.risterral.util.configuration.AliasDTO;
import com.gmail.risterral.util.configuration.ConfigurationController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AliasesPanel extends TabPanel {
    private static final int PADDING = 25;

    private boolean isSaveOrChangeEnabled = true;
    private ArrayList<Object[]> data;

    @Override
    public void update() {

    }

    public AliasesPanel(String tabTitle) {
        super(tabTitle);
        this.setLayout(new BorderLayout());

        isSaveOrChangeEnabled = false;
        this.add(createHeader(), BorderLayout.NORTH);
        this.add(createForm(), BorderLayout.CENTER);
        isSaveOrChangeEnabled = true;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        JLabel headerTitle = new JLabel("Aliases panel");
        headerTitle.setFont(headerTitle.getFont().deriveFont(16.0f));
        header.add(headerTitle);

        return header;
    }

    private JPanel createForm() {
        Object[] columnNames = {"Pattern", "Value", "Enabled", ""};
        data = new ArrayList<>();
        for (AliasDTO alias : ConfigurationController.getInstance().getConfigurationDTO().getAliases()) {
            Object[] row = new Object[4];
            row[0] = alias.getPattern();
            row[1] = alias.getValue();
            row[2] = alias.getEnabled();
            row[3] = "images/delete.png";
            data.add(row);
        }

        final DefaultTableModel model = new DefaultTableModel(data.toArray(new Object[data.size()][4]), columnNames);

        final JTable table = new JTable(model) {
            private static final long serialVersionUID = 1L;

            @Override
            public Class getColumnClass(int column) {
                return column != 2 ? String.class : Boolean.class;
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
        new ButtonColumn(table, delete, 3);

        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(Color.orange);
        table.getColumnModel().getColumn(2).setMinWidth(60);
        table.getColumnModel().getColumn(2).setMaxWidth(60);
        table.getColumnModel().getColumn(3).setMinWidth(30);
        table.getColumnModel().getColumn(3).setMaxWidth(30);

        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        JScrollPane scrollPane = new JScrollPane(table);

        JButton connectButton = new JButton("Add new alias");
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.requestFocus();
                isSaveOrChangeEnabled = false;
                model.addRow(new Object[]{"", "", true, ""});
                data.add(new Object[]{"", "", true, ""});
                isSaveOrChangeEnabled = true;
                table.setRowHeight(data.size() - 1, 30);
                table.editCellAt(data.size() - 1, 0);
            }
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BorderLayout());
        buttonsPanel.add(connectButton, BorderLayout.EAST);
        buttonsPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BorderLayout());
        formPanel.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING));
        formPanel.add(buttonsPanel, BorderLayout.NORTH);
        formPanel.add(scrollPane, BorderLayout.CENTER);

        return formPanel;
    }

    private void saveData() {
        if (isSaveOrChangeEnabled) {
            ArrayList<AliasDTO> aliases = new ArrayList<>();
            for (Object[] row : data) {
                String pattern = row[0] != null ? row[0].toString() : "";
                String value = row[1] != null ? row[1].toString() : "";
                if (!pattern.isEmpty() && !value.isEmpty()) {
                    aliases.add(new AliasDTO(pattern, value, (boolean) row[2]));
                }
            }

            ConfigurationController.getInstance().getConfigurationDTO().setAliases(aliases);
            ConfigurationController.getInstance().saveData();
        }
    }
}
