package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Slowpoke;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Inventory {
    private int healthPotionNumber = 0;
    private int damageImprovementNumber = 0;
    private List<PokeBall> pokeBallList = new ArrayList<PokeBall>();
    private List<Pokemon> pokemonList = new ArrayList<Pokemon>();
    private Key key;
    private Pokemon activePokemon;

    public Inventory(){
        pokemonList.add(new Slowpoke("Slowpoke"));
        activePokemon = pokemonList.get(0);
        pokeBallList.add(new PokeBall());
        pokeBallList.add(new PokeBall());
        pokeBallList.add(new PokeBall());
    }

    public Optional<PokeBall> takePokeBall(){
        if (pokeBallList.size() > 0){
            return Optional.of(pokeBallList.remove(pokeBallList.size()-1));
        } else return Optional.empty();
    }

    public void addPokemon(Pokemon pokemon){
        pokemonList.add(pokemon);
    }

    public void addKey(Cell cell){
        key = (Key)cell.getItem();
    }
    public boolean hasKey(){return key != null;}

    public void changeActivePokemon(){
        int idx = (pokemonList.indexOf(activePokemon) + 1 < pokemonList.size()) ? pokemonList.indexOf(activePokemon) + 1 : 0;
        activePokemon = pokemonList.get(idx);
    }
    public Pokemon getActivePokemon(){return activePokemon;}

    @Override
    public String toString() {
        return "health potion = " + healthPotionNumber + "\n" +
                "damage improvement = " + damageImprovementNumber + "\n" +
                "Pokeball number = " + pokeBallList.size() + "\n" +
                "pokemon number = " + pokemonList.size() + "\n";
    }
}
