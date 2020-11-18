package com.codecool.dungeoncrawl.logic.map;

public class MapGenerator {

    public static void generateMap(int level) {
        Layout myMap = new Layout(20, 30, level);
        myMap.generateLayout();
        return;
    }
}
