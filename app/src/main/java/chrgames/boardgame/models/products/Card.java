package chrgames.boardgame.models.products;

import chrgames.boardgame.models.Game;

public class Card extends Product {

    public String getInformation() {
        return null;
    }

    public String getSubmitQuestion() {
        return null;
    }

    /**
     * r
     * @param user
     * @param game
     * @return true if card was successful used; false if current card could not be used right now.
     */
    public boolean use(Game.PlayerState user, Game game) { return false; }
}
