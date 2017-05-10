package com.gnirt69.StreetFoodMaster;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    private static final int SIGNINREQUESTCODE = 100;
    private static final int SIGNUPREQUESTCODE = 101;
    private static final int MANAGEREQUESTCODE = 102;
    private static final String KEY_AUTH = "authToken";
    private static final String KEY_USER = "userID";

    private String authToken = "NULL";
    private int userID = -1;

    private Button signInButton;
    private Button advanceButton;
    private Button searchButton;
    private Button surpriseButton;
    private Button signUpButton;
    private Button mManageButton;
    private Place currentPlace;
    PlaceAutocompleteFragment autocompleteFragment;

    /**
     * Checks for login credentials and hides buttons accordingly
     */
    protected void checkLoggedIn(){
        findViewById(R.id.signin_button).setVisibility(View.GONE);
        findViewById(R.id.register_button).setVisibility(View.GONE);
        findViewById(R.id.manage_button).setVisibility(View.GONE);
        if(userID == -1){
            findViewById(R.id.signin_button).setVisibility(View.VISIBLE);
            findViewById(R.id.register_button).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.manage_button).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Create function, inflates buttons and sets proper onclick listeners
     *
     * @param savedInstanceState - saved state of app
     */
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        checkLoggedIn();
        signInButton = (Button) findViewById(R.id.signin_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SignInActivity.class);
                startActivityForResult(intent, SIGNINREQUESTCODE);
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
//        surpriseButton = (Button) findViewById(R.id.surprise_button);
//        surpriseButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), SurpriseActivity.class);
//                startActivity(intent);
//            }
//        });
        signUpButton = (Button) findViewById(R.id.register_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivityForResult(intent, SIGNUPREQUESTCODE);
            }
        });

        mManageButton = (Button) findViewById(R.id.manage_button);
        mManageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ManageActivity.class);
                intent.putExtra("authToken", authToken);
                intent.putExtra("userID", userID);
                Log.i(TAG,Integer.toString(userID)+" "+authToken);
                startActivityForResult(intent,MANAGEREQUESTCODE);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putString(KEY_AUTH, authToken);
        savedInstanceState.putInt(KEY_USER, userID);
    }

    /**
     * Returns information from the activities called
     *
     * @param requestCode - What was sent in
     * @param resultCode - What was returned
     * @param data - data to parse for information from activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (SIGNINREQUESTCODE) : {
                if (resultCode == Activity.RESULT_OK) {
                    authToken = data.getStringExtra("authToken");
                    userID = data.getIntExtra("userID", -1);
                    checkLoggedIn();
                }
                break;
            }
            case (SIGNUPREQUESTCODE): {
                if (resultCode == Activity.RESULT_OK) {
                    authToken = data.getStringExtra("authToken");
                    userID = data.getIntExtra("userID", -1);
                    checkLoggedIn();
                }
                break;
            }
            case (MANAGEREQUESTCODE): {
                if (resultCode == Activity.RESULT_OK) {
                    authToken = data.getStringExtra("authToken");
                    userID = data.getIntExtra("userID", -1);
                    checkLoggedIn();
                }
                break;
            }
        }
    }
}
