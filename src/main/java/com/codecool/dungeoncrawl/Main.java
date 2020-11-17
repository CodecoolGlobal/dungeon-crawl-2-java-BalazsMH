package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.Converter;
import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.map.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import com.codecool.dungeoncrawl.logic.ui.WindowElement;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


public class Main extends Application {
    private static Stage pStage;
    boolean m = MapGenerator.generateMap(1);
    GameMap map = MapLoader.loadMap("Level1");
    List<List<Integer>> mapWallsLevel1 = MapLoader.getWalls();
    boolean mapReady = MapGenerator.generateMap(2);
    //TODO: figure out why it doesn't allow simply calling Mapgenerator with a void return value
    GameMap map2 = MapLoader.loadMap("Level2");
    List<List<Integer>> mapWallsLevel2 = MapLoader.getWalls();

    Timeline enemyMove;

    MapChanger mapChanger = new MapChanger(map, map2);

    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.DEFAULT_TILE_WIDTH,
            map.getHeight() * Tiles.DEFAULT_TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();


    Label nameLabel = new Label();
    Label inv = new Label();
    Label currentInfo = new Label();
    Label currentLevel = new Label();
    StringBuilder text = new StringBuilder();
    String[] developers = new String[]{"Fruzsi", "Dani", "Peti", "Balázs"};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        pStage = primaryStage;
        primaryStage.setTitle("JavaMon");
        primaryStage.getIcons().add(new Image("file:logo.png"));

        Scene game = game();
        Scene mainMenu = mainMenu(primaryStage, game);

        primaryStage.setScene(mainMenu);
        addEnemyMoveHandler();
        refresh(map.getPlayer().getInventory());
        primaryStage.show();
    }

    private Scene mainMenu(Stage primaryStage, Scene game) {
        TextField nameInput = WindowElement.createNameInput();
        Button newGameButton = WindowElement.createNewGameButton();
        Button loadGameButton = WindowElement.createLoadGameButton();
        //TODO: implement functionality for load game button.
        newGameButton.setOnMouseClicked((event)-> this.onSubmitPressed(primaryStage, game, nameInput));
        VBox mainPane = WindowElement.createMainPane(nameInput, newGameButton, loadGameButton);
        Scene mainMenu = new Scene(mainPane);


        //TODO:remove when testing finished
        Layout2 generationTest = new Layout2(20, 30, 1, MapType.GRASS);
/*        String[][] sea = generationTest.createSea(MapSide.SOUTH);
        for (String[]line : sea) {
            System.out.println(Arrays.toString(line));
        }*/
        generationTest.createEmptyBase();
        generationTest.placeSeaOnBase();
        String[][]layout = generationTest.getLayout();
        for (String[] line : layout){
            System.out.println(Arrays.toString(line));
        }

/*        String[][] forest = generationTest.createForest(MapSide.NORTH;
        for (String[]line : forest) {
            System.out.println(Arrays.toString(line));
        }*/


        return mainMenu;
    }

    private Scene game() {
        WindowElement.setLabels(currentLevel, nameLabel, currentInfo, inv, map);

        VBox rightPane = WindowElement.createRightPane(map.getPlayer().getInventory(), map, nameLabel, inv, currentInfo);
        VBox levelBox = WindowElement.createLevelBox(currentLevel);
        VBox bottom = WindowElement.createBottomBox();

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(canvas);
        borderPane.setBottom(bottom);
        borderPane.setRight(rightPane);
        borderPane.setTop(levelBox);

        Scene scene = new Scene(borderPane);
        scene.setOnKeyPressed(this::onKeyPressed);
        return scene;
    }

    private void onSubmitPressed(Stage primaryStage, Scene gameScene, TextField nameInput) {
        String enteredName = nameInput.getText();
        map.getPlayer().setUserName(enteredName);
        if (Arrays.asList(developers).contains(enteredName)) {
            map.getPlayer().setSuperUser(true);
        }
        nameLabel.setText(map.getPlayer().getUserName());
        primaryStage.setScene(gameScene);
    }

    private void onKeyPressed(KeyEvent keyEvent) {
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
                    map = mapChanger.changeMap(map);
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
                // TODO should be Ctrl + S
                Converter converter = new Converter(mapChanger.getActive(), mapChanger.getStored());
                converter.run();
        }
        refresh(inventory);
    }

    private void refresh(Inventory inventory) {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        WindowElement.refreshInfoWindow(text, currentInfo, map);
        WindowElement.refreshLevelAndInventory(inventory, inv, currentLevel, map);
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                Tiles.drawTile(context, cell, x, y);

                if (cell.getItem() != null){
                    Tiles.drawTile(context, cell.getItem(), x, y);
                }
                if(cell.getDoor() != null) {
                    Tiles.drawTile(context, cell.getDoor(), x, y);
                }
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                }

                if(cell.getPokemon() != null){
                    Tiles.drawTile(context, cell.getPokemon(), x, y);
                }

            }
        }
    }

    public void checkIfGameEnds(Inventory inventory){
        if (inventory.getActivePokemon() == null){
            WindowElement.gameEndWindow(EndCondition.LOSE, pStage);
        } else if (map2.getRocketGrunt().getRocketPokemonList().size() == 0 && map2.getRocketGrunt().getRocketPokemonOnBoard().size() == 0){
            WindowElement.gameEndWindow(EndCondition.WIN, pStage);
        }
    }
    private void addEnemyMoveHandler() {
        enemyMove = new Timeline(
                new KeyFrame(Duration.seconds(1), (event) -> {
                    map.moveAllPokemon(mapChanger, mapWallsLevel1,  mapWallsLevel2);
                    refresh(map.getPlayer().getInventory()); }));
        enemyMove.setCycleCount(Timeline.INDEFINITE);
        enemyMove.play();
    }
}
