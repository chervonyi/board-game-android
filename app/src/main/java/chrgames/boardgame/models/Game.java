package chrgames.boardgame.models;

import java.util.ArrayList;

public class Game {

    enum PlayerState {
        ENEMY,
        ALLIANCE
    }

    private ArrayList<Cell> board;

    // Constants
    public static final int ROWS = 10;

    public static final int COLUMNS = 5;

    public static final int CELLS = ROWS * COLUMNS;

    public static final int BASE_SIZE = 3;

    private Base enemyBase;

    private Base allianceBase;

    public Game() {
        board = new ArrayList<>();

        for (int i = 0; i < CELLS; i++) {
            board.add(new Cell(i));
        }

        enemyBase = new Base(BASE_SIZE, PlayerState.ENEMY);

        allianceBase = new Base(BASE_SIZE, PlayerState.ALLIANCE);
    }

    private void locateFirstFigures() {

    }

    private void clearBoard() {

    }


}
