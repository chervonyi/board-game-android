package chrgames.boardgame.models.products.cards;

import java.util.ArrayList;

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
    public void use(Game game) {
        ArrayList<Cell> board = game.getBoard();

        
    }
}
