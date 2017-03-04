package com.gnirt69.actionbarsearchview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by minhtvu on 3/3/17.
 */

public class MainActivity extends AppCompatActivity {
    private Button signInButton;
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
        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OptionActivity.class);
                startActivity(intent);
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
        locationText = (EditText) findViewById(R.id.enter_location);
        locationText.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable text){
                //call the search engine with the text
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }
}
