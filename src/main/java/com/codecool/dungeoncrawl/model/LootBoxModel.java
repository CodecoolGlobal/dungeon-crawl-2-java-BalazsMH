package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.items.LootBox;

public class LootBoxModel extends BaseModel {

    private int playerId;
    private int healthPotionNumber;
    private int pokeBallNumber;
    private Integer x;
    private Integer y;
    private int level;
    private int lootBoxId;

    public LootBoxModel(LootBox lootBox){
        this.healthPotionNumber = lootBox.getPotionNumber();
        this.pokeBallNumber = lootBox.getPokeBallList().size();
        this.x = lootBox.getX();
        this.y = lootBox.getY();
        this.level = lootBox.getLevel();
        this.lootBoxId = lootBox.getLootBoxId();
    }

    public LootBoxModel(int playerId, int healthPotionNumber, int pokeBallNumber, Integer x, Integer y, int level, int lootBoxId){
        this.playerId = playerId;
        this.healthPotionNumber = healthPotionNumber;
        this.pokeBallNumber = pokeBallNumber;
        this.x = x;
        this.y = y;
        this.level = level;
        this.lootBoxId = lootBoxId;
    }


    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getHealthPotionNumber() {
        return healthPotionNumber;
    }

    public void setHealthPotionNumber(int healthPotionNumber) {
        this.healthPotionNumber = healthPotionNumber;
    }

    public int getPokeBallNumber() {
        return pokeBallNumber;
    }

    public void setPokeBallNumber(int pokeBallNumber) {
        this.pokeBallNumber = pokeBallNumber;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLootBoxId() {
        return lootBoxId;
    }
}
