package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.items.Layout;

public class MapGenerator {

    public static boolean generateMap(int level) {
        Layout myMap = new Layout(20, 30, level);
        myMap.generateLayout();
        return true;
    }
}
