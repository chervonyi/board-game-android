package chrgames.boardgame.models.products.cards;

import java.util.ArrayList;

import chrgames.boardgame.models.Cell;
import chrgames.boardgame.models.Game;
import chrgames.boardgame.models.products.Card;

public class Reversal extends Card {

    public Reversal() {
        cost = 20;

        level = Level.HARD;

        productView = "reversal";
    }

    @Override
    public String getInformation() {
        return null;
    }

    @Override
    public String getSubmitQuestion() {
        return null;
    }

    @Override
    public boolean use(Game.PlayerState user, Game game) {

        // All alliance's figures set as enemy's
        for (Cell alliance : game.getAllFiguresOf(Game.PlayerState.ALLIANCE)) {
            alliance.setOwner(Game.PlayerState.ENEMY);
        }

        // All enemy's figures set as alliance's
        for (Cell enemy : game.getAllFiguresOf(Game.PlayerState.ENEMY)) {
            enemy.setOwner(Game.PlayerState.ALLIANCE);
        }

        Cell tmp;
        ArrayList<Cell> board = game.getBoard();

        // Swap cells - [i] and [CELLS - i]
        for (int i = 0; i < Game.CELLS / 2; i++) {
            tmp = board.get(i);
            board.add(i, board.get(Game.CELLS - i));
            board.add(Game.CELLS - i, tmp);

        }

        // Set new id's for each cell
        for (int i = 0; i < Game.CELLS; i++) {
            board.get(i).setId(i);
        }

        return true;
    }

}
