package com.gnirt69.actionbarsearchview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * Created by minhtvu on 3/3/17.
 */

public class OptionActivity extends Activity {
    private Button currentButton;
    private Button streetButton;
    private Button foodButton;
    private static final String TAG = "OptionActivity";
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_activity);
        Intent intent = getIntent();
        currentButton = (Button) findViewById(R.id.search_location_button);
        currentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Log.AG, "Here");
                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        foodButton = (Button) findViewById(R.id.search_street_button);
        foodButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        foodButton = (Button) findViewById(R.id.search_food_button);
        foodButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.option_activity);

    }
}
