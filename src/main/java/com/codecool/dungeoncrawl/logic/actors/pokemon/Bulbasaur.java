package com.codecool.dungeoncrawl.logic.actors.pokemon;

import com.codecool.dungeoncrawl.logic.Cell;

public class Bulbasaur extends Pokemon{

    public Bulbasaur(Cell cell, String name){
        super(cell, name);
    }
    @Override
    public String getTileName(){return "bulbasaur";}

    @Override
    public void move(){
        /***This pokemon takes one random step every time the player moves  */
        Cell moveTo = findRandomEmptyNeighbouringCell();
        takeStep(moveTo);
    }

    @Override
    public void fight(){}
}
