package com.codecool.dungeoncrawl.logic.map;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.RocketGrunt;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.items.Door;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class GameMap {
    private final int width;
    private final int height;
    private final Cell[][] cells;
    private Door door;
    private final int gameLevel;
    private List<List<Integer>>  walls = new ArrayList<>();
    private int displayWidth = 30;
    private int displayHeight = 20;



    private Player player;
    private RocketGrunt rocketGrunt;
    private final List<Pokemon> pokemonList = new ArrayList<Pokemon>();

    public GameMap(int width, int height, CellType defaultCellType, int gameLevel) {
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

    public List<List<Integer>> getWalls() {
        return walls;
    }

    public void setWalls(List<List<Integer>> walls) {
        this.walls = walls;
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

    public void removePokemon(Pokemon pokemon){ pokemonList.remove(pokemon); }

    public Player getPlayer() {
        return player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLevel() {
        return this.gameLevel;

    }
    public Cell[][] getCells(){return cells;}

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

    public void moveAllPokemon() {
        List<List<Integer>> mapWalls =  this.walls;

        List playerCoordinates = new ArrayList(List.of(player.getX(), player.getY()));
        for (Pokemon pokemon : pokemonList) {
            if (rocketGrunt != null && rocketGrunt.getRocketPokemonOnBoard().contains(pokemon)) pokemon.move();
            else {
                pokemon.attackMove(mapWalls, playerCoordinates, pokemon.getX(), pokemon.getY());
            }
        }
    }

    public String layoutToString() {
        StringJoiner mapString = new StringJoiner("\n");
        mapString.add(width + " " + height);
        for (int y = 0; y<height;y++){
            StringBuilder r = new StringBuilder();
            for (int x = 0; x<width;x++){
                if(cells[x][y].getTileName().equals("floor")) r.append(".");
                else if (cells[x][y].getTileName().equals("wall")) r.append("#");
                else if (cells[x][y].getTileName().equals("empty")) r.append(" ");
                else if (cells[x][y].getTileName().equals("door")) r.append("d");
            }
            mapString.add(r.toString());
        }
        return mapString.toString();
    }

    public Cell[][] getVisibleMap () {
        Cell[][] visibleCells = new Cell[displayWidth][displayHeight];
        int x = player.getX();
        int y = player.getY();
        int playerRelativeX = displayWidth / 2;
        int playerRelativeY = displayHeight / 2;
        int mapOffsetX;
        int mapOffsetY;

        mapOffsetX = offsetCalculator(x, playerRelativeX, displayWidth, width);
        mapOffsetY = offsetCalculator(y, playerRelativeY, displayHeight, height);

        for (int i = 0; i < displayWidth; i++) {
            for (int j = 0; j < displayHeight; j++) {
                visibleCells[i][j] = cells[i + mapOffsetX][j + mapOffsetY];
            }
        }
        return visibleCells;
    }


    public int getDisplayWidth() {
        return displayWidth;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    private int offsetCalculator (int pos, int relativePos, int displaySize, int mapSize ) {
        if (pos - relativePos >= 0 && (pos + displaySize - relativePos) < mapSize) {
            return pos - relativePos;
        }
        else if (pos - relativePos < 0) {
            return 0;
        }
        else {
            return mapSize - displaySize;
        }
    }
}
