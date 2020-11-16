package com.codecool.dungeoncrawl.logic.actors.pokemon;

import com.codecool.dungeoncrawl.logic.Cell;

import java.util.List;

public class Dustox extends Pokemon {
    private boolean shouldMove;
    public Dustox(Cell cell, String name, String level){ super(cell, name, level); }
    public Dustox(String name){ super(name); }

    @Override
    public String getTileName(){return "dustox";}

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
    public void attackMove(List<List<Integer>> mapWalls, List playerCoordinates, int npcX, int npcY) { }

    @Override
    public boolean npcCanSeePlayer(List<List<Integer>> mapWalls, List playerCoordinate, int npcX, int npcY) {
        return false;
    }
}
