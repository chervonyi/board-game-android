package chrgames.boardgame.models;

import java.util.ArrayList;
import java.util.Random;

import static chrgames.boardgame.models.Figure.*;

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

        locateFirstFigures();
    }

    /**
     * Locate an identical, fixed set of figures to each sides.
     */
    private void locateFirstFigures() {
        final Kind startSetOfFigures[] = new Kind[]{
                Kind.Source,
                Kind.Stone,
                Kind.Stone,
                Kind.Soldier
        };

        locateSet(startSetOfFigures, enemyBase);
        locateSet(startSetOfFigures, allianceBase);
    }

    /**
     * Place figures at random positions in according to bases.
     * @param set - list of necessary figures
     * @param base - enemy's or alliance's base
     */
    private void locateSet(Kind[] set, Base base) {

        Figure figure;

        for (Kind kind : set) {

            switch (kind) {
                case Master:
                    figure = new Master();
                    break;

                case Predator:
                    figure = new Predator();
                    break;

                case Soldier:
                    figure = new Soldier();
                    break;

                case Source:
                    figure = new Source();
                    break;

                case Stone:
                    figure = new Stone();
                    break;

                default: figure = new Stone();
            }

            setFigureAt(figure, base.getRandomYourCell(), base.getOwner());
        }
    }

    /**
     * Set figure at necessary cell.
     * @param figure - appropriate figure such as Stone, Soldier etc.
     * @param position - position on the board.
     * @param owner - owner of current figure. (Enemy or Alliance)
     */
    private void setFigureAt(Figure figure, int position, PlayerState owner) {
        Cell cell = board.get(position);

        if (cell.isEmpty()) {
            cell.setFigure(figure, owner);
        }
    }

    /**
     * @return a random integer according to size of the board
     */
    private int getRandomPosition() {
        Random random = new Random();
        return random.nextInt(CELLS - 1);
    }

    /**
     * Removes all figures from the board.
     */
    private void clearBoard() {
        for (Cell cell : board) {
            cell.resetFigure();
        }
    }

    /**
     * Getter of board.
     * @return array of cells
     */
    public ArrayList<Cell> getBoard() {
        return board;
    }
}
