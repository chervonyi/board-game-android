package chrgames.boardgame.models.products;

import chrgames.boardgame.models.Game;

public abstract class Card extends Product {


    public abstract String getInformation();

    public abstract String getSubmitQuestion();

    public abstract void use(Game game);
}
