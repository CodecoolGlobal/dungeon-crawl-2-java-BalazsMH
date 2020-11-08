package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

import java.util.List;

public class Skeleton extends Actor {

    public Skeleton(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }

    @Override
    public Cell getCell() {
        return super.getCell();
    }


    public void move(List playerCoordinates, int npcX, int npcY, boolean npcCanSee) {
        if (npcCanSee) {
            Cell moveTo = getEmptyCellCloserToPlayer(playerCoordinates, npcX, npcY);
            takeStep(moveTo);
        }
    }

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
