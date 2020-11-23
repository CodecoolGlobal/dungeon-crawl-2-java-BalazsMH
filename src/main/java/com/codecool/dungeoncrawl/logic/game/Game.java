package com.codecool.dungeoncrawl.logic.game;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.Tiles;
import com.codecool.dungeoncrawl.dao.Converter;
import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.EndCondition;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.map.MapChanger;
import com.codecool.dungeoncrawl.logic.map.MapGenerator;
import com.codecool.dungeoncrawl.logic.map.MapLoader;
import com.codecool.dungeoncrawl.logic.ui.WindowElement;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.InventoryModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.canvas.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Game {
    private final GameMap map1;
    private final GameMap map2;
    private Player player;
    private final List<List<Integer>> mapWallsLevel1;
    private final List<List<Integer>> mapWallsLevel2;
    private final Canvas canvas;
    private final GraphicsContext context;
    private final Label nameLabel = new Label();
    private final Label inv = new Label();
    private final Label currentInfo = new Label();
    private final Label currentLevel = new Label();
    private int activeMap = 1;
    private final StringBuilder text = new StringBuilder();
    private Timeline enemyMove;
    private Converter converter;




    public Game() {
        MapGenerator.generateMap(1);
        this.map1 = MapLoader.loadMap(1);
        this.mapWallsLevel1 = map1.getWalls();

        MapGenerator.generateMap(2);
        this.map2 = MapLoader.loadMap(2);
        this.mapWallsLevel2 = map2.getWalls();

        this.player = this.map1.getPlayer();

        this.canvas = new Canvas(
                map1.getDisplayWidth() * Tiles.DEFAULT_TILE_WIDTH,
                map1.getDisplayHeight() * Tiles.DEFAULT_TILE_WIDTH);
        this.context = canvas.getGraphicsContext2D();
        this.addEnemyMoveHandler();
        converter = new Converter(map1, map2);
    }

    public Game(GameState gameState) {
        PlayerModel playerModel = gameState.getPlayer();
        InventoryModel inventoryModel = gameState.getInventoryModel();
        int currentLevel = playerModel.getLevel();
        String activeMap = gameState.getActiveMap();
        String storedMap = gameState.getStoredMap();

        //create current map from String, without actors/items placed
        this.map1 = MapLoader.loadMapFromSave(currentLevel == 1 ? storedMap : activeMap, currentLevel);
        this.mapWallsLevel1 = map1.getWalls();

        //create stored map from String, without actors/items placed
        this.map2 = MapLoader.loadMapFromSave(currentLevel == 1 ? activeMap : storedMap, currentLevel == 1 ? 2 : 1);
        this.mapWallsLevel2 = map2.getWalls();

        //create player on the cell it was previously on
        this.player = new Player(map1.getCell(playerModel.getX(), playerModel.getY()));
        this.player.setUserName(playerModel.getPlayerName());
        this.player.setSuperUser(playerModel.getGodMode());
        map1.getCell(playerModel.getX(), playerModel.getY()).setActor(this.player);
        map1.setPlayer(this.player);

        //set inventory for the player. Not all data is updated currently.
        player.setInventory(new Inventory(inventoryModel.getHealthPotionNumber(),
                                          inventoryModel.getPokeBallNumber(),
                                          inventoryModel.hasKey()
                                          ));

        this.canvas = new Canvas(
                map1.getWidth() * Tiles.DEFAULT_TILE_WIDTH,
                map1.getHeight() * Tiles.DEFAULT_TILE_WIDTH);
        this.context = canvas.getGraphicsContext2D();
        this.addEnemyMoveHandler();
        converter = new Converter(map1, map2);
    }


    public Scene showGameScene() {
        GameMap map = this.activeMap == 1 ? this.map1 : this.map2;
        WindowElement.setLabels(this.currentLevel, this.nameLabel, this.currentInfo, this.inv, map);


        VBox rightPane = WindowElement.createRightPane(map.getPlayer().getInventory(), map, nameLabel, inv, currentInfo);
        VBox levelBox = WindowElement.createLevelBox(currentLevel);
        VBox bottom = WindowElement.createBottomBox();

        this.nameLabel.setText(this.player.getUserName());

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(canvas);
        borderPane.setBottom(bottom);
        borderPane.setRight(rightPane);
        borderPane.setTop(levelBox);

        Scene scene = new Scene(borderPane);
        scene.setOnKeyPressed(this::onKeyPressed);

        refresh(map.getPlayer().getInventory());

        return scene;
    }



    private void onKeyPressed(KeyEvent keyEvent) {
        GameMap map = this.activeMap == 1 ? this.map1 : this.map2;
        text.setLength(0);
        Inventory inventory = map.getPlayer().getInventory();
        Player player = map.getPlayer();
        KeyCode keyPressed = keyEvent.getCode();
        switch (keyPressed) {
            case UP:
                player.move(0, -1, "up");
                break;
            case DOWN:
                player.move(0, 1, "down");
                break;
            case LEFT:
                player.move(-1, 0, "left");
                break;
            case RIGHT:
                player.move(1,0, "right");
                break;
            case R:
                map.getRocketGrunt().releasePokemon(map);
                break;
            case T:
                player.throwPokeBall(text, map.getPokemonInRange(currentInfo), map);
                checkIfGameEnds(inventory);
                break;
            case E:
                if (player.whatAmIStandingOn() instanceof Key){
                    inventory.addKey(player.getCell());
                    player.getCell().setItem(null);
                } else {
                    player.pickupItem(text);
                }
                break;
            case O:
                if (player.hasKey() && player.standingOnDoor()){
                    player.openDoor();
                    this.activeMap = this.activeMap == 1 ? 2 : 1; // change activeMap

                    GameMap  nextMap = this.activeMap == 1 ? this.map1 : map2; //the new activeMap will be the next map
                    player.setLevel(this.activeMap);
                    Player toKeep = map.getPlayer(); //get the player from the original map

                    Cell doorCell = nextMap.getDoor().getCell();
                    toKeep.setCell(doorCell);
                    doorCell.setActor(toKeep);
                    nextMap.getDoor().setOpen();
                    nextMap.setPlayer(toKeep);


                }
                break;
            case A:
                inventory.changeActivePokemon();
                break;
            case F:
                player.fightPokemon(text, map.getPokemonInRange(currentInfo), map);
                checkIfGameEnds(inventory);
                break;
            case H:
                inventory.heal();
                break;
            case S:
                if (keyEvent.isControlDown()){
                    while(true){
                        String saveName = getSaveName();
                        String playerName = player.getUserName();
                        if (converter.ifPlayerSaveExists(saveName, playerName)) {
                            String decision = getDecision();
                            if (decision.equals("s")) {
                                converter.run("update", saveName, playerName);
                                System.out.println("Save succesful");
                                break;
                            }
                        } else {
                            converter.run("save", saveName, playerName);
                            System.out.println("Save succesful");
                            break;
                        }
                    }
                }
        }
        refresh(inventory);
    }

    private String getSaveName() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter save name: ");
        if (scanner.hasNextLine()) return scanner.nextLine();
        return null;
    }

    private String getDecision(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Save already exists, press S to overwrite or C to cancel: ");
        if (scanner.hasNextLine()) return scanner.nextLine();
        return null;
    }

    private void refresh(Inventory inventory) {
        GameMap map = this.activeMap == 1 ? this.map1 : this.map2;
        Cell[][] visibleMap = map.getVisibleMap();

        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        WindowElement.refreshInfoWindow(text, currentInfo, map);
        WindowElement.refreshLevelAndInventory(inventory, inv, currentLevel, map);
        for (int x = 0; x < map.getDisplayWidth(); x++) {
            for (int y = 0; y < map.getDisplayHeight(); y++) {
                Cell cell = visibleMap[x][y];
                Tiles.drawTile(context, cell, x, y);

                if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x, y);
                }
                if (cell.getDoor() != null) {
                    Tiles.drawTile(context, cell.getDoor(), x, y);
                }
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                }

                if (cell.getPokemon() != null) {
                    Tiles.drawTile(context, cell.getPokemon(), x, y);
                }

            }
        }
    }

    public void checkIfGameEnds(Inventory inventory){
        if (inventory.getActivePokemon() == null){
            WindowElement.gameEndWindow(EndCondition.LOSE, Main.getpStage());
        } else if (map2.getRocketGrunt().getRocketPokemonList().size() == 0 && map2.getRocketGrunt().getRocketPokemonOnBoard().size() == 0){
            WindowElement.gameEndWindow(EndCondition.WIN, Main.getpStage());
        }
    }
    private void addEnemyMoveHandler() {
        //GameMap map = this.activeMap == 1 ? this.map1 : this.map2;

        enemyMove = new Timeline(
                new KeyFrame(Duration.seconds(1), (event) -> {
                    this.getActiveMap().moveAllPokemon();
                    refresh(this.getActiveMap().getPlayer().getInventory()); }));
        enemyMove.setCycleCount(Timeline.INDEFINITE);
        enemyMove.play();
    }





    public GameMap getActiveMap() {
        return this.activeMap == 1 ? this.map1 : this.map2;
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public GameMap getMap1() {
        return map1;
    }

    public GameMap getMap2() {
        return map2;
    }
}
