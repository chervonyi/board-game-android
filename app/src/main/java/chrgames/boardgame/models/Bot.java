package chrgames.boardgame.models;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Bot {

    /**
     * All available Bot levels.
     * The selected level directly affects the quality of the selected move.
     * E-g. 'Elementary' level selects random cells to move.
     *      'Hard' level analyzes the board for the best move.
     */
    enum Level {
        ELEMENTARY,
        EASY,
        NORMAL,
        HARD,
        EXPERT
    }

    /**
     * Sets of artificial times for bot thinking.
     */
    private final int[] delaySamples = new int[]{500, 1000, 1500, 2000, 2500, 3000};

    /**
     * Actual level of bot.
     * The selected level directly affects the quality of the selected move.
     * Sets on instance creation.
     */
    private Level selectedLevel;

    Bot(Level level) {
        this.selectedLevel = level;
    }

    /**
     * Calculates the possible move of the bot
     * @param board - actual board (list of cells)
     * @return array with two numbers which contains information about selected move.
     *      array[0] = cellFrom (id)
     *      array[1] = cellTo (id)
     */
    public int[] getMove(ArrayList<Cell> board) {

        HashMap<Integer, Integer> availableMoves = getMapOfAvailableMoves(board);
        List<Integer> keysAsArray = new ArrayList<>(availableMoves.keySet());

        Random random = new Random();

        int cellTo = keysAsArray.get(random.nextInt(keysAsArray.size()));
        int cellFrom = availableMoves.get(cellTo);

        return new int[] {cellFrom, cellTo};
    }

    /**
     * Compose a special map.<br>
     * In this map <b>keys</b> will be available cells to move,
     * and <b>variables</b> will be cells with figures that could make this move.
     * So each pair represents possible moves for Bot side.
     * @param board - actual board (list of cells)
     * @return composed map of available moves.
     */
    private HashMap<Integer, Integer> getMapOfAvailableMoves(ArrayList<Cell> board) {
        @SuppressLint("UseSparseArrays")
        HashMap<Integer, Integer> availableMoves = new HashMap<>();

        ArrayList<Integer> availableMovesForCell;

        for (int i = 0; i < board.size(); i++) {

            Cell cell = board.get(i);

            if (cell.getOwner() == Game.PlayerState.ENEMY) {

                availableMovesForCell = cell.getAvailableCellsToMove();

                for (Integer cellId : availableMovesForCell) {
                    if (board.get(cellId).isEmpty() ||
                            (board.get(cellId).getOwner() == Game.PlayerState.ALLIANCE &&
                                    cell.isAbleToFight())) {
                        availableMoves.put(cellId, i);
                    }
                }
            }
        }

        return availableMoves;
    }

    /**
     * Get a random delay time from prepared array.
     * @return artificial time of bot thinking
     */
    public int getDelay() {
        Random random = new Random();
        return delaySamples[random.nextInt(delaySamples.length)];
    }
}
