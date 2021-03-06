package com.codecool.dungeoncrawl.logic.map;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Layout {
    private final int rows;
    private final int cols;
    private final int upperMargin;
    private final int lowerMargin;
    List<String> pokemonAndItemList = new ArrayList<String>();
    String[][] layout;
    String filename;

    public Layout(int rows, int cols, int level){
        this.rows = rows;
        this.cols = cols;
        upperMargin = (int) (rows * cols * 0.8);
        lowerMargin = (int) (rows * cols * 0.6);
        if (level == 1){
            this.filename = "./src/main/java/com/codecool/dungeoncrawl/logic/map/map1.txt";
            this.pokemonAndItemList.addAll(List.of("C", "S", "B", "B", "k", "L", "L", "@", "d"));
        } else if (level == 2){
            this.filename = "./src/main/java/com/codecool/dungeoncrawl/logic/map/map2.txt";
            this.pokemonAndItemList.addAll(List.of("C", "S", "R", "L", "d"));
        }
    }

    public void generateLayout(){
        while (true){
            createEmptyBase();
            generatePath();
            if (checkIfWithinMargin(upperMargin, lowerMargin)) break;
        }
        markEdges();
        markBoardEdges();
        addPokemonAndItems();
        writeTxt();
    }

    private void createEmptyBase(){
        this.layout = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                layout[i][j] = " ";
            }
        }
    }
    private void generatePath(){
        int[] startCoord = new int[]{(int) ((Math.random() * (rows/3 - 1)) + 1),(int) ((Math.random() * (cols/3 - 1)) + 1)};
        int[] endCoord = new int[]{(int) (Math.random() * (rows-1 - (rows-rows/3)) + (rows-rows/3)),
                             (int) ((Math.random() * (cols-1 - (cols-cols/3))) + (cols-1 - (cols-cols/3)))};
        int[] currentPosition = startCoord;
        while (! Arrays.equals(currentPosition, endCoord)){
            currentPosition = takeRandomStep(currentPosition);
            layout[currentPosition[0]][currentPosition[1]] = ".";
            }
    }
    private boolean checkIfWithinMargin(int upper, int lower){
        StringBuilder str = new StringBuilder();
        for (String[] strings : layout) {
            str.append(String.join("",strings));
        }
        String map = str.toString();
        int floorNum = rows * cols - map.replace(".", "").length();
        return floorNum <= upper && floorNum >= lower;
    }
    private int[] takeRandomStep(int[] currentPosition){
        int r = currentPosition[0];
        int c = currentPosition[1];
        int rDir;
        int cDir;
        if (Math.random() < 0.5){
            rDir = (Math.random() < 0.5)? -1 : 1;
            cDir = 0;
        } else {
            rDir = 0;
            cDir = (Math.random() < 0.5)? -1 : 1;
        }

        int newR = (r + rDir < rows && r + rDir >= 0) ? r + rDir : r + -rDir;
        int newC = (c + cDir < cols && c + cDir >= 0) ? c + cDir : c + -cDir;
        return new int[]{newR, newC};
    }
    private void markEdges() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (layout[r][c].equals(" ") && (hasNeigbour(r, c, ".") || hasNeigbour(r, c, "@"))){
                    layout[r][c] = "#";
                }
            }
        }
    }
    private void markBoardEdges() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if ((r == 0 || r == rows - 1) && layout[r][c].equals(".")) layout[r][c] = "#";
                if ((c == 0 || c == cols - 1) && layout[r][c].equals(".")) layout[r][c] = "#";
            }
        }
    }
    private boolean hasNeigbour(int r, int c, String target) {
        return layout[r][c].equals(" ") &&
                        (layout[(r + 1 < rows)? r + 1 : r][c].equals(target) ||
                        layout[(r - 1 >= 0) ? r-1 : r][c].equals(target) ||
                        layout[r][(c + 1 < cols) ? c + 1 : c].equals(target) ||
                        layout[r][(c - 1 >= 0)? c - 1 : c].equals(target));
    }
    private void writeTxt() {
        try{
            File file = new File(filename);
            FileWriter fw = new FileWriter(file);
            fw.write(String.format("%s %s" + System.lineSeparator(), cols, rows));
            for (String[] row: layout){
                fw.write(String.join("", row) + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e){
            System.out.println("could not write file");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
    private void addPokemonAndItems() {
        pokemonAndItemList.forEach(s -> {
            while (true){
                int r = (int) (Math.random() * (rows - 1) + 1);
                int c = (int) (Math.random() * (cols - 1) + 1);
                if (layout[r][c].equals(".")){
                    layout[r][c] = s;
                    break;
                }
            }
        });
    }

    private void printToConsole() {
        for (int i = 0; i < rows; i++) {
            System.out.println(lineToString(layout[i]));
        }
    }
    private String lineToString(String[] line){
        return String.join("",line);
    }
}
