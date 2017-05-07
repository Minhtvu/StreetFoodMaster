package com.gnirt69.StreetFoodMaster;

import android.location.Location;
import android.net.Uri;
import android.util.Log;

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
    private static final String TAG = "FlickrFetchr";

    private static final String FETCH_RECENTS_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";
    private static final String ENDPOINT = "http://45.55.220.154:3000";

    public byte[] getUrlBytes(String urlSpec, String method, Uri.Builder body) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod(method);
        Log.i(TAG, urlSpec);
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
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
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

    public String getUrlString(String urlSpec, String method, Uri.Builder body) throws IOException {
        return new String(getUrlBytes(urlSpec, method, body));
    }

    private JSONObject getJson(String url, String method, Uri.Builder body) {

        try {
            String jsonString = getUrlString(url, method, body);
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

    public String getLogin(String uname, String pass) {
        String url = buildLogin(uname, pass);
        String results = getJson(url, "GET", null).toString();
        Log.i(TAG, results);
        return results;
    }

    public String postRegister(String username,
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
        String results = getJson(buildRegister(), "POST", body).toString();
        Log.i(TAG, results);
        return results;
    }

    public String getStandsByLLR(String lat, String lng, String radius){
        String results = getJson(ENDPOINT
                +"/stands"
                +"/"+lat
                +"/"+lng
                +"/"+radius, "GET", null).toString();
        Log.i(TAG, results);
        return results;
    }
//    private void parseItems(List<GalleryItem> items, JSONObject jsonBody)
//            throws IOException, JSONException {
//
//        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
//        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");
//
//        for (int i = 0; i < photoJsonArray.length(); i++) {
//            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
//
//            GalleryItem item = new GalleryItem();
//            item.setId(photoJsonObject.getString("id"));
//            item.setCaption(photoJsonObject.getString("title"));
//
//            if (!photoJsonObject.has("url_s")) {
//                continue;
//            }
//
//            item.setUrl(photoJsonObject.getString("url_s"));
//            item.setOwner(photoJsonObject.getString("owner"));
//            item.setLat(photoJsonObject.getDouble("latitude"));
//            item.setLon(photoJsonObject.getDouble("longitude"));
//            items.add(item);
//        }
//    }

}