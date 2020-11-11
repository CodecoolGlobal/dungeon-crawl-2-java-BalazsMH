package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Ivysaur;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;

import java.util.ArrayList;
import java.util.List;

public class RocketGrunt extends Actor {
    private List<Pokemon> rocketPokemonList = new ArrayList<Pokemon>();

    public RocketGrunt(Cell cell) {
        super(cell);
        rocketPokemonList.add(new Ivysaur("Ivysaur"));
    }

    public void releasePokemon(GameMap map){
        int r, c;
        while (true){
            r = (int)(Math.random() * map.getHeight());
            c = (int)(Math.random() * map.getWidth());
            if (map.getCell(c, r).getTileName() == CellType.FLOOR.getTileName()) break;
        }
        rocketPokemonList.get(0).setCell(map.getCell(c, r));
        map.getCell(c, r).setPokemon(rocketPokemonList.get(0));
        map.addPokemon(rocketPokemonList.get(0));
    }

    public List<Pokemon> getRocketPokemonList() {
        return rocketPokemonList;
    }

    @Override
    public String getTileName() {
        return "rocketGrunt";
    }
}
