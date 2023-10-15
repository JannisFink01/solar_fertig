package com.example.springclient.controller;

import com.example.springclient.entity.Adresse;
import com.example.springclient.service.ConsumerService;
import com.example.springclient.service.ProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;

import static com.example.springclient.entity.Adresse.toAdresse;
import static com.example.springclient.service.ConsumerService.extractTimestampFromJsonResponse;
import static com.example.springclient.service.ConsumerService.getForecastSolarUrl;

@Slf4j
@Controller
@RequiredArgsConstructor
public class KafkaController {

    private final ProducerService producerService;

    @KafkaListener(topics = "solarapi", groupId = "group")
    public void nachrichtEmpfangen(String nachricht) {
        log.debug("Eingabe: {}", nachricht);

        Adresse adresse = toAdresse(nachricht);

        WebClient.Builder builder = WebClient.builder();
        ConsumerService.getCoordinates(adresse);
        log.debug("Adresse: {}", adresse);

        String forecastSolarUrl = getForecastSolarUrl(adresse.getKoordinaten());
        String forecastData = builder.build()
                .get()
                .uri(forecastSolarUrl, new Object[0])
                .retrieve()
                .bodyToMono(String.class)
                .block();
        adresse.setWattNextDay(extractTimestampFromJsonResponse(forecastData));

        //adresse.setWattNextDay("Test");

        producerService.sendeVollstaendigeAdresse(adresse);
    }
}
