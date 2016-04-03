package com.example.supernova.pfe.tasks;

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

public class ApiAccess2 extends AsyncTask<Void, Void, Response>{
    private final static String TAG = "TEST";

    private String http_method;
    private String body;
    private ApiAccessWork caller;
    private Uri uri;

    @Override
    protected void onPostExecute(Response response) {
        if (caller != null) {
            caller.doStuffWithResult(response);
        }
    }

    @Override
    protected Response doInBackground(Void...params) {
        Response response = new Response();
        HttpURLConnection connection;
        BufferedReader reader = null;
        Boolean flag = this.http_method.equalsIgnoreCase("post");
        try {
            Log.v(TAG, "\t\t***************");
            connection = (HttpURLConnection) new URL(this.uri.toString()).openConnection();
            connection.setRequestMethod(this.http_method.toUpperCase());
            connection.setRequestProperty("Content-Type", "application/json");
            if (flag){
                connection.setDoOutput(true);
                connection.setDoInput(true);
                OutputStream oStream = connection.getOutputStream();
                oStream.write(this.body.getBytes());
                oStream.flush();
                oStream.close();
            }
            connection.connect();
            int code = connection.getResponseCode();
            response.setCode(code);
            if (Response.isSuccess(code)){
                StringBuilder buffer = new StringBuilder();
                if (connection.getInputStream() == null) return null;
                InputStream iStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(iStream));
                String line;
                while ((line = reader.readLine()) != null) buffer.append(line).append("\n");
                if (buffer.length() == 0) return null;
                response.setBody(buffer.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.v(TAG, " "+e.toString()+"\t\t++++++++++++++++++");
            try {
                if (reader != null) reader.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return response;
    }

    public ApiAccess2 setCaller(ApiAccessWork caller){
        this.caller = caller;
        return this;
    }

    public ApiAccess2 setMethod(String method){
        this.http_method = method;
        return this;
    }

    public ApiAccess2 setBody(String body){
        this.body = body;
        return this;
    }

    public ApiAccess2 setUri(Uri uri){
        this.uri = uri;
        return this;
    }

    /*
    * une interface pour les classes qui on besoin de récuperer le resultat du ApiAccess class
    */
    public interface ApiAccessWork {
        void doStuffWithResult(Response response);
    }
}
