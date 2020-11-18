package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
import com.codecool.dungeoncrawl.model.PokemonModel;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class GameDatabaseManager {
    private PlayerDao playerDao;
    private GameStateDao gameStateDao;
    private PokemonDao pokemonDao;
    private int playerId;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        pokemonDao = new PokemonDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource);
    }

    public void savePlayer(Player player) {
        PlayerModel model = new PlayerModel(player);
        playerDao.add(model);
        playerId = model.getId();
    }

    public void updatePlayer(Player player){
        PlayerModel model = new PlayerModel(player);
        playerDao.update(model);
    }

    public PlayerModel getPlayerByName(Player player){
        return playerDao.getByName(player.getUserName());
    }

    public void savePokemon(Pokemon pokemon){
        PokemonModel model = new PokemonModel(pokemon);
        pokemonDao.add(model, playerId);
    }

    public void updatePokemon(Pokemon pokemon){
        PokemonModel model = new PokemonModel(pokemon);
        pokemonDao.update(model, playerId);
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
