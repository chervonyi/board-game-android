package chrgames.boardgame.models.products;

import chrgames.boardgame.models.Game;

public abstract class Card extends Product {

    /**
     * Basic information about extended card and its cost.
     * @return string of this information
     */
    public abstract String getInformation();

    /**
     * Title for button 'TO USE'
     * @return string for submit button
     */
    public abstract String getSubmitQuestion();

    /**
     * Describes behavior (using) of extended card.
     * @param user id of player which used this card
     * @param game instance of current game
     * @return true if card was successful used; false if current card could not be used right now.
     */
    public abstract boolean use(Game.PlayerState user, Game game);
}
