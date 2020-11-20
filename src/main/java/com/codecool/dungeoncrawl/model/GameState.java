package com.codecool.dungeoncrawl.model;

import java.sql.Date;

public class GameState extends BaseModel {
    private Date savedAt;
    private String active;
    private String stored;
    private PlayerModel player;
    private String playerName;
    private String saveName;

    public GameState(String currentMap, String storedMap, Date savedAt, PlayerModel player, String saveName) {
        this.active = currentMap;
        this.stored = storedMap;
        this.savedAt = savedAt;
        this.player = player;
        this.playerName = player.getPlayerName();
        this.saveName = saveName;
    }

    public Date getSavedAt() {
        return savedAt;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getSaveName() {
        return saveName;
    }

    public void setSavedAt(Date savedAt) {
        this.savedAt = savedAt;
    }

    public String getCurrentMap() {
        return active;
    }

    public void setCurrentMap(String currentMap) {
        this.active = currentMap;
    }

    public String getStoredMap() {
        return stored;
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
        sb.append("currentMap:").append(this.active).append(",");
        sb.append("playerName:").append(this.player.getPlayerName()).append(",");
        sb.append("x:").append(this.player.getX()).append(",");
        sb.append("y:").append(this.player.getY());

        return sb.toString();
    }
}
