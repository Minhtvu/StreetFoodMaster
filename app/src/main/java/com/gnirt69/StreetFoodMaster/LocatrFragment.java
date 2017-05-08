package com.gnirt69.StreetFoodMaster;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Kyle DiSandro
 * Creates and handles all map and marker implementations
 */


public class LocatrFragment extends SupportMapFragment {

    private static final String TAG = "LocatrFragment";
    private static final String LAT = "Latitude";
    private static final String LONG = "Longitude";
    private static final String FOOD = "Food";
    private GoogleApiClient mClient;
    private GoogleMap mMap;
    private double latitude;
    private double longitude;
    private String food;
    private ArrayList<Stand> stands = new ArrayList<Stand>();

    public static LocatrFragment newInstance(double lat, double log, String foodx) {
        Bundle args = new Bundle();
        args.putDouble( LAT, lat);
        args.putDouble(LONG, log);
        args.putString( FOOD, foodx);
        LocatrFragment frag = new LocatrFragment();
        frag.setArguments( args );
        return frag;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().invalidateOptionsMenu();
        latitude = getArguments().getDouble( LAT );
        longitude = getArguments().getDouble( LONG );
        food = getArguments().getString(FOOD);
        mClient = new GoogleApiClient.Builder(getActivity()).addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                LatLng coordinate = new LatLng(latitude, longitude);
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
                mMap.animateCamera(yourLocation);

            }

        });
        searchInDatabase();
        updateUI();
    }
    private void searchInDatabase(){
        //TODO: Implement a query to search in the database to get the data back to stands
        //Search by food name
        if (food !=  null )
        {
            //query by Foodtype && current location (lat,long)
            Log.i(TAG, food + latitude + longitude);

        }
        else //Search by address or current location
        {
            Log.i(TAG, food + latitude + longitude);
            // query by lat long
        }

    }
    private void updateUI() {
        if (mMap == null){
            Log.i(TAG, "Error");
            return;
        }
        for(Stand m : stands) {
            MarkerOptions current = new MarkerOptions()
                    .position(new LatLng(m.getLat(), m.getLng()))
                    .title(m.getName() + " - " + m.getAddress())
                    .snippet(m.getId().toString());
            mMap.addMarker(current);
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0 ) {
                Stand chosen = getStand(arg0);
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("NAME", chosen.getName());
                i.putExtra("LAT", chosen.getLat());
                i.putExtra("LONG", chosen.getLng());
                i.putExtra("ADDRESS", chosen.getAddress());
                i.putExtra("CITY", chosen.getCity());
                i.putExtra("STATE", chosen.getState());
                i.putExtra("ZIP", chosen.getZip());
                i.putExtra("FOODTYPE", chosen.getFoodtype());
                startActivity(i);
                return false;
            }
        });
    }
    private Stand getStand(Marker arg0) {
        for (Stand m : stands) {
            if (m.getId().toString() == arg0.getSnippet())
                return m;
        }
        //Should never happen
        return new Stand();
    }
    /*
    private class SearchTask extends AsyncTask<Location,Void,Void> {

        private Location mLocation;

        JSONObject data = null;
        String name;
        Double lat;
        Double lng;
        String address;
        String city;
        String state;
        Integer zip;
        String foodtype;

        @Override
        protected Void doInBackground(Location... params) {
            try {
                mLocation = params[0];

                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=" + mLocation.getLatitude() + "&lon=" + mLocation.getLongitude() + "&APPID=fe71689b89d124ad6fd92c6def8f48ac");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp = "";


                while ((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                data = new JSONObject(json.toString());

                if (data.getInt("cod") != 200) {
                    System.out.println("Cancelled");
                    return null;
                }

                JSONObject main = data.getJSONObject("main");
                temperature = main.getString("temp");

                JSONArray main1 = data.getJSONArray("weather");
                JSONObject hi = main1.getJSONObject(0);
                desc = hi.getString("description");


                Stand stand = new Stand();
                stand.setName(name);
                stand.setLat(lat);
                stand.setLng(lng);
                stand.setFoodtype(foodtype);
                stand.setAddress(address);
                stand.setCity(city);
                stand.setState(state);
                stand.setZip(zip);

            } catch (Exception e) {

                System.out.println("Exception " + e.getMessage());
                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (data != null) {
                Log.d("my weather received", data.toString());
            }

            Log.i(TAG, "my weather received" + desc);
            Log.i(TAG, "my weather received" + temperature);
            mCurrentLocation = mLocation;

            double amount = Double.parseDouble(temperature);
            amount = (amount * 9 / 5) - 459.67;

            Log.i(TAG, "my weather received" + amount);
            updateUI();

        }
    }

        /**
     *
     * Starts and Stops client connection with google maps
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        //setHasOptionsMenu(true);
        //getActivity().invalidateOptionsMenu();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        mClient.disconnect();
    }
}
