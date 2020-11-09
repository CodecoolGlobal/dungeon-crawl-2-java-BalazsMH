package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.items.Layout;

public class App {
    public static void main(String[] args) {
        Layout myMap = new Layout(20, 30, "./src/main/resources/map.txt");
        myMap.generateLayout();
        Main.main(args);
    }
}
