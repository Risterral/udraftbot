package com.gmail.risterral.gui.common;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener, MouseListener {
    private final JTable table;
    private final Action action;

    private JButton renderButton;
    private JButton editButton;
    private boolean isButtonColumnEditor;

    public ButtonColumn(final JTable table, final Action action, int column) {
        this.table = table;
        this.action = action;

        renderButton = new JButton(new ImageIcon("images/delete.png"));
        renderButton.setBackground(new Color(255, 255, 255, 0));
        renderButton.setBorder(new LineBorder(Color.white));
        editButton = new JButton(new ImageIcon("images/delete.png"));
        editButton.setBackground(new Color(255, 255, 255, 0));
        editButton.setBorder(new LineBorder(Color.white));
        editButton.addActionListener(this);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
        table.addMouseListener(this);
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return editButton;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return renderButton;
    }

    public void actionPerformed(ActionEvent e) {
        int row = table.convertRowIndexToModel(table.getEditingRow());
        fireEditingStopped();

        ActionEvent event = new ActionEvent(table, ActionEvent.ACTION_PERFORMED, "" + row);
        action.actionPerformed(event);
    }

    public void mousePressed(MouseEvent e) {
        isButtonColumnEditor = table.isEditing() && table.getCellEditor() == this;
    }

    public void mouseReleased(MouseEvent e) {
        if (isButtonColumnEditor && table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }

        isButtonColumnEditor = false;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
