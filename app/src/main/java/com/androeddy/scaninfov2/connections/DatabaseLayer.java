package com.androeddy.scaninfov2.connections;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.androeddy.scaninfov2.statics.DBTableSearchHistory;

import java.util.ArrayList;

public class DatabaseLayer extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ilacrehberi.db";

    public DatabaseLayer(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBTableSearchHistory.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<DBTableSearchHistory> getAllHistory() {
        ArrayList<DBTableSearchHistory> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(DBTableSearchHistory.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                DBTableSearchHistory dbTableSearchHistory = new DBTableSearchHistory();
                dbTableSearchHistory.setDate(cursor.getString(cursor.getColumnIndex(DBTableSearchHistory.COLUMN_DATE)));
                dbTableSearchHistory.setMedicineBarcode(cursor.getString(cursor.getColumnIndex(DBTableSearchHistory.COLUMN_MEDICINE_BARCODE)));
                dbTableSearchHistory.setMedicineName(cursor.getString(cursor.getColumnIndex(DBTableSearchHistory.COLUMN_MEDICINE_NAME)));
                dbTableSearchHistory.setId(cursor.getInt(cursor.getColumnIndex(DBTableSearchHistory.COLUMN_ID)));
                result.add(dbTableSearchHistory);

            } while (cursor.moveToNext());
        }
        return result;
    }

    public long saveSearchHistory(DBTableSearchHistory dbTableSearchHistory) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBTableSearchHistory.COLUMN_DATE, dbTableSearchHistory.getDate());
        contentValues.put(DBTableSearchHistory.COLUMN_MEDICINE_BARCODE, dbTableSearchHistory.getMedicineBarcode());
        contentValues.put(DBTableSearchHistory.COLUMN_MEDICINE_NAME, dbTableSearchHistory.getMedicineName());
        return db.insert(DBTableSearchHistory.TABLE_NAME, null, contentValues);
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DBTableSearchHistory.TABLE_NAME, null, null);
    }
}
