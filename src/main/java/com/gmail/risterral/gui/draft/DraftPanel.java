package com.gmail.risterral.gui.draft;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DraftPanel extends JPanel {
    private static final String VOTES_CHART_LABEL_PATTERN = "{0}";
    private static final String VOTES_CHART_LABEL_TEXT = "Number of votes: " + VOTES_CHART_LABEL_PATTERN;

    private VotesChart votesChart;
    private JLabel votesChartLabel;
    private CardImage cardImage;

    public DraftPanel() {

        super();
        this.setLayout(new BorderLayout());

        votesChart = new VotesChart(new LinkedHashMap<String, Integer>());

        JScrollPane votesChartScrollPane = new JScrollPane();
        votesChartScrollPane.getViewport().add(votesChart);

        JPanel votesChartPanel = new JPanel(new BorderLayout());
        votesChartPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        votesChartLabel = new JLabel(VOTES_CHART_LABEL_TEXT.replace(VOTES_CHART_LABEL_PATTERN, "0"));
        votesChartLabel.setHorizontalAlignment(SwingConstants.CENTER);
        votesChartLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        votesChartPanel.add(votesChartLabel, BorderLayout.NORTH);
        votesChartPanel.add(votesChartScrollPane, BorderLayout.CENTER);

        this.add(votesChartPanel, BorderLayout.WEST);


        cardImage = new CardImage();

        JPanel cardImagePanel = new JPanel(new BorderLayout());
        cardImagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel cardImageLabel = new JLabel("Currently winning card");
        cardImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardImageLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        cardImagePanel.add(cardImageLabel, BorderLayout.NORTH);
        cardImagePanel.add(cardImage, BorderLayout.CENTER);

        this.add(cardImagePanel, BorderLayout.EAST);
    }

    public void setVotesMap(LinkedHashMap<String, Integer> votesMap) {
        Integer numberOfVotes = 0;
        for (String card : votesMap.keySet()) {
            numberOfVotes += votesMap.get(card);
        }
        votesChartLabel.setText(VOTES_CHART_LABEL_TEXT.replace(VOTES_CHART_LABEL_PATTERN, numberOfVotes.toString()));

        if (!votesMap.isEmpty()) {
            cardImage.setImage(new ArrayList<>(votesMap.keySet()).get(0), 0);
        } else {
            cardImage.setImage("", 0);
        }

        votesChart.setVotes(votesMap);
    }
}
