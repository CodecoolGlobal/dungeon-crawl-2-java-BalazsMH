package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.actors.Player;

public class PlayerModel extends BaseModel {
    private String playerName;
//    private int hp;
    private int x;
    private int y;
    private boolean godMode;

    public PlayerModel(String playerName, int x, int y) {
        this.playerName = playerName;
        this.x = x;
        this.y = y;
    }

    public PlayerModel(Player player) {
        this.playerName = player.getUserName();
        this.x = player.getX();
        this.y = player.getY();
        this.godMode = player.getGodMode();
//        this.hp = player.getHealth();

    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

//    public int getHp() { return hp; }
//
//    public void setHp(int hp) {
//        this.hp = hp;
//    }
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
