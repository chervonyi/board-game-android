package chrgames.boardgame;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class BoardActivity extends AppCompatActivity {

    // UI
    private TextView incomeView;
    private TextView amountView;
    private ArrayList<ImageView> cells = new ArrayList<>();

    // Constants
    private final int COUNT_OF_SELLS = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        incomeView = findViewById(R.id.textViewIncome);
        amountView = findViewById(R.id.textViewAmount);

        String pattern = "cell_";

        for (int i = 0; i < COUNT_OF_SELLS; i++) {
            cells.add((ImageView) findViewById(getResources().getIdentifier(pattern + i, "id", getPackageName())));
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
}
