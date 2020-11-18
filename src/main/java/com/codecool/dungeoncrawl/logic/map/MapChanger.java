package com.codecool.dungeoncrawl.logic.map;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;

public class MapChanger {
    private GameMap activeLevel;
    private GameMap storedLevel;
    private int level = 1;

    public MapChanger(GameMap map, GameMap map2) {
        this.activeLevel = map;
        this.storedLevel = map2;
    }

    //TODO: made redundant probably.
    public GameMap changeMap(GameMap currentMap) {
        Player toKeep = currentMap.getPlayer();

        level = (level == 1) ? 2 : 1;
        toKeep.setLevel(level);
        GameMap temp = storedLevel;
        this.storedLevel = currentMap;
        this.activeLevel = temp;

        Cell doorCell = activeLevel.getDoor().getCell();
        toKeep.setCell(doorCell);
        doorCell.setActor(toKeep);
        activeLevel.getDoor().setOpen();
        activeLevel.setPlayer(toKeep);

        return activeLevel;
    }

    public int getLevel() {
        return level;
    }

    public GameMap getStored() { return storedLevel; }

    public GameMap getActive() { return activeLevel; }
}
