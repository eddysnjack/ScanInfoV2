package com.androeddy.scaninfov2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.androeddy.scaninfov2.adapters.AdapterListviewSearchHistory;
import com.androeddy.scaninfov2.connections.DatabaseLayer;
import com.androeddy.scaninfov2.statics.DBTableSearchHistory;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {
    CheckBox settingsActiviy_checkbox_saveHistory;
    LinearLayout settingsActiviy_linearLayout_saveHistory;
    SharedPreferences sp;
    ListView settingsActiviy_listview_history;
    ImageButton settings_imagebutton_deleteAll;
    DatabaseLayer dbLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settingsActiviy_checkbox_saveHistory = findViewById(R.id.settingsActiviy_checkbox_saveHistory);
        settingsActiviy_linearLayout_saveHistory = findViewById(R.id.settingsActiviy_linearLayout_saveHistory);
        settingsActiviy_listview_history = findViewById(R.id.settingsActiviy_listview_history);
        settings_imagebutton_deleteAll = findViewById(R.id.settings_imagebutton_deleteAll);
        dbLayer = new DatabaseLayer(this);

        sp = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        settingsActiviy_checkbox_saveHistory.setChecked(sp.getBoolean("SAVEHISTORY", true));
        settingsActiviy_checkbox_saveHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sp.edit().putBoolean("SAVEHISTORY", b).apply();
            }
        });

        settingsActiviy_linearLayout_saveHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkboxVal = settingsActiviy_checkbox_saveHistory.isChecked();
                sp.edit().putBoolean("SAVEHISTORY", !checkboxVal).apply();
                settingsActiviy_checkbox_saveHistory.setChecked(!checkboxVal);
            }
        });

        settings_imagebutton_deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Settings.this)
                        .setTitle("Tüm Geçmiş silinecek?")
                        .setMessage("Geçmiş siliencek Onaylıyor musunuz?")
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbLayer.deleteAll();
                                settingsActiviy_listview_history.setAdapter(null);
                            }
                        })
                        .setNegativeButton("Hayır", null)
                        .show();
            }
        });


        //fill the list

        ArrayList<DBTableSearchHistory> history = dbLayer.getAllHistory();
        if (history != null) {
            AdapterListviewSearchHistory adapter = new AdapterListviewSearchHistory(this, dbLayer.getAllHistory());
            settingsActiviy_listview_history.setAdapter(adapter);
        }


    }
}
