package com.gnirt69.actionbarsearchview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by minhtvu on 3/3/17.
 */

public class ProfileActivity extends AppCompatActivity {
    private Button menuButton;
    private Button faveButton;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
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
