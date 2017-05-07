package com.gnirt69.StreetFoodMaster;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

/**
 * Kyle DiSandro
 * Creates and handles all map and marker implementations
 */


public class LocatrFragment extends SupportMapFragment {

    private static final String TAG = "LocatrFragment";
    private static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    private static final int REQUEST_LOCATION_PERMISSIONS = 0;
    private static final String LAT = "Latitude";
    private static final String LONG = "Longitude";
    private static final String FOOD = "Food";
    private GoogleApiClient mClient;
    private GoogleMap mMap;
    private Bitmap mMapImage;
    private Location mCurrentLocation;
    private double latitude;
    private double longitude;
    private String food;
    private ArrayList<Stand> stands = new ArrayList<Stand>();
    //private RelativeLayout activityMain;

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
                        setHasOptionsMenu(true);
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
                LatLng coordinate = new LatLng(39.7555, -105.2211);
                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 10);
                mMap.animateCamera(yourLocation);

                /*
                final MarkerLab markerLab = MarkerLab.get(getActivity());
                List<Stand> markers = markerLab.getMarkers();
                for (Stand m : markers) {
                    Log.i(TAG, "hia");

                    MarkerOptions current = new MarkerOptions().position(new LatLng(m.getLat(), m.getLng()))
                            .title("lat/lng"+m.getLat()+"   "+m.getLng())
                            .snippet(m.getId().toString());

                    mMap.addMarker(current);
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                        @Override
                        public boolean onMarkerClick(final com.google.android.gms.maps.model.Stand arg0) {

                            Stand m = markerLab.getMarker(arg0.getSnippet());
                            Snackbar snackbar = Snackbar
                                    .make(getView(), "Visited: " + m.getDate() + "\nThe weather was " + m.getAddress(), Snackbar.LENGTH_LONG);
                            View snackbarView = snackbar.getView();
                            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setMaxLines(5);
                            snackbar.show();
                            return false;
                        }

                    });


                }
                */

                updateUI();
            }

        });

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasLocationPermission()) {
                    findImage();
                } else {
                    requestPermissions(LOCATION_PERMISSIONS,
                            REQUEST_LOCATION_PERMISSIONS);
                }
            }
        });




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
        getActivity().invalidateOptionsMenu();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        mClient.disconnect();
    }


    /**
     *
     * creates the top menu with it's respective functions
     *
     * @param menu the menu item up top
     * @param inflater inflates the proper xml
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_locatr, menu);

        //MenuItem searchItem = menu.findItem(R.id.action_locate);

        //searchItem.setEnabled(mClient.isConnected());
        //searchItem1.setEnabled(mClient.isConnected());
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
                    findImage();
                } else {
                    requestPermissions(LOCATION_PERMISSIONS,
                            REQUEST_LOCATION_PERMISSIONS);
                }
                return true;
                */
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
                    findImage();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    /**
     *
     * was used in the lab for finding image, I kept it for simplicity on locating location
     */
    private void findImage() {
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
                            new SearchTask().execute(location);
                        }
                    });
        } catch(SecurityException e) {
            Log.i(TAG, "Unable to decode bitmap wat");
            return;
        }
    }

    private boolean hasLocationPermission() {
        int result = ContextCompat
                .checkSelfPermission(getActivity(), LOCATION_PERMISSIONS[0]);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    /**
     *
     * Updates the actual API and properly sizes map
     */
    private void updateUI() {
        if (mMap == null || mCurrentLocation == null){// || mMapImage == null) {
            Log.i(TAG, "Unableasdnasd");
            return;
        }
        //LatLng itemPoint = new LatLng(mMapItem.getLat(), mMapItem.getLon());
        LatLng myPoint = new LatLng(
                mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());


/*
        BitmapDescriptor itemBitmap = BitmapDescriptorFactory.fromBitmap(mMapImage);
        MarkerOptions itemMarker = new MarkerOptions()
                .position(itemPoint)
                .icon(itemBitmap);

                */



        //MarkerOptions myMarker = new MarkerOptions()
          //      .position(myPoint);



       // mMap.clear();
        //mMap.addMarker(myMarker);

        LatLng northEast = move(myPoint, 709, 709);
        LatLng southWest = move(myPoint, -709, -709);

        LatLngBounds bounds = new LatLngBounds.Builder()
                //.include(myMarker)
                .include(northEast)
                .include(southWest)
                .include(myPoint)
                .build();

        //int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
        int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
        mMap.animateCamera(update);
        showAllMarkers();
    }

    public void showAllMarkers() {

        mMap.clear();

        for (Stand s : stands) {
            Log.i(TAG, "hia");

            MarkerOptions current = new MarkerOptions().position(new LatLng(s.getLat(), s.getLng()))
                    .title("lat/lng"+s.getLat()+"   "+s.getLng())
                    .snippet(s.getId().toString());

            mMap.addMarker(current);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(final Marker arg0) {

                    Snackbar snackbar = Snackbar
                            .make(getView(), "Yay", Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setMaxLines(5);
                    snackbar.show();

                    return false;
                }

            });
        }
        /*

        final MarkerLab markerLab = MarkerLab.get(getActivity());
        List<Stand> markers = markerLab.getMarkers();
        for (Stand m : markers) {
            Log.i(TAG, "hia");

            MarkerOptions current = new MarkerOptions().position(new LatLng(m.getLat(), m.getLng()))
                    .title("lat/lng"+m.getLat()+"   "+m.getLng())
                    .snippet(m.getId().toString());

            mMap.addMarker(current);
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(final com.google.android.gms.maps.model.Stand arg0) {

                    Stand m = markerLab.getMarker(arg0.getSnippet());
                    Snackbar snackbar = Snackbar
                            .make(getView(), "Visited: " + m.getDate() + "\nThe weather was " + m.getAddress(), Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setMaxLines(5);
                    snackbar.show();

                    return false;
                }

            });

        }
        */
    }


    private class SearchTask extends AsyncTask<Location,Void,Void> {

        private Location mLocation;

        JSONObject data = null;
        String temperature;
        String desc;
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

                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat="+mLocation.getLatitude()+"&lon="+mLocation.getLongitude()+"&APPID=fe71689b89d124ad6fd92c6def8f48ac");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuffer json = new StringBuffer(1024);
                String tmp = "";


                while((tmp = reader.readLine()) != null)
                    json.append(tmp).append("\n");
                reader.close();

                data = new JSONObject(json.toString());

                if(data.getInt("cod") != 200) {
                    System.out.println("Cancelled");
                    return null;
                }

                JSONObject main  = data.getJSONObject("main");
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

                System.out.println("Exception "+ e.getMessage());
                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if(data!=null){
                Log.d("my weather received", data.toString());
            }

            Log.i(TAG, "my weather received"+desc);
            Log.i(TAG,"my weather received"+ temperature);

            //mMapImage = mBitmap;
            //mMapItem = mGalleryItem;
            mCurrentLocation = mLocation;

            double amount = Double.parseDouble(temperature);
            amount = (amount*9/5) - 459.67;

            Log.i(TAG,"my weather received"+ amount);

            /*

            Stand marker = new Stand();
            marker.setLat(mLocation.getLatitude());
            marker.setLng(mLocation.getLongitude());
            marker.setTemp(amount);
            marker.setAddress(desc);
            MarkerLab.get(getActivity()).addMarker(marker);
*/

            updateUI();

        }



