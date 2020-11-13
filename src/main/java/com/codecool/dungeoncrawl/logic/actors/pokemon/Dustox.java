package com.codecool.dungeoncrawl.logic.actors.pokemon;

import com.codecool.dungeoncrawl.logic.Cell;

public class Dustox extends Pokemon {

    public Dustox(Cell cell, String name){ super(cell, name); }
    public Dustox(String name){ super(name); }

    @Override
    public String getTileName(){return "dustox";}

    @Override
    public void move(){
        /***This pokemon takes one random step every time the player moves  */
        Cell moveTo = findRandomEmptyNeighbouringCell();
        takeStep(moveTo);
    }
}
