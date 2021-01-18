package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.RocketGrunt;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.logic.items.LootBox;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.model.*;
import com.google.gson.Gson;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Converter {
    private GameMap map1;
    private GameMap map2;
    private GameMap active;
    private GameMap stored;
    private Player player;
    private Inventory inventory;
    private final List<Pokemon> pokemonList = new ArrayList<>();
    private final GameDatabaseManager manager;
    private GameSerialization serializaton;
    private String saveNameStored;
    private List<LootBox> lootBoxes = new ArrayList<>();

    public Converter(){
        manager = new GameDatabaseManager();
        serializaton = new GameSerialization();
        try {
            manager.setup();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void save(String saveName) {
        saveNameStored = saveName;
        extractDataFromMap();
        manager.savePlayer(player);
        manager.saveGameState(active.layoutToString(), stored.layoutToString(), new Date(System.currentTimeMillis()), saveName);
        manager.saveInventory(inventory);
        for (Pokemon pokemon : pokemonList) manager.savePokemon(pokemon);
        for (LootBox lootBox : lootBoxes) manager.saveLootbox(lootBox);
    }

    public void update(String saveName, String playerName) {
        if (! saveName.equals(saveNameStored)){
            saveNameStored = saveName;
            loadPreviousGame(playerName, saveName);
        }
        manager.updatePlayer(player);
        sortMaps();
        manager.updateGameState(active.layoutToString(), stored.layoutToString(), new Date(System.currentTimeMillis()));
        manager.updateInventory(inventory);
        pokemonList.forEach(manager::updatePokemon);
        manager.updateLootboxes(lootBoxes);
    }

    public void export(Stage stage){
        extractDataFromMap();
        GameState gameState = new GameState(active.layoutToString(), stored.layoutToString(), new Date(System.currentTimeMillis()),
                new PlayerModel(player), new InventoryModel(inventory), null);
        gameState.setPokemonModelList(pokemonList.stream().map(p -> new PokemonModel(p)).collect(Collectors.toList()));
        gameState.setLootBoxModelList(lootBoxes.stream().map(l -> new LootBoxModel(l)).collect(Collectors.toList()));
        Gson gson = new Gson();
        serializaton.onExportPressed(stage, gson.toJson(gameState));
    }

    private void extractDataFromMap() {
        pokemonList.clear();
        lootBoxes.clear();
        player = (map1.getPlayer() != null)? map1.getPlayer() : map2.getPlayer();
        sortMaps();
        inventory = player.getInventory();
        getPokemonAndLootboxFromField(map1);
        getPokemonAndLootboxFromField(map2);
        getPokemonFromInventory();
        getRocketPokemon();
    }


    private void sortMaps() {
        active = (player.getLevel() == 1)? map1 : map2;
        stored = (player.getLevel() == 1)? map2 : map1;
    }

    private void getPokemonAndLootboxFromField(GameMap map){
        for (Cell[] row : map.getCells()){
            for (Cell cell : row){
                if (cell.getPokemon() != null) pokemonList.add(cell.getPokemon());
                if (cell.getItem() instanceof LootBox) lootBoxes.add((LootBox) cell.getItem());
            }
        }
    }

    private void getPokemonFromInventory() {
        pokemonList.addAll(player.getInventory().getAllPokemon());
    }

    private void getRocketPokemon(){
        RocketGrunt rocketGrunt = (map1.getRocketGrunt() != null)? map1.getRocketGrunt() : map2.getRocketGrunt();
        pokemonList.addAll(rocketGrunt.getRocketPokemonList());
    }

    public boolean ifPlayerSaveExists(String saveName, String playerName) {
        return manager.findPlayerId(playerName, saveName) != null;
    }

    /** I use this to create the gameStateModel and playerModel objects, so I have access to the database ID-s
     * Only used when player doesn't start game by loading prior game, but then chooses to overwrite a prior game*/
    private void loadPreviousGame(String playerName, String saveName){
        extractDataFromMap(); // sets up objects in Converter
        Integer playerId = manager.findPlayerId(playerName, saveName);
        manager.loadGame(playerId, saveName); // sets up models in GameDatabaseManager
    }

    public List<GameState> getAllSaves(){
        return manager.getSaves();
    }

    public GameState returnFullGameStateOf(int gameId){
        manager.loadGame(gameId);
        return manager.getGameStateModel();
    }

    public Player getPlayer() {
        return player;
    }

    public List<LootBox> getLootBoxes() {
        return lootBoxes;
    }


    public void setMap1(GameMap map1){ this.map1 = map1; }
    public void setMap2(GameMap map2){ this.map2 = map2; }
}
