package chrgames.boardgame;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import chrgames.boardgame.activities.BoardActivity;

public class ConfirmDialog extends Dialog {

    private BoardActivity activity;
    private Context context;

    private String mainText;
    private String mainImage;
    private String buttonText;

    public ConfirmDialog(BoardActivity activity, @NonNull Context context, String text, String img, String buttonText) {
        super(context);

        this.activity = activity;
        this.context = context;
        this.mainText = text;
        this.mainImage = img;
        this.buttonText = buttonText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Settings
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm);
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Find UI views
        TextView textViewDialog = findViewById(R.id.textViewDialog);
        ImageView closeImg = findViewById(R.id.closeDialog);
        ImageView mainImg = findViewById(R.id.imageDialog);
        Button buttonConfirm = findViewById(R.id.buttonConfirm);

        // Set content
        textViewDialog.setText(mainText);
        Log.d("CHR_GAMES_TEST", "img in Dialog: " + mainImage);
        int imageId = context.getResources().getIdentifier(mainImage,"drawable",
                context.getPackageName());
        mainImg.setImageResource(imageId);

        buttonConfirm.setText(buttonText);

        // Close Dialog action
        closeImg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Confirm action
        buttonConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                activity.onConfirmDialog();

                dismiss();
            }
        });
    }
}
