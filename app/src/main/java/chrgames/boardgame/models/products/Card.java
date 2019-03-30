package chrgames.boardgame.models.products;

import chrgames.boardgame.models.Game;

public abstract class Card extends Product {

    public abstract String getInformation();

    public abstract String getSubmitQuestion();

    /**
     * r
     * @param user
     * @param game
     * @return true if card was successful used; false if current card could not be used right now.
     */
    public abstract boolean use(Game.PlayerState user, Game game);
}
