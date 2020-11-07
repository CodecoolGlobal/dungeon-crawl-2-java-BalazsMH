package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.actors.pokemon.Pokemon;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    GameMap map = MapLoader.loadMap();
    List<List<Integer>> walls = MapLoader.getWalls();
    Skeleton skeleton = map.getSkeleton();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 1, 0);

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);

        primaryStage.setTitle("Dungeon Crawl");
        primaryStage.show();
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case W:
                map.getPlayer().move(0, -1);
                refresh();
                break;
            case S:
                map.getPlayer().move(0, 1);
                refresh();
                break;
            case A:
                map.getPlayer().move(-1, 0);
                refresh();
                break;
            case D:
                map.getPlayer().move(1, 0);
                refresh();
                break;
        }
    }

    private void refresh() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        System.out.println(skeletonIsSeeingPlayer());

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x, y);
                } else if (cell.getPokemon() != null) {
                    Tiles.drawTile(context, cell.getPokemon(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
    }

    private void moveAllPokemon() {
        List<Pokemon> pokemonList = map.getPokemonList();
        pokemonList.forEach(p -> p.move());
    }

    private void moveSkeleton() {
        List playerCoordinates = map.returnPlayerCoordinates();
        int skeletonX = skeleton.getX();
        int skeletonY = skeleton.getY();
        skeleton.findPlayer(playerCoordinates);
    }

    private double distanceBetweenPlayerAndSkeleton() {
        List playerCoordinates = map.returnPlayerCoordinates();
        int playerX = (int) playerCoordinates.get(0);
        int playerY = (int) playerCoordinates.get(1);
        int skeletonX = skeleton.getX();
        int skeletonY = skeleton.getY();
        double distance = Math.sqrt(Math.pow((playerX - skeletonX), 2) + Math.pow((playerY - skeletonY), 2));
        return distance;
    }

    private double skeletonPlayerDegree() {
        double degree = 0;
        double distance = distanceBetweenPlayerAndSkeleton();
        List playerCoordinates = map.returnPlayerCoordinates();
        int playerX = (int) playerCoordinates.get(0);
        int playerY = (int) playerCoordinates.get(1);
        int skeletonX = skeleton.getX();
        int skeletonY = skeleton.getY();
        if (skeletonY == playerY) {
            degree = 90;
        }
        if (skeletonX < playerX) {
            degree = Math.toDegrees(Math.asin((playerY - skeletonY) / distance));
        }
        if (skeletonX > playerX) {
            degree = -1.00 * Math.toDegrees(Math.asin(Math.abs((playerY - skeletonY)) / distance));
        }
        if (skeletonY > playerY) {
            degree = 999;
        }
        return degree;
    }

    private List<List<Integer>> necessaryWallsFinder() {
        List<List<Integer>> playerIsBehind = returnWithMinusOne();
        double degree = skeletonPlayerDegree();
        if (degree == 999) return playerIsBehind;

        List<List<Integer>> necessaryWalls = new ArrayList<>();
        List playerCoordinates = map.returnPlayerCoordinates();
        int playerX = (int) playerCoordinates.get(0);
        int playerY = (int) playerCoordinates.get(1);
        int skeletonX = skeleton.getX();
        int skeletonY = skeleton.getY();

        for (int i = 0; i < walls.size(); i++) {
            if (90 > degree && degree > 0) {
                /*System.out.println("Skeleton - wall: "+skeletonWallsDegree(walls.get(i).get(0), walls.get(i).get(1)));
                System.out.println("Skeleton - Player: "+degree);
                System.out.println(walls.get(i).get(0) + " " + walls.get(i).get(1));
                System.out.println(" ");*/
                if ((walls.get(i).get(0) >= skeletonX && walls.get(i).get(0) <= playerX) &&
                        (walls.get(i).get(1) >= skeletonY && walls.get(i).get(1) <= playerY)) {
                            necessaryWalls.add(walls.get(i));
                }
            }
                if (degree < 0) {
                    if ((walls.get(i).get(0) <= skeletonX && walls.get(i).get(0) >= playerX) &&
                            (walls.get(i).get(1) >= skeletonY && walls.get(i).get(1) <= playerY)) {
                        necessaryWalls.add(walls.get(i));
                    }
                }
        }
//        System.out.println("-- end --");
        if (necessaryWalls.size() == 0) {return playerIsBehind;}

//        System.out.println(necessaryWalls);
        return necessaryWalls;
    }


    private List<List<Integer>> fieldsBehindWallsFinder() {
        List<List<Integer>> walls = necessaryWallsFinder();
        List<List<Integer>> fieldsBehindWalls = new ArrayList<>();
        List<List<Integer>> fields = new ArrayList<>();
        List playerCoordinates = map.returnPlayerCoordinates();
        int playerX = (int) playerCoordinates.get(0);
        int playerY = (int) playerCoordinates.get(1);
        int skeletonX = skeleton.getX();
        int skeletonY = skeleton.getY();
        int fieldsQuantity = (Math.abs((playerX-skeletonX))+1) * (Math.abs((playerY-skeletonY))+1);

        for (int i = 0; i < fieldsQuantity;i++) {
            List<Integer> tmp = new ArrayList<>();
            tmp.add(skeletonX);
            tmp.add(skeletonY);
            fields.add(tmp);
            if (skeletonX<playerX) {
                skeletonX++;
            }
            else if (skeletonX == playerX) {
                skeletonY++;
                skeletonX = skeleton.getX();
            }
        }

        for (List<Integer> wall : walls) {
            for (List<Integer> field : fields) {
                if ((field.get(0) >= wall.get(0)) && (field.get(1) >= wall.get(1)) && !(fieldsBehindWalls.contains(field))
                        && !(walls.contains(field))) {
                    fieldsBehindWalls.add(field);
                }
            }
        }

        return fieldsBehindWalls;
    }


    private List<List<Integer>> fieldsCannotBeSeenBySkeletonFinder() {
        List<List<Integer>> fieldsCannotBeSeenBySkeleton = new ArrayList<>();
        List<List<Integer>> walls = necessaryWallsFinder();
        List<List<Integer>> fieldsBehindWalls = fieldsBehindWallsFinder();
        int skeletonX = skeleton.getX();
        int skeletonY = skeleton.getY();

        for (List<Integer> wall : walls) {
            double skeletonWallDegree = skeletonFieldsDegree(wall.get(0), wall.get(1));
            for (List<Integer> field : fieldsBehindWalls) {
                double skeletonFieldDegree = skeletonFieldsDegree(field.get(0), field.get(1));
                if ((skeletonFieldDegree < skeletonWallDegree) && !(fieldsCannotBeSeenBySkeleton.contains(field)) ) {
                    fieldsCannotBeSeenBySkeleton.add(field);
                }
            }
        }
        return fieldsCannotBeSeenBySkeleton;
    }


    private double distanceBetweenFields(int x, int y) {
        int skeletonX = skeleton.getX();
        int skeletonY = skeleton.getY();
        double distance = Math.sqrt(Math.pow((x - skeletonX), 2) + Math.pow((y - skeletonY), 2));
        return distance;
    }

    private double skeletonFieldsDegree(int x, int y) {
        double degree = 0;
        double distance = distanceBetweenFields(x, y);
        int skeletonX = skeleton.getX();
        int skeletonY = skeleton.getY();
        if (skeletonY == y) {
            degree = 90;
        }
        if (skeletonX < x) {
            degree = Math.toDegrees(Math.asin((y - skeletonY) / distance));
        }
        if (skeletonX > x) {
            degree = -1.00 * Math.toDegrees(Math.asin(Math.abs((y - skeletonY)) / distance));
        }
        if (skeletonY > y) {
            degree = 999;
        }
        return degree;
    }

    private boolean skeletonIsSeeingPlayer() {
        List<List<Integer>> fieldsCannotBeSeenBySkeleton = fieldsCannotBeSeenBySkeletonFinder();
        List playerCoordinates = map.returnPlayerCoordinates();
        int playerX = (int) playerCoordinates.get(0);
        int playerY = (int) playerCoordinates.get(1);
        List<Integer> pc = new ArrayList<>();
        pc.add(playerX);
        pc.add(playerY);
        if (fieldsCannotBeSeenBySkeleton.contains(pc)) return false;
        else return true;
    }

    private List<List<Integer>> returnWithMinusOne () {
        List<List<Integer>> playerIsBehind = new ArrayList<>();
        List<Integer> minusOne = new ArrayList<>();
        minusOne.add(-1);
        playerIsBehind.add(minusOne);
        return playerIsBehind;
    }
    }