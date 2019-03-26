package chrgames.boardgame.models;


import java.util.ArrayList;
import java.util.Random;

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
            minRange = Game.CELLS - Game.COLUMNS * baseSize;
            maxRange = Game.CELLS - 1;
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

    /**
     * @return a random cell which belongs to current base.
     */
    public int getRandomYourCell() {
        Random random = new Random();
        return random.nextInt(maxRange - minRange + 1) + minRange;
    }

    /**
     * Returns a owner of current base.
     * @return state of owner.
     */
    public Game.PlayerState getOwner() {
        return this.owner;
    }
}
