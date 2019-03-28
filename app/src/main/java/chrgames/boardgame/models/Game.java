package chrgames.boardgame.models;

import java.util.ArrayList;

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

    private Shop shop;

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


    // Player's info
    private int amount;

    private int income;

    // Vars to work with selection and moves

    private int selectedCell;
    private int selectedProduct = -1;


    // Constructor
    public Game(BoardActivity activity) {

        // Set all required fields
        this.activity = activity;
        board = new ArrayList<>();
        bot = new Bot(Bot.Level.EASY);
        shop = new Shop();
        turn = getPlayerWithFirstTurn();
        isRunning = true;
        amount = 100;

        // Fill up a board with cells
        for (int i = 0; i < CELLS; i++) {
            board.add(new Cell(i));
        }

        // Connect bases
        enemyBase = new Base(BASE_SIZE, PlayerState.ENEMY);
        allianceBase = new Base(BASE_SIZE, PlayerState.ALLIANCE);

        // Locate a fixed set with figures on the board
        locateFirstFigures();
    }

    public boolean canBuy(int position) {
        boolean tmp = shop.canBuy(position, amount);

        if (tmp) {
            selectedProduct = position;
        } else {
            selectedProduct = -1;
        }

        return tmp;
    }

    public Figure buyFigure(int pos) {

        Figure boughtFigure = shop.buy(pos);
        amount -= boughtFigure.getCost();
        selectedProduct = -1;
        return boughtFigure;
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

            if (selectedProduct != -1 && cell.isEmpty()) {
                Figure boughtFigure = buyFigure(selectedProduct);
                setFigureAt(boughtFigure, position, PlayerState.ALLIANCE);

                selectedProduct = -1;
                removeSelectionCells();
                endTurn();
                return;
            }

            if (cell.isEmpty() ||
                    (cell.getOwner() == PlayerState.ENEMY
                    && board.get(selectedCell).isAbleToFight())) {
                move(selectedCell, position);

                removeSelectionCells();
                return;
            }
        }

        removeSelectionCells();

        // Make selection if was pressed on alliance figure
        if (cell.getOwner() == PlayerState.ALLIANCE) {
            ArrayList<Integer> availableCellsToMove = cell.getAvailableCellsToMove();

            if (availableCellsToMove.size() > 0) {
                setHighlightForSet(availableCellsToMove, true);
                selectedCell = position;
            }
        }
    }


    public void selectProduct(int position) {
        selectedProduct = position;

        highlightAllianceBase();
    }

    public void removeSelectionCells() {
        for (int i = 0; i < board.size(); i++) {
            board.get(i).setHighlighted(false);
        }
        selectedCell = -1;
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

    public void highlightAllianceBase() {
        setHighlightForSet(allianceBase.getFreeCells(board), true);
    }

    /**
     * Execute move created by Bot according to selected level. {@link Bot.Level}
     */
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

    public ArrayList<Figure> getShop() {
        return shop.getProducts();
    }

    public int getSelectedProduct() { return selectedProduct; }

    public void removeSelectionProduct() {
        selectedProduct = -1;
    }

    public boolean isHighlighted(int position) {
        return board.get(position).isHighlighted();
    }
}
