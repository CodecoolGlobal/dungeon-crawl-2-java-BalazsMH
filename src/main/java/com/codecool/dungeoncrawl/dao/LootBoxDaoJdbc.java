package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.LootBoxModel;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LootBoxDaoJdbc implements LootBoxDao {
    private DataSource dataSource;

    public LootBoxDaoJdbc(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void add(LootBoxModel lootBox) {

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
                    (rs.getObject("y") != null) ? rs.getInt("y") : null);
            model.setId(rs.getInt("id"));
            return model;
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
