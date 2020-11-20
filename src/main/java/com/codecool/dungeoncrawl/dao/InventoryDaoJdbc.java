package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.InventoryModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class InventoryDaoJdbc implements InventoryDao {

    private DataSource dataSource;

    public InventoryDaoJdbc(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void add(InventoryModel inventory) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO inventory (player_id, health_potion_number, poke_ball_number, has_key, active_pokemon_id)" +
                            "VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, inventory.getPlayerId());
            ps.setInt(2, inventory.getHealthPotionNumber());
            ps.setInt(3, inventory.getPokeBallNumber());
            ps.setBoolean(4, inventory.hasKey());
            ps.setInt(5, inventory.getActivePokemonId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            inventory.setId(rs.getInt(1));
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void update(InventoryModel inventory) {
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement("UPDATE inventory " +
                    "SET health_potion_number = ?, poke_ball_number = ?, has_key = ?, active_pokemon_id = ?" +
                    "WHERE player_id = ?");
            ps.setInt(1, inventory.getHealthPotionNumber());
            ps.setInt(2, inventory.getPokeBallNumber());
            ps.setBoolean(3, inventory.hasKey());
            ps.setInt(4, inventory.getActivePokemonId());
            ps.setInt(5, inventory.getPlayerId());
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public InventoryModel get(int id) {
        return null;
    }

    @Override
    public List<InventoryModel> getAll() {
        return null;
    }
}
