package com.androeddy.scaninfov2.connections;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncGetWithJsoup extends AsyncTask<String, Void, Document> {
    private AsyncResponse delegate = null;
    private int position;

    public AsyncGetWithJsoup(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    public AsyncGetWithJsoup(AsyncResponse delegate, int position) {
        this.delegate = delegate;
        this.position = position;
    }

    public interface AsyncResponse {
        void processFinish(Document output, int position);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Document document) {
        delegate.processFinish(document, position);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Document doInBackground(String... strings) {
        Document document = null;
        try {
            document = Jsoup.connect(strings[0]).timeout(10000).userAgent("Mozilla").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return document;
    }
}
