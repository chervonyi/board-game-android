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

import chrgames.boardgame.R;
import chrgames.boardgame.models.Cell;
import chrgames.boardgame.models.Game;

public class BoardActivity extends AppCompatActivity {

    // UI
    private TextView incomeView;
    private TextView amountView;
    private ArrayList<ImageView> cells = new ArrayList<>();
    private LinearLayout shopLayout;
    private LinearLayout enemyTurnLayout;

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

        for (int i = 0; i < COUNT_OF_SELLS; i++) {
            cells.add((ImageView) findViewById(getResources().getIdentifier(pattern + i,
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
        game = new Game(BoardActivity.this);

        locateFiguresOnBoard();
    }

    /**
     * Set view of board according to data from 'game'. <br>
     * Go through all cells and check if cell's view must be changed.
     */
    public void locateFiguresOnBoard() {

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

    private void changeBottomPanel() {
        if (game.isPlayerTurn()) {
            shopLayout.setVisibility(View.VISIBLE);
            enemyTurnLayout.setVisibility(View.GONE);
        } else {
            shopLayout.setVisibility(View.GONE);
            enemyTurnLayout.setVisibility(View.VISIBLE);
        }
    }


    public void onClickCell(View view) {
        String fullName = view.getResources().getResourceName(view.getId());
        String name = fullName.substring(fullName.lastIndexOf("/cell_") + 6);
        int cellId = Integer.parseInt(name);

        Log.d("CHR_GAMES_TEST", "Pressed on: cell_" + cellId);

        if (!game.isOver() && game.isPlayerTurn()) {
            game.selectCell(cellId);

            locateFiguresOnBoard();

            updateCellsView(cellId);

            if (game.isOver()) {
                // TODO: Go to the next activity
                Toast.makeText(this, "YOU WIN", Toast.LENGTH_LONG).show();
            }
        }


    }



    private void updateCellsView(int cellId) {
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
}
