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

public class GameDatabaseManager {
    private PlayerDao playerDao;
    private GameStateDao gameStateDao;
    private PokemonDao pokemonDao;
    private InventoryDao inventoryDao;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        pokemonDao = new PokemonDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource, playerDao);
        inventoryDao = new InventoryDaoJdbc(dataSource);
    }

    public GameState saveGameState(String map, String storedMap, Date date, PlayerModel player, String saveName){
        GameState gameStateModel = new GameState(map, storedMap, date, player, saveName);
        gameStateDao.add(gameStateModel);
        return gameStateModel;
    }

    public void updateGameState(String map, String storedMap, Date date, PlayerModel player, GameState gameStateModel){
        gameStateModel.setCurrentMap(map);
        gameStateModel.setStoredMap(storedMap);
        gameStateModel.setSavedAt(date);
        gameStateDao.update(gameStateModel);
    }

    public PlayerModel savePlayer(Player player) {
        PlayerModel model = new PlayerModel(player);
        playerDao.add(model);
        return model;
    }

    public void updatePlayer(Player player, PlayerModel playerModel){
        playerModel.setX(player.getX());
        playerModel.setY(player.getY());
        playerModel.setLevel(player.getLevel());
        playerDao.update(playerModel);
    }

    public void savePokemon(Pokemon pokemon, int playerId){
        PokemonModel model = new PokemonModel(pokemon);
        pokemonDao.add(model, playerId);
    }

    public void updatePokemon(Pokemon pokemon, int playerId){
        PokemonModel model = new PokemonModel(pokemon);
        pokemonDao.update(model, playerId);
    }

    public void saveInventory(Inventory inventory, int playerId){
        InventoryModel inventoryModel = new InventoryModel(inventory);
        inventoryDao.add(inventoryModel, playerId);
    }

    public PokemonModel getPokemon(int id){
        return pokemonDao.get(id);
    }

    public List<PokemonModel> getAllPokemon(){
        return pokemonDao.getAll();
    }

    public List<GameState> getSaves(){
        List<GameState> saves = gameStateDao.getAll();

        return saves;
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
}
