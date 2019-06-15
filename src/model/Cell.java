package model;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

public class Cell {
    private int x1Coordinate;
    private int x2Coordinate;
    private Deployable cardInCell;
    private int onFireTurns;
    private int onPoisonTurns;
    private int isHolyTurns;
    private boolean hasFlag;
    private Item item;
    private Polyline polygon;

    public Polyline getPolygon() {
        return polygon;
    }

    public void setPolygon(Polyline polygon) {
        this.polygon = polygon;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public boolean doesHaveFlag() {
        return hasFlag;
    }

    public void setHasFlag(boolean hasFlag) {
        this.hasFlag = hasFlag;
    }

    public Cell(int x1Coordinate, int x2Coordinate, Deployable cardInCell, Polyline polygon) {
        this.x1Coordinate = x1Coordinate;
        this.x2Coordinate = x2Coordinate;
        this.cardInCell = cardInCell;
        onFireTurns = 0;
        onPoisonTurns = 0;
        hasFlag = false;
        item = null;
        this.polygon = polygon;
    }

    public void setIsHolyTurns(int isHolyTurns) {
        this.isHolyTurns = isHolyTurns;
    }

    public void setOnFireTurns(int onFireTurns) {
        this.onFireTurns = onFireTurns;
    }

    public void setOnPoisonTurns(int onPoisonTurns) {
        this.onPoisonTurns = onPoisonTurns;
    }

    public int getOnFireTurns() {
        return onFireTurns;
    }

    public int getOnPoisonTurns() {
        return onPoisonTurns;
    }

    public void setX1Coordinate(int x1Coordinate) {
        this.x1Coordinate = x1Coordinate;
    }

    public void setX2Coordinate(int x2Coordinate) {
        this.x2Coordinate = x2Coordinate;
    }

    public void setCardInCell(Deployable cardInCell) {
        this.cardInCell = cardInCell;
    }

    public void decreaseOnFire() {
        if (onFireTurns > 0)
            onFireTurns--;
    }

    public void decreaseOnPoison() {
        if (onPoisonTurns > 0)
            onPoisonTurns--;
    }


    public int getX1Coordinate() {
        return x1Coordinate;
    }

    public int getX2Coordinate() {
        return x2Coordinate;
    }

    public Deployable getCardInCell() {
        return cardInCell;
    }

    public boolean isOnFire() {
        return onFireTurns >= 1;
    }

    public boolean isPoisoned() {
        return onPoisonTurns >= 1;
    }

    public Double[] calculateCenter() {
        Double[] coordinates = new Double[2];
        double x = 0, y = 0;
        for (int i = 0; i < polygon.getPoints().size(); i += 2) {
            x += polygon.getPoints().get(i);
        }
        for (int i = 1; i < polygon.getPoints().size(); i += 2) {
            y += polygon.getPoints().get(i);
        }
        coordinates[0] = x / 4;
        coordinates[1] = y / 4;
        return coordinates;
    }

}
