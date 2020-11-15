package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.RocketGrunt;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.items.Door;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameMap {
    private int width;
    private int height;
    private Cell[][] cells;
    private Door door;
    private String gameLevel;

    private Player player;
    private RocketGrunt rocketGrunt;
    private List<Pokemon> pokemonList = new ArrayList<Pokemon>();

    public GameMap(int width, int height, CellType defaultCellType, String gameLevel) {
        this.gameLevel = gameLevel;
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Door getDoor() { return door; }

    public void setDoor(Door door) { this.door = door; }

    public void addPokemon(Pokemon pokemon){this.pokemonList.add(pokemon);}

    public List<Pokemon> getPokemonList(){return pokemonList;}

    public void removePokemon(Pokemon pokemon){ pokemonList.remove(pokemon); }

    public Player getPlayer() {
        return player;
    }

    public List<Integer> returnPlayerCoordinates() {
        List<Integer> cellList = new ArrayList<Integer>();
        cellList.add(this.player.getX());
        cellList.add(this.player.getY());
        return cellList;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getLevel() {
        return this.gameLevel;

    }

    public void setRocketGrunt(RocketGrunt rocketGrunt) { this.rocketGrunt = rocketGrunt; }

    public RocketGrunt getRocketGrunt(){ return rocketGrunt;}

    public Optional<List<Pokemon>> getPokemonInRange(Label currentInfo) {
        Optional<List<Pokemon>> toReturn = Optional.empty();
        List<Pokemon> pokemonInRange = new ArrayList<Pokemon>();
        int playerX = player.getCell().getX();
        int playerY = player.getCell().getY();
        pokemonList.forEach(p -> {
            if (Math.abs(p.getCell().getX() - playerX) + Math.abs(p.getCell().getY() - playerY) <= 3){
                pokemonInRange.add(p);
                currentInfo.setText(p.toString());
            }
        });
        if (pokemonInRange.size() > 0) toReturn = Optional.of(pokemonInRange);
        return toReturn;
    }

    public void moveAllPokemon(MapChanger mapChanger, List<List<Integer>> mapWallsLevel1, List<List<Integer>> mapWallsLevel2) {
        int level = mapChanger.getLevel();
        List<List<Integer>> mapWalls = (level == 1) ? mapWallsLevel1 : mapWallsLevel2;

        List playerCoordinates = new ArrayList(List.of(player.getX(), player.getY()));
        for (Pokemon pokemon : pokemonList) {
            if (rocketGrunt != null && rocketGrunt.getRocketPokemonOnBoard().contains(pokemon)) pokemon.move();
            else {
                pokemon.attackMove(mapWalls, playerCoordinates, pokemon.getX(), pokemon.getY());
            }
        }
    }
}
