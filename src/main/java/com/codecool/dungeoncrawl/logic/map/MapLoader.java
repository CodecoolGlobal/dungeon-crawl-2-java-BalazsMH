package com.codecool.dungeoncrawl.logic.map;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.RocketGrunt;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.actors.pokemon.PokemonFactory;
import com.codecool.dungeoncrawl.logic.items.Door;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.LootBox;
import com.codecool.dungeoncrawl.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MapLoader {
    private List<List<Integer>> walls = new ArrayList<>();
    private final String mapLocation = "./src/main/java/com/codecool/dungeoncrawl/logic/map/";
    public GameMap loadMap(int gameLevel) throws IOException {
        BufferedReader reader;
        if (gameLevel == 1) {
            reader = new BufferedReader(new FileReader(mapLocation + "map1.txt"));
        } else {
            reader = new BufferedReader(new FileReader(mapLocation + "map2.txt"));
        }

        String[] nums = reader.readLine().split(" ");
        int width = Integer.parseInt(nums[0]);
        int height = Integer.parseInt(nums[1]);

        GameMap map = new GameMap(width, height, CellType.EMPTY, gameLevel);
        for (int y = 0; y < height; y++) {
            String line = reader.readLine();
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
                            map.addPokemon(PokemonFactory.getPokemon(cell, "Charizard", gameLevel));
                            break;
                        case 'S':
                            cell.setType(CellType.FLOOR);
                            map.addPokemon(PokemonFactory.getPokemon(cell, "Slowpoke", gameLevel));
                            break;
                        case 'B':
                            cell.setType(CellType.FLOOR);
                            map.addPokemon(PokemonFactory.getPokemon(cell, "Bulbasaur", gameLevel));
                            break;
                        case 'R':
                            cell.setType(CellType.FLOOR);
                            RocketGrunt grunt = new RocketGrunt(cell);
                            grunt.addPokemon(PokemonFactory.getPokemon(null, "Koffing", -1));
                            grunt.addPokemon(PokemonFactory.getPokemon(null, "Arbok", -1));
                            grunt.addPokemon(PokemonFactory.getPokemon(null, "Dustox", -1));
                            map.setRocketGrunt(grunt);
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
    public List<List<Integer>> getWalls() {
        return walls;
    }


    public GameMap loadMapFromSave(String mapString, int gameLevel) {
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

    public Player createPlayer(GameMap map, PlayerModel playerModel) {
        Player player = new Player(map.getCell(playerModel.getX(), playerModel.getY()));
        player.setLevel(playerModel.getLevel());
        map.setPlayer(player);
        map.getCell(playerModel.getX(), playerModel.getY()).setActor(player);
        player.setUserName(playerModel.getPlayerName());
        player.setSuperUser(playerModel.getGodMode());
        return player;
    }

    public void addInventoryToPlayer(InventoryModel model, GameState gameState, Player player){
        List<Pokemon> playersPokemon = gameState.getPokemonModelList()
                .stream()
                .filter(p -> p.getGameLevel() == 0)
                .map(p -> PokemonFactory.getPokemon(null, p))
                .collect(Collectors.toList());
        player.setInventory(new Inventory(model.getHealthPotionNumber(), model.getPokeBallNumber(), model.hasKey(), playersPokemon));
    }

    public void addRocketPokemons(GameState gameState, GameMap map2) {
        List<Pokemon> rocketPokemon = gameState.getPokemonModelList()
                .stream()
                .filter(p -> p.getGameLevel() == -1)
                .map(p -> PokemonFactory.getPokemon(null, p))
                .collect(Collectors.toList());
        rocketPokemon.forEach(p -> map2.getRocketGrunt().addPokemon(p));
    }

    public void placePokemons(GameMap map, GameState gameState) {
        List<String> rocketPokeNames = new ArrayList<>(List.of(new String[]{"Koffing", "Dustox", "Arbok"}));

        for (PokemonModel pokemonModel : gameState.getPokemonModelList()){
            if (pokemonModel.getGameLevel() == map.getLevel()){
                Cell cell = map.getCell(pokemonModel.getX(), pokemonModel.getY());
                Pokemon pokemon = PokemonFactory.getPokemon(cell, pokemonModel);
                cell.setPokemon(pokemon);
                map.addPokemon(pokemon);
                if (rocketPokeNames.contains(pokemon.getPokeName())){
                    map.getRocketGrunt().addRocketPokemonOnBoard(pokemon);
                };
            }
        }
    }

    public void placeLootBoxes(GameMap map, GameState gameState) {
        for (LootBoxModel lootboxModel : gameState.getLootBoxModelList()){
            if (lootboxModel.getLevel() == map.getLevel()){
                Cell cell = map.getCell(lootboxModel.getX(), lootboxModel.getY());
                LootBox lootBox = new LootBox(cell, lootboxModel);
            }
        }
    }

}
