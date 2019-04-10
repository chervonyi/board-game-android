package chrgames.boardgame.models;

import java.util.ArrayList;

import chrgames.boardgame.activities.TutorialActivity;
import chrgames.boardgame.models.products.Figure;


public class TutorialGame {

    // Constants:
    public static final int ROWS = 4;
    public static final int COLUMNS = 5;
    public static final int CELLS = ROWS * COLUMNS;

    // Vars:
    private TutorialActivity activity;
    private ArrayList<Cell> board;
    private int selectedCell;

    /**
     * Instance responsible for artificial moves.
     */
    private Bot bot;

    /**
     * Divides the game into moves on both sides.
     * If turn is PlayerState.ENEMY - bot will make a next move.
     * Else if turn is PlayerState.ALLIANCE - real player will make a next move.
     */
    private Game.PlayerState turn;

    // Flags
    private boolean isRunning;
    private boolean canEnemyMove;

    // Constructor:
    public TutorialGame(TutorialActivity activity) {

        this.activity = activity;
        board = new ArrayList<>();
        turn = Game.PlayerState.ALLIANCE;
        isRunning = true;
        canEnemyMove = false;

        bot = new Bot(0, 0);

        // Fill up a board with cells
        for (int i = 0; i < CELLS; i++) {
            board.add(new Cell(i));
        }
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

        Cell cell = board.get(position);

        // Preconditions to make a move
        if (cell.isHighlighted()) {

            if (cell.isEmpty() ||
                    (cell.getOwner() == Game.PlayerState.ENEMY
                            && board.get(selectedCell).isAbleToFight())) {
                move(selectedCell, position);

                removeSelectionCells();
                return;
            }
        }

        removeSelectionCells();

        // Make selection if was pressed on alliance figure
        if (cell.getOwner() == Game.PlayerState.ALLIANCE) {
            ArrayList<Integer> availableCellsToMove = cell.getAvailableCellsToMove();

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
                //win = cellFrom.getOwner();
                // Get cellFrom.owner and make appropriate information about WIN or LOSE
            }
        }

        cellTo.setFigure(cellFrom);
        cellFrom.resetFigure();

        endTurn();
    }

    /**
     * Change turn.<br>
     * If it's going to be a Enemy's turn, method calls 'makeBotMove' function
     * to execute enemy's move.
     * When it's changed on Alliance's turn, method calls some Activity's method to
     * update a board view according to changes that contributed Bot move.
     */
    private void endTurn() {

        if (canEnemyMove) {
            if (turn == Game.PlayerState.ENEMY) {
                turn = Game.PlayerState.ALLIANCE;
                // Update board view
                activity.updateBoardContent();
            } else {
                turn = Game.PlayerState.ENEMY;
                makeFriendlyMove();
            }
        }
    }

    /**
     * Execute friendly move by bot. (only on empty cell)
     */
    private void makeFriendlyMove() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Move move = bot.getFriendlyMove(board);
                        if (move.to != -1 && isRunning) {
                            move(move.from, move.to);
                        } else {
                            // Enemy has no more figures
                            endTurn();
                        }
                    }
                },
                bot.getDelay()
        );
    }

    /**
     * Set figure at necessary cell.
     * @param figure - appropriate figure such as Stone, Soldier etc.
     * @param position - position on the board.
     * @param owner - owner of current figure. (Enemy or Alliance)
     */
    public void setFigureAt(Figure figure, int position, Game.PlayerState owner) {
        Cell cell = board.get(position);

        if (cell.isEmpty()) {
            cell.setFigure(figure, owner);
        }
    }

    /**
     * Make identical highlight status for given set of cells.
     * @param set - a set of sequence numbers to change their highlight-status
     * @param highlighted - desired status
     */
    private void setHighlightForSet(ArrayList<Integer> set, boolean highlighted) {
        try {
            for (int i = 0; i < set.size(); i++) {
                board.get(set.get(i)).setHighlighted(highlighted);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set flag 'isHighlighted' to false for all cells.
     */
    public void removeSelectionCells() {
        for (int i = 0; i < board.size(); i++) {
            board.get(i).setHighlighted(false);
        }
        selectedCell = -1;
    }

    /**
     * Check if game is running.
     * @return true if game is not running anymore; false if game is running.
     */
    public boolean isOver() {
        return !isRunning;
    }

    /**
     * Check if it is user's turn right now.
     * @return true if user should make move; false if bot is making move.
     */
    public boolean isPlayerTurn() {
        return turn == Game.PlayerState.ALLIANCE;
    }

    /**
     * @return list of cells
     */
    public ArrayList<Cell> getBoard() {
        return board;
    }

    /**
     * @return id of selected cell.
     */
    public int getSelectedCell() {
        return selectedCell;
    }

    /**
     * Set flag 'canEnemyMove'.
     * If this flag value is true, bot will make a friendly move;
     * if false - user can make any times of moves.
     * @param canEnemyMove - new boolean value
     */
    public void setBotMoving(boolean canEnemyMove) {
        this.canEnemyMove = canEnemyMove;
    }

    /**
     * Set flag 'isRunning'.
     * @param running - new boolean value
     */
    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    /**
     * Reset figure at each cell on board.
     */
    public void clearBoard() {
        for(Cell cell : board) {
            cell.resetFigure();
        }
    }
}
