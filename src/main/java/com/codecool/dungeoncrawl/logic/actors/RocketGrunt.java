package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.actors.pokemon.*;

import java.util.ArrayList;
import java.util.List;

public class RocketGrunt extends Actor {
    private List<Pokemon> rocketPokemonList = new ArrayList<Pokemon>();
    private List<Pokemon> rocketPokemonOnBoard = new ArrayList<Pokemon>();

    public RocketGrunt(Cell cell) {
        super(cell);
        rocketPokemonList.add(new Dustox("Dustox"));
        rocketPokemonList.add(new Koffing("Koffing"));
        rocketPokemonList.add(new Arbok("Arbok"));
        rocketPokemonList.forEach(p -> p.setLevel(-1));
    }

    public void releasePokemon(GameMap map){
        if (rocketPokemonList.size() == 0) return;
        int r, c;
        while (true){
            r = (int)(Math.random() * map.getHeight());
            c = (int)(Math.random() * map.getWidth());
            if (map.getCell(c, r).getTileName() == CellType.FLOOR.getTileName()) break;
        }
        Pokemon released = rocketPokemonList.get(0);
        released.setLevel(2);
        rocketPokemonList.remove(released);
        rocketPokemonOnBoard.add(released);
        released.setCell(map.getCell(c, r));
        released.setLevel(2);
        map.getCell(c, r).setPokemon(released);
        map.addPokemon(released);
    }

    public List<Pokemon> getRocketPokemonList() {
        return rocketPokemonList;
    }

    public List<Pokemon> getRocketPokemonOnBoard() {return rocketPokemonOnBoard; }

    @Override
    public String getTileName() {
        return "rocketGrunt";
    }
}
