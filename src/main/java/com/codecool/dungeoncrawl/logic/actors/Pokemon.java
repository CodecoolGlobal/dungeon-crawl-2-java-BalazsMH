package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Pokemon implements Drawable {
    private boolean isActive = false;
    private int pokeHealth;
    private int pokeDamage;
    private final String pokeName;

    public Pokemon(Cell cell, String name){
        this.pokeName = name;
        this.pokeDamage = 2;
        this.pokeHealth = 4;
    }

    @Override
    public String getTileName() {
        return "pokemon";
    }

    public abstract void move();
    public abstract void fight();
}
