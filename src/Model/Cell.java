package Model;

public class Cell {
    private int xCoordinate;
    private int yCoordinate;
    private Card cardInCell;
    private boolean isOnFire;
    private boolean isPoisoned;

    public Cell(int xCoordinate, int yCoordinate, Card cardInCell) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.cardInCell = cardInCell;
        this.isOnFire = false;
        this.isPoisoned = false;
    }

    public Cell(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.cardInCell = Map.getCell(xCoordinate, yCoordinate).cardInCell;
        this.isOnFire = Map.getCell(xCoordinate, yCoordinate).isOnFire;
        this.isPoisoned = Map.getCell(xCoordinate, yCoordinate).isPoisoned;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
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

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
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
