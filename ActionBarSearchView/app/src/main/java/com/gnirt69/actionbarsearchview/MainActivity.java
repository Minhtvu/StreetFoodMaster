package com.gnirt69.actionbarsearchview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by minhtvu on 3/3/17.
 */

public class MainActivity extends AppCompatActivity {
    private Button signInButton;
    private Button advanceButton;
    private Button searchButton;
    private Button surpriseButton;
    private Button signUpButton;
    private EditText locationText;
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
        locationText = (EditText) findViewById(R.id.enter_location);
        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (locationText.getText().toString().length() > 0 ){
                    Toast toast = Toast.makeText(getApplicationContext(),"The list of shop will be given based on your location input", Toast.LENGTH_LONG);
                    toast.show();}
                else{
                    Toast toast = Toast.makeText(getApplicationContext(),"Enter a location", Toast.LENGTH_LONG);
                    toast.show();
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
                Intent intent = new Intent(v.getContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}
