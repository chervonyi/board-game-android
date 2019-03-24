package chrgames.boardgame.models;

class Cell {

    /**
     * States of cell.
     *  - PLAYER_1: using by player #1
     *  - PLAYER_2: using by player #2
     *  - No one is using this cell
     */
    enum State {
        EMPTY,
        PLAYER_1,
        PLAYER_2
    }

    /**
     * ID of cell. E-g cell_0, cell_5, cell_49
     */
    private int id;

    /**
     * Shows if cell is used by somebody of players
     */
    private State state;

    /**
     * Shows if this cell checked and available to move.
     */
    private boolean isAvailableToMove;


    // private Figure occupation

    Cell (int id) {
        this.id = id;
        this.state = State.EMPTY;
        isAvailableToMove = false;
    }

    /*
    public void setFigure(Figure figure, State playerId) {
        occupation = figure;
        state = playerId;
    }
    */

    public void getAvailableCellsToMove() {
        // occupation.getAvailableCellsToMoveFrom(id);
    }
}
