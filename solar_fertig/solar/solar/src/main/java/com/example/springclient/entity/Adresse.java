package com.example.springclient.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Adresse {
    private String strasse;
    private int hausnummer;
    private String plz;
    private String stadt;
    private String land;
    private int leistung;
    private String bundesland;
    private Koordinaten koordinaten;
    private String wattNextDay;
    @Setter
    private UUID id;

    public String toJson(){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        }
        catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }


    public static Adresse toAdresse(String JSonAdresse){
        Adresse adresse = new Adresse();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(objectMapper.readTree(JSonAdresse));
            adresse = objectMapper.readValue(jsonString, Adresse.class);
        } catch (Exception e){
            log.debug(e.getMessage());
        }
        return adresse;
    }

}
