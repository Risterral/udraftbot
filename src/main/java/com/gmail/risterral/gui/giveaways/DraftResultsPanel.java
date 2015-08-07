package com.gmail.risterral.gui.giveaways;

import com.gmail.risterral.gui.TabPanel;
import com.gmail.risterral.gui.common.CardImage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class DraftResultsPanel extends TabPanel {
    private static final int PADDING = 30;
    private static final int PADDING_RIGHT = 100;

    private GridBagConstraints inputConstraints = null;
    private GridBagConstraints labelConstraints = null;

    private JLabel numberOfAllVotes;
    private JLabel mostPopularCard;
    private CardImage cardImage;
    private JLabel giveawayLabel;
    private JScrollPane scrollPane;
    private JPanel giveawayPanel;

    @Override
    public void update() {

    }

    public DraftResultsPanel(String tabTitle) {
        super(tabTitle);
        this.setLayout(new BorderLayout());
        this.initConstrains();

        this.add(createHeader(), BorderLayout.NORTH);
        this.add(createForm(), BorderLayout.CENTER);

        cardImage = new CardImage();
        JPanel cardImagePanel = new JPanel(new BorderLayout());
        cardImagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        cardImagePanel.add(cardImage, BorderLayout.CENTER);

        this.add(cardImagePanel, BorderLayout.EAST);
    }

    public void updateGiveawaysResultPanel(Boolean isGiveawaysEnabled) {
        giveawayLabel.setVisible(isGiveawaysEnabled);
        giveawayPanel.setVisible(isGiveawaysEnabled);
    }

    public void updateGiveawaysResultPanel(Integer numberOfAllVotes, String mostPopularCard, Integer votesPerMostPopularCard, ArrayList<GiveawayResultDTO> unlockedGiveaways) {
        this.numberOfAllVotes.setText(numberOfAllVotes.toString());
        if (!mostPopularCard.isEmpty()) {
            this.mostPopularCard.setText(mostPopularCard + " (" + votesPerMostPopularCard + ")");
            cardImage.setWinningCardImage(mostPopularCard);
        } else {
            this.mostPopularCard.setText("");
            cardImage.setWinningCardImage("");
        }

        giveawayPanel.removeAll();
        for (GiveawayResultDTO unlockedGiveaway : unlockedGiveaways) {
            createFormLabel(giveawayPanel, null, unlockedGiveaway.getName() + " ( " + unlockedGiveaway.getChance() + "% )", unlockedGiveaway.getIsSuccessful() ? Color.green : Color.red);
            createSpacer(giveawayPanel, new Dimension(0, 5));
        }
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();

        JLabel headerTitle = new JLabel("Draft results panel");
        headerTitle.setFont(headerTitle.getFont().deriveFont(16.0f));
        header.add(headerTitle);

        return header;
    }

    private JPanel createForm() {
        JPanel outerLayout = new JPanel(new BorderLayout());
        JPanel innerLayout = new JPanel();
        innerLayout.setLayout(new BoxLayout(innerLayout, BoxLayout.PAGE_AXIS));
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(PADDING, PADDING, PADDING, PADDING_RIGHT));

        numberOfAllVotes = createFormLabel(form, "Number of all votes", "0", null);
        createSpacer(form, new Dimension(0, 10));
        mostPopularCard = createFormLabel(form, "The most popular card", "", null);
        createSpacer(form, new Dimension(0, 10));
        giveawayLabel = createFormLabel(form, "Unlocked giveaways", null, null);

        giveawayPanel = new JPanel(new GridBagLayout());
        giveawayPanel.setAlignmentY(TOP_ALIGNMENT);
        JPanel outerGiveawayLayout = new JPanel(new BorderLayout());
        outerGiveawayLayout.add(giveawayPanel, BorderLayout.NORTH);
        scrollPane = new JScrollPane(outerGiveawayLayout);
        scrollPane.setBorder(new EmptyBorder(0, 100, PADDING, PADDING_RIGHT));
        scrollPane.setPreferredSize(new Dimension(300, 400));
        scrollPane.setAlignmentY(JScrollPane.TOP_ALIGNMENT);

        innerLayout.add(form);
        innerLayout.add(scrollPane);
        outerLayout.add(innerLayout, BorderLayout.NORTH);
        return outerLayout;
    }

    private JLabel createFormLabel(JPanel form, String title, String value, Color valueLabelColor) {
        GridBagLayout layout = (GridBagLayout) form.getLayout();

        JLabel titleLabel = null;
        if (title != null) {
            titleLabel = new JLabel(title + ": ");
            titleLabel.setFont(titleLabel.getFont().deriveFont(14.0f));
            layout.setConstraints(titleLabel, labelConstraints);
            form.add(titleLabel);
        }

        if (value != null) {
            JLabel valueLabel = new JLabel(value);
            valueLabel.setFont(valueLabel.getFont().deriveFont(14.0f));

            if (valueLabelColor != null) {
                valueLabel.setForeground(valueLabelColor);
            }

            layout.setConstraints(valueLabel, inputConstraints);
            form.add(valueLabel);

            return valueLabel;
        }
        return titleLabel;
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
}
