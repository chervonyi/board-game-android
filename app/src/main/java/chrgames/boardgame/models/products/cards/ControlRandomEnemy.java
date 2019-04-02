package chrgames.boardgame.models.products.cards;

import java.util.ArrayList;
import java.util.Collections;

import chrgames.boardgame.models.Cell;
import chrgames.boardgame.models.Game;
import chrgames.boardgame.models.products.Card;

public class ControlRandomEnemy extends Card {

    public ControlRandomEnemy() {
        cost = 8;

        level = Level.NORMAL;

        productView = "control";
    }

    @Override
    public String getInformation() {
        return "Take control of the random enemy's figure for $" + cost;
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

        for (int i = 0; i < enemyFigures.size(); i++) {
            if (!enemyFigures.get(i).isEndingFigure()) {
                enemyFigures.get(i).setOwner(user);
                return true;
            }
        }

        return true;
    }
}
