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
     * Sets of artificial times for bot thinking.
     */
    private final int[] delaySamples = new int[]{500, 1000, 1500, 2000, 2500, 3000};

    private Shop shop;

    private Player botAccount;

    Bot(int amount, int income) {

        botAccount = new Player(amount, income);

        shop = new Shop(false);
    }

    public Move getMove(Game game, ArrayList<Cell> board) {

        Random random = new Random();

        int hazard = random.nextInt(100);

        if (hazard > 40) {
            return getBestMove(board);
        } else if (hazard > 20) {
            return getRandomMove(board);
        } else if (buyRandomProduct(game)) {
            return new Move(-1, -1);
        }

        return getBestMove(board);
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
     * @param board - actual board (list of cells)
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

    public void reward(int value) {
        botAccount.reward(value);
    }

    public void round() {
        botAccount.round();
    }

    public Player getBotAccount() {
        return botAccount;
    }
}
