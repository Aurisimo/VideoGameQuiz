package com.example.android.videogamequiz;

import java.util.List;

public class Company {
    private List<Game> games;
    private int companyLogoResid;
    private String companyName;

    public Company(List<Game> games, int companyLogoResid, String companyName) {
        this.games = games;
        this.companyLogoResid = companyLogoResid;
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getCompanyLogoResid() {
        return companyLogoResid;
    }

    public List<Game> getGames() {
        return games;
    }
}
