package com.codecool.dungeoncrawl.logic.actors.pokemon;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Pokemon implements Drawable {
    private boolean isActive = false;
    private int pokeHealth;
    private int pokeDamage;
    private final String pokeName;
    protected Cell cell;

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

    public void takeStep(Cell moveTo){
        cell.setPokemon(null);
        moveTo.setPokemon(this);
        cell = moveTo;
    }
    public Cell findRandomEmptyNeighbouringCell() {
        int[] newCoordinates = new int[2];
        while (true) {
            int changes = (int) Math.round(Math.random());
            int add = ((int) Math.round(Math.random()) == 0) ? -1 : 1;
            newCoordinates[0] = (changes == 0) ? add : 0;
            newCoordinates[1] = (changes == 0) ? 0 : add;
            Cell neighbour = cell.getNeighbor(newCoordinates[0], newCoordinates[1]);
            if (neighbour.getActor() == null && neighbour.getItem() == null && neighbour.getPokemon() == null
                    && neighbour.getTileName().equals(CellType.FLOOR.getTileName())) {
                return neighbour;
            }
        }
    }
}
