package chrgames.boardgame.activities;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import chrgames.boardgame.ConfirmDialog;
import chrgames.boardgame.R;
import chrgames.boardgame.models.Cell;
import chrgames.boardgame.models.Game;
import chrgames.boardgame.models.Shop;
import chrgames.boardgame.models.products.Product;

public class BoardActivity extends AppCompatActivity {

    // UI
    private TextView incomeView;
    private TextView amountView;
    private ArrayList<ImageView> cells = new ArrayList<>();
    private LinearLayout shopLayout;
    private LinearLayout enemyTurnLayout;
    private ArrayList<ImageView> products = new ArrayList<>();
    private ArrayList<TextView> productsPrice = new ArrayList<>();

    // Constants
    private final int COUNT_OF_SELLS = 50;

    // Vars
    private Game game;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        incomeView = findViewById(R.id.textViewIncome);
        amountView = findViewById(R.id.textViewAmount);
        shopLayout = findViewById(R.id.layout_shop);
        enemyTurnLayout = findViewById(R.id.layout_enemy_turn);

        context = this;

        String pattern = "cell_";

        // Connect all views of each cell (50)
        for (int i = 0; i < COUNT_OF_SELLS; i++) {
            cells.add((ImageView) findViewById(getResources().getIdentifier(pattern + i,
                    "id", getPackageName())));
        }

        pattern = "shop_";

        // Connect all views of each product (3)
        for (int i = 0; i < Shop.PRODUCT_COUNT; i++) {
            products.add((ImageView)findViewById(getResources().getIdentifier(pattern + i,
                    "id", getPackageName())));
        }

        pattern = "price_";

        // Connect all viws of each price label (3)
        for (int i = 0; i < Shop.PRODUCT_COUNT; i++) {
            productsPrice.add((TextView)findViewById(getResources().getIdentifier(pattern + i,
                    "id", getPackageName())));
        }

        // Some magic to get actual size of height with value of MATCH_PARENT
        final ImageView sample = findViewById(R.id.cell_0);
        sample.post(new Runnable() {
            public void run() {
                int height = sample.getHeight();

                for (ImageView cell: cells) {
                    // Set width size as a height (Make squares to each of cell)
                    cell.getLayoutParams().width = height;
                    cell.requestLayout();
                }
            }
        });

        // Start a new game
        game = new Game(BoardActivity.this, 0, 2);

        updateBoardContent();
        updateShopContent();

