package com.gmail.risterral.util.statistics;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gmail.risterral.util.log.LogController;
import com.gmail.risterral.util.log.LogMessageType;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class HexEntitiesController {
    private static final HexEntitiesController instance = new HexEntitiesController();
    private static final String INPUT_GZIP_FILE = "data/gamedata";

    private Map<String, HexCardDTO> cardsData = new HashMap<>();

    private HexEntitiesController() {
    }

    public static HexEntitiesController getInstance() {
        return instance;
    }

    public HexCardDTO getCardData(String cardName) {
        return cardsData.containsKey(cardName) ? cardsData.get(cardName) : null;
    }

    public void prepareCardsData() {
        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(INPUT_GZIP_FILE));
            String file = new String(IOUtils.toByteArray(gzipInputStream), "utf-8");
            gzipInputStream.close();

            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);

            int j = 0;
            for (String bundle : file.split("\\$\\$\\$---\\$\\$\\$")) {
                if (bundle.isEmpty()) continue;

                String[] jsons = bundle.split("\\$\\$--\\$\\$");
                String dataType = jsons[0].replaceAll("\\s", "");

                if (!dataType.equalsIgnoreCase("CardTemplate")) continue;

                for (int i = 1; i < jsons.length; i++) {
                    try {
                        ObjectNode cardNode = (ObjectNode) mapper.readTree(jsons[i].replaceAll("\\n", "").replaceAll("\"m_Guid\" : '\\S+'", "\"m_Guid\" : 0").replaceAll(",\\s*}", "}"));
                        HexCardDTO dto = new HexCardDTO();
                        String cardName = cardNode.get("m_Name").asText();
                        dto.setName(cardName);
                        dto.setCost(cardNode.get("m_ResourceCost").asInt());
                        dto.setType(cardNode.get("m_CardType").asText());

                        Map<String, Integer> thresholds = new HashMap<>();
                        for (JsonNode threshold : (cardNode.get("m_Threshold"))) {
                            String color = threshold.get("m_ColorFlags").asText();
                            Integer requirement = threshold.get("m_ThresholdColorRequirement").asInt();
                            thresholds.put(color, requirement);
                        }
                        dto.setThresholds(thresholds);

                        cardsData.put(cardName, dto);
                    } catch (Exception e) {
                        j++;
                    }
                }
            }

            LogController.log(this.getClass(), null, LogMessageType.SUCCESS, "Loading card data successful.");
        } catch (Exception e) {
            LogController.log(this.getClass(), e, LogMessageType.ERROR, "Unexpected error while loading card data.");
        }
    }
}
