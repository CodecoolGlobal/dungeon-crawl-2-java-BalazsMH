package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Slowpoke;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Inventory {
    private int healthPotionNumber = 0;
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

    public void increaseHealthPotionNumber(int potionNumber) {
        this.healthPotionNumber += potionNumber;
    }

    public void addPokeBalls(List<PokeBall> newPokeBallList) {
        pokeBallList.addAll(newPokeBallList);
    }

    public void addPokemon(Pokemon pokemon){
        pokemonList.add(pokemon);
    }

    public void activePokemonDies(){
        pokemonList.remove(activePokemon);
        if (pokemonList.size() > 0) {
            activePokemon = pokemonList.get(0);
        }
        else activePokemon = null;
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
        StringBuilder str = new StringBuilder(",");
        String list = String.join(", ", pokemonList.stream().filter(p -> p != activePokemon).map(p -> p.getPokeName()).collect(Collectors.toList()));
        return "Active: " + activePokemon.toString() + "\n" +
                "Health potion: " + healthPotionNumber + "\n" +
                "Pokeball number: " + pokeBallList.size() + "\n" +
                "Other Pokemons: " + list + "\n";
    }

    public void heal() {
        if (healthPotionNumber > 0) activePokemon.setPokeHealth(activePokemon.getPokeHealth() + 1);
    }
}
