package com.gnirt69.StreetFoodMaster;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by minhtvu on 3/3/17.
 */

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignIn";
    private Button signInButton;
    private Button signUpButton;
    private String mUsername;
    private String mPassword;
    private String mAuthToken;
    private int mUserID;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);
        final AutoCompleteTextView emailAddress = (AutoCompleteTextView)findViewById(R.id.email);
        final EditText password = (EditText)findViewById(R.id.password);
        signInButton = (Button) findViewById(R.id.email_sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mUsername = emailAddress.getText().toString();
                mPassword = password.getText().toString();
                if (mUsername.length() == 0 || mPassword.length() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter your email and password", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
//                    Toast toast = Toast.makeText(getApplicationContext(), "Logging in ...", Toast.LENGTH_LONG);
//                    toast.show();
                    new LoginHandler().execute();
                }
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
    /*
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.signin_activity);
    }*/
    private class LoginHandler extends AsyncTask <Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params){
            return new NetworkHandler().getLogin(mUsername,mPassword);
        }

        @Override
        protected void onPostExecute(JSONObject results){
            try {
                if(results.getString("success").equals("false")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Invalid Login credentials.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    mAuthToken = results.getString("token");
                    mUserID = results.getInt("userID");
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("authToken", mAuthToken);
                    resultIntent.putExtra("userID", mUserID);
                    Log.i(TAG, Integer.toString(mUserID) + " " + mAuthToken);
                    setResult(Activity.RESULT_OK, resultIntent);
                    Toast toast = Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
