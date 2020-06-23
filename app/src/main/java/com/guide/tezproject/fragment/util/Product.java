package com.guide.tezproject.fragment.util;

import android.net.Uri;

import com.guide.tezproject.entity.GezilecekYer;
import com.guide.tezproject.entity.Sonuc;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Product {
    private String productName;
    private String productKeywords;
    private String productContent;
    private String productTur;
    private String productTurizmTur;
    private String productNasilGidilir;
    private String productAdres;
    private String productZiyaretSaatleri;
    private String productCity;
    private String productCountry;
    private String productIlce;
    private Double productLatitude;
    private Double productLongitude;
    private Uri imageURL;

    public Product() {
    }

    public Product(String productName, String productKeywords, String productContent, String productTur, String productTurizmTur, String productNasilGidilir, String productAdres, String productZiyaretSaatleri, String productCity, String productCountry, String productIlce, Double productLatitude, Double productLongitude, Uri imageURL) {
        this.productName = productName;
        this.productKeywords = productKeywords;
        this.productContent = productContent;
        this.productTur = productTur;
        this.productTurizmTur = productTurizmTur;
        this.productNasilGidilir = productNasilGidilir;
        this.productAdres = productAdres;
        this.productZiyaretSaatleri = productZiyaretSaatleri;
        this.productCity = productCity;
        this.productCountry = productCountry;
        this.productIlce = productIlce;
        this.productLatitude = productLatitude;
        this.productLongitude = productLongitude;
        this.imageURL = imageURL;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductKeywords() {
        return productKeywords;
    }

    public void setProductKeywords(String productKeywords) {
        this.productKeywords = productKeywords;
    }

    public String getProductContent() {
        return productContent;
    }

    public void setProductContent(String productContent) {
        this.productContent = productContent;
    }

    public String getProductTur() {
        return productTur;
    }

    public void setProductTur(String productTur) {
        this.productTur = productTur;
    }

    public String getProductTurizmTur() {
        return productTurizmTur;
    }

    public void setProductTurizmTur(String productTurizmTur) {
        this.productTurizmTur = productTurizmTur;
    }

    public String getProductNasilGidilir() {
        return productNasilGidilir;
    }

    public void setProductNasilGidilir(String productNasilGidilir) {
        this.productNasilGidilir = productNasilGidilir;
    }

    public String getProductAdres() {
        return productAdres;
    }

    public void setProductAdres(String productAdres) {
        this.productAdres = productAdres;
    }

    public String getProductZiyaretSaatleri() {
        return productZiyaretSaatleri;
    }

    public void setProductZiyaretSaatleri(String productZiyaretSaatleri) {
        this.productZiyaretSaatleri = productZiyaretSaatleri;
    }

    public String getProductCity() {
        return productCity;
    }

    public void setProductCity(String productCity) {
        this.productCity = productCity;
    }

    public String getProductCountry() {
        return productCountry;
    }

    public void setProductCountry(String productCountry) {
        this.productCountry = productCountry;
    }

    public String getProductIlce() {
        return productIlce;
    }

    public void setProductIlce(String productIlce) {
        this.productIlce = productIlce;
    }

    public Double getProductLatitude() {
        return productLatitude;
    }

    public void setProductLatitude(Double productLatitude) {
        this.productLatitude = productLatitude;
    }

    public Double getProductLongitude() {
        return productLongitude;
    }

    public void setProductLongitude(Double productLongitude) {
        this.productLongitude = productLongitude;
    }

    public Uri getImageID() {
        return imageURL;
    }

    public void setImageID(Uri imageURL) {
        this.imageURL = imageURL;
    }

    public static ArrayList<Product> getData(ArrayList<GezilecekYer> gezilecekYerler) {

        ArrayList<Product> productGezilecek= new ArrayList<>();

        for (int i = 0; i < gezilecekYerler.size(); i++) {
            Product temp = new Product();
            temp.setProductName(gezilecekYerler.get(i).getName());
            temp.setProductKeywords(gezilecekYerler.get(i).getKeywords());
            temp.setProductContent(gezilecekYerler.get(i).getContent());
            temp.setProductTur(gezilecekYerler.get(i).getTur());
            temp.setProductTurizmTur(gezilecekYerler.get(i).getTurizmTur());
            temp.setProductNasilGidilir(gezilecekYerler.get(i).getNasilGidilir());
            temp.setProductAdres(gezilecekYerler.get(i).getAdres());
            temp.setProductZiyaretSaatleri(gezilecekYerler.get(i).getZiyaretSaatleri());
            temp.setProductLatitude(gezilecekYerler.get(i).getLatitude());
            temp.setProductLongitude(gezilecekYerler.get(i).getLongitude());
            temp.setProductCountry(gezilecekYerler.get(i).getCountry());
            temp.setProductCity(gezilecekYerler.get(i).getCity());
            temp.setProductIlce(gezilecekYerler.get(i).getIlce());
            temp.setImageID(Uri.parse(gezilecekYerler.get(i).getIconUrl()));

            productGezilecek.add(temp);
        }
        return productGezilecek;
    }

    public static ArrayList<Product> getData2(ArrayList<Sonuc> ugrakYerler) {
        ArrayList<Product> productUgrakYerler = new ArrayList<Product>();
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < ugrakYerler.size(); i++) {
            Product temp = new Product();

            temp.setProductName(ugrakYerler.get(i).getName()+" / "+ugrakYerler.get(i).getCity());
            temp.setProductTurizmTur("Yolunuzdan "+df.format(ugrakYerler.get(i).getDistance()/1000)+" km içeridedir.");
            temp.setProductCity(ugrakYerler.get(i).getCity());
            temp.setProductLatitude(ugrakYerler.get(i).getLatitude());
            temp.setProductLongitude(ugrakYerler.get(i).getLongitude());
            temp.setImageID(Uri.parse(ugrakYerler.get(i).getIconUrl()));
            temp.setProductKeywords(ugrakYerler.get(i).getKeywords());
            temp.setProductContent(ugrakYerler.get(i).getContent());
            temp.setProductTur(ugrakYerler.get(i).getTur());
            //temp.setProductTurizmTur(gezilecekYerler.get(i).getTurizmTur());
            temp.setProductNasilGidilir(ugrakYerler.get(i).getNasilGidilir());
            temp.setProductAdres(ugrakYerler.get(i).getAdres());
            temp.setProductZiyaretSaatleri(ugrakYerler.get(i).getZiyaretSaatleri());
            temp.setProductCountry(ugrakYerler.get(i).getCountry());
            temp.setProductIlce(ugrakYerler.get(i).getIlce());

            productUgrakYerler.add(temp);
        }
        return productUgrakYerler;
    }
}