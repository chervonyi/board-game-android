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

        ArrayList<Cell> board = game.getBoard();

        Cell tmpCell;
        int reversePos;

        // Change position of figures
        for (int i = 0; i < Game.CELLS / 2; i++) {
            reversePos = Game.CELLS - 1 - i;

            tmpCell = new Cell(board.get(i));

            board.get(i).setFigure(board.get(reversePos));
            board.get(reversePos).setFigure(tmpCell);
        }

        ArrayList<Cell> allianceFigures = game.getAllFiguresOf(Game.PlayerState.ALLIANCE);
        ArrayList<Cell> enemyFigures = game.getAllFiguresOf(Game.PlayerState.ENEMY);

        for (Cell alliance : allianceFigures) {
            alliance.setOwner(Game.PlayerState.ENEMY);
        }

        // All enemy's figures set as alliance's
        for (Cell enemy : enemyFigures) {
            enemy.setOwner(Game.PlayerState.ALLIANCE);
        }

        return true;
    }

}
