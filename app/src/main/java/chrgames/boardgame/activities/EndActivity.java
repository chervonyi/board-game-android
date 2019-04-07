package chrgames.boardgame.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import chrgames.boardgame.R;
import chrgames.boardgame.activities.BoardActivity;

public class EndActivity extends AppCompatActivity {

    private final String WIN_STRING = "YOU WON!";
    private final String LOSE_STRING = "YOU LOSE!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        TextView mainText = findViewById(R.id.mainText);

        Intent intent = getIntent();

        String textToShow = intent.getBooleanExtra("user_win", false) ? WIN_STRING :
                LOSE_STRING;

        mainText.setText(textToShow);

        final Context context = this;

        findViewById(R.id.button_restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, BoardActivity.class));
                finish();
            }
        });
    }
}
