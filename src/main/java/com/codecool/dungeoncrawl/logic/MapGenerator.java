package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.items.Layout;

public class MapGenerator {

    public static boolean generateMap(String filename) {
        Layout myMap = new Layout(20, 30, filename);
        myMap.generateLayout();
        return true;
    }
}
