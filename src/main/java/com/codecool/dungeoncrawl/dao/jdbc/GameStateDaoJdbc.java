package com.codecool.dungeoncrawl.dao.jdbc;

import com.codecool.dungeoncrawl.dao.daoInterface.GameStateDao;
import com.codecool.dungeoncrawl.dao.daoInterface.InventoryDao;
import com.codecool.dungeoncrawl.dao.daoInterface.PlayerDao;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.InventoryModel;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {
    private DataSource dataSource;
    private PlayerDao playerDao;
    private InventoryDao inventoryDao;

    public GameStateDaoJdbc(DataSource dataSource, PlayerDao playerDao, InventoryDao inventoryDao) {
        this.dataSource = dataSource;
        this.playerDao = playerDao;
        this.inventoryDao = inventoryDao;
    }


    @Override
    public void add(GameState state) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO game_state (current_map, stored_map, saved_at, player_id, save_name)" +
                            "VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, state.getActiveMap());
            ps.setString(2, state.getStoredMap());
            ps.setDate(3, state.getSavedAt());
            ps.setInt(4, state.getPlayer().getId());
            ps.setString(5, state.getSaveName());
            ps.executeUpdate();
            ResultSet resultSet = ps.getGeneratedKeys();
            resultSet.next();
            state.setId(resultSet.getInt(1));
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(GameState state) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE game_state SET current_map = ?, stored_map = ?, saved_at = ? WHERE player_id = ?");
            ps.setString(1, state.getActiveMap());
            ps.setString(2, state.getStoredMap());
            ps.setDate(3, state.getSavedAt());
            ps.setInt(4, state.getPlayer().getId());
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public GameState get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM game_state WHERE id = ?");
            pst.setInt(1, id);
            ResultSet rss = pst.executeQuery();
            if (! rss.next()) return null;
            return createGameState(rss);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameState> getAll() {
        List<GameState> output = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM game_state");
            ResultSet rss = pst.executeQuery();
            while (rss.next()){
                output.add(createGameState(rss));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    public GameState getByPlayerSave(int playerId, String saveName){
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM game_state WHERE player_id = ? AND save_name = ?");
            pst.setInt(1, playerId);
            pst.setString(2, saveName);
            ResultSet rss = pst.executeQuery();
            if (! rss.next()) return null;
            return createGameState(rss);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private GameState createGameState(ResultSet rss) throws SQLException {
        PlayerModel pm = playerDao.get(rss.getInt("player_id"));
        InventoryModel im = inventoryDao.get(rss.getInt("player_id"));
        GameState gs = new GameState(rss.getString("current_map"),
                rss.getString("stored_map"),
                rss.getDate("saved_at"),
                pm,
                im,
                rss.getString("save_name"));
        gs.setId(rss.getInt("id"));
        return gs;
    }
}
