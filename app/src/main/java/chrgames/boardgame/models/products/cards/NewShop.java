package chrgames.boardgame.models.products.cards;

import chrgames.boardgame.models.Game;
import chrgames.boardgame.models.Shop;
import chrgames.boardgame.models.products.Card;

public class NewShop extends Card {

    public NewShop() {
        cost = 6;

        level = Level.NORMAL;

        productView = "new_shop";
    }
    @Override
    public String getInformation() {
        return "Refill the shop with new products for $" + cost;
    }

    @Override
    public String getSubmitQuestion() {
        return "Use";
    }

    @Override
    public boolean use(Game.PlayerState user, Game game) {

        game.setShop(new Shop(true));

        return true;
    }
}
