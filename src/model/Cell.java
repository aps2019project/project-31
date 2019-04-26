package model;

public class Cell {
    private int x1Coordinate;
    private int x2Coordinate;
    private Deployable cardInCell;
    private int onFireTerms;
    private int onPoisonTerms;

    public Cell(int x1Coordinate, int x2Coordinate, Deployable cardInCell) {
        this.x1Coordinate = x1Coordinate;
        this.x2Coordinate = x2Coordinate;
        this.cardInCell = cardInCell;

    }

    public Cell(int x1Coordinate, int x2Coordinate) {
        this.x1Coordinate = x1Coordinate;
        this.x2Coordinate = x2Coordinate;
        this.cardInCell = Map.getCell(x1Coordinate, x2Coordinate).cardInCell;
        this.onFireTerms = Map.getCell(x1Coordinate, x2Coordinate).onFireTerms;
        this.onPoisonTerms = Map.getCell(x1Coordinate, x2Coordinate).onPoisonTerms;
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
        if (onFireTerms > 0)
            onFireTerms--;
    }

    public void decreaseOnPoison() {
        if (onPoisonTerms > 0)
            onPoisonTerms--;
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
        return onFireTerms >= 1;
    }

    public boolean isPoisoned() {
        return onPoisonTerms >= 1;
    }

}