/*

        @Override
        protected Void doInBackground(Location... params) {
            Log.i(TAG, "wat" + params[0]);
            mLocation = params[0];
            FlickrFetchr fetchr = new FlickrFetchr();
            List<GalleryItem> items = fetchr.searchPhotos(params[0]);

            if (items.size() == 0) {
                return null;
            }

            mGalleryItem = items.get(0);

            try {
                byte[] bytes = fetchr.getUrlBytes(mGalleryItem.getUrl());
                mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } catch (IOException ioe) {
                Log.i(TAG, "Unable to decode bitmap", ioe);
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            mMapImage = mBitmap;
            mMapItem = mGalleryItem;
            mCurrentLocation = mLocation;


            updateUI();
        }
    */

    }

    private static final double EARTHRADIUS = 6366198;
    /**
     * Create a new LatLng which lies toNorth meters north and toEast meters
     * east of startLL
     */
    private static LatLng move(LatLng startLL, double toNorth, double toEast) {
        double lonDiff = meterToLongitude(toEast, startLL.latitude);
        double latDiff = meterToLatitude(toNorth);
        return new LatLng(startLL.latitude + latDiff, startLL.longitude
                + lonDiff);
    }

    private static double meterToLongitude(double meterToEast, double latitude) {
        double latArc = Math.toRadians(latitude);
        double radius = Math.cos(latArc) * EARTHRADIUS;
        double rad = meterToEast / radius;
        return Math.toDegrees(rad);
    }


    private static double meterToLatitude(double meterToNorth) {
        double rad = meterToNorth / EARTHRADIUS;
        return Math.toDegrees(rad);
    }

}
