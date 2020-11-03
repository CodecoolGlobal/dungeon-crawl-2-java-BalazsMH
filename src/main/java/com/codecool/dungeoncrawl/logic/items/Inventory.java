package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Slowpoke;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private int healthPotionNumber = 0;
    private int damageImprovementNumber = 0;
    private List<PokeBall> pokeBallList = new ArrayList<PokeBall>();
    private List<Pokemon> pokemonList = new ArrayList<Pokemon>();

    public Inventory(){
        pokemonList.add(new Slowpoke("Slowpoke"));
        pokeBallList.add(new PokeBall());
        pokeBallList.add(new PokeBall());
        pokeBallList.add(new PokeBall());
    }

    @Override
    public String toString() {
        return "health potion = " + healthPotionNumber + "\n" +
                "damage improvement = " + damageImprovementNumber + "\n" +
                "Pokeball number = " + pokeBallList.size() + "\n" +
                "pokemon number = " + pokemonList.size() + "\n";
    }
}
