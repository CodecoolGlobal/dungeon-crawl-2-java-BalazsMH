package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.RocketGrunt;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.map.GameMap;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Converter {
    private GameMap active;
    private GameMap stored;
    private java.util.Date date;
    private Player player;
    private List<Pokemon> pokemonList = new ArrayList<>();
    private GameDatabaseManager manager;

    public Converter(GameMap active, GameMap stored){
        this.active = active;
        this.stored = stored;
        date = new java.util.Date();
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

    private void extractDataFromMap() {
        player = active.getPlayer();
        getPokemonFromField(active);
        getPokemonFromField(stored);
        getPokemonFromInventory();
        getRocketPokemon();
    }


    private void save() {
        extractDataFromMap();
        manager.savePlayer(player);
        for (Pokemon pokemon : pokemonList) manager.savePokemon(pokemon);
    }

    public void update() {
        manager.updatePlayer(player);
        pokemonList.forEach(p -> manager.updatePokemon(p));
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
        RocketGrunt rocketGrunt = (active.getRocketGrunt() != null)? active.getRocketGrunt() : stored.getRocketGrunt();
        rocketGrunt.getRocketPokemonOnBoard().forEach(p -> pokemonList.add(p));
        rocketGrunt.getRocketPokemonList().forEach(p -> pokemonList.add(p));
    }

    public boolean ifPlayerExists(Player player) {
        if (manager.getPlayerByName(player) != null) return true;
        return false;
    }
}
