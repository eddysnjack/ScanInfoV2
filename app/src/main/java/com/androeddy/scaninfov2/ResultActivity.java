package com.androeddy.scaninfov2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import java.lang.reflect.Field;

public class ResultActivity extends AppCompatActivity {
    String[] extractedData=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle incomingdata = getIntent().getExtras();
        if(incomingdata == null){
            return;
        }
        extractedData = incomingdata.getStringArray("MedicineDetails"); //Gelen bilgi : {MedName, BaseUrl, MedicineResult} şeklinde

//        TextView textview_warning_messages =findViewById(R.id.txtBody);
//        textview_warning_messages.setMovementMethod(new ScrollingMovementMethod());
//        textview_warning_messages.setMovementMethod(LinkMovementMethod.getInstance());
//        textview_warning_messages.setText(Html.fromHtml(extractedData[3]));

        TextView title = findViewById(R.id.txtBaslik);
        title.setText(extractedData[0]);

        WebView webobject = findViewById(R.id.webviewid);
//        webobject.getSettings().setDefaultTextEncodingName("utf-8");
        webobject.getSettings().setJavaScriptEnabled(true);

        webobject.setWebViewClient(new WebViewClient(){//başka taraycıya yönlendirmesin diye. kendi ekranımızda kalıyoruz.
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        webobject.loadDataWithBaseURL(extractedData[1],extractedData[2],"text/html","utf-8",null);
        //webobject.loadUrl(extractedData[1]);

        //-----------------------------------------3dot menüsü her cihazda görünsün diye dolaylı bir hack kaynak :https://stackoverflow.com/questions/19741319/android-action-bar-three-dots-not-displayed
        getOverflowMenu();

    }
    //Android 3dot Menü ekleme metodları
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuINF = getMenuInflater();
        menuINF.inflate(R.menu.result_activity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Intent tarayici = new Intent(Intent.ACTION_VIEW, Uri.parse(extractedData[1]));
                startActivity(tarayici);
        }
        return super.onOptionsItemSelected(item);
    }


    //Dolaylı 3dot menü metodu (fiziksel menü butonu olan chazlar için arayüzü manuel konfigüre ediyoruz)
    private void getOverflowMenu(){

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
