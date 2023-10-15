package com.example.springclient.service;

import com.example.springclient.entity.Adresse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Service Klasse.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class ProducerService {

    private final KafkaConnectionCheck checker;
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Methode, die Nachricht an Message Broker sendet.
     */
    public void sendeVollstaendigeAdresse(Adresse adresse){
        if(!checker.isKafkaServerReachable())
            log.error("Server not reachable!");
        log.debug("sendeVollständigeAdresse: {}", adresse);
        kafkaTemplate.send("solarResult", adresse.toJson());

    }

}
