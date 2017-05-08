package com.gnirt69.StreetFoodMaster;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

/**
 * Created by minhtvu on 3/3/17.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Place";
    private Button signInButton;
    private Button advanceButton;
    private Button searchButton;
    private Button surpriseButton;
    private Button signUpButton;
    private Place currentPlace;
    PlaceAutocompleteFragment autocompleteFragment;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        signInButton = (Button) findViewById(R.id.signin_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
        advanceButton = (Button) findViewById(R.id.advance_button);
        advanceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OptionActivity.class);
                startActivity(intent);
            }
        });
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                currentPlace = place;
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (currentPlace == null)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"Please choose a specific place!", Toast.LENGTH_LONG);
                    toast.show();
                }
                else
                {
                    Intent targetActivityIntent = LocatrActivity.newIntent( getApplicationContext(), currentPlace.getLatLng().latitude,
                            currentPlace.getLatLng().longitude,null);
                    startActivity(targetActivityIntent);
                    currentPlace = null;
                }
            }
        });
        surpriseButton = (Button) findViewById(R.id.surprise_button);
        surpriseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SurpriseActivity.class);
                startActivity(intent);
            }
        });
        signUpButton = (Button) findViewById(R.id.new_business);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
