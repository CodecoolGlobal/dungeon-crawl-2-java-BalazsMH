package com.codecool.dungeoncrawl.dao.daoInterface;

import com.codecool.dungeoncrawl.model.PlayerModel;

import java.util.List;

public interface PlayerDao {
    void add(PlayerModel player);
    void update(PlayerModel player);
    PlayerModel get(int id);
    List<PlayerModel> getAll();
    List<PlayerModel> getByPlayerName(String playerName);
}
