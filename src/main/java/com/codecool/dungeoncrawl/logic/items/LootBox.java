package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

import java.util.ArrayList;
import java.util.List;

public class LootBox extends Item {
    private final int potionNumber;
    private List<PokeBall> pokeBallList;
    private Cell cell;


    public LootBox(Cell cell) {
        super(cell);
        this.potionNumber = (int) Math.floor(Math.random()*10.00);
        this.pokeBallList = new ArrayList<PokeBall>();
        int pokeBallCount = (int) Math.floor(Math.random()*10.00);
        for (int i = 0; i < pokeBallCount; i++) {
            this.pokeBallList.add(new PokeBall());
        }
    }

    public int getPotionNumber(){
        return potionNumber;
    }

    public List<PokeBall> getPokeBallList(){
        return pokeBallList;
    }

    @Override
    public String getTileName() {
        return "lootbox";
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }
}
