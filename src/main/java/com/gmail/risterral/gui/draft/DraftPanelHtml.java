package com.gmail.risterral.gui.draft;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class DraftPanelHtml extends JFXPanel {
    private static final String LOAD_FAILED_IMAGE_SRC = "images/load_failed.png";
    private static final String IMAGE_PREFIX = "https://hextcg.com/wp-content/themes/hex/images/autocard/";
    private static final String IMAGE_EXTENSION = ".png";
    private static final int MAXIMUM_NUMBER_OF_CARDS = 15;
    private static final int MAXIMUM_NUMBER_OF_LOADING_IMAGE_TRIES = 3;

    private final String pureContent;
    private final WebEngine webEngine;
    private final String loadFailedImage;

    private String currentlyWinningCardName;
    private String contentWithoutImage;
    private HashMap<String, Thread> loadImageWorkers = new HashMap<>();
    private volatile HashMap<String, String> imageCache = new HashMap<>();

    public DraftPanelHtml(String content) {
        super();
        this.pureContent = content;

        loadFailedImage = readImage(new File(LOAD_FAILED_IMAGE_SRC));

        WebView browser = new WebView();
        webEngine = browser.getEngine();

        this.setScene(new Scene(browser));

        setVotesMap(new LinkedHashMap<String, Integer>());
    }

    public void setVotesMap(final LinkedHashMap<String, Integer> votesMap) {
        Integer numberOfAllVotes = 0;
        Integer highestNumberOfCardVotes = 1;
        currentlyWinningCardName = !votesMap.isEmpty() ? new ArrayList<>(votesMap.keySet()).get(0) : "";
        for (String card : votesMap.keySet()) {
            numberOfAllVotes += votesMap.get(card);
            if (votesMap.get(card) > highestNumberOfCardVotes) {
                highestNumberOfCardVotes = votesMap.get(card);
            }
        }

        String content = pureContent.replace("{number_of_votes}", numberOfAllVotes.toString())
                .replace("{currently_winning_card_name}", currentlyWinningCardName);

        int i = 0;
        for (String cardName : votesMap.keySet()) {
            ++i;
            Integer cardNumberOfVotes = votesMap.get(cardName);
            content = content.replace("{card_" + i + "_visibility}", "visible")
                    .replace("{card_" + i + "_name}", cardName)
                    .replace("{card_" + i + "_votes_bar_width}", ((double) cardNumberOfVotes * 100 / highestNumberOfCardVotes) + "%")
                    .replace("{card_" + i + "_number_of_votes}", cardNumberOfVotes.toString());
        }

        for (int j = i; j <= MAXIMUM_NUMBER_OF_CARDS; j++) {
            content = content.replace("{card_" + j + "_visibility}", "hidden");
        }

        contentWithoutImage = content;
        if (currentlyWinningCardName.isEmpty()) {
            setImage("");
        } else if (imageCache.containsKey(currentlyWinningCardName)) {
            setImage(imageCache.get(currentlyWinningCardName));
        } else if (!loadImageWorkers.containsKey(currentlyWinningCardName)) {
            Thread worker = new Thread(new LoadImage(currentlyWinningCardName));
            worker.start();
            loadImageWorkers.put(currentlyWinningCardName, worker);
            setImage("");
        }
    }

    private String readImage(File imageFile) {
        try {
            return imageToBase64(ImageIO.read(imageFile));
        } catch (Exception e) {
            return null;
        }
    }

    private String readImage(URL url) {
        try {
            return imageToBase64(ImageIO.read(url));
        } catch (Exception e) {
            return null;
        }
    }

    private String imageToBase64(BufferedImage image) {
        if (image == null) {
            return null;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArrayOutputStream);
            byteArrayOutputStream.flush();
            byte[] imageInByte = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();

            return "data:image/png;base64," + DatatypeConverter.printBase64Binary(imageInByte);

        } catch (Exception e) {
            return null;
        }
    }

    private void updateCurrentWinningCard() {
        if (imageCache.containsKey(currentlyWinningCardName)) {
            setImage(imageCache.get(currentlyWinningCardName));
        }
    }

    private void setImage(final String base64Image) {
        if (contentWithoutImage != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    webEngine.loadContent(contentWithoutImage.replace("{currently_winning_card_src}", base64Image));
                }
            });
        }
    }

    private class LoadImage implements Runnable {
        private final String cardName;

        public LoadImage(String cardName) {
            this.cardName = cardName;
        }

        @Override
        public void run() {
            boolean isImageLoaded = false;
            for (int i = 0; i < MAXIMUM_NUMBER_OF_LOADING_IMAGE_TRIES; i++) {
                try {
                    String image = readImage(new URL(IMAGE_PREFIX + URLEncoder.encode(cardName, "UTF-8").replace("+", "%20").replace("%E2%80%99", "%27").replace("%3A", "").replace(".", "") + IMAGE_EXTENSION));
                    if (image == null) {
                        throw new IIOException("Can't get input stream from URL!");
                    }
                    imageCache.put(cardName, image);
                    isImageLoaded = true;
                    break;
                } catch (Exception ignored) {
                }
            }
            if (!isImageLoaded) {
                imageCache.put(cardName, loadFailedImage != null ? loadFailedImage : "");
            }
            updateCurrentWinningCard();
        }
    }

}
