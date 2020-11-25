package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Item implements Drawable{
    private final Cell cell;
    public Item(Cell cell) {
        this.cell = cell;
        this.cell.setItem(this);
    }
    public Cell getCell() {
        return cell;
    }

    public Integer getX() {
        return (cell != null)? cell.getX() : null;
    }

    public Integer getY() {
        return (cell != null)? cell.getY() : null;
    }
}

