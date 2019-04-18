package model;

public class Map {
    private static final int maxMoveRange = 2;
    private static Cell[][] map = new Cell[5][8];

    public static Cell getCell(int x, int y) {
        if (x <= 8 && y <= 5) {
            return map[x][y];
        }
        return null;
    }

    public static int getDistance(Cell cell1, Cell cell2) {
        return Math.abs(cell1.getxCoordinate() - cell2.getxCoordinate()) +
                Math.abs(cell1.getyCoordinate() - cell2.getyCoordinate());
    }

    public static int getMaxMoveRange() {
        return maxMoveRange;
    }

    public static int getDistance(Cell cell1, int x, int y){
        return Math.abs(cell1.getxCoordinate() - x) +
                Math.abs(cell1.getyCoordinate() - y);
    }

    public void move(int x, int y, Card card) {
        Cell cellDestination = getCell(x, y);

    }
    public static Cell findCellByCardId(int cardId){
        for (Cell[] cells:map) {
            for (Cell cell:cells) {
                if(cell.getCardInCell().getId()==cardId){
                    return cell;
                }
            }
        }
        return null;
    }
}
