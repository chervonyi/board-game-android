package chrgames.boardgame.models.products;

public abstract class Product {

    protected String productView;

    /**
     * Figure price which will be shown in shop.
     */
    protected int cost;


    /**
     * Figure level affects the appearance time. This attribute answers on questions: <br>
     * "How quickly some figure will be available in a shop?" <br>
     * 'EASY' figures will be available immediately after start. <br>
     * 'NORMAL' figures will appear in the store later. <br>
     * And 'HARD' will be opened much more later. <br> <br>
     */
    protected enum Level {
        EASY, NORMAL, HARD
    }

    /**
     * Level of every extended figure explains importance on a board.
     */
    protected Level level;

    /**
     * @return name of image which uses to show product into shop panel.
     */
    public String getProductView() {
        return productView;
    }

    /**
     * @return assigned cost of figure.
     */
    protected int getCost() {
        return cost;
    }

}
