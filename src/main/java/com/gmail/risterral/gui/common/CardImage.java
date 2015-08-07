package com.gmail.risterral.gui.common;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class CardImage extends JPanel {
    private static final String IMAGE_LOADING_SRC = "images/image_loading.gif";
    private static final String LOAD_FAILED_IMAGE_SRC = "images/load_failed.png";
    private static final String IMAGE_PREFIX = "https://hextcg.com/wp-content/themes/hex/images/autocard/";
    private static final String IMAGE_EXTENSION = ".png";
    private static final int IMAGE_WIDTH = 350;
    private static final int IMAGE_HEIGHT = 487;
    private static final int MAXIMUM_NUMBER_OF_LOADING_IMAGE_TRIES = 3;

    private final JLabel loadingImageLabel;
    private final BufferedImage loadFailedImage;

    private String currentlyWinningCard;
    private HashMap<String, Thread> loadImageWorkers = new HashMap<>();
    private volatile HashMap<String, Image> imageCache = new HashMap<>();
    private Image image = null;

    public CardImage() {
        this.setLayout(new GridBagLayout());
        loadingImageLabel = new JLabel(new ImageIcon(IMAGE_LOADING_SRC));
        loadingImageLabel.setVisible(false);
        this.add(loadingImageLabel);
        this.loadFailedImage = readImageFile(new File(LOAD_FAILED_IMAGE_SRC));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.drawImage(image, 0, 0, this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    public void setWinningCardImage(String cardName) {
        this.currentlyWinningCard = cardName;
        if (cardName == null || cardName.isEmpty()) {
            setImage(null);
        } else if (imageCache.containsKey(cardName)) {
            setImage(imageCache.get(cardName));
        } else if (!loadImageWorkers.containsKey(cardName)) {
            Thread worker = new Thread(new LoadImage(cardName));
            worker.start();
            loadingImageLabel.setVisible(true);
            loadImageWorkers.put(cardName, worker);
        }
    }

    private BufferedImage readImageFile(File imageFile) {
        try {
            return ImageIO.read(imageFile);
        } catch (Exception e) {
            return null;
        }
    }

    private void updateCurrentWinningCard() {
        if (imageCache.containsKey(currentlyWinningCard) && !imageCache.get(currentlyWinningCard).equals(image)) {
            setImage(imageCache.get(currentlyWinningCard));
        }
    }

    private void setImage(Image image) {
        this.image = image;
        loadingImageLabel.setVisible(false);
        this.updateUI();
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
                    URL url = new URL(IMAGE_PREFIX + URLEncoder.encode(cardName, "UTF-8").replace("+", "%20").replace("%E2%80%99", "%27").replace("%3A", "").replace("%21", "").replace(".", "") + IMAGE_EXTENSION);
                    BufferedImage imageRead = ImageIO.read(url);
                    if (imageRead == null) {
                        throw new IIOException("Can't get input stream from URL!");
                    }
                    imageCache.put(cardName, imageRead);
                    isImageLoaded = true;
                    break;
                } catch (Exception ignored) {
                }
            }
            if (!isImageLoaded && loadFailedImage != null) {
                imageCache.put(cardName, loadFailedImage);
            }
            updateCurrentWinningCard();
        }
    }
}
