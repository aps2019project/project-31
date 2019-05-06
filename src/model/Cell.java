package model;

public class Cell {
    private int x1Coordinate;
    private int x2Coordinate;
    private Deployable cardInCell;
    private int onFireTurns;
    private int onPoisonTurns;
    private int isHolyTurns;
    private boolean hasFlag;
    private Item item;

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

    public Cell(int x1Coordinate, int x2Coordinate, Deployable cardInCell) {
        this.x1Coordinate = x1Coordinate;
        this.x2Coordinate = x2Coordinate;
        this.cardInCell = cardInCell;
        onFireTurns = 0;
        onPoisonTurns = 0;
        hasFlag = false;
        item = null;

    }

    public Cell(int x1Coordinate, int x2Coordinate) {
        this.x1Coordinate = x1Coordinate;
        this.x2Coordinate = x2Coordinate;
        this.cardInCell = Map.getCell(x1Coordinate, x2Coordinate).cardInCell;
        this.onFireTurns = Map.getCell(x1Coordinate, x2Coordinate).onFireTurns;
        this.onPoisonTurns = Map.getCell(x1Coordinate, x2Coordinate).onPoisonTurns;
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

}
