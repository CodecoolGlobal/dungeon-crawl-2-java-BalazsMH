package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class RocketGrunt extends Actor {
    public RocketGrunt(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "rocketGrunt";
    }
}
