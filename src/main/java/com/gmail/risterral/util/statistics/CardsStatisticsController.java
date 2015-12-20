package com.gmail.risterral.util.statistics;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gmail.risterral.bot.BotController;
import com.gmail.risterral.bot.BotMessageType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class CardsStatisticsController {
    private static final CardsStatisticsController instance = new CardsStatisticsController();

    private volatile HashMap<String, Long> cardsGoldPriceMap = new HashMap<>();

    private CardsStatisticsController() {
    }

    public static CardsStatisticsController getInstance() {
        return instance;
    }

    public void checkCosts(ArrayList<String> cards) {
        HashMap<Integer, Integer> numberOfCardsPerCost = new HashMap<>();
        for (String card : cards) {
            HexCardDTO cardDTO = HexEntitiesController.getInstance().getCardData(card);
            if (cardDTO == null || cardDTO.getCost() == null) continue;
            Integer numberOfCards = numberOfCardsPerCost.containsKey(cardDTO.getCost()) ? numberOfCardsPerCost.get(cardDTO.getCost()) + 1 : 1;
            numberOfCardsPerCost.put(cardDTO.getCost(), numberOfCards);
        }

        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(numberOfCardsPerCost.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> a, Map.Entry<Integer, Integer> b) {
                return a.getKey().compareTo(b.getKey());
            }
        });
        int i = 0;
        for (Map.Entry<Integer, Integer> entry : entries) {
            BotController.getInstance().sendMessage("Number of " + entry.getKey() + " cost cards: " + entry.getValue(), BotMessageType.CARDS_STATISTICS, i++);
        }
    }

    public void checkRatios(ArrayList<String> cards) {
        HashMap<String, Integer> numberOfCardsPerType = new HashMap<>();
        for (String card : cards) {
            HexCardDTO cardDTO = HexEntitiesController.getInstance().getCardData(card);
            if (cardDTO == null || cardDTO.getType() == null) continue;
            Integer numberOfCards = numberOfCardsPerType.containsKey(cardDTO.getType()) ? numberOfCardsPerType.get(cardDTO.getType()) + 1 : 1;
            numberOfCardsPerType.put(cardDTO.getType(), numberOfCards);
        }

        List<Map.Entry<String, Integer>> entries = new ArrayList<>(numberOfCardsPerType.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return b.getValue().compareTo(a.getValue());
            }
        });
        int i = 0;
        for (Map.Entry<String, Integer> entry : entries) {
            BotController.getInstance().sendMessage(entry.getKey() + ": " + entry.getValue(), BotMessageType.CARDS_STATISTICS, i++);
        }
    }

    public void checkThresholds(ArrayList<String> cards) {
        HashMap<String, ArrayList<Integer>> thresholdsPerColor = new HashMap<>();
        Integer sumOfThresholds = 0;
        for (String card : cards) {
            HexCardDTO cardDTO = HexEntitiesController.getInstance().getCardData(card);
            if (cardDTO == null || cardDTO.getThresholds() == null) continue;
            for (String thresholdColor : cardDTO.getThresholds().keySet()) {
                Integer numberOfThresholds = cardDTO.getThresholds().get(thresholdColor);
                sumOfThresholds += numberOfThresholds;
                if (thresholdsPerColor.containsKey(thresholdColor)) {
                    thresholdsPerColor.get(thresholdColor).add(numberOfThresholds);
                } else {
                    ArrayList<Integer> list = new ArrayList<>();
                    list.add(numberOfThresholds);
                    thresholdsPerColor.put(thresholdColor, list);
                }
            }
        }

        HashMap<Long, String> messages = new HashMap<>();
        for (String color : thresholdsPerColor.keySet()) {
            Integer sumOfThresholdsPerColor = 0;
            Integer maxThresholdPerColor = 1;
            for (Integer thresholdNumber : thresholdsPerColor.get(color)) {
                sumOfThresholdsPerColor += thresholdNumber;
                if (maxThresholdPerColor < thresholdNumber) {
                    maxThresholdPerColor = thresholdNumber;
                }
            }
            Long percent = Math.round((double) sumOfThresholdsPerColor * 100 / sumOfThresholds);
            String average = String.valueOf((double) Math.round((double) sumOfThresholdsPerColor * 10 / thresholdsPerColor.get(color).size()) / 10);

            String message = color + " - " + percent + "% - " + average + " / " + maxThresholdPerColor + " (average / max)";
            messages.put(percent, message);
        }

        List<Map.Entry<Long, String>> entries = new ArrayList<>(messages.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<Long, String>>() {
            public int compare(Map.Entry<Long, String> a, Map.Entry<Long, String> b) {
                return b.getKey().compareTo(a.getKey());
            }
        });
        int i = 0;
        for (Map.Entry<Long, String> entry : entries) {
            BotController.getInstance().sendMessage(entry.getValue(), BotMessageType.CARDS_STATISTICS, i++);
        }
    }

    public void checkPrice(String cardName) {
        try {
            Thread worker = new Thread(new CheckPriceThread(cardName, true));
            worker.start();
        } catch (Exception ignored) {
            BotController.getInstance().sendMessage("Cannot obtain value of " + cardName, BotMessageType.CARDS_STATISTICS, null);
        }
    }

    public String getMostValuable(List<String> cardNames) {
        try {
            List<Thread> workers = new ArrayList<>();

            for (String cardName : cardNames) {
                if (!cardsGoldPriceMap.containsKey(cardName)) {
                    workers.add(new Thread(new CheckPriceThread(cardName, false)));
                }
            }

            for (Thread worker : workers) {
                worker.start();
                worker.join();
            }

            synchronized (cardsGoldPriceMap) {
                Long highestValue = 0L;
                String result = null;
                for (String cardName : cardNames) {
                    if (cardsGoldPriceMap.containsKey(cardName) && cardsGoldPriceMap.get(cardName) > highestValue) {
                        result = cardName;
                        highestValue = cardsGoldPriceMap.get(cardName);
                    }
                }
                return result;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private class CheckPriceThread implements Runnable {
        private final String cardName;
        private final Boolean outputMessage;

        public CheckPriceThread(String cardName, Boolean outputMessage) {
            this.cardName = cardName;
            this.outputMessage = outputMessage;
        }

        @Override
        public void run() {
            try {
                JsonFactory factory = new JsonFactory();
                ObjectMapper mapper = new ObjectMapper(factory);

                String overviewUrl = "http://hex.tcgbrowser.com/tools/tooltips/cardbyname.php?name=" + URLEncoder.encode(cardName, "UTF-8");
                HttpURLConnection overviewConnection = (HttpURLConnection) new URL(overviewUrl).openConnection();
                overviewConnection.setRequestMethod("GET");
                overviewConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                BufferedReader overviewInputStream = new BufferedReader(new InputStreamReader(overviewConnection.getInputStream()));
                String jsonOverviewResponse = overviewInputStream.readLine();
                overviewInputStream.close();

                if (jsonOverviewResponse != null) {
                    ObjectNode cardOverview = (ObjectNode) mapper.readTree(jsonOverviewResponse.replaceAll("\\(|\\)", ""));
                    Integer cardId = cardOverview.get("id").asInt();

                    String priceUrl = "http://hex.tcgbrowser.com/server/card.php?action=getCardDetails&cardid=" + cardId;
                    HttpURLConnection priceConnection = (HttpURLConnection) new URL(priceUrl).openConnection();
                    priceConnection.setRequestMethod("GET");
                    priceConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                    BufferedReader priceInputStream = new BufferedReader(new InputStreamReader(priceConnection.getInputStream()));
                    String jsonPriceResponse = priceInputStream.readLine();
                    priceInputStream.close();

                    if (jsonPriceResponse != null) {
                        ObjectNode cardPrice = (ObjectNode) (mapper.readTree(jsonPriceResponse.replaceAll("\\(|\\)", ""))).get("price");
                        String platinumValue = cardPrice.get("platinum").asText();
                        String goldValue = cardPrice.get("gold").asText();

                        cardsGoldPriceMap.put(cardName, Long.valueOf(goldValue));

                        if (outputMessage) {
                            BotController.getInstance().sendMessage(cardName + " value: " + platinumValue + " platinum or " + goldValue + " gold", BotMessageType.CARDS_STATISTICS, null);
                        }
                    }
                }
            } catch (Exception e) {
                if (outputMessage) {
                    BotController.getInstance().sendMessage("Cannot obtain value of " + cardName, BotMessageType.CARDS_STATISTICS, null);
                }
            }
        }
    }
}
