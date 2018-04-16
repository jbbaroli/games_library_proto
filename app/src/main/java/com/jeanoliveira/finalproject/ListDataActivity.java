package com.jeanoliveira.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListDataActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    DatabaseHelper mDatabaseHelper;

    private ListView mlistView;
    ArrayList<String> listData;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);

        mlistView = findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this);
        
        populateListView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
        Log.d(TAG, "onResume");
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Displaying data in ListView.");

        Cursor data = mDatabaseHelper.getData();
        listData = new ArrayList<>();
        while(data.moveToNext()){
            listData.add(data.getString(1));
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mlistView.setAdapter(adapter);

        if(mlistView.getCount() == 0){

        }

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemClick: You clicked on " + name);

                Cursor data = mDatabaseHelper.getGameId(name);
                int gameId = -1;
                while(data.moveToNext()){
                    gameId = data.getInt(0);
                }
                if (gameId > -1) {
                    Log.d(TAG, "onItemClick: The ID is " + gameId);
                    Cursor gameTest = mDatabaseHelper.getGame(gameId);

                    Intent intent = new Intent(ListDataActivity.this, DetailActivity.class);
                    String test = gameTest.getString(1);
                    Log.d(TAG, "onItemClick: The game name is " + test);
                    String[] gameDetails = {String.valueOf(gameTest.getInt(0)),
                            gameTest.getString(1),
                            gameTest.getString(2),
                            gameTest.getString(3),
                            gameTest.getString(4)};

                    intent.putExtra("gameDetails", gameDetails);
                    startActivityForResult(intent, 0);

                } else {
                    toastMessage("No ID associated with that name");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            adapter.notifyDataSetChanged();
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
