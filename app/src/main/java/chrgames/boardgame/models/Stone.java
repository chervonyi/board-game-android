package chrgames.boardgame.models;

import java.util.ArrayList;

public class Stone extends Figure {

    /**
     * Sets the basic attributes for current figure - Stone
     */
    Stone() {
        cost = 2;
        level = Level.EASY;
    }

    /**
     * Explains scheme of moves exactly this figure (Stone)
     * @param position - absolute position on board of exactly this figure
     * @return - all possible cells to move
     */
    @Override
    public ArrayList<Integer> getAvailableCellsToMoveFrom(int position) {

        if (position < 0 || position >= Game.CELLS) {
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
