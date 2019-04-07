package chrgames.boardgame;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import chrgames.boardgame.models.Game;

public class JSONAssistant {

    private final String FILE_NAME = "boardState.json";


    public void writeJSON(Game game, Context context) {


        Gson gson = new Gson();

        String json = gson.toJson(game);

        try {
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            if (json != null) {
                fos.write(json.getBytes());
            }

            //Log.d("CHR_GAMES_TEST", "Written successful");
        } catch (IOException e) {
            Log.e("CHR_GAMES_TEST", e.getMessage());
        }
    }

    public Game readJSON(Context context){

        Gson gson = new Gson();


        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            Log.d("CHR_GAMES_TEST", "Read successful");
        } catch (FileNotFoundException e) {
            Log.e("CHR_GAMES_TEST", e.getMessage());
        } catch (IOException e) {
            Log.e("CHR_GAMES_TEST", e.getMessage());
        }

        return gson.fromJson(sb.toString(), Game.class);
    }
}
