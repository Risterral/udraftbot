package com.gmail.risterral.gui.draft;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public class CardImage extends JPanel {
    private static final String IMAGE_PREFIX = "https://hextcg.com/wp-content/themes/hex/images/autocard/";
    private static final String IMAGE_EXTENSION = ".png";
    private static final int MAXIMUM_NUMBER_OF_LOADING_IMAGE_TRIES = 3;

    private HashMap<String, BufferedImage> imageCache = new HashMap<>();
    private BufferedImage image = null;

    public CardImage() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.drawImage(image, 0, 0, null);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(350, 487);
    }

    public void setImage(String cardName, Integer tryNumber) {
        if (cardName == null || cardName.isEmpty() || tryNumber > MAXIMUM_NUMBER_OF_LOADING_IMAGE_TRIES) {
            image = null;
            this.updateUI();
            return;
        }
        try {
            if (imageCache.containsKey(cardName)) {
                image = imageCache.get(cardName);
            } else {
                URL url = new URL(IMAGE_PREFIX + URLEncoder.encode(cardName, "UTF-8").replace("+", "%20") + IMAGE_EXTENSION);
                BufferedImage imageRead = ImageIO.read(url);
                if (imageRead == null) {
                    throw new IIOException("Can't get input stream from URL!");
                }
                image = imageRead;
                imageCache.put(cardName, imageRead);
            }
            this.updateUI();
        } catch (Exception ignored) {
            image = null;
            setImage(cardName, ++tryNumber);
        }
    }
}
