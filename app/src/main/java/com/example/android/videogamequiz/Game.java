package com.example.android.videogamequiz;

public class Game {
    private String title;
    private int imageResid;

    public Game(String title, int imageResid) {
        this.title = title;
        this.imageResid = imageResid;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResid() {
        return imageResid;
    }
}
