package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    /*
    public static int TILE_WIDTH = 32;

    private static Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);
    private static Map<String, Tile> tileMap = new HashMap<>();
    public static class Tile {
        public final int x, y, w, h;
        Tile(int i, int j) {
            x = i * (TILE_WIDTH + 2);
            y = j * (TILE_WIDTH + 2);
            w = TILE_WIDTH;
            h = TILE_WIDTH;
        }
    }

    static {
        tileMap.put("empty", new Tile(0, 0));
        tileMap.put("wall", new Tile(10, 17));
        tileMap.put("floor", new Tile(2, 0));
        tileMap.put("player", new Tile(27, 0));
        tileMap.put("skeleton", new Tile(29, 6));
        tileMap.put("lootbox", new Tile(12, 31));
        tileMap.put("slowpoke", new Tile(28, 6));
        tileMap.put("charizard", new Tile(27, 8));
        tileMap.put("bulbasaur", new Tile(24, 8));

    }*/

    public static int DEFAULT_TILE_WIDTH = 32;

    private static Image backgroundTileset = new Image("/emerald_rip.png", 1408 * 2, 1104 * 2, true, false);
    private static Image charactersTileset = new Image("/playersprites.png", 800*2, 600*2, true, false );
    private static Image pokeTileset = new Image("/pokesprites.png", 896*2, 576*2, true, false );
    private static Map<String, Tile> tileMap = new HashMap<>();
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
        tileMap.put("player", new Tile(1, 0, 64));
        tileMap.put("skeleton", new Tile(29, 6));
        tileMap.put("lootbox", new Tile(20, 58));
        tileMap.put("slowpoke", new Tile(22, 2, 64));
        tileMap.put("charizard", new Tile(5, 0, 64 ));
        tileMap.put("bulbasaur", new Tile(0,0, 64 ));

    }


    public static void drawTile(GraphicsContext context, Drawable d, int x, int y) {
        Tile tile = tileMap.get(d.getTileName());
        if (d.getTileName().equals("player")) {
            context.drawImage(charactersTileset, tile.x, tile.y, tile.w, tile.h,
                    x * DEFAULT_TILE_WIDTH, y * DEFAULT_TILE_WIDTH, DEFAULT_TILE_WIDTH, DEFAULT_TILE_WIDTH);
        } else if (d.getTileName().matches("charizard|slowpoke|bulbasaur")){
            context.drawImage(pokeTileset, tile.x, tile.y, tile.w, tile.h,
                    x * DEFAULT_TILE_WIDTH, y * DEFAULT_TILE_WIDTH, DEFAULT_TILE_WIDTH, DEFAULT_TILE_WIDTH);
        } else {
            context.drawImage(backgroundTileset, tile.x, tile.y, tile.w, tile.h,
                    x * DEFAULT_TILE_WIDTH, y * DEFAULT_TILE_WIDTH, DEFAULT_TILE_WIDTH, DEFAULT_TILE_WIDTH);
        }
    }
}
