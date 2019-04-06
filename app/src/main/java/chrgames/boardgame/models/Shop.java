package chrgames.boardgame.models;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import chrgames.boardgame.models.products.Product.Kind;
import chrgames.boardgame.models.products.Product;
import chrgames.boardgame.models.products.cards.Advertisement;
import chrgames.boardgame.models.products.cards.BlackDay;
import chrgames.boardgame.models.products.cards.Businessman;
import chrgames.boardgame.models.products.cards.ControlRandomEnemy;
import chrgames.boardgame.models.products.cards.CreateRandomFigure;
import chrgames.boardgame.models.products.cards.KillRandomEnemy;
import chrgames.boardgame.models.products.cards.NewShop;
import chrgames.boardgame.models.products.cards.Reversal;
import chrgames.boardgame.models.products.figures.Master;
import chrgames.boardgame.models.products.figures.Predator;
import chrgames.boardgame.models.products.figures.Soldier;
import chrgames.boardgame.models.products.figures.Source;
import chrgames.boardgame.models.products.figures.Stone;

public class Shop {

    /**
     * Count of spots for products
     */
    public static final int PRODUCT_COUNT = 3;

    /**
     * Vault of all product kinds that may be used to fill up a cart.
     */
    private HashMap<Kind, Integer> fullList = new HashMap<Kind, Integer>() {
        {
            put(Kind.Soldier, 50);
            put(Kind.Master, 20);
            put(Kind.Predator, 20);
            put(Kind.Source, 30);
            put(Kind.Stone, 50);

            put(Kind.KillRandomEnemy, 50);
            put(Kind.Advertisement, 50);
            put(Kind.Businessman, 50);
            put(Kind.ControlRandomEnemy, 50);
            put(Kind.CreateRandomEnemy, 50);
            put(Kind.NewShop, 50);
            put(Kind.Reversal, 50);
        }
    };

    private HashMap<Kind, Integer> shortList = new HashMap<Kind, Integer>() {
        {
            put(Kind.Soldier, 50);
            put(Kind.Master, 20);
            put(Kind.Predator, 20);
            put(Kind.Source, 30);
            put(Kind.Stone, 50);

            put(Kind.KillRandomEnemy, 50);
            put(Kind.ControlRandomEnemy, 50);
            put(Kind.CreateRandomEnemy, 50);
        }
    };

    private final boolean isUserShop;

    /**
     * Cart with PRODUCT_COUNT spots for product instance.
     */
    private ArrayList<Product> vault;

    // Constructor
    public Shop(boolean userShop) {
        isUserShop = userShop;

        vault = new ArrayList<>();

        // Fill up a vault with new products
        vault.add(nextProduct());
        vault.add(nextProduct());
        vault.add(nextProduct());
    }

    /**
     * Check if player could buy desirable product
     * @param positionOfProduct product position in shop
     * @param amountOfPlayer amount of player's money
     * @return true if player can buy this product; false if
     */
    public boolean canBuy(int positionOfProduct, int amountOfPlayer) {
        return amountOfPlayer >= vault.get(positionOfProduct).getCost();
    }

    /**
     * Make the purchase - return product from given position
     * and at that place paste a new random product.
     * @param position - required sequence number of product
     * @return bought instance of necessary product class.
     */
    public Product buy(int position) {
        Product tmp = vault.get(position);
        vault.set(position, nextProduct());
        return tmp;
    }

    /**
     * @return a instance of random class according to
     * prepared map with list of available products to sell.
     */
    private Product nextProduct() {
        ArrayList<Kind> allProducts;

        if (isUserShop) {
            allProducts = new ArrayList<>(fullList.keySet());
        } else {
            allProducts = new ArrayList<>(shortList.keySet());
        }

        Random random = new Random();
        switch (allProducts.get(random.nextInt(allProducts.size()))) {
            case Master: return new Master();
            case Predator: return new Predator();
            case Soldier: return new Soldier();
            case Source: return new Source();
            case Stone: return new Stone();

            case KillRandomEnemy: return new KillRandomEnemy();
            case Advertisement: return new Advertisement();
            case BlackDay: return new BlackDay();
            case Businessman: return new Businessman();
            case ControlRandomEnemy: return new ControlRandomEnemy();
            case CreateRandomEnemy: return new CreateRandomFigure();
            case NewShop: return new NewShop();
            case Reversal: return new Reversal();
        }

        return new Stone();
    }

    // Getters:

    /**
     * @return list of available product to buy.
     */
    public ArrayList<Product> getProducts() {
        return vault;
    }

    /**
     * Check if product at given position is figure (Stone, Soldier, Master...)
     * @param position - necessary sequence number
     * @return <b>true</b> if product at given position is a figure;
     * <b>false</b> if product at that position is not a figure.
     */
    public boolean isFigure(int position) {
        return Product.isFigure(vault.get(position));
    }

    /**
     * Return instance of product which locate at given position
     * @param position - sequence number
     * @return instance of Product class from given position.
     */
    public Product getProductById(int position) {
        return vault.get(position);
    }
}
