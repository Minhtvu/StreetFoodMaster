package com.gnirt69.StreetFoodMaster;

import android.*;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class AddStandActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
//    private static final String[] LOCATION_PERMISSIONS = new String[]{
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION,
//    };

    private Location mLocation;
    private GoogleApiClient mClient;
    private static final String TAG = "AddStand";


    private static final String[] LOCATION_PERMISSIONS = new String[]{
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    private static final int REQUEST_LOCATION_PERMISSIONS = 0;
    private String mName;
    private String mFoodType;
    private String mAddress;
    private String mCity;
    private String mState;
    private String mAuthToken;
    private double mLat;
    private double mLng;
    private int mZipcode;

    private EditText mNewStandName;
    private EditText mNewStandLat;
    private EditText mNewStandLng;
    private EditText mNewStandAddress;
    private EditText mNewStandCity;
    private EditText mNewStandState;
    private EditText mNewStandZipcode;
    private Spinner mNewStandFoodtype;
    private Button mNewStandLatLng;
    private Button mNewStandPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_stand_activity);

        Bundle extras = getIntent().getExtras();
        mAuthToken = extras.getString("authToken");

        mClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();

        mNewStandName = (EditText) findViewById(R.id.new_stand_name);
        mNewStandLat = (EditText) findViewById(R.id.new_stand_lat);
        mNewStandLng = (EditText) findViewById(R.id.new_stand_lng);
        mNewStandAddress = (EditText) findViewById(R.id.new_stand_address);
        mNewStandCity = (EditText) findViewById(R.id.new_stand_city);
        mNewStandState = (EditText) findViewById(R.id.new_stand_state);
        mNewStandZipcode = (EditText) findViewById(R.id.new_stand_zipcode);

        mNewStandFoodtype = (Spinner) findViewById(R.id.new_stand_foodtype);
        mNewStandFoodtype.setOnItemSelectedListener(this);
        final ArrayList<String> arrayFood= new ArrayList<>();
        arrayFood.addAll(Arrays.asList(getResources().getStringArray(R.array.array_food)));

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayFood);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mNewStandFoodtype.setAdapter(aa);

        mNewStandLatLng = (Button) findViewById(R.id.new_stand_LatLng_Button);
        mNewStandLatLng.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (hasLocationPermission()) {
                    findLatLng();
                } else {
                    requestPermissions(LOCATION_PERMISSIONS,
                            REQUEST_LOCATION_PERMISSIONS);
                }
                mNewStandLat.setText(Double.toString(mLat));
                mNewStandLng.setText(Double.toString(mLng));
            }
        });

        mNewStandPost = (Button) findViewById(R.id.new_stand_post_button);
        mNewStandPost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                mName = mNewStandName.getText().toString();
                mLat = Double.parseDouble(mNewStandLat.getText().toString());
                mLng = Double.parseDouble(mNewStandLng.getText().toString());
                mAddress = mNewStandAddress.getText().toString();
                mCity = mNewStandCity.getText().toString();
                mState = mNewStandState.getText().toString();
                mZipcode = Integer.parseInt(mNewStandZipcode.getText().toString());
                new PostHandler().execute();
            }
        });
    }

    private boolean hasLocationPermission() {
        int result = ContextCompat
                .checkSelfPermission(this, LOCATION_PERMISSIONS[0]);
        return result == PackageManager.PERMISSION_GRANTED;
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
                    findLatLng();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    /**
     *
     * was used in the lab for finding image, I kept it for simplicity on locating location
     */

    private void findLatLng() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
//        try {
//            LocationServices.FusedLocationApi.getLastLocation()
//        }


        try {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mClient, request, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            Log.i(TAG, "Got a fix: " + location);
                            mLocation = location;
                            mLat = location.getLatitude();
                            mLng = location.getLongitude();
                        }
                    });
        } catch(SecurityException e) {
            Log.i(TAG, "Unable to decode bitmap wat");
            return;
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
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        mNewStandFoodtype.setSelection(position);
        mFoodType = (String) mNewStandFoodtype.getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private class PostHandler extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params){
            return new NetworkHandler().postStand(mName,mFoodType,mLat,mLng,mAddress,mCity,mState,mZipcode,mAuthToken);
        }

        @Override
        protected void onPostExecute(JSONObject results){
            finish();
        }
    }
}
