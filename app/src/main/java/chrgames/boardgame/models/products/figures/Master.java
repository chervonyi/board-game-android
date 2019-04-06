package chrgames.boardgame.models.products.figures;

import java.util.ArrayList;

import chrgames.boardgame.models.Cell;
import chrgames.boardgame.models.products.Figure;

public class Master extends Figure {

    public Master() {
        cost = 12;

        priority = 50;

        level = Level.NORMAL;

        ableToMove = true;

        ableToFight = true;

        blackFigureIcon = "master_b";

        redFigureIcon = "master_r";

        productView = "master_shop";
    }

    /**
     * Explains scheme of moves exactly this figure (Master)
     * @param position - absolute position on board of exactly this figure
     * @return - all possible cells to move
     */
    @Override
    public ArrayList<Integer> getAvailableCellsToMoveFrom(int position) {

        if (Cell.isRealPosition(position)) {
            return new ArrayList<>();
        }

        int xy[] = Cell.getXY(position);
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

                if (Cell.isExist(j, i) && !(x == j && y == i)) {
                    availableCells.add(Cell.getPosition(j, i));
                }
            }
        }

        return availableCells;

    }
}
