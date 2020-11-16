package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PokemonModel;

import java.util.List;

public interface PokemonDao {
    void add(PokemonModel pokemon, int playerId);
    void update(PokemonModel pokemon);
    PokemonModel get(int id);
    List<PokemonModel> getAll();
}
