package com.codecool.dungeoncrawl.logic.items;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Layout {
    private final int rows;
    private final int cols;
    private int[] startCoord;
    private int[] endCoord;
    private final int upperMargin;
    private final int lowerMargin;
    String[][] layout;
    String filename = "./src/main/resources/map.txt";

    public Layout(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        upperMargin = (int) (rows * cols * 0.8);
        lowerMargin = (int) (rows * cols * 0.6);
    }

    public void generateLayout(){
        while (true){
            createEmptyBase();
            markStartEnd();
            generatePath();
            if (checkIfWithinMargin(upperMargin, lowerMargin)) break;
        }
        markEdges();
        printToConsole(); // remove when done
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
    private void markStartEnd(){
        startCoord = new int[]{2,4}; // needs to be randomized
        layout[startCoord[0]][startCoord[1]] = "@";
        endCoord = new int[]{rows-3, cols-5}; // needs to be randomized
        layout[endCoord[0]][endCoord[1]] = "#"; //we need a character to signal door
    }
    private void generatePath(){
        int[] currentPosition = startCoord;
        while (! Arrays.equals(currentPosition, endCoord)){
            currentPosition = takeRandomStep(currentPosition);
            if (! Arrays.equals(currentPosition, startCoord) && ! Arrays.equals(currentPosition, endCoord)){
                layout[currentPosition[0]][currentPosition[1]] = ".";
            }
        }
    }
    private boolean checkIfWithinMargin(int upper, int lower){
        StringBuilder str = new StringBuilder();
        for (String[] strings : layout) {
            str.append(String.join("",strings));
        }
        String map = str.toString();
        int floorNum = rows * cols - map.replace(".", "").length();
        if (floorNum > upper || floorNum < lower) return false;
        return true;
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
                if (layout[r][c].equals(" ") && hasNeigbour(r, c)){
                    layout[r][c] = "#";
                }
            }
        }
    }

    private boolean hasNeigbour(int r, int c) {
        return layout[r][c].equals(" ") &&
                        (layout[(r + 1 < rows)? r + 1 : r][c].equals(".") ||
                        layout[(r - 1 >= 0) ? r-1 : r][c].equals(".") ||
                        layout[r][(c + 1 < cols) ? c + 1 : c].equals(".") ||
                        layout[r][(c - 1 >= 0)? c - 1 : c].equals("."));
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
            System.out.println(e.getMessage());
            System.exit(1);
        }
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
