package chrgames.boardgame.models;

public class Range {

    public final int from;
    public final int to;

    public Range(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public boolean isInRanbe(int num) {
        return num >= from && num <= to;
    }
}
