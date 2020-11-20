package com.codecool.dungeoncrawl.model;


import com.codecool.dungeoncrawl.logic.items.Inventory;


public class InventoryModel extends BaseModel {

    private int healthPotionNumber;
    private int pokeBallNumber;
    private boolean key;
    private int activePokemonId;
    private int playerId;

    public InventoryModel(Inventory inventory){
        healthPotionNumber = inventory.getHealthPotionNumber();
        pokeBallNumber = inventory.getPokeBallNumber();
        key = inventory.hasKey();
        activePokemonId = inventory.getActivePokemon().getPokeId();
    }

    public InventoryModel(int playerId, int healthPotionNumber, int pokeBallNumber, boolean key, int activePokemonId){
        this.healthPotionNumber = healthPotionNumber;
        this.pokeBallNumber = pokeBallNumber;
        this.key = key;
        this.activePokemonId = activePokemonId;
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

    public boolean hasKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }

    public int getActivePokemonId() {
        return activePokemonId;
    }

    public void setActivePokemonId(int activePokemonId) {
        this.activePokemonId = activePokemonId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
