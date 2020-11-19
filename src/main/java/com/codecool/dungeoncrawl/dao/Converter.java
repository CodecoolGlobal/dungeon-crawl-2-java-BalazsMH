package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.RocketGrunt;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.model.PlayerModel;

import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Converter {
    private GameMap map1;
    private GameMap map2;
    private Date date;
    private Player player;
    private PlayerModel playerModel; // this has the database generated ID
    private List<Pokemon> pokemonList = new ArrayList<>();
    private GameDatabaseManager manager;

    public Converter(GameMap map1, GameMap map2){
        this.map1 = map1;
        this.map2 = map2;
        manager = new GameDatabaseManager();
        try {
            manager.setup();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void run(String mode) {
        if (mode.equals("save")) save();
        else update();
    }

    private void save() {
        extractDataFromMap();
        playerModel = manager.savePlayer(player);
        date = new Date(System.currentTimeMillis());
        GameMap active = (player.getLevel() == 1)? map1 : map2;
        GameMap stored = (player.getLevel() == 1)? map2 : map1;
        manager.saveGameState(active.layoutToString(), stored.layoutToString(), date, playerModel);
        for (Pokemon pokemon : pokemonList) manager.savePokemon(pokemon);
    }

    public void update() {
        date = new Date(System.currentTimeMillis());
        manager.updatePlayer(player);
        GameMap active = (player.getLevel() == 1)? map1 : map2;
        GameMap stored = (player.getLevel() == 1)? map2 : map1;
        manager.updateGameState(active.layoutToString(), stored.layoutToString(), date, playerModel);
        pokemonList.forEach(p -> manager.updatePokemon(p));
    }

    private void extractDataFromMap() {
        player = map1.getPlayer();
        getPokemonFromField(map1);
        getPokemonFromField(map2);
        getPokemonFromInventory();
        getRocketPokemon();
    }

    private void getPokemonFromField(GameMap map){
        for (Cell[] row : map.getCells()){
            for (Cell cell : row){
                if (cell.getPokemon() != null) pokemonList.add(cell.getPokemon());
            }
        }
    }

    private void getPokemonFromInventory() {
        player.getInventory().getAllPokemon().forEach(p -> pokemonList.add(p));
    }
    private void getRocketPokemon(){
        RocketGrunt rocketGrunt = (map1.getRocketGrunt() != null)? map1.getRocketGrunt() : map2.getRocketGrunt();
        rocketGrunt.getRocketPokemonOnBoard().forEach(p -> pokemonList.add(p));
        rocketGrunt.getRocketPokemonList().forEach(p -> pokemonList.add(p));
    }

    public boolean ifPlayerExists(Player player) {
        if (manager.getPlayerByName(player) != null) return true;
        return false;
    }
}
