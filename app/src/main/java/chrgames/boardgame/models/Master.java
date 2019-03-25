package chrgames.boardgame.models;

import java.util.ArrayList;

public class Master extends Figure {

    Master() {
        cost = 12;

        level = Level.NORMAL;

        ableToMove = true;

        blackFigureIcon = "master_b";

        redFigureIcon = "master_r";
    }

    /**
     * Explains scheme of moves exactly this figure (Master)
     * @param position - absolute position on board of exactly this figure
     * @return - all possible cells to move
     */
    @Override
    public ArrayList<Integer> getAvailableCellsToMoveFrom(int position) {

        if (!isRealPosition(position)) {
            return new ArrayList<>();
        }

        int xy[] = getXY(position);
        int x = xy[0];
        int y = xy[1];

        ArrayList<Integer> availableCells = new ArrayList<>();

        // Set search zone borders (adjacent cells)
        int minY = y - 2;
        int maxY = y + 2;
        int minX = x - 2;
        int maxX = x + 2;

        for (int i = minY; i <= maxY; i++) {
            for (int j = minX; j <= maxX; j++) {

                if (isExist(j, i) && !(x == j && y == i)) {
                    availableCells.add(getPosition(j, i));
                }
            }
        }

        return availableCells;

    }
}
