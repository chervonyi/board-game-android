package chrgames.boardgame.models.products;


import java.util.ArrayList;

import chrgames.boardgame.models.Game;

public abstract class Figure extends Product {


    /**
     * Name of image with black (own) appropriate figure
     */
    protected  String blackFigureIcon;

    /**
     * Name of image with red (enemy's) appropriate figure
     */
    protected String redFigureIcon;

    /**
     * Shows if current figure is able to move.<br>
     * This state should be always checked before using <b>'getAvailableCellsToMoveFrom'</b> method.
     */
    protected boolean ableToMove;

    /**
     * Shows if current figure is able to fight.<br>
     */
    protected boolean ableToFight;

    /**
     * Explains scheme of moves some figure.
     * @param position - absolute position on board of exactly this figure
     * @return - all possible cells to move
     */
    public abstract ArrayList<Integer> getAvailableCellsToMoveFrom(int position);

    /**
     * Converts sequence number of cell to position in coordinate system.
     * @param position - absolute position on board (sequence number of cell).
     * @return array with two numbers - x and y that responsible for cell position in coordinate system.
     */
    public int[] getXY(int position) {
        if (isRealPosition(position)) { return new int[]{}; }

        int y = position / Game.COLUMNS;
        int x = position - Game.COLUMNS * y;
        return new int[]{x, y};
    }

    /**
     * Converts cell position in coordinate system to sequence number.
     * @param x - X coordinate.
     * @param y - Y coordinate.
     * @return absolute position on board (sequence number of cell).
     */
    public int getPosition(int x, int y) {
        if (!isExist(x, y)) { return -1; }

        return y * Game.COLUMNS + x;
    }

    /**
     * Checks if transmitted coordinates of cell are real.
     * @param x - X coordinate.
     * @param y - Y coordinate.
     * @return true if cell position is real; false if position is unreal.
     */
    public boolean isExist(int x, int y) {
        return x >= 0 && y >= 0 && x <= Game.COLUMNS - 1 && y <= Game.ROWS - 1;
    }

    public boolean isRealPosition(int position) {
        return position < 0 || position >= Game.CELLS;
    }

    /**
     * @return amount of reward which depends on level of selected figure.
     */
    public int getReward() {
        switch (level) {
            case EASY:
                return 1;

            case NORMAL:
                return 2;

            case HARD:
                return 3;
        }
        return -1;
    }

    /**
     * @return name of image with appropriate black figure
     */
    public String getNameOfBlackFigure() {
        return blackFigureIcon;
    }

    /**
     * @return name of image with appropriate red figure
     */
    public String getNameOfRedFigure() {
        return redFigureIcon;
    }

    /**
     * This method should be always called before using <b>'getAvailableCellsToMoveFrom'</b> method.
     * If this method returns 'true', you could call 'getAvailableCellsToMoveFrom' either.
     * @return <b>true</b> if current figure is able to move;
     * <b>false</b> if figure is not able to move.
     */
    public boolean isMoving() { return ableToMove; }

    public boolean isFighting() { return ableToFight; }

    public int getCost() {
        return cost;
    }
}
