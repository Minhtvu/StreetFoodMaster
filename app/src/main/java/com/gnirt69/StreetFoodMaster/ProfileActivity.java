package com.gnirt69.StreetFoodMaster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.UUID;

/**
 * Created by minhtvu on 3/3/17.
 */

public class ProfileActivity extends AppCompatActivity {
    private Button menuButton;
    private Button faveButton;
    private String mName;
    private Double mLat;
    private Double mLng;
    private String mAddress;
    private String mCity;
    private String mState;
    private Integer mZip;
    private String mFoodtype;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        mName = getIntent().getStringExtra("NAME");
        mLat = getIntent().getDoubleExtra("LAT",0);
        mLng = getIntent().getDoubleExtra("LONG",0);
        mAddress = getIntent().getStringExtra("ADDRESS");
        mCity = getIntent().getStringExtra("CITY");
        mState = getIntent().getStringExtra("STATE");
        mZip = getIntent().getIntExtra("ZIP",0);
        mFoodtype = getIntent().getStringExtra("FOODTYPE");

        menuButton = (Button) findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(),"Menu of this shop will show up", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        faveButton = (Button) findViewById(R.id.fave_button);
        faveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(),"Favorite food of this shop will be suggested", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }
}
