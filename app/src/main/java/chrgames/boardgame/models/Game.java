package chrgames.boardgame.models;

import java.util.ArrayList;

import chrgames.boardgame.activities.BoardActivity;
import chrgames.boardgame.models.products.Card;
import chrgames.boardgame.models.products.Product;
import chrgames.boardgame.models.products.figures.Master;
import chrgames.boardgame.models.products.figures.Predator;
import chrgames.boardgame.models.products.figures.Soldier;
import chrgames.boardgame.models.products.figures.Source;
import chrgames.boardgame.models.products.figures.Stone;
import chrgames.boardgame.models.products.Figure;

import static chrgames.boardgame.models.products.Figure.*;

public class Game {

    public enum PlayerState {
        ENEMY,
        ALLIANCE
    }

    // General vars:
    private ArrayList<Cell> board;

    private BoardActivity activity;

    private Shop shop;

    /**
     * A special flag to keep track the game status.<br>
     * When it's true - game is running and player can go on.<br>
     * If it's false - game is over and player cannot do anything (e-g. moves figures, take cards etc).
     */
    private boolean isRunning;

    private PlayerState win;

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

    // Constants:
    public static final int ROWS = 10;
    public static final int COLUMNS = 5;
    public static final int CELLS = ROWS * COLUMNS;
    static final int BASE_SIZE = 3;

    // Bases:
    private Base enemyBase;
    private Base allianceBase;

    private Player alliance;

    // Vars to work with selection and moves:
    private int selectedCell;
    private int selectedProduct = -1;
    private int preparedProductToUse = -1;

