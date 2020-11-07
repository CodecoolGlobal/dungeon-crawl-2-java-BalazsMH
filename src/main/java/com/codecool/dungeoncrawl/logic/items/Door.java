package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

public class Door implements Drawable {
    private Cell cell;
    private boolean isOpen = false;

    public Door(Cell cell){
        this.cell = cell;
        cell.setDoor(this);
    }

    @Override
    public String getTileName() {
        if (isOpen) return "openDoor";
        else return "door";
    }

    public void setOpen() {
        isOpen = true;
    }
}
