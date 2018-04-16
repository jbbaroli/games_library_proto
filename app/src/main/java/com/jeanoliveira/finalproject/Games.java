package com.jeanoliveira.finalproject;

/**
 * Created by Jean on 3/22/2018.
 */

public class Games {

    private String gameId;
    private String title;
    private String image_small;
    private String image_medium;
    private String description;
    private String release_date;
    private String game_url;

    public Games(String gameId, String title, String image_small, String image_medium, String release_date, String game_url, String description) {
        this.gameId = gameId;
        this.title = title;
        this.image_small = image_small;
        this.image_medium = image_medium;
        this.release_date = release_date;
        this.game_url = game_url;
        this.description = description;
    }

    public String getGameId() {  return gameId;  }

    public void setGameId(String gameId) {  this.gameId = gameId;  }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_medium() { return image_medium; }

    public void setImage_medium(String image_medium) { this.image_medium = image_medium; }

    public String getImage_small() {
        return image_small;
    }

    public void setImage_small(String image_small) {
        this.image_small = image_small;
    }

    public String getDescription() { return description;  }

    public void setDescription(String description) { this.description = description;  }

    public String getRelease_date() { return release_date; }

    public void setRelease_date(String release_date) { this.release_date = release_date; }

    public String getGame_url() { return game_url; }

    public void setGame_url(String game_url) { this.game_url = game_url; }
}
