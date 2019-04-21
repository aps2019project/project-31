package model;

public class Map {
    private static final int maxMoveRange = 2;
    public static final int MAP_X1_LENGTH = 9;
    public static final int MAP_x2_LENGTH = 5;
    private static Cell[][] map = new Cell[MAP_x2_LENGTH][MAP_X1_LENGTH];

    public static Cell getCell(int x1, int x2) {
        if (x1 <= MAP_X1_LENGTH && x2 <= MAP_x2_LENGTH && x1 > 0 && x2 > 0) {
            return map[x1][x2];
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

    public static int getDistance(Cell cell1, int x, int y) {
        return Math.abs(cell1.getxCoordinate() - x) +
                Math.abs(cell1.getyCoordinate() - y);
    }

    public static void move(int x, int y, Card card) {
        Cell cellDestination = getCell(x, y);

    }

    public static Cell findCellByCardId(int cardId) {
        for (Cell[] cells : map) {
            for (Cell cell : cells) {
                if (cell.getCardInCell().getId() == cardId) {
                    return cell;
                }
            }
        }
        return null;
    }

    public static Card getCardInCell(int x1, int x2) {
        return getCell(x1, x2).getCardInCell();
    }

    public static void putCardInCell(Card card, int x1, int x2) {
        getCell(x1, x2).setCardInCell(card);
    }
}
