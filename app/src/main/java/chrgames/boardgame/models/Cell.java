package chrgames.boardgame.models;

import java.util.ArrayList;

import chrgames.boardgame.models.products.figures.Source;
import chrgames.boardgame.models.products.Figure;

public class Cell {

    /**
     * ID of cell. E-g. - cell_0, cell_5, cell_49
     */
    private int id;

    /**
     * Contains instance of figure which has been placed at current cell.
     */
    private Figure occupation;

    /**
     * Explains which one of player is owner of current cell. (Enemy or Alliance)
     */
    private Game.PlayerState owner;

    /**
     * Shows if this cell checked and available to move.
     */
    private boolean highlighted;

    // Constructor
    Cell (int id) {
        this.id = id;
        highlighted = false;
    }

    // Constructor of copy
    public Cell (Cell newCell) {
       id = newCell.id;
       occupation = newCell.occupation;
       owner = newCell.owner;
       highlighted = false;
    }

    /**
     * Set some figure at this cell
     * @param figure - appropriate figure (like Stone, Soldier, Master etc).
     * @param playerId - owner of this figure (Alliance or Enemy)
     */
    public void setFigure(Figure figure, Game.PlayerState playerId) {
        occupation = figure;
        owner = playerId;
    }

    /**
     * Set figure from given cell to current one.
     * @param cell - given instance to copy properties
     */
    public void setFigure(Cell cell) {
        this.occupation = cell.occupation;
        this.owner = cell.owner;
    }

    /**
     * Remove figure from current cell if it has been set before.
     */
    public void resetFigure() {
        occupation = null;
        owner = null;
    }

    /**
     * Set new owner at current cell.
     * @param newOwner - state of necessary player
     */
    public void setOwner(Game.PlayerState newOwner) {
        this.owner = newOwner;
    }

    /**
     * Check if current cell is empty.
     * @return <b>true</b> if cell is empty; <b>false</b> if is not empty (occupied)
     */
    public boolean isEmpty() {
        return owner == null;
    }

    /**
     * Main method to get all available cells for this cell with the current figure at a present position.
     * @return list of sequence numbers of available cells to move
     */
    public ArrayList<Integer> getAvailableCellsToMove() {

        if (occupation != null) {
            return occupation.getAvailableCellsToMoveFrom(id);
        }

        return new ArrayList<>();
    }

    /**
     * @return name of image for according to present figure.
     */
    public String getView() {
        if (occupation != null) {
            if (owner == Game.PlayerState.ENEMY) {
                return occupation.getNameOfRedFigure();
            } else if (owner == Game.PlayerState.ALLIANCE) {
                return occupation.getNameOfBlackFigure();
            }
        }

        return "";
    }

    /**
     * Set special flag at given state (true or false).
     * @param highlighted - required state of this flag.
     */
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    /**
     * @return a current state of special flag which shows if
     * this cell is able to be moved right now.
     */
    public boolean isHighlighted() {
        return highlighted;
    }

    /**
     * @return state of current owner (Alliance or Enemy)
     */
    public Game.PlayerState getOwner() {
        return owner;
    }

    /**
     * Check if occupied figure is able to fight.
     * @return <b>true</b> if this figure can fight;
     * <b>false</b> if current figure is not able to fight.
     */
    public boolean isAbleToFight() {
        return occupation.isFighting();
    }

    /**
     * On death of figure of Source class game ends.<br>
     * So this method helps to keep track of the game status.
     * @return true if figure at current cell is instance of Source class; false if it is not.
     */
    public boolean isEndingFigure() {
        return occupation instanceof Source;
    }

    /**
     * @return amount of reward for destroy occupied figure.
     */
    public int getRewardForDestroy() {
        return occupation.getReward();
    }

    /**
     * @return instance of occupied figure.
     */
    public Figure getFigure() {
        return occupation;
    }

    public int getId() {
        return id;
    }

    /**
     * Converts sequence number of cell to position in coordinate system.
     * @param position - absolute position on board (sequence number of cell).
     * @return array with two numbers - x and y that responsible for cell position in coordinate system.
     */
    public static int[] getXY(int position) {
        if (isBadPosition(position)) { return new int[]{}; }

        int y = position / Game.COLUMNS;
        int x = position - Game.COLUMNS * y;
        return new int[]{x, y};
    }

    /**
     * Checks if transmitted coordinates of cell are real.
     * @param x - X coordinate.
     * @param y - Y coordinate.
     * @return true if cell position is real; false if position is unreal.
     */
    public static boolean isExist(int x, int y) {
        return x >= 0 && y >= 0 && x <= Game.COLUMNS - 1 && y <= Game.ROWS - 1;
    }

    /**
     * Check if given number is unreal position.
     * @param position sequence number of cell on the main board (with 50 cells).
     * @return
     *      <b><u>true</u></b> if given number is unreal (not in the range from 0 to 49). <br>
     *      <b><u>false</u></b> if it's number is real. (position is in [0, 49])
     */
    public static boolean isBadPosition(int position) {
        return position < 0 || position >= Game.CELLS;
    }

    /**
     * Converts cell position in coordinate system to sequence number.
     * @param x - X coordinate.
     * @param y - Y coordinate.
     * @return absolute position on board (sequence number of cell).
     */
    public static int getPosition(int x, int y) {
        if (!isExist(x, y)) { return -1; }

        return y * Game.COLUMNS + x;
    }

}
