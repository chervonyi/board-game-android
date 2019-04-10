package chrgames.boardgame.models;

public class Range {

    public final int from;
    public final int to;

    /**
     * Instance of this class contains two variables and describes range like - [from, to].
     * @param from
     * @param to
     */
    Range(int from, int to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Check if given number is in the current range.
     * Like - [from...num...to]
     * @param num - given number to check.
     * @return true if given number is in the range;
     *         false if given number out of this range.
     */
    boolean isInRange(int num) {
        return num >= from && num <= to;
    }
}
