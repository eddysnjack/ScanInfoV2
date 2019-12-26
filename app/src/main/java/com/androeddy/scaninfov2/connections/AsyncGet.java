package com.androeddy.scaninfov2.connections;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncGet extends AsyncTask<String, Void, String> {
    private AsyncResponse delegate = null;
    private int position;

    public AsyncGet(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    public AsyncGet(AsyncResponse delegate, int position) {
        this.delegate = delegate;
        this.position = position;
    }

    public interface AsyncResponse {
        void processFinish(String output, int position);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        delegate.processFinish(s, position);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = null;
        String urlAddress = strings[0];
        HttpURLConnection httpURLConnection = null;
        StringBuilder stringBuilder = null;
        String line = null;
        BufferedReader bfr;

        try {
            URL url = new URL(urlAddress);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            bfr = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            stringBuilder = new StringBuilder();
            line = null;
            while ((line = bfr.readLine()) != null) {
                stringBuilder.append(line).append("\r\n");
            }
            result = stringBuilder.toString();
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return result;
    }
}
