package com.androeddy.scaninfov2.statics;

public class DBTableSearchHistory {
    private int id;
    private String medicineName;
    private String medicineBarcode;
    private String date;

    public static final String CREATE_TABLE = "CREATE TABLE SearchHistory (\n" +
            "    id              INTEGER PRIMARY KEY\n" +
            "                            NOT NULL,\n" +
            "    MedicineName    VARCHAR,\n" +
            "    MedicineBarcode VARCHAR,\n" +
            "    Date            DATE\n" +
            ");\n";
    public static final String TABLE_NAME = "SearchHistory";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MEDICINE_NAME = "MedicineName";
    public static final String COLUMN_MEDICINE_BARCODE = "MedicineBarcode";
    public static final String COLUMN_DATE = "Date";

    public DBTableSearchHistory() {
    }

    public DBTableSearchHistory(String medicineName, String medicineBarcode, String date) {
        this.medicineName = medicineName;
        this.medicineBarcode = medicineBarcode;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getMedicineBarcode() {
        return medicineBarcode;
    }

    public void setMedicineBarcode(String medicineBarcode) {
        this.medicineBarcode = medicineBarcode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
