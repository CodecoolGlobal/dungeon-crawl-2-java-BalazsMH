package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {
    private DataSource dataSource;

    public GameStateDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void add(GameState state) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO game_state (current_map, stored_map, saved_at, player_id)" +
                            "VALUES (?,?,?,?)");
            ps.setString(1, state.getCurrentMap());
            ps.setString(2, state.getStoredMap());
            ps.setDate(3, state.getSavedAt());
            ps.setInt(4, state.getPlayer().getId());
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(GameState state) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE game_state SET current_map = ?, stored_map = ?, saved_at = ? WHERE player_id = ?");
            ps.setString(1, state.getCurrentMap());
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
            String query = "SELECT gs.*, p.player_name, p.god_mode, p.x, p.y, p.game_level " +
                    "FROM game_state gs INNER JOIN player p on p.id = gs.player_id";
            ResultSet rs = conn.createStatement().executeQuery(query);
            if (! rs.next()) return null;
            PlayerModel playerModel = createPlayerModel(rs);
            GameState gameState = new GameState(rs.getString("current_map"),
                    rs.getString("stored_map"),
                    rs.getDate("saved_at"),
                    playerModel);
            gameState.setId(rs.getInt("id"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<GameState> getAll() {
        List<GameState> output = new ArrayList<GameState>();

        try (Connection conn = dataSource.getConnection()) {
            String query = "SELECT gs.*, p.player_name, p.god_mode, p.x, p.y, p.game_level " +
                    "FROM game_state gs INNER JOIN player p on p.id = gs.player_id";
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                PlayerModel playerModel = createPlayerModel(rs);
                GameState row = new GameState(rs.getString("current_map"),
                                              rs.getString("stored_map"),
                                              rs.getDate("saved_at"),
                                              playerModel);
                row.setId(rs.getInt("id"));
                output.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    private PlayerModel createPlayerModel(ResultSet rs) throws SQLException {
        PlayerModel playerModel = new PlayerModel(rs.getString("player_name"),
                rs.getBoolean("god_mode"),
                rs.getInt("x"),
                rs.getInt("y"),
                rs.getInt("game_level"));
        playerModel.setId(rs.getInt("player_id"));
        return playerModel;
    }
}
