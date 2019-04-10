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
        return "Kill a random piece for $" + cost;
    }

    @Override
    public String getSubmitQuestion() {
        return "Use";
    }

    @Override
    public boolean use(Game.PlayerState user, Game game) {

        ArrayList<Cell> enemyFigures = game.getAllFiguresOf(game.getEnemyStateOf(user));

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
