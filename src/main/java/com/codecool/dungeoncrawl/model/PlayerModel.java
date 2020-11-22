package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.actors.Player;

public class PlayerModel extends BaseModel {
    private String playerName;
    private int x;
    private int y;
    private boolean godMode;
    private int level;

    public PlayerModel(String playerName, boolean godMode, int x, int y, int level) {
        this.playerName = playerName;
        this.godMode = godMode;
        this.x = x;
        this.y = y;
        this.level = level;
    }

    public PlayerModel(Player player) {
        this.playerName = player.getUserName();
        this.x = player.getX();
        this.y = player.getY();
        this.godMode = player.getGodMode();
        this.level = player.getLevel();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean getGodMode(){return godMode;}
    public void setGogMode(boolean mode) {this.godMode = mode; }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
