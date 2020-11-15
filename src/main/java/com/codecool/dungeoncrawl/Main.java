package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.MapGenerator;

import com.codecool.dungeoncrawl.logic.ui.LayoutItem;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class Main extends Application {
    private static Stage pStage;
    boolean m = MapGenerator.generateMap(1);
    GameMap map = MapLoader.loadMap("Level1");
    List<List<Integer>> mapWallsLevel1 = MapLoader.getWalls();
    boolean mapReady = MapGenerator.generateMap(2);
    //TODO: figure out why it doesn't allow simply calling Mapgenerator with a void return value
    GameMap map2 = MapLoader.loadMap("Level2");
    List<List<Integer>> mapWallsLevel2 = MapLoader.getWalls();


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
        refresh(map.getPlayer().getInventory());
        primaryStage.show();
    }

    private Scene mainMenu(Stage primaryStage, Scene game) {
        TextField nameInput = LayoutItem.createNameInput();
        Button submitButton = LayoutItem.createSubmitButton();
        submitButton.setOnMouseClicked((event)-> this.onSubmitPressed(primaryStage, game, nameInput));
        VBox mainPane = LayoutItem.createMainPane(nameInput, submitButton);
        Scene mainMenu = new Scene(mainPane);

        return mainMenu;
    }

    private Scene game() {
        LayoutItem.setLabels(currentLevel, nameLabel, currentInfo, inv, map);

        VBox rightPane = LayoutItem.createRightPane(map.getPlayer().getInventory(), map, nameLabel, inv, currentInfo);
        VBox levelBox = LayoutItem.createLevelBox(currentLevel);
        VBox bottom = LayoutItem.createBottomBox();

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
        KeyCode keyPressed = keyEvent.getCode();
        switch (keyPressed) {
            case UP:
                map.getPlayer().setFacing("up");
                map.getPlayer().move(0, -1);
                break;
            case DOWN:
                map.getPlayer().setFacing("down");
                map.getPlayer().move(0, 1);
                break;
            case LEFT:
                map.getPlayer().setFacing("left");
                map.getPlayer().move(-1, 0);
                break;
            case RIGHT:
                map.getPlayer().setFacing("right");
                map.getPlayer().move(1,0);
                break;
            case R:
                map.getRocketGrunt().releasePokemon(map);
                break;
            case T:
                map.getPlayer().throwPokeBall(text, getPokemonInRange(), map);
                checkIfGameEnds(inventory);
                break;
            case E:
                if (map.getPlayer().getCell().getItem() instanceof Key){
                    inventory.addKey(map.getPlayer().getCell());
                    map.getPlayer().getCell().setItem(null);
                } else {
                    map.getPlayer().pickupItem(text);
                }
                break;
            case O:
                if (inventory.hasKey() && map.getPlayer().getCell().getType() == CellType.DOOR){
                    map.getPlayer().getCell().getDoor().setOpen();
                    map = mapChanger.changeMap(map);
                }
                break;
            case A:
                inventory.changeActivePokemon();
                break;
            case F:
                map.getPlayer().fightPokemon(text, getPokemonInRange(), map);
                checkIfGameEnds(inventory);
                break;
            case H:
                inventory.heal();
                break;
        }
        refresh(inventory);
    }

    private void refresh(Inventory inventory) {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        moveAllPokemon();
        refreshInfoWindow();
        refreshLevelAndInventory(inventory);
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

    private void moveAllPokemon() {
        int level = mapChanger.getLevel();
        List<List<Integer>> mapWalls = (level == 1) ? mapWallsLevel1 : mapWallsLevel2;
        List<Pokemon> pokemonList= map.getPokemonList();
        List playerCoordinates = map.returnPlayerCoordinates();
        for (Pokemon pokemon : pokemonList) {
            if (map.getRocketGrunt() != null && map.getRocketGrunt().getRocketPokemonOnBoard().contains(pokemon)) pokemon.move();
            else {
                pokemon.attackMove(mapWalls, playerCoordinates, pokemon.getX(), pokemon.getY());
            }
        }
    }

    public void checkIfGameEnds(Inventory inventory){
        if (inventory.getActivePokemon() == null){
            LayoutItem.gameEndWindow(EndCondition.LOSE, pStage);
        } else if (map2.getRocketGrunt().getRocketPokemonList().size() == 0 && map2.getRocketGrunt().getRocketPokemonOnBoard().size() == 0){
            LayoutItem.gameEndWindow(EndCondition.WIN, pStage);
        }
    }

    private void refreshLevelAndInventory(Inventory inventory) {
        inv.setText(inventory.toString());
        currentLevel.setText(map.getLevel());
    }

    private void refreshInfoWindow() {
        Cell standingOn = map.getPlayer().getCell();
        if (standingOn.getDoor() != null){
            text.append("\nOpen door by 'O'\n");
        } else if (standingOn.getItem() != null){
            text.append(String.format("\nPick up %s by 'E'!\n", standingOn.getItem().getTileName()));
        }
        if (getPokemonInRange().isPresent()) {
            text.append("\n\nPokemon in range:\n");
            getPokemonInRange().get().forEach(p -> text.append("\n" + p.toString()));
        }
        currentInfo.setText(text.toString());
    }

    private Optional<List<Pokemon>> getPokemonInRange() {
        Optional<List<Pokemon>> toReturn = Optional.empty();
        List<Pokemon> pokemonInRange = new ArrayList<Pokemon>();
        int playerX = map.getPlayer().getCell().getX();
        int playerY = map.getPlayer().getCell().getY();
        List<Pokemon> pokemonList= map.getPokemonList();
        pokemonList.forEach(p -> {
            if (Math.abs(p.getCell().getX() - playerX) + Math.abs(p.getCell().getY() - playerY) <= 3){
                pokemonInRange.add(p);
                currentInfo.setText(p.toString());
            }
        });
        if (pokemonInRange.size() > 0) toReturn = Optional.of(pokemonInRange);
        return toReturn;
    }
}
