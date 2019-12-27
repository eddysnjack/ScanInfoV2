package com.androeddy.scaninfov2.statics;

import java.util.HashMap;

public class MedicineUrlObject {
    HashMap<String, String> urlData;
    int urlType;

    public HashMap<String, String> getUrlData() {
        return urlData;
    }

    public void setUrlData(HashMap<String, String> urlData) {
        this.urlData = urlData;
    }

    public int getUrlType() {
        return urlType;
    }

    public void setUrlType(int urlType) {
        this.urlType = urlType;
    }
}