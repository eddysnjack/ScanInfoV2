package com.androeddy.scaninfov2.connections;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AsyncGetWithJsoupMulti extends AsyncTask<HashMap<String, String>, Void, HashMap<String, Document>> {
    private AsyncResponse delegate = null;
    private int position;

    public AsyncGetWithJsoupMulti(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    public AsyncGetWithJsoupMulti(AsyncResponse delegate, int position) {
        this.delegate = delegate;
        this.position = position;
    }

    public interface AsyncResponse {
        void processFinish(HashMap<String, Document> output, int position);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(HashMap<String, Document> document) {
        delegate.processFinish(document, position);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @SafeVarargs
    @Override
    protected final HashMap<String, Document> doInBackground(HashMap<String, String>... strings) {
        if (strings != null) {
            Iterator iterator = strings[0].entrySet().iterator();
            HashMap<String, Document> resultObject = new HashMap<>();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String url = (String) entry.getValue();
                try {
                    Document document = Jsoup.connect(url).timeout(10000).userAgent("Mozilla").get();
                    resultObject.put(key, document);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return resultObject;
        } else {
            System.out.println("ASYNC PARAMETRELERI BOŞ!!");
            return null;
        }
    }
}
