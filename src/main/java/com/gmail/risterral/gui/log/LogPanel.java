package com.gmail.risterral.gui.log;

import com.gmail.risterral.util.log.LogMessageDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogPanel extends JPanel {

    public static final int ROW_HEIGHT = 16;
    private Integer height = 0;
    private Timer resizeTimer = null;
    private Map<LogMessageDTO, Timer> removeTimers = new HashMap<>();
    private Map<LogMessageDTO, Component> componentsMap = new HashMap<>();

    private JPanel messagesPanel;
    private List<LogMessageDTO> messages = new ArrayList<>();

    public LogPanel() {
        super(new BorderLayout());

        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.PAGE_AXIS));
        this.add(messagesPanel, BorderLayout.CENTER);

        resizeTimer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                height += 1;
                if (height >= messages.size() * ROW_HEIGHT) {
                    height = messages.size() * ROW_HEIGHT;
                    resizeTimer.stop();
                }
                revalidate();
            }
        });
    }


    public void logMessage(final LogMessageDTO logMessage) {
        addMessage(logMessage);

        if (logMessage.getDuration() != null) {
            Timer timer = new Timer(logMessage.getDuration(), new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    messagesPanel.remove(componentsMap.get(logMessage));
                    messages.remove(logMessage);
                    height -= ROW_HEIGHT;
                    revalidate();
                    removeTimers.get(logMessage).stop();
                    componentsMap.remove(logMessage);
                    removeTimers.remove(logMessage);
                }
            });
            timer.start();
            removeTimers.put(logMessage, timer);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(this.getWidth(), height);
    }

    private void addMessage(final LogMessageDTO logMessage) {
        messages.add(logMessage);

        JPanel labelPanel = new JPanel(new BorderLayout());

        JLabel label = new JLabel(logMessage.getMessage());
        label.setHorizontalAlignment(SwingConstants.CENTER);

        switch (logMessage.getType()) {
            case SUCCESS:
                label.setForeground(new Color(0, 135, 0));
                break;
            case ERROR:
                label.setForeground(Color.red);
                break;
        }

        labelPanel.add(label, BorderLayout.NORTH);

        messagesPanel.add(labelPanel);
        componentsMap.put(logMessage, labelPanel);

        resizeTimer.start();
    }
}
