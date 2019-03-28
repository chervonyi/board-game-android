package chrgames.boardgame.models;

import java.util.ArrayList;
import java.util.Random;

import chrgames.boardgame.activities.BoardActivity;

import static chrgames.boardgame.models.Figure.*;

public class Game {

    public enum PlayerState {
        ENEMY,
        ALLIANCE
    }

    // General vars
    private ArrayList<Cell> board;

    private BoardActivity activity;

    /**
     * A special flag to keep track the game status.<br>
     * When it's true - game is running and player can go on.<br>
     * If it's false - game is over and player cannot do anything (e-g. moves figures, take cards etc).
     */
    private boolean isRunning;

    /**
     * Instance responsible for artificial moves.
     */
    private Bot bot;

    /**
     * Divides the game into moves on both sides.
     * If turn is PlayerState.ENEMY - bot will make a next move.
     * Else if turn is PlayerState.ALLIANCE - real player will make a next move.
     */
    private PlayerState turn;

    // Constants
    public static final int ROWS = 10;

    public static final int COLUMNS = 5;

    public static final int CELLS = ROWS * COLUMNS;

    public static final int BASE_SIZE = 3;


    // Bases
    private Base enemyBase;

    private Base allianceBase;


    // Vars to work with selection and moves
    private ArrayList<Integer> previousSelectedCells = new ArrayList<>();

    private int selectedCell;

    public Game(BoardActivity activity) {

        this.activity = activity;

        board = new ArrayList<>();

        bot = new Bot(Bot.Level.EASY);

        turn = getPlayerWithFirstTurn();

        isRunning = true;

        for (int i = 0; i < CELLS; i++) {
            board.add(new Cell(i));
        }

        enemyBase = new Base(BASE_SIZE, PlayerState.ENEMY);

        allianceBase = new Base(BASE_SIZE, PlayerState.ALLIANCE);

        locateFirstFigures();
    }


    /**
     * Listener for every click on cells.
     * There are two different types of clicks:
     *      1. Player has not selected any alliance figure before.
     *         So, player does not have any selected cells and
     *         this click means make a field of selected cells.
     *      2. Player has a field of selected cells and this click was done on one of this.
     *         So, this click means to make a move from one cell into another one.
     *         ('pressOnHighlightedCells' method)
     * @param position
     */
    public void selectCell(int position) {

        // Preconditions to make a move
        if (board.get(position).isHighlighted()) {

            if (board.get(position).isEmpty() ||
                    (board.get(position).getOwner() == PlayerState.ENEMY
                    && board.get(selectedCell).isAbleToFight())) {
                move(selectedCell, position);

                selectedCell = -1;
                setHighlightForSet(previousSelectedCells, false);
                return;
            }
        }

        // Remove previous selection
        if (previousSelectedCells.size() > 0) {
            setHighlightForSet(previousSelectedCells, false);
            selectedCell = -1;
        }

        // Make selection if was pressed on alliance figure
        if (board.get(position).getOwner() == PlayerState.ALLIANCE) {
            ArrayList<Integer> availableCellsToMove = board.get(position).getAvailableCellsToMove();

            previousSelectedCells = availableCellsToMove;

            if (availableCellsToMove.size() > 0) {
                setHighlightForSet(availableCellsToMove, true);
                selectedCell = position;
            }
        }
    }

    /**
     * Make move from one cell into another.
     * @param from - departure.
     * @param to - destination.
     */
    private void move(int from, int to) {
        Cell cellFrom = board.get(from);
        Cell cellTo = board.get(to);

        if (!cellTo.isEmpty() && cellFrom.getOwner() != cellTo.getOwner()) {


            if (cellTo.isEndingFigure()) {
                isRunning = false;
                // Get cellFrom.owner and make appropriate information about WIN or LOSE
            } else {
                // TODO: Get reward for a kill
            }

        }

        cellTo.setFigure(cellFrom);
        cellFrom.resetFigure();

        endTurn();
    }

    private void makeBotMove() {

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {

                        int[] move = bot.getMove(board);
                        int cellFrom = move[0];
                        int cellTo = move[1];
                        move(cellFrom, cellTo);
                    }
                },
                bot.getDelay()
        );
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
        ArrayList<Integer> freeCells;
        int randomFreePosition;

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

            freeCells = base.getFreeCells(board);

            randomFreePosition = base.getRandomFreeCell(freeCells);

            setFigureAt(figure, randomFreePosition, base.getOwner());
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
     * Make identical highlight status for given set of cells.
     * @param set - a set of sequence numbers to change their highlight-status
     * @param highlighted - desired status
     */
    private void setHighlightForSet(ArrayList<Integer> set, boolean highlighted) {
        for (int i = 0; i < set.size(); i++) {
            board.get(set.get(i)).setHighlighted(highlighted);
        }
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

    /**
     * @return a sequence number of a cell which has been selected before
     */
    public int getSelectedCell() {
        return selectedCell;
    }

    /**
     * Check if game is running at this time.
     * @return true if game is over; false if it is running right now.
     */
    public boolean isOver() {
        return !isRunning;
    }

    /**
     * Chose one of players who will make a first turn.
     * @return PlayerState of selected player.
     */
    private PlayerState getPlayerWithFirstTurn() {
        // TODO: Change on random selection
        return PlayerState.ALLIANCE;
    }

    /**
     * Check if current turn is real Player's one.
     * @return true if it is a Player's turn; false if it is not.
     */
    public boolean isPlayerTurn() {
        return turn == PlayerState.ALLIANCE;
    }

    /**
     * Change turn.<br>
     * If it's going to be a Enemy's turn, method calls 'makeBotMove' function
     * to execute enemy's move.
     * When it's changed on Alliance's turn, method calls some Activity's method to
     * update a board view according to changes that contributed Bot move.
     */
    private void endTurn() {

        if (turn == PlayerState.ENEMY) {
            turn = PlayerState.ALLIANCE;

            // Update board view
            activity.locateFiguresOnBoard();
        } else {
            turn = PlayerState.ENEMY;

            makeBotMove();
        }
    }
}
