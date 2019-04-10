package chrgames.boardgame.models;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private HashMap<Kind, Range> fullList = new HashMap<Kind, Range>() {
        {
            put(Kind.Soldier, new Range(0, 20)); // 20
            put(Kind.Master, new Range(21, 32)); // 11
            put(Kind.Predator, new Range(33, 39)); // 12
            put(Kind.Source, new Range(40, 59)); // 13
            put(Kind.Stone, new Range(60, 70)); // 10

            put(Kind.KillRandomEnemy, new Range(71, 86)); // 15
            put(Kind.Advertisement, new Range(87, 97)); // 10
            put(Kind.Businessman, new Range(98, 105)); // 7
            put(Kind.ControlRandomEnemy, new Range(106, 116)); // 10
            put(Kind.CreateRandom, new Range(117, 125)); // 7
            put(Kind.NewShop, new Range(126, 133)); // 7
            put(Kind.Reversal, new Range(134, 140)); // 6
        }
    };

    private HashMap<Kind, Range> shortList = new HashMap<Kind, Range>() {
        {
            put(Kind.Soldier, new Range(0, 20)); // 20
            put(Kind.Master, new Range(21, 32)); // 11
            put(Kind.Predator, new Range(33, 39)); // 12
            put(Kind.Source, new Range(40, 59)); // 13
            put(Kind.Stone, new Range(60, 70)); // 10

            put(Kind.KillRandomEnemy, new Range(71, 86)); // 15
            put(Kind.ControlRandomEnemy, new Range(87, 100)); // 10
            put(Kind.CreateRandom, new Range(101, 110)); // 7
        }
    };

    private final int MAX_RANGE_USER_SHOP = 140;
    private final int MAX_RANGE_BOT_SHOP = 110;

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
        Random random = new Random();
        HashMap<Kind, Range> currentHeap;


        int num;

        if (isUserShop) {
            currentHeap = fullList;
            num = random.nextInt(MAX_RANGE_USER_SHOP);
        } else {
            currentHeap = shortList;
            num = random.nextInt(MAX_RANGE_BOT_SHOP);
        }

        Kind kind = Kind.Stone;

        // Find required Kind with appropriate range
        for (Map.Entry<Kind, Range> entry : currentHeap.entrySet()) {
            if (entry.getValue().isInRange(num)) {
                kind = entry.getKey();
                break;
            }
        }

        // Return appropriate class of found kind
        switch (kind) {
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
            case CreateRandom: return new CreateRandomFigure();
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
