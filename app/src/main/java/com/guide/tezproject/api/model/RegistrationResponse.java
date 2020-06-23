package com.guide.tezproject.api.model;

public class RegistrationResponse {
    private Boolean sonuc;

    public RegistrationResponse(){}

    public RegistrationResponse(Boolean sonuc) {
        this.sonuc = sonuc;
    }

    public Boolean getSonuc() {
        return sonuc;
    }

    public void setSonuc(Boolean sonuc) {
        this.sonuc = sonuc;
    }
}