        setAmount(game.getAmount());
        setIncome(game.getIncome());
    }

    /**
     * Listener for all cells.
     * Resend id of selected cell into appropriate game method to simulate a click. {@link Game#selectCell(int)}
     * @param view - selected cell
     */
    public void onClickCell(View view) {

        String fullName = view.getResources().getResourceName(view.getId());
        String name = fullName.substring(fullName.lastIndexOf("/cell_") + 6);
        int cellId = Integer.parseInt(name);

        Log.d("CHR_GAMES_TEST", "Pressed on: cell_" + cellId);

        if (!game.isOver() && game.isPlayerTurn()) {
            game.selectCell(cellId);

            // Update board and shop
            updateBoardContent();
            updateBoardView();
            updateShopView();
            updateShopContent();

            // Update labels
            setAmount(game.getAmount());
            setIncome(game.getIncome());

            if (game.isOver()) {
                // TODO: Go to the next activity
                Toast.makeText(this, "YOU WIN", Toast.LENGTH_LONG).show();
            }
        }

    }

    /**
     * Listener for all products.
     * Resend id of selected product into appropriate game method. {@link Game#selectCell(int)}
     * @param view
     */
    public void onClickProduct(View view) {
        // Reset selection of cell
        game.removeSelectionCells();

        String fullName = view.getResources().getResourceName(view.getId());
        String name = fullName.substring(fullName.lastIndexOf("/shop_") + 6);
        int position = Integer.parseInt(name);

        if (game.selectProduct(position)) {
            // Update board and shop
            updateBoardContent();
            updateBoardView();
            updateShopView();
            updateShopContent();

            // Update labels
            setAmount(game.getAmount());
            setIncome(game.getIncome());
        } else {
            // Not enough money to buy selected product
            Toast.makeText(context, "YOU CANNOT BUY", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Update view of board according to data from 'game'. <br>
     * Go through all cells and check if cell's view must be changed.
     */
    public void updateBoardContent() {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                ArrayList<Cell> board = game.getBoard();

                for (int i = 0; i < COUNT_OF_SELLS; i++) {

                    String imageName = board.get(i).getView();

                    if (imageName.equals("")) {
                        cells.get(i).setImageDrawable(null);
                    } else {
                        int imageId = context.getResources().getIdentifier(imageName,"drawable", context.getPackageName());
                        cells.get(i).setImageResource(imageId);
                    }
                }

                changeBottomPanel();
            }
        });
    }

    /**
     * Update views of each product according to data from 'game'. <br>
     */
    public void updateShopContent() {
        ArrayList<Product> shop = game.getShop();

        for (int i = 0; i < shop.size(); i++) {
            String imageName = shop.get(i).getProductView();

            int imageId = context.getResources().getIdentifier(imageName,"drawable", context.getPackageName());

            products.get(i).setImageResource(imageId);

            int cost = shop.get(i).getCost();

            if (cost > 0) {
                productsPrice.get(i).setText("$" + cost);
            } else {
                productsPrice.get(i).setText("");
            }
        }
    }

    /**
     * Removes any selection of any products in shop
     */
    private void updateShopView() {
        for (int i = 0; i < products.size(); i++) {

            if (game.getSelectedProduct() == i) {
                products.get(i).setBackground(ContextCompat.getDrawable(context, R.drawable.product_selected));
            } else {
                products.get(i).setBackground(null);
            }
        }
    }

    /**
     * Set appropriate view for each cell according to occupation of it.
     */
    private void updateBoardView() {
        ArrayList<Cell> board = game.getBoard();
        Cell cell;
        ImageView cellView;

        for (int i = 0; i < COUNT_OF_SELLS; i++) {
            cell = board.get(i);
            cellView = cells.get(i);

            if (cell.isHighlighted()) {
                if (cell.isEmpty()) {
                    // Highlight free to move cells (Green)
                    cellView.setBackground(ContextCompat.getDrawable(this,
                            R.drawable.cell_highlighted_green));

                } else if (cell.getOwner() == Game.PlayerState.ENEMY && board.get(game.getSelectedCell()).isAbleToFight()) {

                    // Highlight cells that occupied with enemy (Red)
                    cellView.setBackground(ContextCompat.getDrawable(this,
                            R.drawable.cell_highlighted_red));

                } else {
                    // Highlight cells that occupied with alliance (Usual)
                    cellView.setBackground(ContextCompat.getDrawable(this,
                            R.drawable.cell));
                }

            } else if (i == game.getSelectedCell()) {
                // Highlight selected cell
                cellView.setBackground(ContextCompat.getDrawable(this,
                        R.drawable.cell_highlighted_yellow));

            } else {
                // Remove highlight from another cells
                cellView.setBackground(ContextCompat.getDrawable(this,
                        R.drawable.cell));
            }
        }
    }

    /**
     * Replace two panel with same size on each other according to current turn.
     */
    private void changeBottomPanel() {
        if (game.isPlayerTurn()) {
            shopLayout.setVisibility(View.VISIBLE);
            enemyTurnLayout.setVisibility(View.GONE);
        } else {
            shopLayout.setVisibility(View.GONE);
            enemyTurnLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Update label of current amount of money
     * @param newAmount - new value of amount of money
     */
    public void setAmount(int newAmount) {
        amountView.setText("$" + newAmount);
    }

    /**
     * Update label of user income
     * @param newIncome - new value of income
     */
    public void setIncome(int newIncome) {
        incomeView.setText("+$" + newIncome);
    }

    /**
     * Call an appropriate method in 'game' to use prepared card <br>
     * and then update view of board.
     */
    public void onConfirmDialog() {
        game.confirmToUseCard();

        updateBoardContent();
        updateShopContent();

        // Update labels
        setAmount(game.getAmount());
        setIncome(game.getIncome());
    }

    /**
     * Show custom Confirm Dialog with basic information about selected card.
     * Dialog asks to use selected card. <br>
     * If confirm button will be pressed, this card will be used immediately.
     * @param text - main text which explains behaviour of current card
     * @param buttonText - text of confirm button
     * @param image - name of image of selected card
     */
    public void showConfirmDialog(String text, String buttonText, String image) {
        ConfirmDialog confirmDialog = new ConfirmDialog(BoardActivity.this, this, text, image, buttonText);
        confirmDialog.show();

    }
}
