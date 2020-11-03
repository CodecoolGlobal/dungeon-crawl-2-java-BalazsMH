package com.codecool.dungeoncrawl.logic.items;

public class PokeBall {
    private final int catchRate;

    public PokeBall() {
        this.catchRate = (int) Math.floor(Math.random()*(8 - 2) + 2);
    }

    public int getCatchRate() { return catchRate; }
}
