package com.codecool.dungeoncrawl.logic.actors;

public enum Facing {
    UP("player_up"),
    DOWN("player_down"),
    RIGHT("player_right"),
    LEFT("player_left");

    private final String tile;

    Facing(String tile){ this.tile = tile; }
    public String getTileName() { return tile; }
}
