package chrgames.boardgame.models.products;


import java.util.ArrayList;

import chrgames.boardgame.models.Game;

public abstract class Figure extends Product {

    protected int priority;

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

    public int getPriority() {
        return priority;
    }
}
