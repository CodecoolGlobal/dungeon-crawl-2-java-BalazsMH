package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.InventoryModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import com.codecool.dungeoncrawl.model.PokemonModel;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class GameDatabaseManager {
    private PlayerDao playerDao;
    private GameStateDao gameStateDao;
    private PokemonDao pokemonDao;
    private InventoryDao inventoryDao;

    private PlayerModel playerModel; // this has the database generated ID after first save, gets updated with every save
    private GameState gameStateModel; // this has the database generated ID after first save, gets updated with every save
    private InventoryModel inventoryModel;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        pokemonDao = new PokemonDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource, playerDao);
        inventoryDao = new InventoryDaoJdbc(dataSource);
    }

    public void saveGameState(String map, String storedMap, Date date, String saveName){
        gameStateModel = new GameState(map, storedMap, date, playerModel, saveName);
        gameStateDao.add(gameStateModel);
    }

    public void updateGameState(String map, String storedMap, Date date){
        gameStateModel.setCurrentMap(map);
        gameStateModel.setStoredMap(storedMap);
        gameStateModel.setSavedAt(date);
        gameStateDao.update(gameStateModel);
    }

    public void savePlayer(Player player) {
        playerModel = new PlayerModel(player);
        playerDao.add(playerModel);
    }

    public void updatePlayer(Player player){
        playerModel.setX(player.getX());
        playerModel.setY(player.getY());
        playerModel.setLevel(player.getLevel());
        playerDao.update(playerModel);
    }

    public void savePokemon(Pokemon pokemon){
        PokemonModel model = new PokemonModel(pokemon);
        pokemonDao.add(model, playerModel.getId());
    }

    public void updatePokemon(Pokemon pokemon){
        PokemonModel model = new PokemonModel(pokemon);
        pokemonDao.update(model, playerModel.getId());
    }

    public void saveInventory(Inventory inventory){
        inventoryModel = new InventoryModel(inventory);
        inventoryModel.setPlayerId(playerModel.getId());
        inventoryDao.add(inventoryModel);
    }

    public void updateInventory(Inventory inventory) {
        inventoryModel.setHealthPotionNumber(inventory.getHealthPotionNumber());
        inventoryModel.setPokeBallNumber(inventory.getPokeBallNumber());
        inventoryModel.setKey(inventory.hasKey());
        inventoryModel.setActivePokemonId(inventory.getActivePokemon().getPokeId());
        inventoryDao.update(inventoryModel);
    }

    public PokemonModel getPokemon(int id){
        return pokemonDao.get(id);
    }

    public List<PokemonModel> getAllPokemon(){
        return pokemonDao.getAll();
    }

    public List<GameState> getSaves(){
        return gameStateDao.getAll();
    }

    public void loadGame(String playerName, String saveName) {
        List<GameState> saves = gameStateDao.getAll();
        gameStateModel = saves.stream()
                .filter(g -> g.getPlayerName().equals(playerName) && g.getSaveName().equals(saveName))
                .collect(Collectors.toList()).get(0);
        playerModel = gameStateModel.getPlayer();
    }


    private DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = System.getenv("dbName");
        String user = System.getenv("user");
        String password = System.getenv("password");

        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");

        return dataSource;
    }
    private static DataSource connect2() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = System.getenv("dbName");
        String user = System.getenv("user");
        String password = System.getenv("password");

        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");

        return dataSource;
    }
}
