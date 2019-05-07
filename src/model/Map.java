package model;

public class Map {
    private static final int maxMoveRange = 2;
    public static final int MAP_X1_LENGTH = 5;
    public static final int MAP_X2_LENGTH = 9;
    private static Cell[][] map;

    public static Cell getCell(int x1, int x2) {
        if (x1 <= MAP_X1_LENGTH && x2 <= MAP_X2_LENGTH && x1 > 0 && x2 > 0) {
            return map[x1][x2];
        }
        return null;
    }

    public static void createTheMap() {
        map = new Cell[MAP_X1_LENGTH + 1][MAP_X2_LENGTH + 1];
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 9; j++) {
                map[i][j] = new Cell(i, j, null);
            }
        }
    }

    public static int getDistance(Cell cell1, Cell cell2) {
        if (cell1 == null || cell2 == null)
            return 100000;
        return Math.abs(cell1.getX1Coordinate() - cell2.getX1Coordinate()) +
                Math.abs(cell1.getX2Coordinate() - cell2.getX2Coordinate());
    }

    public static Cell[][] getMap() {
        return map;
    }

    public static int getMaxMoveRange() {
        return maxMoveRange;
    }

    public static int getDistance(Cell cell1, int x, int y) {
        return Math.abs(cell1.getX1Coordinate() - x) +
                Math.abs(cell1.getX2Coordinate() - y);
    }

    public static void move(int x, int y, Card card) {
        Cell cellDestination = getCell(x, y);

    }

    public static Cell findCellByCardId(int uniqueCardId) {
        for (Cell[] cells : map) {
            for (Cell cell : cells) {
                if (cell.getCardInCell() != null)
                    if (cell.getCardInCell().uniqueId == uniqueCardId) {
                        return cell;
                    }
            }
        }
        return null;
    }

    public static Card getCardInCell(int x1, int x2) {
        if (getCell(x1, x2) != null)
            return getCell(x1, x2).getCardInCell();
        return null;
    }

    public static void putCardInCell(Deployable card, int x1, int x2) {
        getCell(x1, x2).setCardInCell(card);
    }

    public static void applyPoisonFireAtTheEndOfTurn() {
        for (Cell[] cells : map) {
            for (Cell cell : cells) {
                if (cell.isOnFire() && cell.getCardInCell() != null)
                    cell.getCardInCell().applyFire();
                if (cell.isPoisoned() && cell.getCardInCell() != null)
                    cell.getCardInCell().applyPoison();
                cell.decreaseOnFire();
                cell.decreaseOnPoison();
            }
        }
    }

}
