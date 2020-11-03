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
        switch (this.facing) {
            case "down":
                return "player_down";
            case "up":
                return "player_up";
            case "right":
                return "player_right";
            default:
                return "player_left";
        }

    }

    public void pickupItem() {
    }
}
