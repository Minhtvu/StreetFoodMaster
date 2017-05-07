package com.gnirt69.StreetFoodMaster;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by minhtvu on 3/3/17.
 */

public class RegisterActivity extends AppCompatActivity {
    private Button pictureButton;
    private Button menuButton;
    private Button submitButton;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phoneNumber;
    private EditText password;
    private EditText passwordConfirm;
    private String mFirstname;
    private String mLastname;
    private String mEmail;
    private String mPhoneNumber;
    private String mPassword;
    private String mPasswordConfirm;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        final EditText firstName = (EditText)findViewById(R.id.firstName);
        final EditText lastName = (EditText)findViewById(R.id.lastName);
        final EditText email = (EditText)findViewById(R.id.emailAddress);
        final EditText phoneNumber = (EditText)findViewById(R.id.phoneNumber);
        final EditText password = (EditText)findViewById(R.id.password);
        final EditText passwordConfirm = (EditText)findViewById(R.id.passwordConfirm);

        submitButton = (Button) findViewById(R.id.registerButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mFirstname = firstName.getText().toString();
                mLastname = lastName.getText().toString();
                mEmail = email.getText().toString();
                mPhoneNumber = phoneNumber.getText().toString();
                mPassword = password.getText().toString();
                mPasswordConfirm = passwordConfirm.getText().toString();

                if(mEmail.length() == 0 || mPassword.length() == 0
                        || mPasswordConfirm.length() == 0){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Email and password is required.", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (!mPassword.equals(mPasswordConfirm)){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Passwords do not match.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    new LoginHandler().execute();
                    finish();
                }
            }
        });
    }

    private class LoginHandler extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params){
            return new NetworkHandler().postRegister(mEmail,mPassword,mFirstname,
                    mLastname,mEmail,mPhoneNumber);
        }

        @Override
        protected void onPostExecute(String results){
            Toast toast = Toast.makeText(getApplicationContext(), results, Toast.LENGTH_LONG);
            toast.show();
        }
    }

}
