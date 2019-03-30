package chrgames.boardgame.models.products.figures;

import java.util.ArrayList;

import chrgames.boardgame.models.products.Figure;

public class Predator extends Figure {


    public Predator() {
        cost = 12;

        level = Level.HARD;

        ableToMove = true;

        ableToFight = true;

        blackFigureIcon = "predator_b";

        redFigureIcon = "predator_r";

        productView = "predator_shop";
    }

    /**
     * Explains scheme of moves exactly this figure (Predator)
     * @param position - absolute position on board of exactly this figure
     * @return - all possible cells to move
     */
    @Override
    public ArrayList<Integer> getAvailableCellsToMoveFrom(int position) {

        if (isRealPosition(position)) {
            return new ArrayList<>();
        }

        int xy[] = getXY(position);
        int x = xy[0];
        int y = xy[1];

        ArrayList<Integer> availableCells = new ArrayList<>();

        int[] arrayX = new int[]{x - 1, x + 1, x - 1, x + 1};
        int[] arrayY = new int[]{y - 1, y - 1, y + 1, y + 1};

        for (int i = 0; i < arrayX.length; i++) {
            int tmpX = arrayX[i];
            int tmpY = arrayY[i];

            if (isExist(tmpX, tmpY)) {
                availableCells.add(getPosition(tmpX, tmpY));
            }
        }

        return availableCells;
    }
}
