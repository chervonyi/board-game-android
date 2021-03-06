package chrgames.boardgame.models.products.figures;

import java.util.ArrayList;

import chrgames.boardgame.models.products.Figure;

public class Source extends Figure {

    public static final int INCOME_FOR_EACH_FIGURE = 2;

    public Source() {
        cost = 5;

        priority = 1000;

        level = Level.EASY;

        ableToMove = false;

        ableToFight = false;

        blackFigureIcon = "sorket_b";

        redFigureIcon = "sorket_r";

        productView = "sorket_shop";
    }

    /**
     * Because of the fact that this figure (Source) is not able to move,
     * this method returns empty array. <br>
     *
     * @param position - absolute position on board of exactly this figure
     * @return - empty array which means there are not available cells to move for this figure.
     */
    @Override
    public ArrayList<Integer> getAvailableCellsToMoveFrom(int position) {
        return new ArrayList<>();
    }
}
