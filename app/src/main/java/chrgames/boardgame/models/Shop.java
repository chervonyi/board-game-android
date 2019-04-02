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

    public static final int PRODUCT_COUNT = 3;

    private HashMap<Kind, Integer> availableStuff = new HashMap<Kind, Integer>() {
        {
            put(Kind.Soldier, 50);
            put(Kind.Master, 20);
            put(Kind.Predator, 20);
            put(Kind.Source, 30);
            put(Kind.Stone, 50);

            put(Kind.KillRandomEnemy, 50);
            put(Kind.Advertisement, 50);
            put(Kind.BlackDay, 50);
            put(Kind.Businessman, 50);
            put(Kind.ControlRandomEnemy, 50);
            put(Kind.CreateRandomEnemy, 50);
            put(Kind.NewShop, 50);
            put(Kind.Reversal, 50);
        }
    };

    private ArrayList<Product> vault;


    public Shop() {
        vault = new ArrayList<>();

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

    public Product buy(int position) {
        Product tmp = vault.get(position);
        vault.set(position, nextProduct());
        return tmp;
    }

    private Product nextProduct() {
        ArrayList<Kind> allProducts = new ArrayList<>(availableStuff.keySet());

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

    public ArrayList<Product> getProducts() {
        return vault;
    }

    public boolean isFigure(int position) {
        return Product.isFigure(vault.get(position));
    }

    public Product getProductById(int position) {
        return vault.get(position);
    }
}
