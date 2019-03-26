package chrgames.boardgame.models;

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
    private boolean isAvailableToMove;


    Cell (int id) {
        this.id = id;
        isAvailableToMove = false;
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
     * Remove figure from current cell if it has been set before.
     */
    public void resetFigure() {
        occupation = null;
        owner = null;
    }

    /**
     * Check if current cell is empty.
     * @return <b>true</b> if cell is empty; <b>false</b> if is not empty (occupied)
     */
    public boolean isEmpty() {
        return owner == null;
    }

    public void getAvailableCellsToMove() {
        // occupation.getAvailableCellsToMoveFrom(id);
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
}
