package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

import java.util.ArrayList;
import java.util.List;

public abstract class Actor implements Drawable {
    private Cell cell;
    private int health = 10;

    public Actor() {
    }

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }


    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (nextCell.getType() != CellType.WALL && nextCell.getPokemon() == null && nextCell.getActor() == null) {
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        }
    }

    public int getHealth() {
        return health;
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
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

    private List<List<Integer>> returnWithMinusOne () {
        List<List<Integer>> playerIsBehind = new ArrayList<>();
        List<Integer> minusOne = new ArrayList<>();
        minusOne.add(-1);
        playerIsBehind.add(minusOne);
        return playerIsBehind;
    }
}

