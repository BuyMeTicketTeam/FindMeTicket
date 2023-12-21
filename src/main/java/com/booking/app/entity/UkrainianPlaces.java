package com.booking.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigInteger;

@Entity
@Table(name = "ukrainian_places")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UkrainianPlaces {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    @Column(name = "name_eng")
    private String nameEng;
    private String country;
    private Double lon;
    private Double lat;
    @Column(name = "name_ua")
    private String nameUa;
    @Column(name = "name_ru")
    private String nameRu;

}
