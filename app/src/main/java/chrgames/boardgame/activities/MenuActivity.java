package chrgames.boardgame.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.MobileAds;

import chrgames.boardgame.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        // Advertisement:
        // TODO: Change sample id
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713"); // SAMPLE


        final Context context = this;


        findViewById(R.id.button_start).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, BoardActivity.class));
                finish();
            }
        });
    }
}
