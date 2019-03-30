package chrgames.boardgame.models.products.figures;

import java.util.ArrayList;

import chrgames.boardgame.models.products.Figure;

public class Soldier extends Figure {

    public Soldier() {
        cost = 5;

        level = Level.EASY;

        ableToMove = true;

        ableToFight = true;

        blackFigureIcon = "soldier_b";

        redFigureIcon = "soldier_r";

        productView = "soldier_shop";
    }

    /**
     * Explains scheme of moves exactly this figure (Soldier)
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

        // Set search zone borders (adjacent cells)
        int minY = y - 1;
        int maxY = y + 1;
        int minX = x - 1;
        int maxX = x + 1;

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
