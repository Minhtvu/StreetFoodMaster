package com.gnirt69.actionbarsearchview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by minhtvu on 3/3/17.
 */

public class SignInActivity extends AppCompatActivity {
    private EditText password;
    private Button signInButton;
    private Button signUpButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_activity);
        final AutoCompleteTextView emailAddress = (AutoCompleteTextView)findViewById(R.id.email);
        final EditText password = (EditText)findViewById(R.id.password);
        signInButton = (Button) findViewById(R.id.email_sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (emailAddress.getText().toString().length() == 0 || password.getText().toString().length() == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter your email and password", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Logging in ...", Toast.LENGTH_LONG);
                    toast.show();
                    finish();
                }
            }
        });
        signUpButton = (Button) findViewById(R.id.new_business);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), SignUpActivity.class);
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
}
