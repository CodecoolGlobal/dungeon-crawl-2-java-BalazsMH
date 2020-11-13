package com.codecool.dungeoncrawl.logic.actors.pokemon;

import com.codecool.dungeoncrawl.logic.Cell;

import java.util.List;

public class Koffing extends Pokemon {

    public Koffing(Cell cell, String name){ super(cell, name); }
    public Koffing(String name){ super(name); }

    @Override
    public String getTileName(){return "koffing";}

    @Override
    public void move(){
        /***This pokemon takes one random step every time the player moves  */
        Cell moveTo = findRandomEmptyNeighbouringCell();
        takeStep(moveTo);
    }

    @Override
    public void fight() {

    }

    @Override
    public void attackMove(List<List<Integer>> mapWalls, List playerCoordinates, int npcX, int npcY) {

    }

    @Override
    public boolean npcCanSeePlayer(List<List<Integer>> mapWalls, List playerCoordinate, int npcX, int npcY) {
        return false;
    }
}
