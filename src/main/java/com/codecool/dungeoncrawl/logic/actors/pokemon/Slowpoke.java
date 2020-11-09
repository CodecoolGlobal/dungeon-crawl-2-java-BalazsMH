package com.codecool.dungeoncrawl.logic.actors.pokemon;


import com.codecool.dungeoncrawl.logic.Cell;

import java.util.List;

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

    @Override
    public void attackMove(List<List<Integer>> mapWalls, List playerCoordinates, int npcX, int npcY) {
        Cell moveTo;
        if (npcCanSeePlayer(mapWalls, playerCoordinates, npcX, npcY)) {
            moveTo = getEmptyCellCloserToPlayer(playerCoordinates, npcX, npcY);
        }
        else {
            moveTo = findRandomEmptyNeighbouringCell();
        }
        takeStep(moveTo);
    }

    @Override
    public boolean npcCanSeePlayer(List<List<Integer>> mapWalls, List playerCoordinate, int npcX, int npcY) {
        if (npcPlayerDegree(playerCoordinate, npcX, npcY) == 999) {
            return false;
        }
        else if(necessaryWallsFinder(mapWalls, playerCoordinate, npcX, npcY).size() == 0) {
            return true;
        }
        else {
            return npcIsSeeingPlayer(mapWalls, playerCoordinate, npcX, npcY);
        }
    }

    @Override
    public void fight(){}

}
