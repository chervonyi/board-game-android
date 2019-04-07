package chrgames.boardgame.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import chrgames.boardgame.JSONAssistant;
import chrgames.boardgame.R;
import chrgames.boardgame.models.Game;

public class MenuActivity extends AppCompatActivity {

    private boolean boardIsActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        boardIsActive = false;
        final Context context = this;


        JSONAssistant jsonAssistant = new JSONAssistant();
        Game game = jsonAssistant.readJSON(this);

        if (game != null) {
            boardIsActive = true;
        }


        findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoardActivity.class);
                intent.putExtra("board_is_active", boardIsActive);
                startActivity(intent);
                finish();
            }
        });
    }
}
