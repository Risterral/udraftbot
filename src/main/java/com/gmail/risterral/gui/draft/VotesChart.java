package com.gmail.risterral.gui.draft;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;

public class VotesChart extends JPanel {

    public static final int PADDING = 10;
    public static final int MEMBERS_MARGIN = 5;
    public static final int STRING_Y_OFFSET = 12;
    private LinkedHashMap<String, Integer> votes;

    public VotesChart(LinkedHashMap<String, Integer> votesMap) {
        this.votes = votesMap;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Integer maxCardNameWidth = 0;
        Integer maxCardVotes = 0;
        for (String cardName : votes.keySet()) {
            Integer cardWidth = g2.getFontMetrics().stringWidth(cardName);
            Integer cardVotes = votes.get(cardName);
            maxCardNameWidth = cardWidth > maxCardNameWidth ? cardWidth : maxCardNameWidth;
            maxCardVotes = cardVotes > maxCardVotes ? cardVotes : maxCardVotes;
        }
        Integer maxCardVotesWidth = g2.getFontMetrics().stringWidth(maxCardVotes.toString());

        Integer y = PADDING;
        for (String cardName : votes.keySet()) {
            g2.setColor(Color.black);
            g2.drawString(cardName, PADDING, y + STRING_Y_OFFSET);

            Integer rectX = PADDING + maxCardNameWidth + MEMBERS_MARGIN;
            Integer rectWidth = (int) (((double) votes.get(cardName) / maxCardVotes) * (this.getWidth() - rectX - PADDING - maxCardVotesWidth - MEMBERS_MARGIN));
            g2.setColor(Color.green);
            g2.fill3DRect(rectX, y, rectWidth, 15, true);

            g2.setColor(Color.black);
            g2.drawString(votes.get(cardName).toString(), rectX + rectWidth + MEMBERS_MARGIN, y + STRING_Y_OFFSET);
            y += 20;
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    public void setVotes(LinkedHashMap<String, Integer> votes) {
        this.votes = votes;
        this.updateUI();
    }
}
