package com.gmail.risterral.gui.draft;

import com.gmail.risterral.gui.GUIController;
import com.gmail.risterral.gui.TabPanel;
import com.gmail.risterral.gui.common.CardImage;
import com.gmail.risterral.util.configuration.ConfigurationController;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DraftPanel extends TabPanel {
    private static final String VOTES_CHART_LABEL_PATTERN = "{0}";
    private static final String VOTES_CHART_LABEL_TEXT = "Number of votes: " + VOTES_CHART_LABEL_PATTERN;
    private static final String DATA_DRAFT_PANEL_HTML_SRC = "data/DraftPanel.html";

    private JLabel warningLabel;

    private DraftPanelHtml draftPanelHtml;
    private String htmlContent;
    private boolean isHtmlValid = true;

    private JPanel votesChartPanel;
    private JPanel cardImagePanel;
    private VotesChart votesChart;
    private JLabel votesChartLabel;
    private CardImage cardImage;

    private LinkedHashMap<String, Integer> lastVotesMap;

    public DraftPanel(String tabTitle, final Boolean useCustomHtmlDraftPanel) {
        super(tabTitle);
        this.setLayout(new BorderLayout());

        warningLabel = new JLabel("You are not listening to draft");
        warningLabel.setFont(warningLabel.getFont().deriveFont(32.0f));
        warningLabel.setHorizontalAlignment(SwingConstants.CENTER);
        warningLabel.setForeground(Color.red);


        Platform.setImplicitExit(false);

        this.add(warningLabel, BorderLayout.NORTH);

        try {
            createDraftPanelHtml(Files.readAllLines(Paths.get(DATA_DRAFT_PANEL_HTML_SRC), Charset.defaultCharset()), useCustomHtmlDraftPanel);
        } catch (Exception e) {
            isHtmlValid = false;
        }
        votesChart = new VotesChart(new LinkedHashMap<String, Integer>());

        JScrollPane votesChartScrollPane = new JScrollPane();
        votesChartScrollPane.getViewport().add(votesChart);

        votesChartPanel = new JPanel(new BorderLayout());
        votesChartPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        votesChartLabel = new JLabel(VOTES_CHART_LABEL_TEXT.replace(VOTES_CHART_LABEL_PATTERN, "0"));
        votesChartLabel.setHorizontalAlignment(SwingConstants.CENTER);
        votesChartLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        votesChartPanel.add(votesChartLabel, BorderLayout.NORTH);
        votesChartPanel.add(votesChartScrollPane, BorderLayout.CENTER);

        this.add(votesChartPanel, BorderLayout.WEST);


        cardImage = new CardImage();

        cardImagePanel = new JPanel(new BorderLayout());
        cardImagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel cardImageLabel = new JLabel("Currently winning card");
        cardImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardImageLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        cardImagePanel.add(cardImageLabel, BorderLayout.NORTH);
        cardImagePanel.add(cardImage, BorderLayout.CENTER);

        this.add(cardImagePanel, BorderLayout.EAST);

        setView(useCustomHtmlDraftPanel);
    }

    public void setListenToDraft(boolean isListening) {
        warningLabel.setVisible(!isListening);
    }

    public void setView(boolean isUseCustomHtmlDraftPanel) {
        if (isHtmlValid) {
            votesChartPanel.setVisible(!isUseCustomHtmlDraftPanel);
            cardImagePanel.setVisible(!isUseCustomHtmlDraftPanel);

            if (draftPanelHtml != null) {
                draftPanelHtml.setVisible(isUseCustomHtmlDraftPanel);
            }

            if (lastVotesMap != null) {
                setVotesMap(lastVotesMap);
            }
        }
    }

    public void setVotesMap(LinkedHashMap<String, Integer> votesMap) {
        this.lastVotesMap = votesMap;
        if (isHtmlValid && GUIController.getInstance().isUseCustomHtmlDraftPanel()) {
            draftPanelHtml.setVotesMap(votesMap);
        } else {
            Integer numberOfVotes = 0;
            for (String card : votesMap.keySet()) {
                numberOfVotes += votesMap.get(card);
            }
            votesChartLabel.setText(VOTES_CHART_LABEL_TEXT.replace(VOTES_CHART_LABEL_PATTERN, numberOfVotes.toString()));

            cardImage.setWinningCardImage(!votesMap.isEmpty() ? new ArrayList<>(votesMap.keySet()).get(0) : "");
            votesChart.setVotes(votesMap);
        }
    }

    private void createDraftPanelHtml(List<String> contentLines, final Boolean useCustomHtmlDraftPanel) {
        final StringBuilder content = new StringBuilder();
        for (String contentLine : contentLines) {
            content.append(contentLine).append("\n");
        }

        this.htmlContent = content.toString();

        new JFXPanel();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                draftPanelHtml = new DraftPanelHtml(htmlContent);
                add(draftPanelHtml, BorderLayout.CENTER);

                if (draftPanelHtml != null) {
                    draftPanelHtml.setVisible(useCustomHtmlDraftPanel);
                }
            }
        });
    }

    @Override
    public void update() {


        new JFXPanel();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (draftPanelHtml != null) {
                    remove(draftPanelHtml);
                }

                draftPanelHtml = new DraftPanelHtml(htmlContent);
                add(draftPanelHtml, BorderLayout.CENTER);

                setView(ConfigurationController.getInstance().getConfigurationDTO().getUseCustomHtmlDraftPanel());
                setView(!ConfigurationController.getInstance().getConfigurationDTO().getUseCustomHtmlDraftPanel());
                setView(ConfigurationController.getInstance().getConfigurationDTO().getUseCustomHtmlDraftPanel());
//                if (draftPanelHtml != null) {
//                    draftPanelHtml.setVisible(true);
//                }
            }
        });


//    }
//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//
//        });
    }
}
