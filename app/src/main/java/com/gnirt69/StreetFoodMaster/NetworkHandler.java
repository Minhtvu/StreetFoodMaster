package com.gnirt69.StreetFoodMaster;

import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyle on 4/12/17.
 * I left this in for the simple reason of not wanting to have deletions be an issue after
 * all of the problems I have had
 */

public class NetworkHandler {
    private static final String TAG = "NetworkHandler";

    private static final String ENDPOINT = "http://45.55.220.154:3000";

    public byte[] getUrlBytes(String urlSpec, String method, Uri.Builder body, String authToken) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod(method);
        Log.i(TAG, urlSpec);
        if (authToken != null){
            connection.setRequestProperty("x-access-token", authToken);
        }
        try {
            switch (method) {
                case "GET":
                    break;
                case "POST":
                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(body.build().getEncodedQuery());
                    writer.flush();
                    writer.close();
                    os.close();
            }
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec, String method, Uri.Builder body, String authToken) throws IOException {
        return new String(getUrlBytes(urlSpec, method, body, authToken));
    }

    private JSONObject getJson(String url, String method, Uri.Builder body, String authToken) {

        try {
            String jsonString = getUrlString(url, method, body, authToken);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            //parseItems(items, jsonBody);
            return jsonBody;
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return null;
    }

    private String buildLogin(String username, String password) {
        return ENDPOINT
                +"/login"
                +"/"+username
                +"/"+password;
    }

    private String buildRegister(){
        return ENDPOINT
                +"/register";
    }

    public JSONObject getLogin(String uname, String pass) {
        String url = buildLogin(uname, pass);
        JSONObject results = getJson(url, "GET", null, null);
        return results;
    }

    public JSONObject postRegister(String username,
                               String password,
                               String first,
                               String last,
                               String email,
                               String phone){
        Uri.Builder body = new Uri.Builder()
                .appendQueryParameter("username", username)
                .appendQueryParameter("password", password)
                .appendQueryParameter("email", email)
                .appendQueryParameter("first", first)
                .appendQueryParameter("last", last)
                .appendQueryParameter("phone", phone);
        JSONObject results = getJson(buildRegister(), "POST", body, null);
//        Log.i(TAG, results.toString());
        return results;
    }

    public JSONObject postStand(String name,
                                String foodType,
                                double lat,
                                double lng,
                                String address,
                                String city,
                                String state,
                                int zipcode,
                                String authToken){
        Uri.Builder body = new Uri.Builder()
                .appendQueryParameter("name", name)
                .appendQueryParameter("foodtype", foodType)
                .appendQueryParameter("lat", Double.toString(lat))
                .appendQueryParameter("long", Double.toString(lng))
                .appendQueryParameter("address", address)
                .appendQueryParameter("city", city)
                .appendQueryParameter("state", state)
                .appendQueryParameter("zip", Integer.toString(zipcode));
        JSONObject results = getJson(ENDPOINT+"/stands", "POST", body, authToken);
//        Log.i(TAG, results.toString());
        return results;
    }

    public JSONObject putStand(int standID,
                               String name,
                                String foodType,
                                double lat,
                                double lng,
                                String address,
                                String city,
                                String state,
                                int zipcode,
                                String authToken){
        Uri.Builder body = new Uri.Builder()
                .appendQueryParameter("name", name)
                .appendQueryParameter("foodtype", foodType)
                .appendQueryParameter("lat", Double.toString(lat))
                .appendQueryParameter("long", Double.toString(lng))
                .appendQueryParameter("address", address)
                .appendQueryParameter("city", city)
                .appendQueryParameter("state", state)
                .appendQueryParameter("zip", Integer.toString(zipcode));
        JSONObject results = getJson(ENDPOINT+"/stands/"+Integer.toString(standID),
                "PUT", body, authToken);
//        Log.i(TAG, results.toString());
        return results;
    }

    public JSONObject deleteStand(int standID, String authToken){
        JSONObject results = getJson(ENDPOINT+"/stands/"+Integer.toString(standID), "DELETE",
                null, authToken);
        return results;
    }

    public JSONObject getStandsByLLR(String lat, String lng, String radius){
        JSONObject results = getJson(ENDPOINT
                +"/stands"
                +"/"+lat
                +"/"+lng
                +"/"+radius, "GET", null, null);
//        Log.i(TAG, results);
        return results;
    }
    public JSONObject getStandsByOwner(int ownerID, String authToken){
        Log.i(TAG, Integer.toString(ownerID)+ " "+authToken);
        JSONObject results = getJson(ENDPOINT
                +"/stands"
                +"/owner/"+ownerID, "GET", null, authToken);
        if(results == null){
            return null;
        }
//        Log.i(TAG, results.toString());
        return results;
    }

}
