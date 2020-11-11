package com.codecool.dungeoncrawl.logic.actors.pokemon;


import com.codecool.dungeoncrawl.logic.Cell;

public class Slowpoke extends Pokemon {
    private boolean shouldMove = true;

    public Slowpoke(Cell cell, String name){ super(cell, name); }
    public Slowpoke(String name){ super(name); }

    @Override
    public String getTileName(){return "slowpoke";}

    @Override
    public void move(){
        /***This pokemon takes one random step every second time the player moves  */
        if (shouldMove) {
            Cell moveTo = findRandomEmptyNeighbouringCell();
            takeStep(moveTo);
        }
        shouldMove = ! shouldMove;
    }
}
