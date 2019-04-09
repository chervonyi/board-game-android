package chrgames.boardgame.models.products.figures;

import java.util.ArrayList;

import chrgames.boardgame.models.Cell;
import chrgames.boardgame.models.products.Figure;

public class Stone extends Figure {

    /**
     * Sets the basic attributes for current figure - Stone
     */
    public Stone() {
        cost = 6;

        priority = 40;

        level = Level.EASY;

        ableToMove = true;

        ableToFight = true;

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

        int[] arrayX = new int[]{x, x, x - 1, x + 1};
        int[] arrayY = new int[]{y - 1, y + 1, y, y};

        for (int i = 0; i < arrayX.length; i++) {
            int tmpX = arrayX[i];
            int tmpY = arrayY[i];

            if (Cell.isExist(tmpX, tmpY)) {
                availableCells.add(Cell.getPosition(tmpX, tmpY));
            }
        }

        return availableCells;
    }
}
