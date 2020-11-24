package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.LootBoxModel;


public interface LootBoxDao {
    void add(LootBoxModel lootBox);
    void update(LootBoxModel lootBox);
    LootBoxModel get(int id);
//    List<InventoryModel> getAll();
}
