package com.gnirt69.actionbarsearchview;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by minhtvu on 3/3/17.
 */

public class SignUpActivity extends AppCompatActivity {
    private Button pictureButton;
    private Button menuButton;
    private Button submitButton;
    private EditText name;
    private EditText mission;
    private EditText hours;
    private EditText email;
    private EditText address;
    private EditText password;
    private EditText password_confirm;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        pictureButton = (Button) findViewById(R.id.upload_picture_button);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Allow businesses to upload their pictures
                Toast toast = Toast.makeText(getApplicationContext(),"Not yet implemented", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        menuButton = (Button) findViewById(R.id.upload_menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Allow businesses to upload their menu
                Toast toast = Toast.makeText(getApplicationContext(),"Not yet implemented", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

}
