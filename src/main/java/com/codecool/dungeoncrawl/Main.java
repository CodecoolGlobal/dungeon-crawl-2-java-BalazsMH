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
    List<List<Integer>> mapWalls = MapLoader.getWalls();
    Skeleton npc = map.getSkeleton();
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
        moveNpc();

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

    private void moveNpc() {
        List playerCoordinates = map.returnPlayerCoordinates();
        int npcX = npc.getX();
        int npcY = npc.getY();
        boolean isSeeing = npcIsSeeingPlayer(playerCoordinates, npcX, npcY);
        System.out.println(isSeeing);

        npc.findPlayer(playerCoordinates);
    }


    private double distanceBetweenPlayerAndNpc(List playerCoordinates, int npcX, int npcY) {
        int playerX = (int) playerCoordinates.get(0);
        int playerY = (int) playerCoordinates.get(1);
        double distance = Math.sqrt(Math.pow((playerX - npcX), 2) + Math.pow((playerY - npcY), 2));
        return distance;
    }

    private double npcPlayerDegree(List playerCoordinates, int npcX, int npcY) {
        double degree = 0;
        double distance = distanceBetweenPlayerAndNpc(playerCoordinates, npcX, npcY);
        int playerX = (int) playerCoordinates.get(0);
        int playerY = (int) playerCoordinates.get(1);
        if (npcY == playerY) {
            degree = 90;
        }
        if (npcX < playerX) {
            degree = Math.toDegrees(Math.asin((playerY - npcY) / distance));
        }
        if (npcX > playerX) {
            degree = -1.00 * Math.toDegrees(Math.asin(Math.abs((playerY - npcY)) / distance));
        }
        if (npcY > playerY) {
            degree = 999;
        }
        if (degree == -0.0) {
            degree = 0;
        }
        return degree;
    }

    private List<List<Integer>> necessaryWallsFinder(List<List<Integer>> walls, List playerCoordinates, int npcX, int npcY) {
        List<List<Integer>> playerIsBehind = returnWithMinusOne();
        double degree = npcPlayerDegree(playerCoordinates, npcX, npcY);
        if (degree == 999) return playerIsBehind;

        List<List<Integer>> necessaryWalls = new ArrayList<>();
        int playerX = (int) playerCoordinates.get(0);
        int playerY = (int) playerCoordinates.get(1);

        for (int i = 0; i < walls.size(); i++) {
            if (90 > degree && degree >= 0) {
                if ((walls.get(i).get(0) >= npcX && walls.get(i).get(0) <= playerX) &&
                        (walls.get(i).get(1) >= npcY && walls.get(i).get(1) <= playerY)) {
                            necessaryWalls.add(walls.get(i));
                }
            }
                if (degree < 0) {
                    if ((walls.get(i).get(0) <= npcX && walls.get(i).get(0) >= playerX) &&
                            (walls.get(i).get(1) >= npcY && walls.get(i).get(1) <= playerY)) {
                        necessaryWalls.add(walls.get(i));
                    }
                }
        }
        if (necessaryWalls.size() == 0) {return playerIsBehind;}
        return necessaryWalls;
    }


    private List<List<Integer>> fieldsBehindWallsFinder(List<List<Integer>> walls, List playerCoordinates, int npcX, int npcY) {
        List<List<Integer>> mapWalls = necessaryWallsFinder(walls, playerCoordinates, npcX, npcY);
        List<List<Integer>> fieldsBehindWalls = new ArrayList<>();
        List<List<Integer>> fields = new ArrayList<>();
        int playerX = (int) playerCoordinates.get(0);
        int playerY = (int) playerCoordinates.get(1);
        int fieldsQuantity = (Math.abs((playerX-npcX))+1) * (Math.abs((playerY-npcY))+1);
        double degree = npcPlayerDegree(playerCoordinates, npcX, npcY);

        if (90 > degree && degree >= 0 ) {
            for (int i = 0; i < fieldsQuantity;i++) {
                List<Integer> tmp = new ArrayList<>();
                tmp.add(npcX);
                tmp.add(npcY);
                fields.add(tmp);
                if (npcX<playerX) {
                    npcX++;
                }
                else if (npcX == playerX) {
                    npcY++;
                    npcX = npc.getX();
                }
            }

            for (List<Integer> wall : mapWalls) {
                for (List<Integer> field : fields) {
                    if ((field.get(0) >= wall.get(0)) && (field.get(1) >= wall.get(1)) && !(fieldsBehindWalls.contains(field))
                            && !(walls.contains(field))) {
                        fieldsBehindWalls.add(field);
                    }
                }
            }
        }

        else if (degree <= 0) {
            for (int i = 0; i < fieldsQuantity;i++) {
                List<Integer> tmp = new ArrayList<>();
                tmp.add(npcX);
                tmp.add(npcY);
                fields.add(tmp);
                if (npcX > playerX) {
                    npcX--;
                }
                else if (npcX == playerX) {
                    npcY++;
                    npcX = npc.getX();
                }
            }
            for (List<Integer> wall : mapWalls) {
                for (List<Integer> field : fields) {
                    if ((field.get(0) <= wall.get(0)) && (field.get(1) >= wall.get(1)) && !(fieldsBehindWalls.contains(field))
                            && !(walls.contains(field))) {
                        fieldsBehindWalls.add(field);
                    }
                }
            }
        }

        return fieldsBehindWalls;
    }


    private List<List<Integer>> fieldsCannotBeSeenByNpcFinder(List<List<Integer>> walls, List playerCoordinates, int npcX, int npcY) {
        List<List<Integer>> fieldsCannotBeSeenByNpc = new ArrayList<>();
        List<List<Integer>> mapWalls = necessaryWallsFinder(walls, playerCoordinates, npcX, npcY);
        List<List<Integer>> fieldsBehindWalls = fieldsBehindWallsFinder(walls, playerCoordinates, npcX, npcY);
        double degree = npcPlayerDegree(playerCoordinates, npcX, npcY);

        for (List<Integer> wall : mapWalls) {
            double npcWallDegree = npcFieldsDegree(wall.get(0), wall.get(1), npcX, npcY);
            for (List<Integer> field : fieldsBehindWalls) {
                if (90 > degree && degree >= 0 ) {
                    double npcFieldDegree = npcFieldsDegree(field.get(0), field.get(1), npcX, npcY);
                    if ((npcFieldDegree <= npcWallDegree+2) && !(fieldsCannotBeSeenByNpc.contains(field)) ) {
                        fieldsCannotBeSeenByNpc.add(field);
                    }
                }
                if (degree <= 0) {
                    double npcFieldDegree = npcFieldsDegree(field.get(0), field.get(1), npcX, npcY);
                    if ((npcFieldDegree >= npcWallDegree-2) && !(fieldsCannotBeSeenByNpc.contains(field)) ) {
                        fieldsCannotBeSeenByNpc.add(field);
                    }
                }
            }
        }
        return fieldsCannotBeSeenByNpc;
    }


    private double distanceBetweenFields(int x, int y, int npcX, int npcY) {
        double distance = Math.sqrt(Math.pow((x - npcX), 2) + Math.pow((y - npcY), 2));
        return distance;
    }

    private double npcFieldsDegree(int x, int y, int npcX, int npcY) {
        double degree = 0;
        double distance = distanceBetweenFields(x, y, npcX, npcY);

        if (npcY == y) {
            degree = 90;
        }
        if (npcX < x) {
            degree = Math.toDegrees(Math.asin((y - npcY) / distance));
        }
        if (npcX > x) {
            degree = -1.00 * Math.toDegrees(Math.asin(Math.abs((y - npcY)) / distance));
        }
        if (npcY > y) {
            degree = 999;
        }
        if (degree == -0.0) {
            degree = 0;
        }
        return degree;
    }

    private boolean npcIsSeeingPlayer(List playerCoordinates, int npcX, int npcY) {
        List<List<Integer>> fieldsCannotBeSeenByNpc = fieldsCannotBeSeenByNpcFinder(playerCoordinates,npcX, npcY);
        int playerX = (int) playerCoordinates.get(0);
        int playerY = (int) playerCoordinates.get(1);
        List<Integer> pc = new ArrayList<>();
        pc.add(playerX);
        pc.add(playerY);
        if (fieldsCannotBeSeenByNpc.contains(pc)) return false;
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