package com.gnirt69.StreetFoodMaster;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Kyle DiSandro
 * Creates the fragment LocatrFragment and handles API availability
 */
public class LocatrActivity extends SingleFragmentActivity {
    private static final int REQUEST_ERROR = 0;
    private static final String LAT = "Latitude";
    private static final String LONG = "Longitude";
    private static final String FOOD = "Food";
    private double latitude;
    private double longitude;
    private String food;
    @Override
    protected Fragment createFragment() {
        latitude = getIntent().getExtras().getDouble( LAT );
        longitude = getIntent().getExtras().getDouble( LONG );
        food = getIntent().getExtras().getString(FOOD);
        return LocatrFragment.newInstance(latitude, longitude, food );
    }
    public static Intent newIntent(Context packageContext, double lat, double lng, String food) {
        Intent intent = new Intent(packageContext, LocatrActivity.class);
        intent.putExtra( LAT, lat );
        intent.putExtra( LONG, lng );
        intent.putExtra( FOOD, food );
        return intent;
    }
    @Override
    protected void onResume() {
        super.onResume();

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = apiAvailability.getErrorDialog(this,
                    errorCode,
                    REQUEST_ERROR,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            // Leave if services are unavailable.
                            finish();
                        }
                    });

            errorDialog.show();
        }
    }
}
