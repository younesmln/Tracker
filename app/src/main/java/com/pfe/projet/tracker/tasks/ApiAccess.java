package com.pfe.projet.tracker.tasks;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiAccess extends AsyncTask<Void, Void, String>{
    private final String TAG = "TEST";

    private String http_method;
    private String body;
    private ApiAccessWork caller;
    private Uri uri;

    @Override
    protected void onPostExecute(String result) {
        caller.doStuffWithResult(result);
    }

    @Override
    protected String doInBackground(Void...params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String result = null;
        Boolean flag = this.http_method.equalsIgnoreCase("post");
        try {
            Log.v(TAG, "\t\t***************");
            connection = (HttpURLConnection) new URL(this.uri.toString()).openConnection();
            connection.setRequestMethod(this.http_method);
            if (flag){
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                OutputStream oStream = connection.getOutputStream();
                oStream.write(this.body.getBytes());
                oStream.flush();
                oStream.close();
            }
            connection.connect();
            Log.v(TAG, "*+*+*"+ connection.getResponseCode()+"+*+*+*+*+*+*+*+*+*+*+*+*+*+");
            StringBuffer buffer = new StringBuffer();
            InputStream iStream = connection.getInputStream();
            if (iStream == null) return null;
            reader = new BufferedReader(new InputStreamReader(iStream));
            String line;
            while ((line = reader.readLine()) != null) buffer.append(line + "\n");
            if (buffer.length() == 0) return null;
            result = buffer.toString();
        }catch(Exception e){
            Log.v(TAG, " "+e.toString()+"\t\t++++++++++++++++++");
            try {
                if (reader != null) reader.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return result;
    }

    public ApiAccess setCaller(ApiAccessWork caller){
        this.caller = caller;
        return this;
    }

    public ApiAccess setMethod(String method){
        this.http_method = method;
        return this;
    }

    public ApiAccess setBody(String body){
        this.body = body;
        return this;
    }

    public ApiAccess setUri(Uri uri){
        this.uri = uri;
        return this;
    }

    /*
    * une interface pour les classes qui on besoin de récuperer le resultat du ApiAccess class
    */
    public interface ApiAccessWork {
        void doStuffWithResult(String result);
    }
}
