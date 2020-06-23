package com.guide.tezproject.entity;

import com.mapbox.geojson.Point;

import androidx.annotation.Nullable;

public class Sonuc implements Comparable<Sonuc>{
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
    private Point gezilecekYerKord;
    private Point stepKord;
    private double kusUcusu;
    private double distance;

    @Override
    public String toString() {
        return "Sonuc{" +
                "name='" + name + '\'' +
                ", keywords='" + keywords + '\'' +
                ", content='" + content + '\'' +
                ", tur='" + tur + '\'' +
                ", turizmTur='" + turizmTur + '\'' +
                ", nasilGidilir='" + nasilGidilir + '\'' +
                ", adres='" + adres + '\'' +
                ", ziyaretSaatleri='" + ziyaretSaatleri + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", ilce='" + ilce + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", gezilecekYerKord=" + gezilecekYerKord +
                ", stepKord=" + stepKord +
                ", kusUcusu=" + kusUcusu +
                ", distance=" + distance +
                '}';
    }

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

    public Point getGezilecekYerKord() {
        return gezilecekYerKord;
    }

    public void setGezilecekYerKord(Point gezilecekYerKord) {
        this.gezilecekYerKord = gezilecekYerKord;
    }

    public Point getStepKord() {
        return stepKord;
    }

    public void setStepKord(Point stepKord) {
        this.stepKord = stepKord;
    }

    public double getKusUcusu() {
        return kusUcusu;
    }

    public void setKusUcusu(double kusUcusu) {
        this.kusUcusu = kusUcusu;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Sonuc sonuc) {
        return this.distance > sonuc.getDistance()? 1 : this.distance < sonuc.getDistance()? -1 : 0;
    }

    @Override
    public int hashCode() {
        return this.gezilecekYerKord.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {

        if(obj instanceof Sonuc){
            if(this.gezilecekYerKord==((Sonuc) obj).gezilecekYerKord)
                return true;
        }
        return false;
    }
}
