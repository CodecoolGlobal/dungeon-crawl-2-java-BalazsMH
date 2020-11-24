package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.LootBoxModel;

import javax.sql.DataSource;
import java.sql.*;

public class LootBoxDaoJdbc implements LootBoxDao {
    private DataSource dataSource;

    public LootBoxDaoJdbc(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void add(LootBoxModel lootBox) {
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO lootbox (player_id, health_potion_number, poke_ball_number, x, y, level, lootbox_id)" +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, lootBox.getPlayerId());
            ps.setInt(2, lootBox.getHealthPotionNumber());
            ps.setInt(3, lootBox.getPokeBallNumber());
            if (lootBox.getX() != null){
                ps.setInt(4, lootBox.getX());
                ps.setInt(5, lootBox.getY());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setInt(6, lootBox.getLevel());
            ps.setInt(7, lootBox.getLootBoxId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            lootBox.setId(rs.getInt(1));
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(LootBoxModel lootBox) {

    }

    @Override
    public LootBoxModel get(int id) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM lootbox WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (! rs.next()) return null;
            LootBoxModel model = new LootBoxModel(
                    rs.getInt("player_id"),
                    rs.getInt("health_potion_number"),
                    rs.getInt("poke_ball_number"),
                    (rs.getObject("x") != null) ? rs.getInt("x") : null,
                    (rs.getObject("y") != null) ? rs.getInt("y") : null,
                    rs.getInt("level"),
                    rs.getInt("lootbox_id"));
            model.setId(rs.getInt("id"));
            return model;
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
