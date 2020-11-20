package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.RocketGrunt;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Converter {
    private final GameMap map1;
    private final GameMap map2;
    private GameMap active;
    private GameMap stored;
    private Player player;
    private Inventory inventory;
    private PlayerModel playerModel; // this has the database generated ID after first save, gets updated with every save
    private GameState gameStateModel; // this has the database generated ID after first save, gets updated with every save
    private final List<Pokemon> pokemonList = new ArrayList<>();
    private final GameDatabaseManager manager;
    private String saveNameStored;

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

    public void run(String mode, String saveName, String playerName) {
        if (mode.equals("save")) save(saveName);
        else update(saveName, playerName);
    }

    private void save(String saveName) {
        saveNameStored = saveName;
        extractDataFromMap();
        playerModel = manager.savePlayer(player);
        gameStateModel = manager.saveGameState(
                active.layoutToString(),
                stored.layoutToString(),
                new Date(System.currentTimeMillis()),
                playerModel,
                saveName);
        for (Pokemon pokemon : pokemonList) manager.savePokemon(pokemon, playerModel.getId());
    }

    public void update(String saveName, String playerName) {
        if (! saveName.equals(saveNameStored)){
            saveNameStored = saveName;
            loadPreviousGame(playerName, saveName);
        }
        manager.updatePlayer(player, playerModel);
        sortMaps();
        manager.updateGameState(active.layoutToString(),
                stored.layoutToString(),
                new Date(System.currentTimeMillis()),
                playerModel,
                gameStateModel);
        pokemonList.forEach(p -> manager.updatePokemon(p, playerModel.getId()));
    }

    private void extractDataFromMap() {
        pokemonList.clear();
        player = (map1.getPlayer() != null)? map1.getPlayer() : map2.getPlayer();
        sortMaps();
        inventory = player.getInventory();
        getPokemonFromField(map1);
        getPokemonFromField(map2);
        getPokemonFromInventory();
        getRocketPokemon();
    }

    private void sortMaps() {
        active = (player.getLevel() == 1)? map1 : map2;
        stored = (player.getLevel() == 1)? map2 : map1;
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
        rocketGrunt.getRocketPokemonList().forEach(p -> pokemonList.add(p));
    }

    public boolean ifPlayerSaveExists(String saveName, String playerName) {
        List<GameState> gameStates = manager.getSaves();
        List<GameState> previous = gameStates.stream()
                .filter(g -> g.getPlayerName().equals(playerName) && g.getSaveName().equals(saveName))
                .collect(Collectors.toList());
        return previous.size() != 0;
    }

    private void loadPreviousGame(String playerName, String saveName){
        /** I use this to create the gameStateModel and playerModel objects, so I have access to the database ID-s
         * Only used when player doesn't start game by loading prior game, but then chooses to overwrite a prior game*/
        extractDataFromMap();
        gameStateModel = manager.getSaves().stream()
                .filter(g -> g.getPlayerName().equals(playerName) && g.getSaveName().equals(saveName))
                .collect(Collectors.toList()).get(0);
        playerModel = gameStateModel.getPlayer();
    }
}
