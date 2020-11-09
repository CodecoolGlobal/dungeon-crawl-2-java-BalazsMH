package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;

public class PokeBall {
    private final int catchRate;

    public PokeBall() {
        this.catchRate = (int) Math.floor(Math.random()*(8 - 3) + 3);
    }

    public int getCatchRate() { return catchRate; }

    public boolean hasCaught(Pokemon toCatch){
        if (toCatch.getPokeHealth() <= 0) return true;
        if (Math.random() <= catchRate/10.0) return true;
        return false;
    }
}
