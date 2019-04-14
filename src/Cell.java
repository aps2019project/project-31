public class Cell {
    private int xCoordinate;
    private int yCoordinate;
    private Cell cardInCell;
    private boolean isOnFire;
    private boolean isPoisoned;

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public void setCardInCell(Cell cardInCell) {
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

    public Cell getCardInCell() {
        return cardInCell;
    }

    public boolean isOnFire() {
        return isOnFire;
    }

    public boolean isPoisoned() {
        return isPoisoned;
    }
}
