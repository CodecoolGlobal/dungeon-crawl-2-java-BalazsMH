package com.codecool.dungeoncrawl.logic.map;

public enum MapType {
    GRASS,
    DESERT,
    WATER,
    CAVE;

    public static String getTileCode(MapType type) {
        switch (type) {
            case GRASS:
                return "2";
            case DESERT:
                return "289";
            case WATER:
                return "368";
            case CAVE:
                return "268";
            default:
                return "265";
        }


    }
}
