package com.codecool.dungeoncrawl.model;


import com.codecool.dungeoncrawl.logic.items.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryModel extends BaseModel {

    private int healthPotionNumber;
    private int pokeBallNumber;
    private List<Integer> pokemonIds = new ArrayList<>();
    private boolean key;
    private int activePokemonId;
    private int playerId;

    public InventoryModel(Inventory inventory){
        healthPotionNumber = inventory.getHealthPotionNumber();
        pokeBallNumber = inventory.getPokeBallNumber();
        pokemonIds = inventory.getAllPokemon().stream().map(p -> p.getPokeId()).collect(Collectors.toList());
        key = inventory.hasKey();
        activePokemonId = inventory.getActivePokemon().getPokeId();
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

    public List<Integer> getPokemonIds() {
        return pokemonIds;
    }

    public void setPokemonIds(List<Integer> pokemonIds) {
        this.pokemonIds = pokemonIds;
    }

    public boolean isKey() {
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
