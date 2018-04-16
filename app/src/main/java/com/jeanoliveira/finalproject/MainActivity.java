package com.jeanoliveira.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private List<Games> lstGames;
    private EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerViewAdapter recyclerViewAdapter;
    private OkHttpHandler okHttpHandler;
    private RecyclerView recyclerView;
    private Parcelable recyclerViewState;

    GridLayoutManager gridLayoutManager;

    EditText etGameName;

    Button btnSearch;

    int numberOfPages = 1;
    int currentPage;
    int newItems;

    String baseAddress = "https://www.giantbomb.com/api/search/?api_key=";
    String apiKey = "f0458f75619cf8fcf55830a21a02d7f64dce981d";
    String format = "&format=json";
    String resource = "&resources=game";
    String page = "&page=";
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        lstGames = new ArrayList<>();

        recyclerView = findViewById(R.id.layRecyclerView);

        btnSearch = findViewById(R.id.btnSearch);
        etGameName = findViewById(R.id.etGameName);

        gridLayoutManager = new GridLayoutManager(this, 2);

        btnSearch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etGameName.getText().toString().matches("")){
                    currentPage = 1;
                    String query = "&query=" + etGameName.getText().toString().trim();
                    url = baseAddress + apiKey + format + query + resource + page;

                    okHttpHandler = new OkHttpHandler();
                    okHttpHandler.execute(url + String.valueOf(currentPage));

                    if (lstGames.size() > 0) {
                        lstGames.clear();
                        recyclerViewAdapter.notifyDataSetChanged();
                        scrollListener.resetState();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "You must enter a game name", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.favourites:
                Intent favouritesIntent = new Intent(MainActivity.this, ListDataActivity.class);
                startActivity(favouritesIntent);
                break;

            case R.id.settings:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            default:

        }

        return super.onOptionsItemSelected(item);
    }

    public class OkHttpHandler extends AsyncTask {
        OkHttpClient client = new OkHttpClient();
//        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object[] params) {
            Request.Builder builder = new Request.Builder();
            builder.url(params[0].toString());
            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (recyclerViewAdapter == null) {
                recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, lstGames);
                recyclerView.setLayoutManager(gridLayoutManager);
            }

            recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            recyclerView.setAdapter(recyclerViewAdapter);

            numberOfPages = parseResponse(o.toString());

            scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if(numberOfPages > currentPage){
                        currentPage ++;
                        loadNextPage(currentPage);
                    }
                }
            };

            recyclerView.addOnScrollListener(scrollListener);
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    public void loadNextPage(int page) {
        okHttpHandler = new OkHttpHandler();
        okHttpHandler.execute(url + String.valueOf(page));
        recyclerViewAdapter.notifyItemRangeInserted(lstGames.size(), newItems);
    }

    private int parseResponse(String response) {
        try{
            JSONObject json = new JSONObject(response);
            if (json.getString("error").equals("OK")) {
                newItems = Integer.valueOf(json.getString("number_of_page_results"));
                numberOfPages = Integer.valueOf(json.getString("number_of_total_results"));
                numberOfPages = (int)Math.ceil(numberOfPages / Integer.valueOf(json.getString("limit")));
                JSONArray parentArray = json.getJSONArray("results");

                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject image = parentArray.getJSONObject(i).getJSONObject("image");

                    String original_release_date, deck;

                    if (!json.isNull(parentArray.getJSONObject(i).getString("deck")) || !parentArray.getJSONObject(i).getString("deck").isEmpty()) {
                        deck = parentArray.getJSONObject(i).getString("deck");
                    } else {
                        deck = "Information not available.";
                    }

                    if (!json.isNull(parentArray.getJSONObject(i).getString("original_release_date"))) {
                        original_release_date = parentArray.getJSONObject(i).getString("original_release_date");
                    } else {
                        original_release_date = "Information not available.";
                    }

                    lstGames.add(new Games(parentArray.getJSONObject(i).getString("id"),
                                           parentArray.getJSONObject(i).getString("name"),
                                           image.getString("small_url"),
                                           image.getString("super_url"),
                                           original_release_date,
                                           parentArray.getJSONObject(i).getString("site_detail_url"),
                                           deck));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return numberOfPages;
    }
}
