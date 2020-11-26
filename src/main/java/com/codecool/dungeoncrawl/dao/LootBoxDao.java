package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.LootBoxModel;

import java.util.List;


public interface LootBoxDao {
    void add(LootBoxModel lootBox);
    void update(LootBoxModel lootBox);
    LootBoxModel get(int id);
    List<LootBoxModel> getAll();
    List<LootBoxModel> getAllForPlayerId(int playerId);
}
