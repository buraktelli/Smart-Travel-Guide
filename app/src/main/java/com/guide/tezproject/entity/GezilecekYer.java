package com.guide.tezproject.entity;

import java.io.Serializable;

public class GezilecekYer implements Serializable {
    private String name;
    private String keywords;
    private String content;
    private String tur;
    private String turizmTur;
    private String nasilGidilir;
    private String adres;
    private String ziyaretSaatleri;
    private Double latitude;
    private Double longitude;
    private String country;
    private String city;
    private String ilce;
    private String iconUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTur() {
        return tur;
    }

    public void setTur(String tur) {
        this.tur = tur;
    }

    public String getTurizmTur() {
        return turizmTur;
    }

    public void setTurizmTur(String turizmTur) {
        this.turizmTur = turizmTur;
    }

    public String getNasilGidilir() {
        return nasilGidilir;
    }

    public void setNasilGidilir(String nasilGidilir) {
        this.nasilGidilir = nasilGidilir;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getZiyaretSaatleri() {
        return ziyaretSaatleri;
    }

    public void setZiyaretSaatleri(String ziyaretSaatleri) {
        this.ziyaretSaatleri = ziyaretSaatleri;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIlce() {
        return ilce;
    }

    public void setIlce(String ilce) {
        this.ilce = ilce;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}