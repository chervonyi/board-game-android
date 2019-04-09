package chrgames.boardgame.models;

import java.util.ArrayList;

import chrgames.boardgame.activities.TutorialActivity;
import chrgames.boardgame.models.products.Figure;


public class TutorialGame {

    // Constants:
    public static final int ROWS = 4;

    public static final int COLUMNS = 5;

    public static final int CELLS = ROWS * COLUMNS;


    // ------- Vars
    private TutorialActivity activity;
    private ArrayList<Cell> board;

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

    // -------- Selection vars
    private int selectedCell;


    // -------- Flags
    private boolean isRunning;
    private boolean canEnemyMove;

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

    public void setFigureAt(Figure figure, int position, Game.PlayerState owner) {
        Cell cell = board.get(position);

        if (cell.isEmpty()) {
            cell.setFigure(figure, owner);
        }
    }

    private void setHighlightForSet(ArrayList<Integer> set, boolean highlighted) {
        try {
            for (int i = 0; i < set.size(); i++) {
                board.get(set.get(i)).setHighlighted(highlighted);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void removeSelectionCells() {
        for (int i = 0; i < board.size(); i++) {
            board.get(i).setHighlighted(false);
        }
        selectedCell = -1;
    }

    public boolean isOver() {
        return !isRunning;
    }

    public boolean isPlayerTurn() {
        return turn == Game.PlayerState.ALLIANCE;
    }

    public ArrayList<Cell> getBoard() {
        return board;
    }

    public int getSelectedCell() {
        return selectedCell;
    }

    public void setBotMoving(boolean canEnemyMove) {
        this.canEnemyMove = canEnemyMove;
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    public void clearBoard() {
        for(Cell cell : board) {
            cell.resetFigure();
        }
    }
}
