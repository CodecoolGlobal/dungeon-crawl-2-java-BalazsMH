package com.codecool.dungeoncrawl.dao.jdbc;

import com.codecool.dungeoncrawl.dao.daoInterface.InventoryDao;
import com.codecool.dungeoncrawl.model.InventoryModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
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
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM inventory WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (! rs.next()) return null;
            InventoryModel inventoryModel = new InventoryModel(
                    rs.getInt(2),
                    rs.getInt(3),
                    rs.getInt(4),
                    rs.getBoolean(5),
                    rs.getInt(6));
            inventoryModel.setId(rs.getInt(1));
            return inventoryModel;
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    @Override
    public List<InventoryModel> getAll() {
        List<InventoryModel> output = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM inventory");
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                InventoryModel inventoryModel = new InventoryModel(
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getInt(4),
                        rs.getBoolean(5),
                        rs.getInt(6));
                inventoryModel.setId(rs.getInt(1));
                output.add(inventoryModel);
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return output;
    }
}
