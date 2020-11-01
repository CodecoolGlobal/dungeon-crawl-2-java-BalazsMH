package com.codecool.dungeoncrawl.logic.items;

public class PokeBall {
    private final int catchRate;

    public PokeBall() {
        this.catchRate = (int) Math.floor(Math.random()*10.00);
    }


}
