package com.codecool.dungeoncrawl.logic.actors.pokemon;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Pokemon implements Drawable {
    private boolean isActive = false;
    private int pokeHealth;
    private int pokeDamage;
    private final String pokeName;
    protected Cell cell;

    public Pokemon(Cell cell, String name){
        this(name);
        this.cell = cell;
        this.cell.setPokemon(this);
    }

    public Pokemon(String name){
        this.pokeName = name;
        this.pokeDamage = 2;
        this.pokeHealth = 4;
    }

    /***
     * VERY BAD SOLUTION to ensure that each pokemon class can override getTileName (which comes from Drawable)
     * @return as Pokemon cannot be instantiated, null will never be returned
     */
    @Override
    public String getTileName(){return null;}

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell){
        this.cell = cell;
    }

    public void removePokemonFromCell(){
        cell.setPokemon(null);
        cell = null;
    }

    public abstract void move();

    public int damage(){return (int)(Math.random() * pokeDamage + 1);}
    public abstract void fight();
    public abstract void attackMove(List<List<Integer>> mapWalls, List playerCoordinates, int npcX, int npcY);
    public abstract boolean npcCanSeePlayer(List<List<Integer>> mapWalls, List playerCoordinate, int npcX, int npcY);

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public int getPokeHealth() { return pokeHealth; }

    public void setPokeHealth(int pokeHealth) { this.pokeHealth = pokeHealth; }

    public int getPokeDamage() { return pokeDamage; }

    public void setPokeDamage(int pokeDamage) { this.pokeDamage = pokeDamage; }

    public String getPokeName(){ return this.pokeName; }


    /***
     * Aim: make sure that info is updated everywhere it needs to be updated
     * @param moveTo this Cell has to be determined for each pokemon depending on their moving patterns (e.g. chasing, random...)
     */
    public void takeStep(Cell moveTo){
        cell.setPokemon(null);
        moveTo.setPokemon(this);
        cell = moveTo;
    }

    public Cell findRandomEmptyNeighbouringCell() {
        int[] newCoordinates = new int[2];
        while (true) {
            int changes = (int) Math.round(Math.random());
            int add = ((int) Math.round(Math.random()) == 0) ? -1 : 1;
            newCoordinates[0] = (changes == 0) ? add : 0;
            newCoordinates[1] = (changes == 0) ? 0 : add;
            Cell neighbour = cell.getNeighbor(newCoordinates[0], newCoordinates[1]);
            if (neighbour.getActor() == null && neighbour.getItem() == null && neighbour.getPokemon() == null
                    && neighbour.getTileName().equals(CellType.FLOOR.getTileName())) {
                return neighbour;
            }
        }
    }

    public Cell getCellCloserToPlayer(List playerCoordinates, int npcX, int npcY) {
        int playerX = (int) playerCoordinates.get(0);
        int playerY = (int) playerCoordinates.get(1);
        int dx;
        int dy;
        dx = (playerX > npcX) ? 1 : (playerX < npcX) ? -1 : 0;
        dy = (playerY > npcY) ? 1 : (playerY < npcY) ? -1 : 0;
        Cell neighbour = cell.getNeighbor(dx, dy);
        if (neighbour.getActor() == null && neighbour.getItem() == null && neighbour.getPokemon() == null
                && neighbour.getTileName().equals(CellType.FLOOR.getTileName())) {
            return neighbour;
        }

        return null;
    }

    public List<List<Integer>> getClosestCellsAsc(List playerCoordinates, int npcX, int npcY, double degree) {
        double distanceSide;
        double distanceDown;
        double distanceDiag;
        List<List<Integer>> closestCellsAsc = new ArrayList<>();
        List<Integer> side;
        List<Integer> down;
        List<Integer> diag;

        if (90 > degree && degree >= 0) {
            side = Arrays.asList(1, 0);
            down = Arrays.asList(0, 1);
            diag = Arrays.asList(1, 1);
        }
        else {
            side = Arrays.asList(-1, 0);
            down = Arrays.asList(0, 1);
            diag = Arrays.asList(-1, -1);
        }

        List<Integer> tmp;
        Cell neighbourSide = cell.getNeighbor(side.get(0), side.get(1));
        Cell neighbourDown = cell.getNeighbor(down.get(0), down.get(1));
        Cell neighbourDiag = cell.getNeighbor(diag.get(0), diag.get(1));
        distanceSide = distanceBetweenFields(neighbourSide.getX(),neighbourSide.getY(), (int) playerCoordinates.get(0), (int) playerCoordinates.get(1));
        distanceDown = distanceBetweenFields(neighbourDown.getX(),neighbourDown.getY(), (int) playerCoordinates.get(0), (int) playerCoordinates.get(1));
        distanceDiag = distanceBetweenFields(neighbourDiag.getX(),neighbourDiag.getY(), (int) playerCoordinates.get(0), (int) playerCoordinates.get(1));
        if (distanceSide < distanceDown) {
            tmp = new ArrayList<>();
            tmp.add(side.get(0));
            tmp.add(side.get(1));
            closestCellsAsc.add(tmp);
            tmp = new ArrayList<>();
            tmp.add(down.get(0));
            tmp.add(down.get(1));
            closestCellsAsc.add(tmp);
        }
        else {
            tmp = new ArrayList<>();
            tmp.add(down.get(0));
            tmp.add(down.get(1));
            closestCellsAsc.add(tmp);
            tmp = new ArrayList<>();
            tmp.add(side.get(0));
            tmp.add(side.get(1));
            closestCellsAsc.add(tmp);
        }
        if (distanceDiag < distanceSide && distanceDiag < distanceDown) {
            tmp = new ArrayList<>();
            tmp.add(diag.get(0));
            tmp.add(diag.get(1));
            closestCellsAsc.add(0,tmp);
        }

        else if (distanceDiag > distanceSide && distanceDiag > distanceDown) {
            tmp = new ArrayList<>();
            tmp.add(diag.get(0));
            tmp.add(diag.get(1));
            closestCellsAsc.add(tmp);
        }
        else {
            tmp = new ArrayList<>();
            tmp.add(diag.get(0));
            tmp.add(diag.get(1));
            closestCellsAsc.add(1,tmp);
        }
        return closestCellsAsc;
    }

    protected Cell getEmptyCellCloserToPlayer(List playerCoordinates, int npcX, int npcY) {
        double degree = npcPlayerDegree(playerCoordinates, npcX, npcY);
        List<List<Integer>> closestCellsAsc = getClosestCellsAsc(playerCoordinates, npcX, npcY, degree);
        Cell neighbour;
        for (List<Integer> field : closestCellsAsc) {
            neighbour = cell.getNeighbor(field.get(0), field.get(1));
            if (neighbour.getActor() == null && neighbour.getItem() == null && neighbour.getPokemon() == null
                    && neighbour.getTileName().equals(CellType.FLOOR.getTileName())) {
                return neighbour;
            }
        }
        return cell.getNeighbor(0, 0);
    }


    protected double distanceBetweenPlayerAndNpc(List playerCoordinates, int npcX, int npcY) {
        int playerX = (int) playerCoordinates.get(0);
        int playerY = (int) playerCoordinates.get(1);
        double distance = Math.sqrt(Math.pow((playerX - npcX), 2) + Math.pow((playerY - npcY), 2));
        return distance;
    }

    protected double npcPlayerDegree(List playerCoordinates, int npcX, int npcY) {
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
            degree = -1.00 * (Math.toDegrees(Math.asin(Math.abs((playerY - npcY)) / distance))+0.001);
        }
        if (npcY > playerY) {
            degree = 999;
        }
        return degree;
    }

    protected List<List<Integer>> necessaryWallsFinder(List<List<Integer>> walls, List playerCoordinates, int npcX, int npcY) {
        double degree = npcPlayerDegree(playerCoordinates, npcX, npcY);
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
            if (degree < 0 || degree == -0.0) {
                if ((walls.get(i).get(0) <= npcX && walls.get(i).get(0) >= playerX) &&
                        (walls.get(i).get(1) >= npcY && walls.get(i).get(1) <= playerY)) {
                    necessaryWalls.add(walls.get(i));
                }
            }
        }
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

        if (90 > degree && degree >= 0) {
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
                    npcX = getX();
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

        else if (degree < 0) {
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
                    npcX = getX();
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

    protected boolean npcIsSeeingPlayer(List<List<Integer>> walls, List playerCoordinates, int npcX, int npcY) {
        List<List<Integer>> fieldsCannotBeSeenByNpc = fieldsCannotBeSeenByNpcFinder(walls, playerCoordinates,npcX, npcY);
        int playerX = (int) playerCoordinates.get(0);
        int playerY = (int) playerCoordinates.get(1);
        List<Integer> pc = new ArrayList<>();
        pc.add(playerX);
        pc.add(playerY);
        if (fieldsCannotBeSeenByNpc.contains(pc)) return false;
        else return true;
    }


    @Override
    public String toString() {
        return pokeName + "\n" +
                "Health = " + pokeHealth + '\n' +
                "Damage = " + pokeDamage +  '\n';
    }

}
