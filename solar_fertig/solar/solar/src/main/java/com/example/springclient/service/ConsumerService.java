package com.example.springclient.service;

import com.example.springclient.entity.Adresse;
import com.example.springclient.entity.Koordinaten;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class ConsumerService {

    private final ProducerService service;

    public static void getCoordinates(Adresse adresse) {
        String url = getUrl(adresse);
        WebClient.Builder builder = WebClient.builder();
        String geoData = builder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(geoData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String lat = jsonNode.get(0).get("lat").asText();
        String lon = jsonNode.get(0).get("lon").asText();
        adresse.setKoordinaten(new Koordinaten(lon,lat));

    }

    private static String getUrl(Adresse adresse) {
        String strasse = adresse.getStrasse();
        int hausnummer = adresse.getHausnummer();
        String stadt = adresse.getStadt();
        String bundesland = adresse.getBundesland();
        String postleitzahl = adresse.getPlz();
        String land = adresse.getLand();

        return String.format(
                "https://geocode.maps.co/search?street=%s+%d&city=%s&state=%s&postalcode=%s&country=%s",
                strasse, hausnummer, stadt, bundesland, postleitzahl, land
        );
    }

    public static String getForecastSolarUrl(Koordinaten koordinaten) {
        String lat = koordinaten.getLat();
        String lon = koordinaten.getLon();
        return String.format("https://api.forecast.solar/estimate/%s/%s/45/0/15?time=utc", lat, lon);
    }

    public static String extractTimestampFromJsonResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);
            JsonNode wattHoursDayNode = jsonNode.path("result").path("watt_hours_day");
            Iterator<Entry<String, JsonNode>> fields = wattHoursDayNode.fields();
            if (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                return (entry.getValue()).asText();
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return null;
    }

}
