package chrgames.boardgame.models.products.cards;

import java.util.ArrayList;

import chrgames.boardgame.models.Cell;
import chrgames.boardgame.models.Game;
import chrgames.boardgame.models.products.Card;

public class BlackDay extends Card {

    public BlackDay() {
        cost = 18;

        level = Level.HARD;

        productView = "black_day";
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

        for (Cell cell : board) {
            if (!cell.isEndingFigure()) {
                cell.resetFigure();
            }
        }

        return true;
    }
}
