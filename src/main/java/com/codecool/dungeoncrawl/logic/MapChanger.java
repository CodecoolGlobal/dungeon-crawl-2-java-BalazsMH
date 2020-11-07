package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;

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

        Player player = activeLevel.getPlayer();
        Cell doorCell = activeLevel.getDoor().getCell();
        player.getCell().setActor(null);
        player.setCell(doorCell);
        doorCell.setActor(player);


        return activeLevel;
    }



}
