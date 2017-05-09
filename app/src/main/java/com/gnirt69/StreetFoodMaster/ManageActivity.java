package com.gnirt69.StreetFoodMaster;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ManageActivity extends AppCompatActivity {
    private static int mUserID;
    private static String mAuthToken;
    private ArrayList<Stand> mOwnedStands;
    private ListView mStandsList;
    private ArrayList<String> mStandsNames;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_activity);
        if(savedInstanceState != null) {
            mUserID = savedInstanceState.getInt("userID");
            mAuthToken = savedInstanceState.getString("authToken");
        }

        mOwnedStands = new ArrayList<>();
        mStandsNames = new ArrayList<>();
        mStandsList = (ListView) findViewById(R.id.manage_stands_list);

        mStandsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(view.getContext(), StandActivity.class);
                intent.putExtra("standID", mOwnedStands.get(position).getId());
                intent.putExtra("authToken", mAuthToken);
                intent.putExtra("update", true);
                startActivity(intent);
            }

        });

        if(mAuthToken == null){
            Bundle extras = getIntent().getExtras();
            mUserID = extras.getInt("userID");
            mAuthToken = extras.getString("authToken");
        }
        Log.i("manage", Integer.toString(mUserID)+" "+mAuthToken);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(view.getContext(), StandActivity.class);
                intent.putExtra("authToken", mAuthToken);
                intent.putExtra("update", false);
                intent.putExtra("standID", -1);
                startActivity(intent);
                new LookupHandler().execute();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new LookupHandler().execute();
    }
    @Override
    protected void onResume(){
        super.onResume();
        new LookupHandler().execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("userID", mUserID);
        savedInstanceState.putString("authToken", mAuthToken);
    }

    private class LookupHandler extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Void... params){
            return new NetworkHandler().getStandsByOwner(mUserID, mAuthToken);
        }

        @Override
        protected void onPostExecute(JSONObject results){
            try {
                mOwnedStands = new ArrayList<>();
                mStandsNames = new ArrayList<>();
                JSONArray standsArray = results.getJSONArray("Stands");

                for (int i = 0; i < standsArray.length(); i++) {
                    JSONObject stand = standsArray.getJSONObject(i);

                    Stand s = new Stand(stand.getInt("standID"),
                            stand.getString("name"),
                            stand.getDouble("lat"),
                            stand.getDouble("lng"),
                            stand.getString("address"),
                            stand.getString("city"),
                            stand.getString("state"),
                            Integer.parseInt(stand.getString("zipcode")),
                            stand.getString("foodtype"));
                    mOwnedStands.add(s);
                    mStandsNames.add(stand.getString("name"));
                    adapter = new ArrayAdapter<>(
                            ManageActivity.this,
                            android.R.layout.simple_list_item_1,
                            mStandsNames);
                    mStandsList.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
