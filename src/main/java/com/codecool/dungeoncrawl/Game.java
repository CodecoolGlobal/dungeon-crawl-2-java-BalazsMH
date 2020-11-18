package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.map.MapChanger;
import com.codecool.dungeoncrawl.logic.map.MapGenerator;
import com.codecool.dungeoncrawl.logic.map.MapLoader;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.canvas.*;
import java.util.List;

public class Game {
    private final GameMap map1;
    private final GameMap map2;
    private Player player;
    private final List<List<Integer>> mapWallsLevel1;
    private final List<List<Integer>> mapWallsLevel2;
    private final MapChanger mapChanger;
    private final Canvas canvas;
    private final GraphicsContext context;


    public Game() {
        boolean m = MapGenerator.generateMap(1);
        this.map1 = MapLoader.loadMap(1);
        this.mapWallsLevel1 = map1.getWalls();
        this.map2 = MapLoader.loadMap(2);
        this.mapWallsLevel2 = map2.getWalls();
        this.mapChanger = new MapChanger(map1, map2);
        this.canvas = new Canvas(
                map1.getWidth() * Tiles.DEFAULT_TILE_WIDTH,
                map1.getHeight() * Tiles.DEFAULT_TILE_WIDTH);
        this.context = canvas.getGraphicsContext2D();
    }


}
