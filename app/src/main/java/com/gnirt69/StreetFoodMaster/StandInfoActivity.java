package com.gnirt69.StreetFoodMaster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by Robert on 5/6/17
 */

public class StandInfoActivity extends AppCompatActivity {
    private EditText mStandName;
    private EditText mStandLat;
    private EditText mStandLng;
    private EditText mAddress;
    private EditText mCity;
    private EditText mState;
    private EditText mZipCode;
    private Spinner mFoodtype;
    private Button mUpdateLatLngButton;
    private Button mDeleteButton;
    private Button mUpdateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stand_info_activity);

    }
}
