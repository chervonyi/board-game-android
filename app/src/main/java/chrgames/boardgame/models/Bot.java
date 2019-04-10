package chrgames.boardgame.models;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import chrgames.boardgame.models.products.Card;
import chrgames.boardgame.models.products.Figure;
import chrgames.boardgame.models.products.Product;
import chrgames.boardgame.models.products.figures.Source;

public class Bot {

    /**
     * Sets of artificial times of bot thinking.
     */
    private final int[] delaySamples = new int[]{500, 1000, 1500, 2000, 2500, 3000};

    private Shop shop;

    /**
     * Instance of class Player contains main information about bot's income and amount
     */
    private Player botAccount;

    Bot(int amount, int income) {

        botAccount = new Player(amount, income);
        shop = new Shop(false);
    }

    /**
     * Creating a move according to random decision:
     *  - Make the best move;
     *  - Make a random move;
     *  - Buy some product from the shop.
     *
     * @param game instance of current game.
     * @return some instance of class Move.<br><br>
     * <b>Returned instance could be real and contains actual cells positions,
     * or it could contain -1 for two variables that means Bot bought some product from the shop.</b>
     */
    Move getMove(Game game) {

        int prediction = new Random().nextInt(100);

        if (prediction > 40) {
            // 60% change to make the best move
            return getBestMove(game.getBoard());
        } else if (prediction > 30) {
            // 10% change to make random move
            return getRandomMove(game.getBoard());
        } else if (buyRandomProduct(game)) {
            // 30% change to check if bot can make a random product.
            // If not could buy - do the best move
            return new Move(-1, -1);
        }

        return getBestMove(game.getBoard());
    }

    /**
     * Creating a random friendly move.<br>
     * <i>Friendly means that method will make move only on an empty cell.</i>
     * @param board a list of all cells which describes situation on the board.
     * @return some instance of class Move.<br><br>
     *      * <b>Returned instance could be real and contains actual cells positions,
     *      * or it could contain -1 for two variables that means Bot could not make move.</b>
     */
    Move getFriendlyMove(ArrayList<Cell> board) {
        ArrayList<Move> moves = new ArrayList<>();

        ArrayList<Integer> availableMovesForCell;

        for (int i = 0; i < board.size(); i++) {

            Cell cell = board.get(i);

            if (cell.getOwner() == Game.PlayerState.ENEMY) {

                availableMovesForCell = cell.getAvailableCellsToMove();

                for (Integer cellId : availableMovesForCell) {
                    if (board.get(cellId).isEmpty()) {
                        moves.add(new Move(i, cellId));
                    }
                }
            }
        }

        // Check if bot has any options for move
        if (moves.size() == 0) {
            return new Move(-1, -1);
        }

        return moves.get(new Random().nextInt(moves.size()));
    }

    private boolean buyRandomProduct(Game game) {

        ArrayList<Integer> availableProducts = new ArrayList<>();

        for (int i = 0; i < Shop.PRODUCT_COUNT; i++) {
            if (shop.canBuy(i, botAccount.getAmount())) {
                availableProducts.add(i);
            }
        }

        if (availableProducts.size() == 0) { return false; }

        int position = new Random().nextInt(availableProducts.size());

        if (Product.isFigure(shop.getProductById(position))) {
            Figure figure = (Figure) shop.buy(position);
            botAccount.setAmount(botAccount.getAmount() - figure.getCost());

            int randPos = game.getBase(Game.PlayerState.ENEMY).getRandomFreeCell(game.getBoard());

            game.getBoard().get(randPos).setFigure(figure, Game.PlayerState.ENEMY);

            if (game.getBoard().get(randPos).isEndingFigure()) {
                game.setNewIncome(Game.PlayerState.ENEMY, botAccount.getIncome() +
                        Source.INCOME_FOR_EACH_FIGURE);
            }

        } else {
            Card card = (Card) shop.buy(position);
            botAccount.setAmount(botAccount.getAmount() - card.getCost());
            card.use(Game.PlayerState.ENEMY, game);
        }
        return true;
    }

    private Move getRandomMove(ArrayList<Cell> board) {
        HashMap<Integer, Move> availableMoves = getMapOfAvailableMoves(board);

        if (availableMoves.size() == 0) { return new Move(-1, -1); }

        List<Integer> priorityList = new ArrayList<>(availableMoves.keySet());

        Random random = new Random();

        return availableMoves.get(priorityList.get(random.nextInt(priorityList.size())));
    }

    /**
     * Calculate priority for all available cells and then choice the best move.
     * @param board list of 50 cells.
     * @return instance of Move with the biggest priority to be moved.
     */
    private Move getBestMove(ArrayList<Cell> board) {
        HashMap<Integer, Move> availableMoves = getMapOfAvailableMoves(board);

        if (availableMoves.size() == 0) { return new Move(-1, -1); }

        List<Integer> priorityList = new ArrayList<>(availableMoves.keySet());

        int maxPriority = 0;

        for (Integer priority : priorityList) {

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
     * @param board actual board (list of cells)
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

    /**
     * According to given variables calculate priority for some cell.
     * @param board list of all 50 cells. Describes situation on the board.
     * @param cellFrom instance of Cell which can move at position of 'cellTo' parameter.
     * @param cellTo
     * @return
     */
    private int calculatePriority(ArrayList<Cell> board, Cell cellFrom, Cell cellTo) {
        int priority = 0;

        // Add priority for user's figure
        if (!cellTo.isEmpty()) {
            priority += cellTo.getFigure().getPriority();
        }

        // Add priority for bot's figure which is able to move on current cell.
        priority += cellFrom.getFigure().getPriority();


        int nearestFinalFigureLength = getLengthToFinalFigure(board, cellTo);

        if (nearestFinalFigureLength > 10) {
            priority += 5;
        } else {
            priority += 100 - nearestFinalFigureLength * 10;
        }

        return priority;
    }

    /**
     * Calculate a length from given cell to the nearest final piece (Sortek aka Source).
     * @param board a list of all cells. Describes situation on the board.
     * @param forCell an interested cell
     * @return the length from given cell to the nearest final piece.
     */
    private int getLengthToFinalFigure(ArrayList<Cell> board, Cell forCell) {
        ArrayList<Cell> finalFigures = new ArrayList<>();

        for (Cell cell:
             board) {
            if (cell.getOwner() == Game.PlayerState.ALLIANCE && cell.isEndingFigure()) {
                finalFigures.add(cell);
            }
        }

        int minLength = 0;
        int length;
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

    /**
     * Return length from one cell to another cell.
     * To calculate this length method uses two arrays with
     * that describes absolute position of every cell on the board (like X and Y).
     * @param a X and Y of cell #1
     * @param b X and Y of cell #2
     * @return the length between two cells.
     */
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

    void reward(int value) {
        botAccount.reward(value);
    }

    void round() {
        botAccount.round();
    }

    Player getBotAccount() {
        return botAccount;
    }
}
