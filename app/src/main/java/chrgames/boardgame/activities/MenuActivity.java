package chrgames.boardgame.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.MobileAds;

import chrgames.boardgame.R;

public class MenuActivity extends AppCompatActivity {

    public static final String TUTORIAL_USED = "attention_label_used";
    public static final String SHR_PREF_CODE = "settings_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        // Advertisement:
        // TODO: Change sample id
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713"); // SAMPLE

        ImageView labelAttention = findViewById(R.id.label_attention);

        final Context context = this;

        SharedPreferences pref = context.getSharedPreferences(MenuActivity.SHR_PREF_CODE, Context.MODE_PRIVATE);
        boolean tutorialWasUsed = pref.getBoolean(MenuActivity.TUTORIAL_USED, false);

        if (tutorialWasUsed) {
            labelAttention.setVisibility(View.INVISIBLE);
        } else {
            labelAttention.setVisibility(View.VISIBLE);
        }


        findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, BoardActivity.class));
                finish();
            }
        });

        findViewById(R.id.button_tutorial).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, TutorialActivity.class));
                finish();
            }
        });
    }
}
