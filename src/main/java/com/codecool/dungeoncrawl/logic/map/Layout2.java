package com.codecool.dungeoncrawl.logic.map;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class Layout2 {
    private final int rows;
    private final int cols;
    private final int upperMargin;
    private final int lowerMargin;
    List<String> pokemonAndItemList = new ArrayList<String>();
    String[][] layout;
    String filename;

    private final MapType mapType;
    private final int seaSideNumber;
    private final int treeSideNumber;
    private final List<MapSide> unpopulatedSides;
    private final int sideNumber = 4;


    public Layout2(int rows, int cols, int level, MapType mapType){
        this.rows = rows;
        this.cols = cols;
        this.mapType = mapType;
        this.seaSideNumber = (int) ((Math.random() * (2)) + 0);
        this.treeSideNumber = (int) ((Math.random() * (4 - this.seaSideNumber - 1)) + 0);
        this.unpopulatedSides = List.of(MapSide.values());

        upperMargin = (int) (rows * cols * 0.8);
        lowerMargin = (int) (rows * cols * 0.6);
        if (level == 1){
            this.filename = "./src/main/resources/map.txt";
            this.pokemonAndItemList.addAll(List.of("C", "S", "B", "B", "k", "L", "L", "@", "d"));
        } else if (level == 2){
            this.filename = "./src/main/resources/map2.txt";
            this.pokemonAndItemList.addAll(List.of("C", "S", "R", "L", "d"));
        }
    }

    /** Calling generateLayout creates new .txt
     * TODO BUG fix: map generated will be used in next game (not the current one)
     * -> program works properly when ran for the second time
     */
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

    public int getSeaSideNumber() {
        return seaSideNumber;
    }

    public int getTreeSideNumber() {
        return treeSideNumber;
    }

    private void createEmptyBase(){
        this.layout = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                layout[i][j] = MapType.getTileCode(this.mapType);
            }
        }
    }

    private void placeSeaOnBase(){
        Stream<MapSide> sides = this.unpopulatedSides.stream();
        Iterator<MapSide> iterator = sides.iterator();
        for (int s = 0; s <this.sideNumber; s++) {
            MapSide side = iterator.next();
            for (int i = 0; i<this.seaSideNumber; i++) {

            }
        }
        //pick a side from the remaining sides and place the sea on the map;
    }

    private void placeForestOnBase(){
        //pick a side from the remaining sides and place the forest on the map;
        //Layout has to be initialized with a list, with the available sides, which will decrease as the elements
        //are placed
    }



    public String[][] createSea(MapSide placement) {
        int maxWidth = 4;
        int minWidth = 1;
        int seaWidth;
        String[][] sea;
        switch (placement) {
            case NORTH:
            case SOUTH:
                sea = new String[this.cols][maxWidth];
                break;
            case EAST:
            case WEST:
            default:
                sea = new String[this.rows][maxWidth];
        }
        for (int i=0; i<sea.length; i++) {
            seaWidth = (int) ((Math.random() * (maxWidth-minWidth)) + minWidth);
            for (int j = 0; j<seaWidth;j++) {
                sea[i][j] = MapType.getTileCode(MapType.WATER);

            }
        }
        //transpose sea if it is on the top or the bottom
        if (placement == MapSide.NORTH || placement == MapSide.SOUTH) {
            String[][] transposedSea = new String[maxWidth][this.cols];
            for (int x = 0; x < maxWidth; x++) {
                for (int y = 0; y<this.cols; y++) {
                    transposedSea[x][y] = sea[y][x];
                }
            }
            return transposedSea;
        }
        return sea;
    }

    public String[][] createForest(MapSide placement){
        int maxWidth = 3;
        int minWidth = 1;
        int forestWidth;
        boolean singleWidth = false;
        String[][] forest;
        switch (placement) {
            case NORTH:
            case SOUTH:
                forest = new String[this.cols][maxWidth];
                break;
            case EAST:
            case WEST:
            default:
                forest = new String[this.rows][maxWidth];
        }

        if (placement == MapSide.EAST || placement == MapSide.WEST) {
            for (int i=0; i<forest.length; ) {
                int numberOfSameType = (int) ((Math.random() * (5-1)) + 1);
                System.out.println(numberOfSameType);
                for (int t = 0; t<numberOfSameType; t++) {
                    try {
                        if (!singleWidth) {
                            forest[i][0] = "468";
                            forest[i][1] = "469";
                            forest[i+1][0] = "476";
                            forest[i+1][1] = "477";
                            i = i+2;
                        } else {
                            forest[i][0] = placement == MapSide.EAST ? "469" : "468";
                            forest[i+1][0] = placement == MapSide.EAST ? "485" : "484";
                            i = i+2;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {}
                }
                singleWidth = !singleWidth;

            }
        } else {
            for (int i=0; i<forest.length; ) {
                TreeType currentType = TreeType.getRandomTreeType();
                int numberOfSameType = (int) ((Math.random() * (4-2)) + 2);
                System.out.println(numberOfSameType);
                for (int t = 0; t<numberOfSameType; t++) {
                    try {
                        if (currentType == TreeType.VERTICAL_FULL) {
                            forest[i][0] = placement == MapSide.SOUTH? "476" : "476";
                            forest[i][1] = placement == MapSide.SOUTH? "468" : "468";
                            forest[i][2] = placement == MapSide.SOUTH? "462" : "484";
                            forest[i+1][0] = placement == MapSide.SOUTH? "477" : "477";
                            forest[i+1][1] = placement == MapSide.SOUTH? "469" : "469";
                            forest[i+1][2] = placement == MapSide.SOUTH? "463" : "485";
                            i = i+2;
                        } else if (currentType == TreeType.VERTICAL_HALF) {
                            forest[i][0] = placement == MapSide.SOUTH? "468" : "468";
                            forest[i][1] = placement == MapSide.SOUTH? "462" : "484";
                            forest[i+1][0] = placement == MapSide.SOUTH? "469" : "469";
                            forest[i+1][1] = placement == MapSide.SOUTH? "463" : "485";
                            i = i+2;
                        } else if (currentType == TreeType.VERTICAL_TOP_ONLY) {
                            forest[i][0] = placement == MapSide.SOUTH? "462" : "484";
                            forest[i+1][0] = placement == MapSide.SOUTH? "463" : "485";
                            i = i+2;
                        }
                    } catch (ArrayIndexOutOfBoundsException ignored) {}
                }

            }

        }
        //transpose forest if it is on the top or the bottom
        if (placement == MapSide.NORTH || placement == MapSide.SOUTH) {
            String[][] transposedForest = new String[maxWidth][this.cols];
            for (int x = 0; x < maxWidth; x++) {
                for (int y = 0; y<this.cols; y++) {
                    transposedForest[x][y] = forest[y][x];
                }
            }
            return transposedForest;
        }
        return forest;
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
