package com.codecool.dungeoncrawl.logic.map;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.RocketGrunt;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Bulbasaur;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Charizard;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Slowpoke;
import com.codecool.dungeoncrawl.logic.items.Door;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.LootBox;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PokemonModel;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapLoader {
    private static List<List<Integer>> walls;
    public static GameMap loadMap(int gameLevel) {
        InputStream is;
        walls = new ArrayList<>();
        //TODO: map should not be part of the resources folder
        if (gameLevel == 1) {
            is = MapLoader.class.getResourceAsStream("/map.txt");
        } else {
            is = MapLoader.class.getResourceAsStream("/map2.txt");
        }

        Scanner scanner = new Scanner(is);

        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY, gameLevel);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            List<Integer> tmp = new ArrayList<>(List.of(x, y));
                            walls.add(tmp);
                            break;
                        case 'd':
                            cell.setType(CellType.DOOR);
                            map.setDoor(new Door(cell));
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 'k':
                            cell.setType(CellType.FLOOR);
                            new Key(cell);
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        case 'L':
                            cell.setType(CellType.FLOOR);
                            new LootBox(cell, gameLevel);
                            break;
                        case 'C':
                            cell.setType(CellType.FLOOR);
                            map.addPokemon(new Charizard(cell, "Charizard", gameLevel));
                            break;
                        case 'S':
                            cell.setType(CellType.FLOOR);
                            map.addPokemon(new Slowpoke(cell, "Slowpoke", gameLevel));
                            break;
                        case 'B':
                            cell.setType(CellType.FLOOR);
                            map.addPokemon(new Bulbasaur(cell, "Bulbasaur", gameLevel));
                            break;
                        case 'R':
                            cell.setType(CellType.FLOOR);
                            map.setRocketGrunt(new RocketGrunt(cell));
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        map.setWalls(walls);
        return map;
    }
    public static List<List<Integer>> getWalls() {
        return walls;
    }


    public static GameMap loadMapFromSave(String mapString, int gameLevel) {
        walls = new ArrayList<>();
        Scanner scanner = new Scanner(mapString);

        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY, gameLevel);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            List<Integer> tmp = new ArrayList<>(List.of(x, y));
                            walls.add(tmp);
                            break;
                        case 'd':
                            cell.setType(CellType.DOOR);
                            map.setDoor(new Door(cell));
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 'k':
                            cell.setType(CellType.FLOOR);
                            new Key(cell);
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        case 'L':
                            cell.setType(CellType.FLOOR);
                            new LootBox(cell, gameLevel);
                            break;
                        case 'C':
                            cell.setType(CellType.FLOOR);
                            map.addPokemon(new Charizard(cell, "Charizard", gameLevel));
                            break;
                        case 'S':
                            cell.setType(CellType.FLOOR);
                            map.addPokemon(new Slowpoke(cell, "Slowpoke", gameLevel));
                            break;
                        case 'B':
                            cell.setType(CellType.FLOOR);
                            map.addPokemon(new Bulbasaur(cell, "Bulbasaur", gameLevel));
                            break;
                        case 'R':
                            cell.setType(CellType.FLOOR);
                            map.setRocketGrunt(new RocketGrunt(cell));
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        map.setWalls(walls);
        return map;
    }
    public static void placePokemons(GameMap map, GameState gameState) {
        for (PokemonModel pokemonModel : gameState.getPokemonModelList()){
            for (Cell[] row : map.getCells()) {
                for (Cell cell : row) {
                    if (pokemonModel.getGameLevel() == map.getLevel()) {
                        if (pokemonModel.getX() == cell.getX() && pokemonModel.getY() == cell.getY()) {
                            Pokemon pokemon;
                            switch (pokemonModel.getPokeName()) {
                                case "Charizard":
                                    pokemon = new Charizard(cell, pokemonModel.getPokeName(), pokemonModel.getGameLevel());
                                    cell.setPokemon(pokemon);
                                    map.addPokemon(pokemon);
                                    break;
                                case "Bulbasaur":
                                    pokemon = new Bulbasaur(cell, pokemonModel.getPokeName(), pokemonModel.getGameLevel());
                                    cell.setPokemon(pokemon);
                                    map.addPokemon(pokemon);
                                    break;
                                case "Slowpoke":
                                    pokemon = new Slowpoke(cell, pokemonModel.getPokeName(), pokemonModel.getGameLevel());
                                    cell.setPokemon(pokemon);
                                    map.addPokemon(pokemon);
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }
}
