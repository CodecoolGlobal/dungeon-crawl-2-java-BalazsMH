package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PokemonModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PokemonDaoJdbc implements PokemonDao{

    DataSource dataSource;

    public PokemonDaoJdbc(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void add(PokemonModel pokemon, int playerId) {
        // I have changed method signature to include playerId - not sure if this is the best solution
        try(Connection conn = dataSource.getConnection()){
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO pokemon (player_id, pokeid, game_level, pokehealth, pokedamage, pokename, x, y, celltype) " +
                       "values (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, playerId);
            ps.setInt(2, pokemon.getPokeId());
            ps.setInt(3, pokemon.getGameLevel());
            ps.setInt(4, pokemon.getPokeHealth());
            ps.setInt(5, pokemon.getPokeDamage());
            ps.setString(6, pokemon.getPokeName());
            if (pokemon.getX() != null) {
                ps.setInt(7, pokemon.getX());
                ps.setInt(8, pokemon.getY());
                ps.setString(9, pokemon.getCellType());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
                ps.setNull(8, java.sql.Types.INTEGER);
                ps.setString(9, null);
            }
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            pokemon.setId(rs.getInt(1));
            pokemon.setPlayerId(playerId);
        } catch (SQLException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("Pokemon could not be saved in database");
        }

    }

    @Override
    public void update(PokemonModel pokemon, int playerId) {
        // I have changed method signature to include playerId - not sure if this is the best solution
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE pokemon SET game_level = ?, pokehealth = ?, pokedamage = ?, x = ?, y = ?, cellType = ?" +
                            "WHERE player_id = ? AND pokeid = ?");

            ps.setInt(1, pokemon.getGameLevel());
            ps.setInt(2, pokemon.getPokeHealth());
            ps.setInt(3, pokemon.getPokeDamage());
            if (pokemon.getX() != null) {
                ps.setInt(4, pokemon.getX());
                ps.setInt(5, pokemon.getY());
                ps.setString(6, pokemon.getCellType());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
                ps.setNull(5, java.sql.Types.INTEGER);
                ps.setString(6, null);
            }
            ps.setInt(7, playerId);
            ps.setInt(8, pokemon.getPokeId());
            ps.executeUpdate();
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

    @Override
    public PokemonModel get(int id) {
        try(Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM pokemon WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (! rs.next()) return null;
            PokemonModel pokemonModel = createPokemonModel(rs);
            return pokemonModel;
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public List<PokemonModel> getAll() {
        List<PokemonModel> pokemonList = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()){
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM pokemon");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) pokemonList.add(createPokemonModel(rs));
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return pokemonList;
    }

    private PokemonModel createPokemonModel(ResultSet rs) throws SQLException {
        String cellType = rs.getString(10);
        Integer x = (cellType == null)? null : rs.getInt(8);
        Integer y = (cellType == null)? null : rs.getInt(9);
        PokemonModel model = new PokemonModel(rs.getInt(3), rs.getInt(4), rs.getInt(5),rs.getInt(6),
                rs.getString(7), x, y, cellType);
        model.setId(rs.getInt(1));
        model.setPlayerId(rs.getInt(2));
        return model;
    }
}
