package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.logic.items.PokeBall;

import java.util.List;
import java.util.Optional;

public class Player extends Actor {
    public Player(Cell cell) {
        super(cell);
    }

    public String getTileName() {
        return "player";
    }
    public void pickupItem() {

    }

    public void throwPokeBall(Inventory inventory, StringBuilder text, Optional<List<Pokemon>> pokemonInRange, GameMap map){
        Optional<PokeBall> currentPB = inventory.takePokeBall();
        List<Pokemon> pokemons = pokemonInRange.get();

        if (currentPB.isEmpty()){
            text.append("\nNo PokeBalls available!");
        } else {
            PokeBall PB = currentPB.get();
            text.append(String.format("\nPokeBall thrown (catch rate: %.1f)", PB.getCatchRate()/10.0));
            if (Math.random() <= PB.getCatchRate()/10.0){
                text.append("\nPokemon caught!");
                Pokemon caught = pokemons.get(0);
                map.removePokemon(caught);
                caught.removePokemonFromCell();
                inventory.addPokemon(pokemons.get(0));
            } else {
                text.append("\nCatch unsuccessful");
            }
        }
    }
}
