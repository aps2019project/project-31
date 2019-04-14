public class Map {
    private final int maxMoveRange = 2;
    private static Cell[][] map = new Cell[5][8];

    public Cell getCell(int x, int y) {
        if (x <= 8 && y <= 5) {
            return map[y - 1][x - 1];
        }
        return null;
    }

    public int getDistance(Cell cell1, Cell cell2) {
        return Math.abs(cell1.getxCoordinate() - cell2.getxCoordinate()) +
                Math.abs(cell1.getyCoordinate() - cell2.getyCoordinate());
    }

    public void move(int x, int y, Card card) {
        Cell cellDestination = getCell(x, y);
        if(cellDestination.getCardInCell()==null && getDistance());
    }
}
