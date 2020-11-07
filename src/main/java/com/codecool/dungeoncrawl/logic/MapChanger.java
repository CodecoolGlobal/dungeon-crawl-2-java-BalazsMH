package com.codecool.dungeoncrawl.logic;

public class MapChanger {
    private GameMap activeLevel;
    private GameMap storedLevel;

    public MapChanger(GameMap map, GameMap map2) {
        this.activeLevel = map;
        this.storedLevel = map2;
    }

    public GameMap changeMap(GameMap currentMap) {
        GameMap temp = storedLevel;
        this.storedLevel = currentMap;
        this.activeLevel = temp;

        return activeLevel;
    }



}
