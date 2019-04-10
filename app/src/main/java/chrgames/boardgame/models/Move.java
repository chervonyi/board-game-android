package chrgames.boardgame.models;

public class Move {

    public final int from;
    public final int to;

    /**
     * Express instance of move.<br>
     * Contains two variables - from and to.<br>
     * Each of them means sequence number on main board (with 50 cells)
     * @param from - sequence number of cell which piece moving right now.
     * @param to - destination of moving piece.
     */
    Move(int from, int to) {
        this.from = from;
        this.to = to;
    }
}
