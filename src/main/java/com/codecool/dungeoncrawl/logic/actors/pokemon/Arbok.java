package com.codecool.dungeoncrawl.logic.actors.pokemon;

import com.codecool.dungeoncrawl.logic.Cell;

public class Arbok extends Pokemon {

    public Arbok(Cell cell, String name){ super(cell, name); }
    public Arbok(String name){ super(name); }

    @Override
    public String getTileName(){return "arbok";}

    @Override
    public void move(){
        /***This pokemon takes one random step every time the player moves  */
        Cell moveTo = findRandomEmptyNeighbouringCell();
        takeStep(moveTo);
    }
}
