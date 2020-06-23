package com.guide.tezproject.api.model;

public class ImagesResponse {
    private Long id;
    private String url;
    private String name;
    private String city;

    public ImagesResponse(Long id, String url, String name, String city) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
