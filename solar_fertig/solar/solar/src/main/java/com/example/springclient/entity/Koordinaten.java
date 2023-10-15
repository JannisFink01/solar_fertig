package com.example.springclient.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Koordinaten {
    String lon;
    String lat;

    public Koordinaten(String lon, String lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public String getLat() {
        return lat;
    }
}
