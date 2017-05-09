package com.gnirt69.StreetFoodMaster;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class AddStandActivity extends AppCompatActivity {
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
        mNewStandName = (EditText) findViewById(R.id.new_stand_name);
        mNewStandLat = (EditText) findViewById(R.id.new_stand_lat);
        mNewStandLng = (EditText) findViewById(R.id.new_stand_lng);
        mNewStandAddress = (EditText) findViewById(R.id.new_stand_address);
        mNewStandCity = (EditText) findViewById(R.id.new_stand_city);
        mNewStandState = (EditText) findViewById(R.id.new_stand_state);
        mNewStandZipcode = (EditText) findViewById(R.id.new_stand_zipcode);

        mNewStandFoodtype = (Spinner) findViewById(R.id.new_stand_foodtype);
        final ArrayList<String> arrayFood= new ArrayList<>();
        arrayFood.addAll(Arrays.asList(getResources().getStringArray(R.array.array_food)));

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayFood);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mNewStandFoodtype.setAdapter(aa);

        mNewStandLatLng = (Button) findViewById(R.id.new_stand_LatLng_Button);
        mNewStandLatLng.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        mNewStandPost = (Button) findViewById(R.id.new_stand_post_button);
        mNewStandPost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

            }
        });
    }

    private class LookupHandler extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params){
            return new NetworkHandler().postStand(mName,mFoodType,mLat,mLng,mAddress,mCity,mState,mZipcode,mAuthToken);
        }

        @Override
        protected void onPostExecute(JSONObject results){

        }
    }
}
