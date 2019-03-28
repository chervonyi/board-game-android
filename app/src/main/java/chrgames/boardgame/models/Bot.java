package chrgames.boardgame.models;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Bot {

    enum Level {
        ELEMENTARY,
        EASY,
        NORMAL,
        HARD,
        EXPERT
    }

    public static final int DELAY = 1000;

    private int income;

    private int amount;

    private Level selectedLevel;

    Bot(Level level) {
        this.selectedLevel = level;

        income = 0;

        amount = 0;
    }


    public int[] getMove(ArrayList<Cell> board) {

        HashMap<Integer, Integer> availableMoves = getMapOfAvailableMoves(board);
        List<Integer> keysAsArray = new ArrayList<Integer>(availableMoves.keySet());

        Random random = new Random();

        int cellTo = keysAsArray.get(random.nextInt(keysAsArray.size()));
        int cellFrom = availableMoves.get(cellTo);


        return new int[] {cellFrom, cellTo};
    }

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
                            board.get(cellId).getOwner() == Game.PlayerState.ALLIANCE) {
                        availableMoves.put(cellId, i);
                    }
                }
            }
        }

        return availableMoves;
    }
}
