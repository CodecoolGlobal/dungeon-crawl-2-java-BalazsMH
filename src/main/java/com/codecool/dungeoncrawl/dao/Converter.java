package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.model.GameState;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Converter {
    private GameMap active;
    private GameMap stored;
    private java.util.Date date;
    private Player player;
    private List<Pokemon> pokemonsOnActiveLevel = new ArrayList<>();

    public Converter(GameMap active, GameMap stored){
        this.active = active;
        this.stored = stored;
        date = new java.util.Date();
    }

    public void run() {
        extractDataFromMap();
        saveThings();

    }

    private void extractDataFromMap() {
        player = active.getPlayer();
        for (Cell[] row : active.getCells()){
            for (Cell cell : row){
                if (cell.getPokemon() != null) pokemonsOnActiveLevel.add(cell.getPokemon());
            }
        }
    }

    private void saveThings() {
        GameDatabaseManager manager = new GameDatabaseManager();
        try{
            manager.setup();
            manager.savePlayer(player);
            for (Pokemon pokemon : pokemonsOnActiveLevel) manager.savePokemon(pokemon);
        } catch (SQLException e){

        }
    }

}
