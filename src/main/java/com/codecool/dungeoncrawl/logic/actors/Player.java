package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.logic.items.LootBox;
import com.codecool.dungeoncrawl.logic.items.PokeBall;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Player extends Actor {
    private String facing = "down";
    private String userName = "";
    private boolean superUser = false;

    public Player(Cell cell) {
        super(cell);

    }

    public void setFacing(String facing) {
        this.facing = facing;
    }


    public void setSuperUser(boolean superUser) {
        this.superUser = superUser;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getUserName() {
        return this.userName;
    }
    public void setCell(Cell newCell) {
        cell = newCell;

    }


    public String getTileName() {
        switch (this.facing) {
            case "down":
                return "player_down";
            case "up":
                return "player_up";
            case "right":
                return "player_right";
            default:
                return "player_left";
        }

    }

    public void pickupItem(Inventory inventory, StringBuilder text) {
        if (cell.getItem() instanceof LootBox) {
            LootBox lootbox = (LootBox) cell.getItem();
            int pickedUpPotions = lootbox.getPotionNumber();
            List<PokeBall> pickedUpPokeBalls = lootbox.getPokeBallList();
            inventory.increaseHealthPotionNumber(pickedUpPotions);
            inventory.addPokeBalls(pickedUpPokeBalls);
            text.append("\nLootbox picked up successfully.\n")
                    .append(pickedUpPotions)
                    .append(" potions, and ")
                    .append(pickedUpPokeBalls.size())
                    .append(" pokeballs added.");
            cell.setItem(null);
        } else {
            text.append("\nNothing to pick up here");
        }
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);

        if (( superUser && nextCell.getPokemon() == null
                        && nextCell.getActor() == null) ||
                (!superUser && nextCell.getType() != CellType.EMPTY
                            && nextCell.getType() != CellType.WALL
                            && nextCell.getPokemon() == null
                            && nextCell.getActor() == null)) {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        }

    }

    public void fightPokemon(Inventory inventory, StringBuilder text, Optional<List<Pokemon>> pokemonInRange, GameMap map){
        if (pokemonInRange.isEmpty()) text.append("\nNothing to catch here");
        else {
            Optional<Pokemon> aliveInRange = pokemonInRange.get().stream()
                                .filter(p -> p.getPokeHealth()>0)
                                .min(Comparator.comparing(Pokemon::getPokeHealth));
            if (aliveInRange.isEmpty()) {
                text.append("\nPokemon already defeated. Catch it don't fight!");
                return;
            } else {
                Pokemon activePokemon = inventory.getActivePokemon();
                Pokemon fightWith = aliveInRange.get();
                // player attacks first
                fightWith.setPokeHealth(fightWith.getPokeHealth() - activePokemon.damage());
                text.append(String.format("\n%s attacks %s!", activePokemon.getPokeName(), fightWith.getPokeName()));
                if (fightWith.getPokeHealth() > 1) {
                    // pokemon doesn't fight back if health below threshold
                    activePokemon.setPokeHealth(activePokemon.getPokeHealth() - fightWith.damage());
                }
                if (fightWith.getPokeHealth() <= 0){
                    text.append(String.format("\n%s defeated, catch by 'T'!", fightWith.getPokeName()));
                    activePokemon.setPokeDamage(activePokemon.getPokeDamage() + 1);
                    removeFromRocketInventory(map, fightWith);
                }
                if (activePokemon.getPokeHealth() <= 0){
                    map.removePokemon(activePokemon);
                    inventory.activePokemonDies();
                    text.setLength(0);
                    text.append(String.format("\nYour %s is defeated", activePokemon.getPokeName()));
                }
            }
        }
    }

    public void throwPokeBall(Inventory inventory, StringBuilder text, Optional<List<Pokemon>> pokemonInRange, GameMap map){

        if (pokemonInRange.isEmpty()){
            text.append("\nNothing to catch here");
        } else {
            Optional<PokeBall> currentPB = inventory.takePokeBall();
            if (currentPB.isEmpty()){
                text.append("\nNo PokeBalls available!");
            } else {
                List<Pokemon> pokemons = pokemonInRange.get();
                Pokemon toCatch = pokemons.stream().min(Comparator.comparing(Pokemon::getPokeHealth)).get();
                PokeBall PB = currentPB.get();
                if (PB.hasCaught(toCatch)){
                    removeFromRocketInventory(map, toCatch);
                    pokemonFromBoardToInventory(map, inventory, toCatch);
                    text.append("\nPokemon caught!");
                } else {
                    text.append("\nCatch unsuccessful");
                }
            }
        }
    }

    private void pokemonFromBoardToInventory(GameMap map, Inventory inventory, Pokemon toCatch){
        map.removePokemon(toCatch);
        toCatch.removePokemonFromCell();
        toCatch.setPokeHealth(3);
        inventory.addPokemon(toCatch);
    }

    private void removeFromRocketInventory(GameMap map, Pokemon pokemon) {
        if (map.getRocketGrunt() != null && map.getRocketGrunt().getRocketPokemonOnBoard().contains(pokemon)) {
            map.getRocketGrunt().getRocketPokemonOnBoard().remove(pokemon);
            map.getRocketGrunt().releasePokemon(map);
        }
    }
}
