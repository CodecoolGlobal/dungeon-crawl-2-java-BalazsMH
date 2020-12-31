package com.codecool.dungeoncrawl.logic.actors.pokemon;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.model.PokemonModel;

public class PokemonFactory {

    /***
    Used to load pokemon from database/json
     */
    public static Pokemon getPokemon(Cell cell, PokemonModel pokemonModel){
        Pokemon pokemon = create(cell, pokemonModel.getPokeName(), pokemonModel.getGameLevel());
        pokemon.setPokeHealth(pokemonModel.getPokeHealth());
        pokemon.setPokeDamage(pokemonModel.getPokeDamage());
        pokemon.setPokeId(pokemonModel.getPokeId());
        return pokemon;
    }

    /***
     Used to create pokemons with default health, damage and ID in new games
     */
    public static Pokemon getPokemon(Cell cell, String pokeName, int level) {
        return create(cell, pokeName, level);
    }

    private static Pokemon create(Cell cell, String pokeName, int level){
        Pokemon pokemon;
        switch (pokeName) {
            case "Charizard":
                pokemon = new Charizard(cell, pokeName, level);
                break;
            case "Bulbasaur":
                pokemon = new Bulbasaur(cell, pokeName, level);
                break;
            case "Slowpoke":
            default:
                pokemon = new Slowpoke(cell, pokeName, level);
                break;
            case "Arbok":
                pokemon = new Arbok(cell, pokeName, level);
                break;
            case "Dustox":
                pokemon = new Dustox(cell, pokeName, level);
                break;
            case "Koffing":
                pokemon = new Koffing(cell, pokeName, level);
                break;
        }
        return pokemon;
    }
}
