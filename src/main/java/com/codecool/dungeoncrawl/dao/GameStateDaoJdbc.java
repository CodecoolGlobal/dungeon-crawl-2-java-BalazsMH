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

    }

    @Override
    public void update(GameState state) {

    }

    @Override
    public GameState get(int id) {
        return null;
    }

    @Override
    public List<GameState> getAll() {
        List<GameState> output = new ArrayList<GameState>();

        try (Connection conn = dataSource.getConnection()) {
            String query = "SELECT gs.*, p.player_name, p.x, p.y FROM game_state gs INNER JOIN player p on p.id = gs.player_id";
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                GameState row = new GameState(rs.getString("current_map"),
                                              rs.getDate("saved_at"),
                                              new PlayerModel(rs.getString("player_name"),
                                                            rs.getInt("x"),
                                                            rs.getInt("y")));
                output.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return output;
    }
}
