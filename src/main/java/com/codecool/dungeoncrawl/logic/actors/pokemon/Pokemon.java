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
        this(name);
        this.cell = cell;
        this.cell.setPokemon(this);
    }

    public Pokemon(String name){
        this.pokeName = name;
        this.pokeDamage = 2;
        this.pokeHealth = 4;
    }

    /***
     * VERY BAD SOLUTION to ensure that each pokemon class can override getTileName (which comes from Drawable)
     * @return as Pokemon cannot be instantiated, null will never be returned
     */
    @Override
    public String getTileName(){return null;}

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell){
        this.cell = cell;
    }

    public void removePokemonFromCell(){
        cell.setPokemon(null);
        cell = null;
    }

    public abstract void move();

    public int getPokeHealth() { return pokeHealth; }

    public void setPokeHealth(int pokeHealth) { this.pokeHealth = pokeHealth; }

    public int getPokeDamage() { return pokeDamage; }

    public void setPokeDamage(int pokeDamage) { this.pokeDamage = pokeDamage; }

    public String getPokeName(){ return this.pokeName; }

    /***
     * Aim: make sure that info is updated everywhere it needs to be updated
     * @param moveTo this Cell has to be determined for each pokemon depending on their moving patterns (e.g. chasing, random...)
     */
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

    @Override
    public String toString() {
        return pokeName + "\n" +
                "Health = " + pokeHealth + '\n' +
                "Damage = " + pokeDamage +  '\n';
    }
}
