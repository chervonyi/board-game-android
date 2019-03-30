package chrgames.boardgame.models.products.cards;

import java.util.ArrayList;
import java.util.Collections;

import chrgames.boardgame.models.Cell;
import chrgames.boardgame.models.Game;
import chrgames.boardgame.models.products.Card;

public class KillRandomEnemy extends Card {


    public KillRandomEnemy() {
        level = Level.EASY;

        cost = 5;

        productView = "random_kill";
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

        ArrayList<Cell> enemyFigures = new ArrayList<>();

        Game.PlayerState enemyState = user == Game.PlayerState.ALLIANCE
                ? Game.PlayerState.ENEMY
                : Game.PlayerState.ALLIANCE;

        for (Cell cell : board) {
            if (cell.getOwner() == enemyState) {
                enemyFigures.add(cell);
            }
        }

        if (enemyFigures.size() == 0) {
            return false;
        }

        Collections.shuffle(enemyFigures);

        for (Cell cell: enemyFigures) {
            if (!cell.isEndingFigure()) {
                cell.resetFigure();
                return true;
            }
        }

        return false;
    }
}
