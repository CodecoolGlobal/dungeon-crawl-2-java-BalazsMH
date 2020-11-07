package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.logic.items.PokeBall;

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

    public void pickupItem() {
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);

        if (( superUser && nextCell.getPokemon() == null && nextCell.getActor() == null ) ||
                (!superUser && nextCell.getType() != CellType.WALL && nextCell.getPokemon() == null && nextCell.getActor() == null)) {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        }

    }

    public void throwPokeBall(Inventory inventory, StringBuilder text, Optional<List<Pokemon>> pokemonInRange, GameMap map){

        if(pokemonInRange.isEmpty()){
            text.append("\nNothing to catch here");
        } else {
            Optional<PokeBall> currentPB = inventory.takePokeBall();
            if (currentPB.isEmpty()){
                text.append("\nNo PokeBalls available!");
            } else {
                List<Pokemon> pokemons = pokemonInRange.get();
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
}
