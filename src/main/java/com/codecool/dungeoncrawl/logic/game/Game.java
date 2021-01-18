package com.codecool.dungeoncrawl.logic.game;

import com.codecool.dungeoncrawl.logic.ui.Tiles;
import com.codecool.dungeoncrawl.dao.Converter;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Facing;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.map.GameMap;
import com.codecool.dungeoncrawl.logic.map.MapGenerator;
import com.codecool.dungeoncrawl.logic.map.MapLoader;
import com.codecool.dungeoncrawl.logic.ui.WindowElement;
import com.codecool.dungeoncrawl.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.canvas.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class Game {
    private GameMap map1;
    private GameMap map2;
    private Player player;
    private List<List<Integer>> mapWallsLevel1;
    private List<List<Integer>> mapWallsLevel2;
    private Canvas canvas;
    private GraphicsContext context;
    private final Label nameLabel = new Label();
    private final Label inventoryLabel = new Label();
    private final Label actionsLabel = new Label();
    private final Label levelLabel = new Label();
    private int activeMap = 1;
    private final StringBuilder actionsStB = new StringBuilder();
    private final StringBuilder inRangeStB = new StringBuilder();
    private Converter converter;
    private Stage pStage;
    private Timeline enemyMove;



    public Game(Converter converter, Stage pStage) {
        MapLoader mapLoader = new MapLoader();
        MapGenerator.generateMap(1);
        MapGenerator.generateMap(2);
        try{
            this.map1 = mapLoader.loadMap(1);
            this.mapWallsLevel1 = map1.getWalls();
            this.map2 = mapLoader.loadMap(2);
            this.mapWallsLevel2 = map2.getWalls();
            this.player = this.map1.getPlayer();
            setUpCanvasContextStage(map1, map2, converter, pStage);
        } catch (IOException e){
            System.out.println("failed to load");
        }

    }

    public Game(GameState gameState, Converter converter, Stage pStage) {
        MapLoader mapLoader = new MapLoader();

        PlayerModel playerModel = gameState.getPlayer();
        InventoryModel inventoryModel = gameState.getInventoryModel();
        activeMap = playerModel.getLevel();
        String activeMapString = gameState.getActiveMap();
        String storedMapString = gameState.getStoredMap();

        //create level1 map from String, without actors/items placed
        this.map1 = mapLoader.loadMapFromSave(activeMap == 1 ? activeMapString : storedMapString, 1);
        this.mapWallsLevel1 = map1.getWalls();

        //create level2 map from String, without actors/items placed
        this.map2 = mapLoader.loadMapFromSave(activeMap == 1 ? storedMapString : activeMapString, 2);
        this.mapWallsLevel2 = map2.getWalls();

        player = mapLoader.createPlayer((this.activeMap==1)? map1:map2, playerModel);
        mapLoader.addInventoryToPlayer(inventoryModel, gameState, player);

        mapLoader.addRocketPokemons(gameState, map2);

        mapLoader.placeLootBoxes(map1, gameState);
        mapLoader.placeLootBoxes(map2, gameState);
        mapLoader.placePokemons(map1, gameState);
        mapLoader.placePokemons(map2, gameState);

        setUpCanvasContextStage(map1, map2, converter, pStage);
    }

    private void setUpCanvasContextStage(GameMap map1, GameMap map2, Converter converter, Stage pStage) {
        this.pStage = pStage;
        this.canvas = new Canvas(
                map1.getDisplayWidth() * Tiles.DEFAULT_TILE_WIDTH,
                map1.getDisplayHeight() * Tiles.DEFAULT_TILE_WIDTH);
        this.context = canvas.getGraphicsContext2D();
        this.addEnemyMoveHandler();
        this.converter = converter;
        this.converter.setMap1(map1);
        this.converter.setMap2(map2);
    }

    public Scene showGameScene() {
        GameMap map = this.activeMap == 1 ? this.map1 : this.map2;
        WindowElement.setLabels(this.levelLabel, this.nameLabel, this.actionsLabel, this.inventoryLabel, map);


        VBox rightPane = WindowElement.createRightPane(map.getPlayer().getInventory(), map, nameLabel, inventoryLabel, actionsLabel);
        VBox levelBox = WindowElement.createLevelBox(levelLabel);
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
        actionsStB.setLength(0);
        Inventory inventory = player.getInventory();
        boolean ends = false;
        switch (keyEvent.getCode()) {
            case UP:
                player.move(0, -1, Facing.UP, actionsStB);
                break;
            case DOWN:
                player.move(0, 1, Facing.DOWN, actionsStB);
                break;
            case LEFT:
                player.move(-1, 0, Facing.LEFT, actionsStB);
                break;
            case RIGHT:
                player.move(1,0, Facing.RIGHT, actionsStB);
                break;
            case R:
                map.getRocketGrunt().releasePokemon(map);
                break;
            case T:
                player.throwPokeBall(actionsStB, map);
                ends = checkIfGameEnds(inventory);
                break;
            case E:
                if (player.whatAmIStandingOn() instanceof Key){
                    inventory.addKey(player.getCell());
                    player.getCell().setItem(null);
                } else {
                    player.pickupItem(actionsStB);
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
                player.fightPokemon(actionsStB, map);
                ends = checkIfGameEnds(inventory);
                break;
            case H:
                inventory.heal();
                break;
            case C:
                converter.export(pStage);
                break;
            case S:
                if (keyEvent.isControlDown()){
                    String saveName = "";

                    while(true){
                        saveName = WindowElement.saveWindow(pStage, saveName);
                        if (saveName == null) break;
                        String playerName = player.getUserName();
                        if (converter.ifPlayerSaveExists(saveName, playerName)) {
                            boolean answer = WindowElement.confirmSaveWindow(pStage);
                            if (answer){
                                converter.update(saveName, playerName);
                                break;
                            }
                        } else {
                            converter.save(saveName);
                            break;
                        }
                    }
                }
        }
        if (!ends) refresh(inventory);
    }

    private void refresh(Inventory inventory) {
        GameMap map = this.activeMap == 1 ? this.map1 : this.map2;
        Cell[][] visibleMap = map.getVisibleMap();

        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        WindowElement.refreshRangeInfo(inRangeStB, actionsStB, actionsLabel, map);
        WindowElement.refreshLevelAndInventory(inventory, inventoryLabel, levelLabel, map);
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

    public boolean checkIfGameEnds(Inventory inventory){
        boolean ends = true;
        if (inventory.getActivePokemon() == null){
            enemyMove.stop();
            WindowElement.gameEndWindow(-1, pStage);
        } else if (map2.getRocketGrunt().getRocketPokemonList().size() == 0 && map2.getRocketGrunt().getRocketPokemonOnBoard().size() == 0){
            WindowElement.gameEndWindow(1, pStage);
        } else ends = false;
        return ends;
    }
    private void addEnemyMoveHandler() {
        enemyMove = new Timeline(
                new KeyFrame(Duration.seconds(1), (event) -> {
                    this.getActiveMap().moveAllPokemon();
                    refresh(player.getInventory()); }));
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

}
