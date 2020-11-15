package com.codecool.dungeoncrawl.logic.actors.pokemon;

import com.codecool.dungeoncrawl.logic.Cell;

import java.util.List;

public class Bulbasaur extends Pokemon{

    public Bulbasaur(Cell cell, String name){
        super(cell, name);
    }
    public Bulbasaur(String name){ super(name); }

    @Override
    public String getTileName(){return "bulbasaur";}

    @Override
    public void move(){ }

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
}
