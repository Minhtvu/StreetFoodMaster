package com.gnirt69.StreetFoodMaster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchFoodActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    private double latitude;
    private double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_food_activity);
        latitude = getIntent().getDoubleExtra("Lat", 0);
        longitude = getIntent().getDoubleExtra("Long", 0);
        ListView lv = (ListView)findViewById(R.id.listViewItem);
        final ArrayList<String> arrayFood= new ArrayList<>();
        arrayFood.addAll(Arrays.asList(getResources().getStringArray(R.array.array_food)));

        adapter = new ArrayAdapter<>(
                SearchFoodActivity.this,
                android.R.layout.simple_list_item_1,
                arrayFood);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id){
                Intent targetActivityIntent = LocatrActivity.newIntent( getApplicationContext(), latitude, longitude, arrayFood.get(position));
                startActivity(targetActivityIntent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_food_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    /*
    @Override
    protected void onStart() {
        super.onStart()search;
        setContentView(R.layout.seativity);

    }*/

}
