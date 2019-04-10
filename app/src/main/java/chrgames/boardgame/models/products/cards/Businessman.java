package chrgames.boardgame.models.products.cards;

import chrgames.boardgame.models.Game;
import chrgames.boardgame.models.products.Card;

public class Businessman extends Card {

    public Businessman() {
        cost = 3;

        level = Level.NORMAL;

        productView = "business";
    }

    @Override
    public String getInformation() {
        return "Add +1$ for every subsequent move for $" + cost;
    }

    @Override
    public String getSubmitQuestion() {
        return "Use";
    }

    @Override
    public boolean use(Game.PlayerState user, Game game) {
        game.setNewIncome(user, game.getPlayerIncome(user) + 1);
        return true;
    }
}
