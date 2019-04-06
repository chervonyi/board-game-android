package chrgames.boardgame.models.products.figures;

import java.util.ArrayList;

import chrgames.boardgame.models.Cell;
import chrgames.boardgame.models.products.Figure;

public class Stone extends Figure {

    /**
     * Sets the basic attributes for current figure - Stone
     */
    public Stone() {
        cost = 2;

        priority = 5;

        level = Level.EASY;

        ableToMove = true;

        ableToFight = false;

        blackFigureIcon = "stone_b";

        redFigureIcon = "stone_r";

        productView = "stone_shop";
    }

    /**
     * Explains scheme of moves exactly this figure (Stone)
     * @param position - absolute position on board of exactly this figure
     * @return - all possible cells to move
     */
    @Override
    public ArrayList<Integer> getAvailableCellsToMoveFrom(int position) {

        if (Cell.isBadPosition(position)) {
            return new ArrayList<>();
        }

        int xy[] = Cell.getXY(position);
        int x = xy[0];
        int y = xy[1];

        ArrayList<Integer> availableCells = new ArrayList<>();

        // Set search zone borders (adjacent cells)
        int minY = y - 1;
        int maxY = y + 1;
        int minX = x - 1;
        int maxX = x + 1;

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
