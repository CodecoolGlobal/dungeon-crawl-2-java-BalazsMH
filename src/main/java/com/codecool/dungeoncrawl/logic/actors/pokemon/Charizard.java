package com.codecool.dungeoncrawl.logic.actors.pokemon;


import com.codecool.dungeoncrawl.logic.Cell;

import java.util.List;

public class Charizard extends Pokemon {
    double seeingDistance = 10.0;
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
    public void attackMove(List<List<Integer>> mapWalls, List playerCoordinates, int npcX, int npcY) {
        Cell moveTo;
        double distance = distanceBetweenPlayerAndNpc(playerCoordinates, npcX, npcY);
        if (distance < this.seeingDistance && getCellCloserToPlayer(playerCoordinates, npcX, npcY) != null) {
            moveTo = getCellCloserToPlayer(playerCoordinates, npcX, npcY);
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
