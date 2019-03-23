package chrgames.boardgame.models;

import java.util.ArrayList;

public class Game {

    private ArrayList<Cell> board;

    // Constants
    public static final int ROW = 10;

    public static final int COLUMNS = 5;

    public static final int CELLS = ROW * COLUMNS;

    public Game() {
        board = new ArrayList<>();

        for (int i = 0; i < CELLS; i++) {
            board.add(new Cell(i));
        }


    }

    private void locateFirstFigures() {

    }

    private void clearBoard() {

    }

}
