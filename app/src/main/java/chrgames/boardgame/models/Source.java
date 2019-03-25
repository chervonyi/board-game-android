package chrgames.boardgame.models;

import java.util.ArrayList;

public class Source extends Figure {

    Source() {
        cost = 6;

        level = Level.EASY;

        ableToMove = false;

        blackFigureIcon = "source_b";

        redFigureIcon = "source_r";
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
