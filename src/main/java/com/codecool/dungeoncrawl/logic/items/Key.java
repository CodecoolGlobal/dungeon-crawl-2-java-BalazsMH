package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

public class Key extends Item implements Drawable {
    @Override
    public String getTileName(){return "key";}
    public Key(Cell cell){super(cell);}
}
