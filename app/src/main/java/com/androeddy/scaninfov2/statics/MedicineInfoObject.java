package com.androeddy.scaninfov2.statics;

import java.util.HashMap;

public class MedicineInfoObject {
    private String name;
    private String firm;
    private String activeIngredient;
    private String barcode;
    private String pieces;
    private String prescription;
    private HashMap<String, String> medicineInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirm() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPieces() {
        return pieces;
    }

    public void setPieces(String pieces) {
        this.pieces = pieces;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public HashMap<String, String> getMedicineInfo() {
        return medicineInfo;
    }

    public void setMedicineInfo(HashMap<String, String> medicineInfo) {
        this.medicineInfo = medicineInfo;
    }
}
