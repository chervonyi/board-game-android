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


    Cell (int id) {
        this.id = id;
        highlighted = false;
    }

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

    public ArrayList<Integer> getAvailableCellsToMove() {

        if (occupation != null) {
            return occupation.getAvailableCellsToMoveFrom(id);
        }

        return new ArrayList<>();
    }

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

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public Game.PlayerState getOwner() {
        return owner;
    }


    public boolean isAbleToMove() { return occupation.isMoving(); }

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


    public int getRewardForDestroy() {
        return occupation.getReward();
    }

    public void setId(int newID) {
        id = newID;
    }

    public Figure getFigure() {
        return occupation;
    }
}
