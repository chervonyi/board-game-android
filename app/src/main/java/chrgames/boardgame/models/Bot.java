package chrgames.boardgame.models;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Collections;
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

    private final int[] boardPositionPriority;

    Bot(Level level) {
        this.selectedLevel = level;

        boardPositionPriority = new int[Game.CELLS];

        int x = 0;
        int priority = 0;

        for (int i = 0; i < boardPositionPriority.length; i++) {
            priority += i / 10;

            switch (x) {
                case 0:
                case 4: priority += 0; break;

                case 1:
                case 3: priority += 1; break;

                case 2: priority += 2; break;
            }

            x++;

            boardPositionPriority[i] = priority;

            if (x % 5 == 0) {
                x = 0;
            }
            priority = 0;
        }
    }


    public int getPriority(int pos) {
        return boardPositionPriority[pos];
    }

    /**
     * Calculates the possible move of the bot
     * @param board - actual board (list of cells)
     * @return array with two numbers which contains information about selected move.
     *      array[0] = cellFrom (id)
     *      array[1] = cellTo (id)
     */
    public Move getMove(ArrayList<Cell> board) {

        HashMap<Integer, Move> availableMoves = getMapOfAvailableMoves(board);

        if (availableMoves.size() == 0) { return new Move(-1, -1); }

        List<Integer> priorityList = new ArrayList<>(availableMoves.keySet());

        int maxPriority = 0;
        int sum = 0;

        for (Integer priority : priorityList) {

            sum += priority;

            if (priority > maxPriority) {
                maxPriority = priority;
            }
        }

        // Get Move with maximum calculated priority
        return availableMoves.get(maxPriority);
    }

    /**
     * Compose a special map.<br>
     * In this map <b>keys</b> will be available cells to move,
     * and <b>variables</b> will be cells with figures that could make this move.
     * So each pair represents possible moves for Bot side.
     * @param board - actual board (list of cells)
     * @return composed map of available moves.
     */
    private HashMap<Integer, Move> getMapOfAvailableMoves(ArrayList<Cell> board) {

        @SuppressLint("UseSparseArrays")
        HashMap<Integer, Move> availableMoves = new HashMap<>();

        ArrayList<Integer> availableMovesForCell;

        int priority;

        for (int i = 0; i < board.size(); i++) {

            Cell cell = board.get(i);

            if (cell.getOwner() == Game.PlayerState.ENEMY) {

                availableMovesForCell = cell.getAvailableCellsToMove();

                for (Integer cellId : availableMovesForCell) {
                    if (board.get(cellId).isEmpty() ||
                            (board.get(cellId).getOwner() == Game.PlayerState.ALLIANCE &&
                                    cell.isAbleToFight())) {
                        // For free or cells with bot enemy's figure:

                        priority = calculatePriority(board, board.get(i), board.get(cellId));
                        availableMoves.put(priority, new Move(i, cellId));
                    }
                }
            }
        }

        return availableMoves;
    }

    private int calculatePriority(ArrayList<Cell> board, Cell cellFrom, Cell cellTo) {
        int priority = 0;

        int pos = cellTo.getId();

        priority += boardPositionPriority[pos];

        if (!cellTo.isEmpty()) {
            priority += cellTo.getFigure().getPriority();
        }

        priority += cellFrom.getFigure().getPriority();

        if (cellFrom.isAbleToFight()) {
            int nearestFinalFigureLength = getLengthToFinalFigure(board, cellTo);

            if (nearestFinalFigureLength > 10) {
                priority += 5;
            } else {
                priority += 100 - nearestFinalFigureLength * 10;
            }
        }


        return priority;
    }

    private int getLengthToFinalFigure(ArrayList<Cell> board, Cell forCell) {
        ArrayList<Cell> finalFigures = new ArrayList<>();

        for (Cell cell:
             board) {
            if (cell.getOwner() == Game.PlayerState.ALLIANCE && cell.isEndingFigure()) {
                finalFigures.add(cell);
            }
        }

        int minLength = 0;
        int length = 0;
        int[] xyFinalFigure, xyCell;

        // Looking for nearest final figure
        for (Cell finalFigure : finalFigures) {
            xyFinalFigure = Cell.getXY(finalFigure.getId());
            xyCell = Cell.getXY(forCell.getId());

            length = getLength(xyFinalFigure, xyCell);

            if (length < minLength || minLength == 0) {
                minLength = length;
            }
        }

        return minLength;
    }

    private int getLength(int[] a, int[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
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
