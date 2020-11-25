package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

import java.util.ArrayList;
import java.util.List;

public class LootBox extends Item {
    private final int potionNumber;
    private List<PokeBall> pokeBallList;
    private int level;
    private int lootBoxId;
    private static int counter;

    public LootBox(Cell cell, int level) {
        super(cell);
        lootBoxId = counter++;
        this.level = level;
        this.potionNumber = (int) Math.floor(Math.random()*(8.00 - 2.00) + 2.00);
        this.pokeBallList = new ArrayList<PokeBall>();
        int pokeBallCount = (int) Math.floor(Math.random()*(8.00 - 2.00) + 2.00);
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

    public int getLevel() {
        return level;
    }

    public int getLootBoxId() {
        return lootBoxId;
    }

    public void setLevel(int level) { this.level = level; }
}
