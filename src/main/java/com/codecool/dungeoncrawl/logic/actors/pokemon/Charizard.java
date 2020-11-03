package com.codecool.dungeoncrawl.logic.actors.pokemon;


import com.codecool.dungeoncrawl.logic.Cell;

public class Charizard extends Pokemon {

    public Charizard(Cell cell, String name){ super(cell, name); }
    public Charizard(String name){ super(name); }

    @Override
    public String getTileName(){return "charizard";}

    @Override
    public void move(){
        /***This pokemon takes one random step every time the player moves  */
        Cell moveTo = findRandomEmptyNeighbouringCell();
        takeStep(moveTo);
    }

    @Override
    public void fight(){}




}
