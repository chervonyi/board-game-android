package chrgames.boardgame.models;


import java.util.ArrayList;

public class Base {

    private Game.PlayerState owner;

    private int minRange;

    private int maxRange;

    Base(int baseSize, Game.PlayerState owner) {
        this.owner = owner;

        if (owner == Game.PlayerState.ENEMY) {
            minRange = 0;
            maxRange = Game.COLUMNS * baseSize - 1;

        } else if (owner == Game.PlayerState.ALLIANCE) {
            minRange = Game.CELLS - Game.COLUMNS * baseSize + 1;
            maxRange = Game.CELLS;
        }
    }

    /**
     * Check if transmitted position belongs to current base.
     * @param position - necessary position.
     * @return <b>true</b> if position belongs to current base; <b>false</b> if does not.
     */
    public boolean doesBelong(int position) {
        return position >= minRange && position <= maxRange;
    }

    /**
     * Composes a list of cells that belong to current base.
     * @return a list of cells from 'minRange' to 'maxRange'
     */
    public ArrayList<Integer> getListOfCells() {

        ArrayList<Integer> vault = new ArrayList<>();

        for (int i = minRange; i <= maxRange; i++) {
            vault.add(i);
        }

        return vault;
    }
}
