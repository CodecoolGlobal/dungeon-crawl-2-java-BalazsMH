package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import com.codecool.dungeoncrawl.logic.items.Inventory;
import com.codecool.dungeoncrawl.logic.items.LootBox;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main extends Application {
    GameMap map = MapLoader.loadMap();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.DEFAULT_TILE_WIDTH,
            map.getHeight() * Tiles.DEFAULT_TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();

    Label healthLabel = new Label();
    Label currentInfo = new Label();
    StringBuilder text = new StringBuilder();
    public Inventory inventory = new Inventory();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));
        //ui.add(new Label("Health: "), 0, 0);   seems redundant
        ui.add(healthLabel, 1, 0);

        VBox infoBox = createInfoBox();
        VBox rightPane = new VBox(ui, infoBox);
        rightPane.setSpacing(100.00);

//        Button pickupButton = new Button("Pickup item");
//        ui.add(pickupButton, 0, 10);
        //pickupButton.setOnAction();

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(rightPane);

        Scene scene = new Scene(borderPane);

        //scene.getStylesheets().add(getClass().getResource("/fontstyle.css").toExternalForm());
        healthLabel.setFont(Font.loadFont("file:Pokemon_Classic.ttf", 14));
        currentInfo.setFont(Font.loadFont("file:Pokemon_Classic.ttf",14));
        currentInfo.setWrapText(true);


        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);

        primaryStage.setTitle("JavaMon");
        primaryStage.getIcons().add(new Image("file:logo.png"));

        primaryStage.show();
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
        }
    }

    private void refresh() {
        //context.setFill(Color.GRAY);
        context.setFill(new ImagePattern(Tiles.getFloorTile(), 0, 0, 960, 960, false));
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        moveAllPokemon();
        refreshInfoWindow();
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else if (cell.getItem() != null){
                    Tiles.drawTile(context, cell.getItem(), x, y);
                } else if(cell.getPokemon() != null){
                    Tiles.drawTile(context, cell.getPokemon(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        healthLabel.setText("Health:" + map.getPlayer().getHealth());
    }

    private void moveAllPokemon() {
        List<Pokemon> pokemonList= map.getPokemonList();
        pokemonList.forEach(p -> p.move());
    }

    private void refreshInfoWindow() {
        if (map.getPlayer().getCell().getItem() instanceof LootBox){
            text.append("Get content of Lootbox!\n\n");
        }
        if(getPokemonInRange().isPresent()) {
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
