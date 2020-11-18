package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class PlayerDaoJdbc implements PlayerDao {
    private DataSource dataSource;

    public PlayerDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO player (player_name, god_mode, x, y, game_level) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, player.getPlayerName());
            statement.setBoolean(2, player.getGodMode());
            statement.setInt(3, player.getX());
            statement.setInt(4, player.getY());
            statement.setInt(5, player.getLevel());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            player.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(PlayerModel player) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE player SET x = ?, y = ?, game_level = ? WHERE player_name = ?");
            ps.setInt(1, player.getX());
            ps.setInt(2, player.getY());
            ps.setString(3, player.getPlayerName());
            ps.setInt(4, player.getLevel());
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public PlayerModel get(int id) {
        return null;
    }

    @Override
    public PlayerModel getByName(String name){
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM player WHERE player_name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (! rs.next()) return null;
            PlayerModel playerModel = new PlayerModel(rs.getString(2), rs.getInt(4), rs.getInt(5), rs.getInt(6));
            return playerModel;
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<PlayerModel> getAll() {
        return null;
    }
}
