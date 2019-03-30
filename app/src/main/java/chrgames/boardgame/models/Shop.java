package chrgames.boardgame.models;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import chrgames.boardgame.models.products.Figure;
import chrgames.boardgame.models.products.Figure.Kind;
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
        }
    };

    private ArrayList<Figure> vault;


    Shop() {
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
        Log.d("CHR_GAMES_TEST", "amountOfPlayer: " + amountOfPlayer);
        Log.d("CHR_GAMES_TEST", "getCost: " + vault.get(positionOfProduct).getCost());
        return amountOfPlayer >= vault.get(positionOfProduct).getCost();
    }

    public Figure buy(int position) {
        Figure tmp = vault.get(position);
        vault.set(position, nextProduct());
        return tmp;
    }

    private Figure nextProduct() {
        ArrayList<Kind> allProducts = new ArrayList<>(availableStuff.keySet());

        Random random = new Random();
        switch (allProducts.get(random.nextInt(allProducts.size()))) {
            case Master: return new Master();
            case Predator: return new Predator();
            case Soldier: return new Soldier();
            case Source: return new Source();
            case Stone: return new Stone();
        }

        return new Stone();
    }

    public ArrayList<Figure> getProducts() {
        return vault;
    }
}
