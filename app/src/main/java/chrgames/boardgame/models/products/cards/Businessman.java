package chrgames.boardgame.models.products.cards;

import chrgames.boardgame.models.Game;
import chrgames.boardgame.models.Player;
import chrgames.boardgame.models.products.Card;

public class Businessman extends Card {

    public Businessman() {
        cost = 3;

        level = Level.NORMAL;

        productView = "business";
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

        Player player = game.getPLayer(user);

        player.setIncome(player.getIncome() + 1);

        return true;
    }
}