    // Constructor:
    public Game(BoardActivity activity, int amount, int income) {

        // Set all required fields
        this.activity = activity;
        board = new ArrayList<>();
        bot = new Bot(amount + 10, income + 3);
        shop = new Shop(true);
        turn = getPlayerWithFirstTurn();
        isRunning = true;

        // Set Player's start economy
        alliance = new Player(amount, income);

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

    // -----------------------------------------------------
    //                    MAIN METHODS
    // -----------------------------------------------------
    /**
     * Calls after every click on cells.
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
                Figure boughtFigure = (Figure) buyProduct(selectedProduct);
                setFigureAt(boughtFigure, position, PlayerState.ALLIANCE);

                if (board.get(position).isEndingFigure()) {
                    setNewIncome(PlayerState.ALLIANCE, alliance.getIncome() + Source.INCOME_FOR_EACH_FIGURE);
                }

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

    /**
     * This methods calls after every click at any product in shop.
     *
     * @param position - id of product in shop (sequence number)
     * @return <b>true</b> if can buy selected product;
     * <b>false</b> if user has not enough money to purchase selected product.
     */
    public boolean selectProduct(int position) {

        if (canBuy(position)) {

            if (shop.isFigure(position)) {
                selectedProduct = position;

                highlightAllianceBase();
            } else {
                Card card = (Card) shop.getProductById(position);

                preparedProductToUse = position;

                activity.showConfirmDialog(card.getInformation(), card.getSubmitQuestion(), card.getProductView());
            }

            return true;
        }
        return false;
    }

    /**
     * Make move from one cell into another.
     * @param from - departure.
     * @param to - destination.
     */
    private void move(int from, int to) {
        Cell cellFrom = board.get(from);
        Cell cellTo = board.get(to);

        // Check if given Move is real. (If it's not occupied by alliance piece)
        if (!cellTo.isEmpty() && cellFrom.getOwner() != cellTo.getOwner()) {

            if (cellTo.isEndingFigure()) {
                isRunning = false;
                win = cellFrom.getOwner();
                // Get cellFrom.owner and make appropriate information about WIN or LOSE
            } else {
                if (cellFrom.getOwner() == PlayerState.ALLIANCE) {
                    alliance.reward(cellTo.getRewardForDestroy());
                } else {
                    bot.reward(cellTo.getRewardForDestroy());
                }
            }
        }

        // Update cells state
        cellTo.setFigure(cellFrom);
        cellFrom.resetFigure();

        endTurn();
    }

    /**
     * Execute buying of selected before necessary product. <br>
     *
     * Should be cast into necessary type of product (Figure or Card). <br>
     *
     * @param pos - sequence number in shop cart of the necessary product.
     * @return instance of necessary product.
     */
    public Product buyProduct(int pos) {
        Product boughtProduct = shop.buy(pos);
        alliance.setAmount(alliance.getAmount() - boughtProduct.getCost());
        selectedProduct = -1;
        return boughtProduct;
    }

    // -----------------------------------------------------
    //                   SUPPORTING METHODS
    // -----------------------------------------------------
    /**
     * Check if user has enough money to buy product with appropriate position number
     * @param position - sequence number
     * @return <b>true</b> if user has enough money and is able to buy selected product;
     * <b>false</b> if user has not enough money, so he cannot buy selected product.
     */
    public boolean canBuy(int position) {
        return shop.canBuy(position, alliance.getAmount());
    }

    /**
     * Set 'false' value at 'isHighlighted' flag for each cell
     */
    public void removeSelectionCells() {
        for (int i = 0; i < board.size(); i++) {
            board.get(i).setHighlighted(false);
        }
        selectedCell = -1;
        selectedProduct = -1;
    }

    /**
     * Update value of income for required player
     * @param player - id of required player (enemy or alliance)
     * @param newValue - new value of income
     */
    public void setNewIncome(PlayerState player, int newValue) {
        if (player == PlayerState.ALLIANCE) {
            alliance.setIncome(newValue);
        } else {
            bot.getBotAccount().setIncome(newValue);
        }
    }

    /**
     * Place figures at random positions in according to bases.
     * @param set - list of necessary figures
     * @param base - enemy's or alliance's base
     */
    private void locateSet(Kind[] set, Base base) {

        Figure figure;
        int randomFreePosition;

        for (Kind kind: set) {

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

            randomFreePosition = base.getRandomFreeCell(board);

            setFigureAt(figure, randomFreePosition, base.getOwner());
        }
    }

    /**
     * Set 'true' for 'isHighlighted' flag for each free cells on user's base
     */
    public void highlightAllianceBase() {
        setHighlightForSet(allianceBase.getFreeCells(board), true);
    }

    /**
     * Make some delay and then call appropriate method from Bot class to get its move.
     */
    private void makeBotMove() {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        Move move = bot.getMove(Game.this, board);
                        if (move.to != -1) {
                            move(move.from, move.to);
                        } else {
                            // Was bought a random product from shop
                            endTurn();
                        }
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
     * Change turn.<br>
     * If it's going to be a Enemy's turn, method calls 'makeBotMove' function
     * to execute enemy's move.
     * When it's changed on Alliance's turn, method calls some Activity's method to
     * update a board view according to changes that contributed Bot move.
     */
    private void endTurn() {

        if (turn == PlayerState.ENEMY) {
            bot.round();

            turn = PlayerState.ALLIANCE;

            // Update board view
            activity.updateBoardContent();
        } else {
            alliance.round();
            turn = PlayerState.ENEMY;

            makeBotMove();
        }
    }

    /**
     * This method calls on click of confirm button in ConfirmDialog.
     * Has the goal to finish the purchase of a new card.
     */
    public void confirmToUseCard() {
        Card card = (Card) buyProduct(preparedProductToUse);
        if (card.use(PlayerState.ALLIANCE, this)) {
            endTurn();
        }
    }

    /**
     * Call apropriate method in BoardActivity to show ad video.
     * @param amountOfReward - amount of money to reward user for watching ad video
     */
    public void showRewardedVideo(int amountOfReward) {
        activity.showRewardedVideo(amountOfReward);
    }

    /**
     * Update user's amount of money
     * @param amountOfReward - amount of money to reward user for watching ad video
     */
    public void rewardUser(int amountOfReward) {
        alliance.setAmount(alliance.getAmount() + amountOfReward);
    }

    // -----------------------------------------------------
    //                       GETTERS
    // -----------------------------------------------------

    /**
     * Check if user won
     * @return <b>true</b> if user won this battle;
     * <b>false</b> - bot won current battle.
     */
    public boolean isUserWin() {
        return win == PlayerState.ALLIANCE;
    }

    /**
     * Return all figures that occupied with certain player.
     * @param owner - id of player state
     * @return list of all figures with one owner.
     */
    public ArrayList<Cell> getAllFiguresOf(PlayerState owner) {
        ArrayList<Cell> vault = new ArrayList<>();

        for (int i = 0; i < board.size(); i++) {
            if (board.get(i).getOwner() == owner) {
                vault.add(board.get(i));
            }
        }

        return vault;
    }

    /**
     * Return a player state opposite to given state.
     * @param alliance - given player state
     * @return player state of enemy. <br>
     *     (if given player state is Alliance -> returns Enemy state;
     *     if given state is Enemy -> returns Alliance state)
     */
    public PlayerState getEnemyStateOf(PlayerState alliance) {
        if (alliance == PlayerState.ALLIANCE) {
            return PlayerState.ENEMY;
        }
        return PlayerState.ALLIANCE;
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
     * @return a list of products that are available to buy in any time.
     */
    public ArrayList<Product> getShop() {
        return shop.getProducts();
    }

    /**
     * @return sequence number (in shop) of selected product
     */
    public int getSelectedProduct() { return selectedProduct; }

    /**
     * @return current value of player's amount of money
     */
    public int getAmount() {
        return alliance.getAmount();
    }

    /**
     * @return current value of player's income
     */
    public int getIncome() {
        return alliance.getIncome();
    }

    /**
     * Return an instance of Base class according to given player state
     * @param player - state of necessary player (Alliance or Enemy)
     * @return required Base instance
     */
    public Base getBase(PlayerState player) {
        return player == PlayerState.ALLIANCE ? allianceBase : enemyBase;
    }

    /**
     * Return income of required player
     * @param playerState - id of player(alliance or enemy).
     * @return value of income.
     */
    public int getPlayerIncome(PlayerState playerState) {
        return playerState == PlayerState.ALLIANCE ? alliance.getIncome() : bot.getBotAccount().getIncome();
    }

    // -----------------------------------------------------
    //                       SETTERS
    // -----------------------------------------------------
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
     * Set new instance of Shop class to 'shop' field.
     * @param newSHop - new instance of Shop class
     */
    public void setShop(Shop newSHop) {
        this.shop = newSHop;
    }

    /**
     * Update value of 'isRunning' flag
     * @param isRunning - new value
     */
    public void setGameStatus(boolean isRunning) {
        this.isRunning = isRunning;
    }
}
