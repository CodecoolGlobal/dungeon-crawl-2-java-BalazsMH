package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;


public class PokemonModel extends BaseModel{
    private int pokeHealth;
    private int pokeDamage;
    private String pokeName;
    private int x;
    private int y;
    private String cellType;

    public PokemonModel(Pokemon pokemon){
        pokeHealth = pokemon.getPokeHealth();
        pokeDamage = pokemon.getPokeDamage();
        pokeName = pokemon.getPokeName();
        x = pokemon.getX();
        y = pokemon.getY();
        cellType = pokemon.getCell().getType().name();
        this.setId((int)(Math.random()*1000000));
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

    public String getCellType() {
        return cellType;
    }

    public void setCellType(String cellType) {
        this.cellType = cellType;
    }
}
