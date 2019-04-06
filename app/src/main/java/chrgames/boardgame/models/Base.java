package chrgames.boardgame.models;


import java.util.ArrayList;
import java.util.Random;

public class Base {

    /**
     * Instruction about owner of current base.
     * (Enemy or Alliance)
     */
    private Game.PlayerState owner;

    /**
     * Lower number in sequence of all cells
     */
    private int minRange;

    /**
     * Bigger number in sequence of all cells
     */
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
    public ArrayList<Integer> getCells() {

        ArrayList<Integer> vault = new ArrayList<>();

        for (int i = minRange; i <= maxRange; i++) {
            vault.add(i);
        }

        return vault;
    }

    /**
     * Composes a list of <b>free</b> cells that belong to current base.
     * @param board - list of cells
     * @return list of free cells (that are not occupied)
     */
    public ArrayList<Integer> getFreeCells(ArrayList<Cell> board) {

        ArrayList<Integer> vault = new ArrayList<>();

        for (int i = minRange; i <= maxRange; i++) {
            if (board.get(i).isEmpty()) {
                vault.add(i);
            }
        }

        return vault;
    }

    /**
     * Looks for a random cell which are not occupied at this time.
     * @return position of random, free cell which belongs to current base.
     */
    public int getRandomFreeCell(ArrayList<Cell> board) {
        Random random = new Random();

        ArrayList<Integer> freeCells = getFreeCells(board);

        int randPosition = random.nextInt(freeCells.size());

        return freeCells.get(randPosition);
    }



    /**
     * Returns a owner of current base.
     * @return state of owner.
     */
    public Game.PlayerState getOwner() {
        return this.owner;
    }
}
