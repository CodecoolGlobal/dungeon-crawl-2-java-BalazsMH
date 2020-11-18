package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;


public class PokemonModel extends BaseModel{
    private int pokeId;
    private int pokeHealth;
    private int pokeDamage;
    private String pokeName;
    private Integer x;
    private Integer y;
    private String cellType;
    private int gameLevel;

    public PokemonModel(Pokemon pokemon){
        pokeId = pokemon.getPokeId();
        pokeHealth = pokemon.getPokeHealth();
        pokeDamage = pokemon.getPokeDamage();
        pokeName = pokemon.getPokeName();
        x = pokemon.getX();
        y = pokemon.getY();
        cellType = (pokemon.getCell() != null)? pokemon.getCell().getType().name(): null;
        gameLevel = pokemon.getGameLevel();
    }

    public PokemonModel(int pokeId, int gameLevel, int pokeHealth, int pokeDamage, String pokeName, Integer x, Integer y, String cellType){
        this.pokeId = pokeId;
        this.gameLevel = gameLevel;
        this.pokeHealth = pokeHealth;
        this.pokeDamage = pokeDamage;
        this.pokeName = pokeName;
        this.x = x;
        this.y = y;
        this.cellType = cellType;
    }

    public int getGameLevel() {
        return gameLevel;
    }

    public void setGameLevel(int gameLevel) {
        this.gameLevel = gameLevel;
    }

    public int getPokeId() {
        return pokeId;
    }

    public int getPokeHealth() {
        return pokeHealth;
    }

    public void setPokeHealth(int pokeHealth) {
        this.pokeHealth = pokeHealth;
    }

    public int getPokeDamage() {
        return pokeDamage;
    }

    public void setPokeDamage(int pokeDamage) {
        this.pokeDamage = pokeDamage;
    }

    public String getPokeName() {
        return pokeName;
    }

    public void setPokeName(String pokeName) {
        this.pokeName = pokeName;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() { return y; }

    public void setY(Integer y) {
        this.y = y;
    }

    public String getCellType() {
        return cellType;
    }

    public void setCellType(String cellType) {
        this.cellType = cellType;
    }
}
