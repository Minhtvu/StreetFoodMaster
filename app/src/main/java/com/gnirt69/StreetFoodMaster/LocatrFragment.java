package com.gnirt69.StreetFoodMaster;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private ArrayList<Stand> stands = new ArrayList<>();
    private ArrayList<String> mStandsNames = new ArrayList<>();
    private Location mLocation;

    private static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    private static final int REQUEST_LOCATION_PERMISSIONS = 0;


    /**
     * Initial Constructor for the fragment
     *
     * @param lat - latitude
     * @param lng - longitude
     * @param food - what type of food that fragment is
     * @return
     */
    public static LocatrFragment newInstance(double lat, double lng, String food) {
        Bundle args = new Bundle();
        args.putDouble( LAT, lat);
        args.putDouble(LONG, lng);
        args.putString( FOOD, food);
        LocatrFragment frag = new LocatrFragment();
        frag.setArguments( args );
        return frag;
    }

    /**
     * Create function
     *
     * @param savedInstanceState - Saved state
     */
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

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasLocationPermission()) {
                    findCurrentLocation();
                } else {
                    requestPermissions(LOCATION_PERMISSIONS,
                            REQUEST_LOCATION_PERMISSIONS);
                }
            }
        });


        searchInDatabase();
        updateUI();
    }

    /**
     * handle database searching needs
     *
     */
    private void searchInDatabase(){
        //TODO: Implement a query to search in the database to get the data back to stands
        //Search by food name

        new StandHandler().execute();

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

    /**
     * Updates the screen and marker settings on the map
     */
    private void updateUI() {
        if (mMap == null){
            Log.i(TAG, "Error");
            return;
        }
        for(Stand m : stands) {
            MarkerOptions current = new MarkerOptions()
                    .position(new LatLng(m.getLat(), m.getLng()))
                    .title(m.getName() + " - " + m.getAddress())
                    .snippet(Integer.toString(m.getId()));
            mMap.addMarker(current);
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0 ) {
                Log.i(TAG, arg0.getSnippet() + " ");
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

    /**
     * Looks through the stands to find a correctly matching stand to an ID
     *
     *
     * @param arg0 - marker that we need to match to an ID
     * @return - if failing return a new stand
     */
    private Stand getStand(Marker arg0) {
        for (Stand m : stands) {
            if (Integer.toString(m.getId()).equalsIgnoreCase(arg0.getSnippet()))
                return m;
        }
        //Should never happen
        return new Stand();
    }

    /**
     * Show all the markers in the map and set onclick listeners properly
     *
     */
    public void showAllMarkers() {

        mMap.clear();

        for(Stand m : stands) {
            MarkerOptions current = new MarkerOptions()
                    .position(new LatLng(m.getLat(), m.getLng()))
                    .title(m.getName() + " - " + m.getAddress())
                    .snippet(Integer.toString(m.getId()));
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

    /**
     * check for the proper permissions
     *
     * @return
     */
    private boolean hasLocationPermission() {
        int result = ContextCompat
                .checkSelfPermission(getActivity(), LOCATION_PERMISSIONS[0]);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    /**
     *
     *
     * Handles functionality of the items in the top menu
     *
     * @param item - the item that the user clicks
     * @return returns a boolean when clicked
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*
            case R.id.action_locate:
                if (hasLocationPermission()) {
                    findCurrentLocation();
                } else {
                    requestPermissions(LOCATION_PERMISSIONS,
                            REQUEST_LOCATION_PERMISSIONS);
                }
                return true; */
            case R.id.delete:
                //MarkerLab.get(getActivity()).clearDB();
                showAllMarkers();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *
     *
     * Permissions requesting
     *
     * @param requestCode - code for permissions
     * @param permissions - what permissions are available
     * @param grantResults - response if permission granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSIONS:
                if (hasLocationPermission()) {
                    findCurrentLocation();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
    /**
     *
     * was used in the lab for finding image, I kept it for simplicity on locating location
     */

    private void findCurrentLocation() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);


        try {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mClient, request, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Log.i(TAG, "Got a fix: " + location);
                            mLocation = location;
                            moveMap(location);
                        }
                    });
        } catch(SecurityException e) {
            Log.i(TAG, "Unable to decode bitmap wat");
            return;
        }
    }

    /**
     * Function to change map view based on a passed in location
     *
     * @param location - Location that we would like to switch to
     */
    private void moveMap(Location location) {
        Log.i(TAG, "Unable to decode bitmap wat");
        LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
        mMap.animateCamera(yourLocation);
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

    /**
     * Handles getting the correct stands within a Lat, Long, and Radius
     *
     */
    private class StandHandler extends AsyncTask <Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params){
            Log.i(TAG, latitude + " " + longitude);
            return new NetworkHandler().getStandsByLLR(Double.toString(latitude),Double.toString(longitude),Integer.toString(3));
        }

        @Override
        protected void onPostExecute(JSONObject results){

            try {

                mStandsNames = new ArrayList<>();
                JSONArray standsArray = results.getJSONArray("stand");

                for (int i = 0; i < standsArray.length(); i++) {
                    JSONObject stand = standsArray.getJSONObject(i);

                    Stand s = new Stand(stand.getInt("standID"),
                            stand.getString("name"),
                            stand.getDouble("lat"),
                            stand.getDouble("lng"),
                            stand.getString("address"),
                            stand.getString("city"),
                            stand.getString("state"),
                            Integer.parseInt(stand.getString("zipcode")),
                            stand.getString("foodtype"));
                    mStandsNames.add(stand.getString("name"));
                    stands.add(s);

                }

                updateUI();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
