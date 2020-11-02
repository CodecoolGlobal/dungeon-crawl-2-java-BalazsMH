package com.codecool.dungeoncrawl.logic.actors.pokemon;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Pokemon implements Drawable {
    private boolean isActive = false;
    private int pokeHealth;
    private int pokeDamage;
    private final String pokeName;
    private Cell cell;

    public Pokemon(Cell cell, String name){
        this.pokeName = name;
        this.pokeDamage = 2;
        this.pokeHealth = 4;
        this.cell = cell;
        this.cell.setPokemon(this);
    }

    @Override
    public String getTileName() {
        return "pokemon";
    }

    public Cell getCell() {
        return cell;
    }

    public abstract void move();
    public abstract void fight();
}
