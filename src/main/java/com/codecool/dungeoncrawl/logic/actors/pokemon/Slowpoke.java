package com.codecool.dungeoncrawl.logic.actors.pokemon;


import com.codecool.dungeoncrawl.logic.Cell;

public class Slowpoke extends Pokemon {
    private boolean shouldMove = true;

    public Slowpoke(Cell cell, String name){
        super(cell, name);
    }

    @Override
    public void move(){
        /***This pokemon takes one random step every second time the player moves  */
        if (shouldMove) {
            Cell moveTo = findRandomEmptyNeighbouringCell();
            takeStep(moveTo);
        }
        shouldMove = ! shouldMove;
    }
    @Override
    public void fight(){}

}
