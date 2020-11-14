package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.util.HashMap;
import java.util.Map;

public class Tiles {

    public static int DEFAULT_TILE_WIDTH = 32;

    private static final Image backgroundTileset = new Image("/emerald_rip.png", 1408 * 2, 1104 * 2, true, false);
    private static final Image charactersTileset = new Image("/playersprites.png", 800*2, 600*2, true, false );
    private static final Image pokeTileset = new Image("/pokesprites.png", 896*2, 576*2, true, false );
    private static final Image floorTile = new Image("/background.png", 510*2, 510*2, true, false );

    private static Map<String, Tile> tileMap = new HashMap<>();

    public static Image getFloorTile() {
        return floorTile;
    }

    public static class Tile {
        public final int x, y, w, h;
        Tile(int i, int j) {
            x = i * (DEFAULT_TILE_WIDTH);
            y = j * (DEFAULT_TILE_WIDTH);
            w = DEFAULT_TILE_WIDTH;
            h = DEFAULT_TILE_WIDTH;
        }
        Tile(int i, int j, int tileWidth) {
            x = i * (tileWidth);
            y = j * (tileWidth);
            w = tileWidth;
            h = tileWidth;
        }
    }

    static {
        tileMap.put("empty", new Tile(0, 0));
        tileMap.put("wall", new Tile(7, 0));
        tileMap.put("floor", new Tile(1, 0));
        tileMap.put("door", new Tile(1, 4)); // !!!
        tileMap.put("openDoor", new Tile(4, 30)); // !!!
        tileMap.put("player_down", new Tile(0, 0, 64));
        tileMap.put("player_up", new Tile(0, 1, 64));
        tileMap.put("player_left", new Tile(1, 1, 64));
        tileMap.put("player_right", new Tile(1, 0, 64));
        tileMap.put("lootbox", new Tile(18, 17, 64));
        tileMap.put("slowpoke", new Tile(22, 2, 64));
        tileMap.put("charizard", new Tile(5, 0, 64 ));
        tileMap.put("bulbasaur", new Tile(0,0, 64 ));
        tileMap.put("ivysaur", new Tile(2,0, 64 ));
        tileMap.put("dustox", new Tile(16,9, 64 ));
        tileMap.put("koffing", new Tile(25,3, 64 ));
        tileMap.put("arbok", new Tile(23,0, 64 ));
        tileMap.put("key", new Tile(3,3));
        tileMap.put("rocketGrunt", new Tile(13,9, 64));

    }


    public static void drawTile(GraphicsContext context, Drawable d, int x, int y) {
        Tile tile = tileMap.get(d.getTileName());
        if (d.getTileName().matches("player_left|player_up|player_down|player_right|rocketGrunt")) {
            context.drawImage(charactersTileset, tile.x, tile.y, tile.w, tile.h,
                    x * DEFAULT_TILE_WIDTH, y * DEFAULT_TILE_WIDTH, DEFAULT_TILE_WIDTH, DEFAULT_TILE_WIDTH);
            //context.setFill(new ImagePattern(backgroundTileset, 1, 0, 32, 32, true));

        } else if (d.getTileName().matches("charizard|slowpoke|bulbasaur|ivysaur|koffing|dustox|arbok|lootbox")){
            context.drawImage(pokeTileset, tile.x, tile.y, tile.w, tile.h,
                    x * DEFAULT_TILE_WIDTH, y * DEFAULT_TILE_WIDTH, DEFAULT_TILE_WIDTH, DEFAULT_TILE_WIDTH);
        } else {
            context.drawImage(backgroundTileset, tile.x, tile.y, tile.w, tile.h,
                    x * DEFAULT_TILE_WIDTH, y * DEFAULT_TILE_WIDTH, DEFAULT_TILE_WIDTH, DEFAULT_TILE_WIDTH);
        }
    }

    public static void drawTerrain(GraphicsContext context, Drawable d, int x, int y) {
        //implement

    }

    public static void drawObjects(GraphicsContext context, Drawable d, int x, int y) {
        //implement

    }

    public static void drawCharacters(GraphicsContext context, Drawable d, int x, int y) {
        //implement

    }

    public static void drawVegetation(GraphicsContext context, Drawable d, int x, int y) {
        //implement

    }

    public static void drawBuildings(GraphicsContext context, Drawable d, int x, int y) {
        //optional

    }

    public static void drawMeta(GraphicsContext context, Drawable d, int x, int y) {

        //optional

    }
}
