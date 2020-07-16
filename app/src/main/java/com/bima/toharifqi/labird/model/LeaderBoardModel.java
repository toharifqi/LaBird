package com.bima.toharifqi.labird.model;

public class LeaderBoardModel {
    String name, username;
    int level, poin;

    public LeaderBoardModel() {
    }

    public LeaderBoardModel(String name, String username, int level, int poin) {
        this.name = name;
        this.level = level;
        this.poin = poin;
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPoin() {
        return poin;
    }

    public void setPoin(int poin) {
        this.poin = poin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
