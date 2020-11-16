package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PokemonModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class PokemonDaoJdbc implements PokemonDao{

    DataSource dataSource;

    public PokemonDaoJdbc(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void add(PokemonModel pokemon) {
        // we need to know player's id to save pokemons!!!
        try(Connection conn = dataSource.getConnection()){
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO pokemon (player_id, pokehealth, pokedamage, pokename, x, y, celltype) " +
                       "values (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, pokemon.getPokeHealth());
            ps.setInt(2, pokemon.getPokeHealth());
            ps.setInt(3, pokemon.getPokeDamage());
            ps.setString(4, pokemon.getPokeName());
            ps.setInt(5, pokemon.getX());
            ps.setInt(6, pokemon.getY());
            ps.setString(7, pokemon.getCellType());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            pokemon.setId(rs.getInt(1));
        } catch (SQLException e){
            throw new RuntimeException("Pokemon could not be saved in database");
        }

    }

    @Override
    public void update(PokemonModel pokemon) {

    }

    @Override
    public PokemonModel get(int id) {
        return null;
    }

    @Override
    public List<PokemonModel> getAll() {
        return null;
    }
}
