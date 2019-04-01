package chrgames.boardgame.models.products.cards;

import chrgames.boardgame.models.Game;
import chrgames.boardgame.models.Shop;
import chrgames.boardgame.models.products.Card;

public class NewShop extends Card {

    public NewShop() {
        cost = 5;

        level = Level.NORMAL;

        productView = "new_shop";
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

        game.setShop(new Shop());

        return true;
    }
}
