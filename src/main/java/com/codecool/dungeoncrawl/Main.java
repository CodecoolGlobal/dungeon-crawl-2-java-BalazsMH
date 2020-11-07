package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.*;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.LootBox;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main extends Application {
    GameMap map = MapLoader.loadMap("level1");
    GameMap map2 = MapLoader.loadMap("level2");
    MapChanger mapChanger = new MapChanger(map, map2);

    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.DEFAULT_TILE_WIDTH,
            map.getHeight() * Tiles.DEFAULT_TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();


    Label nameLabel = new Label();
    Label currentInfo = new Label();
    Label currentLevel = new Label();
    StringBuilder text = new StringBuilder();
    String[] developers = new String[]{"Fruzsi", "Dani", "Peti", "Balázs"};


    public Inventory inventory = new Inventory();


    public static void main(String[] args) {
        launch(args);
    }

    private Scene mainMenu(Stage primaryStage, Scene game) {
        VBox mainPane = new VBox();
        mainPane.setPrefSize(1287/1.5,797/1.5);
        Background background = new Background(new BackgroundImage(new Image("/main_menu.png"),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO,
                                                              BackgroundSize.AUTO,
                                                                false, false, true, true)));
        mainPane.setBackground(background);
        TextField nameInput = new TextField();
        nameInput.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 14));
        nameInput.setMaxSize(220,220);
        nameInput.setPromptText("Enter your name ");
        Button submitButton = new Button("Play!");
        submitButton.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 14));
        mainPane.getChildren().addAll(nameInput, submitButton);
        mainPane.setAlignment(Pos.CENTER);
        Scene mainMenu = new Scene(mainPane);
        mainPane.requestFocus();

        submitButton.setOnMouseClicked((event)-> this.onSubmitPressed(primaryStage, game, event, nameInput));
        return mainMenu;
    }

    private Scene game() {
        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));
        nameLabel.setText(map.getPlayer().getUserName());
        ui.add(nameLabel, 0, 0);

        VBox infoBox = createInfoBox();
        VBox rightPane = new VBox(ui, infoBox);
        rightPane.setSpacing(100.00);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(canvas);
        borderPane.setRight(rightPane);
        currentLevel.setText(map.getLevel());
        borderPane.setTop(currentLevel);

        Scene scene = new Scene(borderPane);

        nameLabel.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 14));
        currentInfo.setFont(Font.loadFont("file:Pokemon_Classic.ttf",14));
        currentInfo.setWrapText(true);

        scene.setOnKeyPressed(this::onKeyPressed);

        return scene;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("JavaMon");
        primaryStage.getIcons().add(new Image("file:logo.png"));

        //Create Game
        Scene game = game();

        //Main menu
        Scene mainMenu = mainMenu(primaryStage, game);


        primaryStage.setScene(mainMenu);
        refresh();
        primaryStage.show();
    }

    private void onSubmitPressed(Stage primaryStage, Scene gameScene, MouseEvent mouseEvent, TextField nameInput) {
        String enteredName = nameInput.getText();
        System.out.println(enteredName);
        System.out.println("hello");
        map.getPlayer().setUserName(enteredName);
        if (Arrays.asList(developers).contains(enteredName)) {
            map.getPlayer().setSuperUser(true);
        }
        nameLabel.setText(map.getPlayer().getUserName());
        primaryStage.setScene(gameScene);
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        text.setLength(0);
        KeyCode keyPressed = keyEvent.getCode();
        switch (keyPressed) {
            case UP:
                map.getPlayer().setFacing("up");
                map.getPlayer().move(0, -1);
                refresh();
                break;
            case DOWN:
                map.getPlayer().setFacing("down");
                map.getPlayer().move(0, 1);
                refresh();
                break;
            case LEFT:
                map.getPlayer().setFacing("left");
                map.getPlayer().move(-1, 0);
                refresh();
                break;
            case RIGHT:
                map.getPlayer().setFacing("right");
                map.getPlayer().move(1,0);
                refresh();
                break;
            case T:
                map.getPlayer().throwPokeBall(inventory, text, getPokemonInRange(), map);
                refresh();
            case E:
                if (map.getPlayer().getCell().getItem() instanceof Key){
                    inventory.addKey(map.getPlayer().getCell());
                    map.getPlayer().getCell().setItem(null);
                    refresh();
                }
            case O:
                if (inventory.hasKey() && map.getPlayer().getCell().getType() == CellType.DOOR){
                    map.getPlayer().getCell().getDoor().setOpen();
                    map = mapChanger.changeMap(map);
                    refresh();
                }

        }
    }

    private void refresh() {
        context.setFill(new ImagePattern(Tiles.getFloorTile(), 0, 0, 960, 960, false));
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        moveAllPokemon();
        refreshInfoWindow();
        refreshLevelInfo();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else if (cell.getItem() != null){
                    Tiles.drawTile(context, cell.getItem(), x, y);
                } else if(cell.getPokemon() != null){
                    Tiles.drawTile(context, cell.getPokemon(), x, y);
                } else if(cell.getDoor() != null) {
                    Tiles.drawTile(context, cell.getDoor(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        //nameLabel.setText("Health:" + map.getPlayer().getHealth()); player health is unused.
    }

    private void moveAllPokemon() {
        List<Pokemon> pokemonList= map.getPokemonList();
        pokemonList.forEach(p -> p.move());
    }


    private void refreshLevelInfo() {
        currentLevel.setText(map.getLevel());
    }

    private void refreshInfoWindow() {
        Cell standingOn = map.getPlayer().getCell();
        if (standingOn.getDoor() != null){
            text.append("Open door by 'O'\n\n");
        } else if (standingOn.getItem() instanceof LootBox){
            text.append("Get content of Lootbox!\n\n");
        } else if (standingOn.getItem() instanceof Key){
            text.append("Pick up key by 'E'!\n\n");
        }
        if (getPokemonInRange().isPresent()) {
            text.append("\n\npokemon in fight range:\n");
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

    private VBox createInfoBox(){

        currentInfo.setWrapText(false);
        currentInfo.setPrefWidth(200);
        TextFlow textFlow = new TextFlow();
        textFlow.setPrefWidth(200);
        textFlow.getChildren().add(currentInfo);

        Image infoImage = new Image(String.valueOf(ClassLoader.getSystemResource("info.png")));
        Label infoTitle = new Label();
        infoTitle.setGraphic(new ImageView(infoImage));

        VBox infoBox = new VBox(infoTitle, textFlow);
//        infoBox.setAlignment(Pos.BASELINE_CENTER);
//        infoBox.setStyle("-fx-border-color: blue;-fx-padding: 10px;");
        infoBox.setPrefHeight(600);
        infoBox.setPrefWidth(200);

        infoBox.setSpacing(20);
        return infoBox;
    }
}
