package com.codecool.dungeoncrawl.logic.actors.pokemon;


import com.codecool.dungeoncrawl.logic.Cell;

import java.util.List;

public class Slowpoke extends Pokemon {
    private boolean shouldMove = true;
    double seeingDistance = 5.0;

    public Slowpoke(Cell cell, String name){ super(cell, name); }
    public Slowpoke(String name){ super(name); }

    @Override
    public String getTileName(){return "slowpoke";}

    @Override
    public void move(){ }

    @Override
    public void attackMove(List<List<Integer>> mapWalls, List playerCoordinates, int npcX, int npcY) {
        Cell moveTo;
        double distance = distanceBetweenPlayerAndNpc(playerCoordinates, npcX, npcY);
        if (npcCanSeePlayer(mapWalls, playerCoordinates, npcX, npcY) && distance < this.seeingDistance) {
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
}
