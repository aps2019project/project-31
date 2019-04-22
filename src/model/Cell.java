package model;

public class Cell {
    private int x1Coordinate;
    private int x2Coordinate;
    private Card cardInCell;
    private boolean isOnFire;
    private boolean isPoisoned;

    public Cell(int x1Coordinate, int x2Coordinate, Card cardInCell) {
        this.x1Coordinate = x1Coordinate;
        this.x2Coordinate = x2Coordinate;
        this.cardInCell = cardInCell;
        this.isOnFire = false;
        this.isPoisoned = false;
    }

    public Cell(int x1Coordinate, int x2Coordinate) {
        this.x1Coordinate = x1Coordinate;
        this.x2Coordinate = x2Coordinate;
        this.cardInCell = Map.getCell(x1Coordinate, x2Coordinate).cardInCell;
        this.isOnFire = Map.getCell(x1Coordinate, x2Coordinate).isOnFire;
        this.isPoisoned = Map.getCell(x1Coordinate, x2Coordinate).isPoisoned;
    }

    public void setX1Coordinate(int x1Coordinate) {
        this.x1Coordinate = x1Coordinate;
    }

    public void setX2Coordinate(int x2Coordinate) {
        this.x2Coordinate = x2Coordinate;
    }

    public void setCardInCell(Card cardInCell) {
        this.cardInCell = cardInCell;
    }

    public void setOnFire(boolean onFire) {
        isOnFire = onFire;
    }

    public void setPoisoned(boolean poisoned) {
        isPoisoned = poisoned;
    }

    public int getX1Coordinate() {
        return x1Coordinate;
    }

    public int getX2Coordinate() {
        return x2Coordinate;
    }

    public Card getCardInCell() {
        return cardInCell;
    }

    public boolean isOnFire() {
        return isOnFire;
    }

    public boolean isPoisoned() {
        return isPoisoned;
    }
}
