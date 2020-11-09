package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;

import java.util.Arrays;

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
        String[] developers = new String[]{"Fruzsi", "Dani", "Peti", "Balázs"};


        Player player = activeLevel.getPlayer();
        Cell doorCell = activeLevel.getDoor().getCell();
        player.getCell().setActor(null);
        player.setCell(doorCell);
        if (Arrays.asList(developers).contains(storedLevel.getPlayer().getUserName())) {
            player.setSuperUser(true);
        }
        doorCell.setActor(player);
        activeLevel.getDoor().setOpen();

        return activeLevel;
    }
}
