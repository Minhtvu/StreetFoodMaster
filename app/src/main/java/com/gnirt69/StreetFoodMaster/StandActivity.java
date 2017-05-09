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

public class StandActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
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
    private boolean mUpdate;
    private int mStandID;

    private EditText mStandName;
    private EditText mStandLat;
    private EditText mStandLng;
    private EditText mStandAddress;
    private EditText mStandCity;
    private EditText mStandState;
    private EditText mStandZipcode;
    private Spinner mStandFoodtype;
    private Button mStandLatLng;
    private Button mStandPost;
    private Button mStandUpdate;
    private Button mStandDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_stand_activity);

        setTitle("Create a New Stand");

        Bundle extras = getIntent().getExtras();
        mAuthToken = extras.getString("authToken");
        mUpdate = extras.getBoolean("update");
        mStandID = extras.getInt("standID");

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

        mStandName = (EditText) findViewById(R.id.stand_name);
        mStandLat = (EditText) findViewById(R.id.stand_lat);
        mStandLng = (EditText) findViewById(R.id.stand_lng);
        mStandAddress = (EditText) findViewById(R.id.stand_address);
        mStandCity = (EditText) findViewById(R.id.stand_city);
        mStandState = (EditText) findViewById(R.id.stand_state);
        mStandZipcode = (EditText) findViewById(R.id.stand_zipcode);

        mStandFoodtype = (Spinner) findViewById(R.id.stand_foodtype);
        mStandFoodtype.setOnItemSelectedListener(this);

        final ArrayList<String> arrayFood= new ArrayList<>();
        arrayFood.addAll(Arrays.asList(getResources().getStringArray(R.array.array_food)));

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayFood);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mStandFoodtype.setAdapter(aa);

        mStandLatLng = (Button) findViewById(R.id.stand_LatLng_Button);
        mStandLatLng.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (hasLocationPermission()) {
                    findLatLng();
                } else {
                    requestPermissions(LOCATION_PERMISSIONS,
                            REQUEST_LOCATION_PERMISSIONS);
                }
                mStandLat.setText(Double.toString(mLat));
                mStandLng.setText(Double.toString(mLng));
            }
        });

        mStandUpdate = (Button) findViewById(R.id.stand_update_button);
        mStandUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mName = mStandName.getText().toString();
                mLat = Double.parseDouble(mStandLat.getText().toString());
                mLng = Double.parseDouble(mStandLng.getText().toString());
                mAddress = mStandAddress.getText().toString();
                mCity = mStandCity.getText().toString();
                mState = mStandState.getText().toString();
                mZipcode = Integer.parseInt(mStandZipcode.getText().toString());
                new UpdateHandler().execute();
            }
        });

        mStandPost = (Button) findViewById(R.id.stand_post_button);
        mStandPost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                mName = mStandName.getText().toString();
                mLat = Double.parseDouble(mStandLat.getText().toString());
                mLng = Double.parseDouble(mStandLng.getText().toString());
                mAddress = mStandAddress.getText().toString();
                mCity = mStandCity.getText().toString();
                mState = mStandState.getText().toString();
                mZipcode = Integer.parseInt(mStandZipcode.getText().toString());
                new PostHandler().execute();
            }
        });

        mStandDelete = (Button) findViewById(R.id.stand_Delete_button);
        mStandDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                new DeleteHandler().execute();
            }
        });

        if(mAuthToken == null){
            new LookupHandler().execute();
            mStandName.setFocusable(false);
            mStandName.setClickable(false);
            mStandLat.setFocusable(false);
            mStandLat.setClickable(false);
            mStandLng.setFocusable(false);
            mStandLng.setClickable(false);
            mStandAddress.setFocusable(false);
            mStandAddress.setClickable(false);
            mStandCity.setFocusable(false);
            mStandCity.setClickable(false);
            mStandState.setFocusable(false);
            mStandState.setClickable(false);
            mStandZipcode.setFocusable(false);
            mStandZipcode.setClickable(false);
            mStandFoodtype.setFocusable(false);
            mStandFoodtype.setClickable(false);
            mStandLatLng.setVisibility(View.GONE);
            mStandDelete.setVisibility(View.GONE);
            mStandPost.setVisibility(View.GONE);
            mStandUpdate.setVisibility(View.GONE);
        } else if(mUpdate) {
            mStandPost.setVisibility(View.GONE);
            mStandLatLng.setText("Update Location");
            new LookupHandler().execute();
        } else {
            mStandUpdate.setVisibility(View.GONE);
            mStandDelete.setVisibility(View.GONE);
        }
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
        mStandFoodtype.setSelection(position);
        mFoodType = (String) mStandFoodtype.getSelectedItem();
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

    private class UpdateHandler extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params){
            return new NetworkHandler().putStand(mStandID,mName,mFoodType,mLat,mLng,mAddress,mCity,mState,mZipcode,mAuthToken);
        }

        @Override
        protected void onPostExecute(JSONObject results){
            finish();
        }
    }

    private class DeleteHandler extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params){
            return new NetworkHandler().deleteStand(mStandID,mAuthToken);
        }

        @Override
        protected void onPostExecute(JSONObject results){
            finish();
        }
    }

    private class LookupHandler extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params){
            return new NetworkHandler().getStand(mStandID);
        }

        @Override
        protected void onPostExecute(JSONObject results){
            try {
                JSONObject stand =results.getJSONObject("stand");

                mName = stand.getString("name");
                mLat = stand.getDouble("lat");
                mLng = stand.getDouble("lng");
                mAddress = stand.getString("address");
                mCity = stand.getString("city");
                mState = stand.getString("state");
                mZipcode = Integer.parseInt(stand.getString("zipcode"));
                mFoodType = stand.getString("foodtype");
                mStandName.setText(mName);
                mStandLat.setText(Double.toString(mLat));
                mStandLng.setText(Double.toString(mLng));
                mStandAddress.setText(mAddress);
                mStandCity.setText(mCity);
                mStandState.setText(mState);
                mStandZipcode.setText(Integer.toString(mZipcode));
                mStandFoodtype.setSelection(Arrays.asList(getResources().getStringArray(R.array.array_food)).indexOf(mFoodType));

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }
}
