package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;


public class PokemonModel extends BaseModel{
    private boolean isActive = false;
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
}
