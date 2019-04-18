package Model;

public class Map {
    private static final int maxMoveRange = 2;
    public static final int MAP_X_LENGTH = 9;
    public static final int MAP_Y_LENGTH = 5;
    private static Cell[][] map = new Cell[MAP_Y_LENGTH][MAP_X_LENGTH];

    public static Cell getCell(int x, int y) {
        if (x <= MAP_X_LENGTH && y <= MAP_Y_LENGTH && x > 0 && y > 0) {
            return map[x - 1][y - 1];
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

    public static void move(int x, int y, Card card) {
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

    public static Card getCardInCell(int x, int y){
        return getCell(x,y).getCardInCell();
    }

    public static void putCardInCell(Card card, int x, int y){
        getCell(x, y).setCardInCell(card);
    }
}
