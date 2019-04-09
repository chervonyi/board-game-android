package chrgames.boardgame.activities;

import android.content.Context;
import android.content.Intent;
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

    // Constants
    private final int CELL_COUNT = 20;

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

        // Connect all UI views
        firstBlock = findViewById(R.id.firstBlock);
        secondBlock = findViewById(R.id.secondBlock);
        imageAccent = findViewById(R.id.imageAccent);
        buttonNext = findViewById(R.id.button_next);

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



        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextTutorial();
            }
        });

        // Load tutorial
        tutorialId = 0;
        loadNextTutorial();
    }


    public void onClickCell(View view) {

        String fullName = view.getResources().getResourceName(view.getId());
        String name = fullName.substring(fullName.lastIndexOf("/cell_") + 6);
        int cellId = Integer.parseInt(name);


        if (!game.isOver() && game.isPlayerTurn()) {
            game.selectCell(cellId);

            checkOnWin();

            updateBoardContent();
            updateBoardView();
        }
    }


    public void updateBoardContent() {

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

                //changeBottomPanel();
            }
        });
    }

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

    private void loadNextTutorial() {

        switch (tutorialId) {
            case 0:
                game.setFigureAt(new Source(), 6, Game.PlayerState.ENEMY);
                game.setFigureAt(new Source(), 18, Game.PlayerState.ALLIANCE);
                updateBoardContent();

                firstBlock.setText(R.string.tutorial_1_1);
                secondBlock.setText(R.string.tutorial_1_2);
                imageAccent.setImageResource(R.drawable.sorket_b);
                break;

            case 1:
                game.setBotMoving(false);
                game.setFigureAt(new Soldier(), 17, Game.PlayerState.ALLIANCE);
                updateBoardContent();

                firstBlock.setText(R.string.tutorial_2_1);
                secondBlock.setText(R.string.tutorial_2_2);
                imageAccent.setImageResource(R.drawable.soldier_b);
                buttonNext.setVisibility(View.INVISIBLE);
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
                buttonNext.setVisibility(View.VISIBLE);
                buttonNext.setText("play");
                break;

            case 4:
                startActivity(new Intent(this, BoardActivity.class));
                finish();
                break;
        }

        tutorialId++;
    }
}
