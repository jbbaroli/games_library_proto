package com.jeanoliveira.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;

    LikeButton heart_button;
    ImageView imgGame;
    Boolean result = false;
    TextView txtGameNameValue, txtGameUrl, txtDescriptionValue;
    String gameId, gameName, gameImg, gameUrl, gameDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        mDatabaseHelper = new DatabaseHelper(this);

        heart_button = findViewById(R.id.heart_button);
        imgGame = findViewById(R.id.imgGame);
        txtGameNameValue = findViewById(R.id.txtGameNameValue);
        txtGameUrl = findViewById(R.id.txtGameUrlValue);
        txtDescriptionValue = findViewById(R.id.txtDescriptionValue);

        final String[] gameDetails = intent.getStringArrayExtra("gameDetails");

        gameId = gameDetails[0];
        gameName = gameDetails[1];
        gameImg = gameDetails[2];
        gameUrl = gameDetails[3];
        gameDescription = gameDetails[4];

        if (txtGameNameValue != null) {
            txtGameNameValue.setText(gameDetails[1]);
        }

        Picasso.get().load(gameDetails[2]).into(imgGame);

        if (txtGameUrl != null) {
            txtGameUrl.setText(gameDetails[3]);
            txtGameUrl.setMovementMethod(LinkMovementMethod.getInstance());
        }

        txtDescriptionValue.setText(gameDetails[4]);

        if (mDatabaseHelper.findGame(Integer.valueOf(gameId))) {
            heart_button.setLiked(true);
        }

        heart_button.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                toastMessage("Adding to your favourites...");
                result = addData(gameId, gameName, gameImg, gameUrl, gameDescription);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                toastMessage("Removing from your favourites...");
                result = removeData(gameId);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent dataIntent = new Intent();
        if(result){
            setResult(Activity.RESULT_OK, dataIntent);
        } else{
            setResult(Activity.RESULT_CANCELED, dataIntent);
        }
        super.onBackPressed();
    }

    public boolean addData(String id, String game, String image, String url, String description){
        boolean insertData = mDatabaseHelper.addData(id, game, image, url, description);

        if (insertData) {
            toastMessage("Added successfully!");
            return true;
        } else {
            toastMessage("Something went wrong...");
            return false;
        }
    }

    public boolean removeData(String id) {
        boolean removeData = mDatabaseHelper.removeGame(id);

        if(removeData){
            toastMessage("Removed successfully!");
            return true;
        } else {
            toastMessage("Something went wrong...");
            return false;
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}