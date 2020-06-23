package com.guide.tezproject.api.model;

public class MostFrequentAranan {
    private Long id;

    private int colCount;

    private String name;

    private String city;

    private String ilce;

    private String icon_Url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getColCount() {
        return colCount;
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIcon_Url() {
        return icon_Url;
    }

    public void setIcon_Url(String icon_Url) {
        this.icon_Url = icon_Url;
    }

    @Override
    public String toString() {
        //return  "\n" + name + " - Aranma sayisi = " + colCount + " - " + ilce + " - " + city + "\n" ;
        return "\n" + name + "(" + colCount + " kez arandi)" + " - " + ilce + " / " + city + "\n" ;
    }
}
