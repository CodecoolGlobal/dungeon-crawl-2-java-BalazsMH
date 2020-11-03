package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Player extends Actor {
    private String facing = "down";

    public Player(Cell cell) {
        super(cell);
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }

    public String getTileName() {
        return "player";
    }
    public void pickupItem() {
    }
}
