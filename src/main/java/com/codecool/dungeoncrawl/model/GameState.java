package com.codecool.dungeoncrawl.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class GameState extends BaseModel {
    private Date savedAt;
    private String currentMap;
    private List<String> discoveredMaps = new ArrayList<>();
    private PlayerModel player;
    private String playerName;

    public GameState(String currentMap, Date savedAt, PlayerModel player) {
        this.currentMap = currentMap;
        this.savedAt = savedAt;
        this.player = player;
        this.playerName = player.getPlayerName();
    }

    public Date getSavedAt() {
        return savedAt;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setSavedAt(Date savedAt) {
        this.savedAt = savedAt;
    }

    public String getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(String currentMap) {
        this.currentMap = currentMap;
    }

    public List<String> getDiscoveredMaps() {
        return discoveredMaps;
    }

    public void addDiscoveredMap(String map) {
        this.discoveredMaps.add(map);
    }

    public PlayerModel getPlayer() {
        return player;
    }

    public void setPlayer(PlayerModel player) {
        this.player = player;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("savedAt:").append(this.savedAt).append(",");
        sb.append("currentMap:").append(this.currentMap).append(",");
        sb.append("discoveredMaps:").append(this.discoveredMaps).append(",");
        sb.append("playerName:").append(this.player.getPlayerName()).append(",");
        sb.append("x:").append(this.player.getX()).append(",");
        sb.append("y:").append(this.player.getY());

        return sb.toString();
    }
}
