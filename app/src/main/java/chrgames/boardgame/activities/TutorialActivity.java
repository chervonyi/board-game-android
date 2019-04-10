package chrgames.boardgame.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import chrgames.boardgame.R;
import chrgames.boardgame.models.Cell;
import chrgames.boardgame.models.Game;
import chrgames.boardgame.models.TutorialGame;
import chrgames.boardgame.models.products.figures.Soldier;
import chrgames.boardgame.models.products.figures.Source;
import chrgames.boardgame.models.products.figures.Stone;

public class TutorialActivity extends AppCompatActivity {

    // UI
    private ArrayList<ImageView> cells;
    private TextView firstBlock;
    private TextView secondBlock;
    private ImageView imageAccent;
    private Button buttonNext;
    private TextView hintToContinue;

    // Constants
    private final int CELL_COUNT = 20;

    // Vars
    private TutorialGame game;
    private Context context;
    private int tutorialId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        context = this;
        cells = new ArrayList<>();
        game = new TutorialGame(TutorialActivity.this);

        // Connect main UI views
        firstBlock = findViewById(R.id.firstBlock);
        secondBlock = findViewById(R.id.secondBlock);
        imageAccent = findViewById(R.id.imageAccent);
        buttonNext = findViewById(R.id.button_next);
        hintToContinue = findViewById(R.id.hindToContinue);

        connectAllViews();

        // On click upload the next tutorial
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextTutorial();
            }
        });

        // Load the first tutorial
        tutorialId = 0;
        loadNextTutorial();
    }

    /**
     * Calls one-time from constructor to connect rest of views.
     * Also, this method set appropriate width for all cells
     * according to height with MATCH_PARENT value.<br>
     * It's required because all of cells must look like squares.
     */
    private void connectAllViews() {
        // Connect all views of each cell (50)
        String pattern = "cell_";

        for (int i = 0; i < CELL_COUNT; i++) {
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
    }

    /**
     * Listener for each cell.
     * Resend id of selected cell into appropriate game method to simulate a click.
     * @param view - instance of ImageView
     */
    public void onClickCell(View view) {

        String fullName = view.getResources().getResourceName(view.getId());
        String name = fullName.substring(fullName.lastIndexOf("/cell_") + 6);
        int cellId = Integer.parseInt(name);

        // If user should make move right now
        if (!game.isOver() && game.isPlayerTurn()) {
            game.selectCell(cellId);

            checkOnWin();

            updateBoardContent();
            updateBoardView();
        }
    }

    /**
     * Update view of board according to data from 'game'. <br>
     * Go through all cells and check if cell's view must be changed.
     */
    public void updateBoardContent() {

        // Some magic to update board content from instance of TutorialGame.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                ArrayList<Cell> board = game.getBoard();

                for (int i = 0; i < CELL_COUNT; i++) {

                    String imageName = board.get(i).getView();

                    if (imageName.equals("")) {
                        cells.get(i).setImageDrawable(null);
                    } else {
                        int imageId = context.getResources().getIdentifier(imageName,"drawable", context.getPackageName());
                        cells.get(i).setImageResource(imageId);
                    }
                }
            }
        });
    }

    /**
     * Set appropriate view for each cell according to occupation of it.
     */
    private void updateBoardView() {
        ArrayList<Cell> board = game.getBoard();
        Cell cell;
        ImageView cellView;

        for (int i = 0; i < CELL_COUNT; i++) {
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
     * Check if user destroyed enemy's figure and then load next tutorial
     */
    private void checkOnWin() {
        if (game.isOver()) {

            int currentTutorialId = tutorialId - 1;

            switch (currentTutorialId) {
                case 1:
                    loadNextTutorial();
                    break;

                case 2:
                    loadNextTutorial();
                    break;
            }
        }
    }

    /**
     * Update views of TutorialActivity according to number of tutorial.
     * Each tutorial has unique view and task.
     */
    private void loadNextTutorial() {

        switch (tutorialId) {
            case 0:
                game.setFigureAt(new Source(), 6, Game.PlayerState.ENEMY);
                game.setFigureAt(new Source(), 18, Game.PlayerState.ALLIANCE);
                updateBoardContent();

                firstBlock.setText(R.string.tutorial_1_1);
                secondBlock.setText(R.string.tutorial_1_2);
                imageAccent.setImageResource(R.drawable.sorket_b);

                buttonNext.setVisibility(View.VISIBLE);
                hintToContinue.setVisibility(View.GONE);
                break;

            case 1:
                game.setBotMoving(false);
                game.setFigureAt(new Soldier(), 17, Game.PlayerState.ALLIANCE);
                updateBoardContent();

                firstBlock.setText(R.string.tutorial_2_1);
                secondBlock.setText(R.string.tutorial_2_2);
                imageAccent.setImageResource(R.drawable.soldier_b);

                buttonNext.setVisibility(View.GONE);
                hintToContinue.setVisibility(View.VISIBLE);
                break;

            case 2:
                game.setRunning(true);
                game.clearBoard();
                game.setBotMoving(true);
                game.setFigureAt(new Source(), 19, Game.PlayerState.ALLIANCE);
                game.setFigureAt(new Soldier(), 14, Game.PlayerState.ALLIANCE);
                game.setFigureAt(new Soldier(), 18, Game.PlayerState.ALLIANCE);
                game.setFigureAt(new Stone(), 17, Game.PlayerState.ALLIANCE);

                game.setFigureAt(new Source(), 0, Game.PlayerState.ENEMY);
                game.setFigureAt(new Soldier(), 5, Game.PlayerState.ENEMY);
                game.setFigureAt(new Soldier(), 2, Game.PlayerState.ENEMY);

                updateBoardContent();

                firstBlock.setText(R.string.tutorial_3_1);
                secondBlock.setVisibility(View.GONE);
                imageAccent.setVisibility(View.GONE);
                break;

            case 3:
                firstBlock.setText(R.string.tutorial_4_1);
                secondBlock.setText(R.string.tutorial_4_2);
                secondBlock.setVisibility(View.VISIBLE);

                hintToContinue.setVisibility(View.GONE);
                buttonNext.setVisibility(View.VISIBLE);
                buttonNext.setText("play");
                break;

            case 4:
                SharedPreferences pref = context.getSharedPreferences(MenuActivity.SHR_PREF_CODE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.remove(MenuActivity.TUTORIAL_USED);
                editor.putBoolean(MenuActivity.TUTORIAL_USED, true);
                editor.apply();

                startActivity(new Intent(this, BoardActivity.class));
                finish();
                break;
        }

        tutorialId++;
    }
}
