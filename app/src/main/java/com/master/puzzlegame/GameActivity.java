package com.master.puzzlegame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private int emptyX=3;
    private int emptyY=3;
    private ConstraintLayout groupView;
    private AppCompatButton[][] buttons;
    private int[] tiles;
    private char[] alphabetChar;
    private TextView totalStep;
    private int stepCount = 0;
    private Dialog popUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        popUp = new Dialog(this);

        loadViews();
        loadAlphabet();
        generateAlphabet();
        loadDataToViews();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);

        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.restart:
//                Toast.makeText(this, "Ulangi", Toast.LENGTH_SHORT).show();
                generateAlphabet();
                loadDataToViews();
                stepCount = 0;
                totalStep.setText(Integer.toString(stepCount));
                for (int i = 0; i < groupView.getChildCount(); i++) {
                    buttons[i/4][i%4].setClickable(true);
                }
                break;
            case R.id.logout:
//                Toast.makeText(this, "Keluar", Toast.LENGTH_SHORT).show();
                this.finishAffinity();

        }

        return super.onOptionsItemSelected(item);
    }

    private void loadDataToViews() {
        emptyX = 3;
        emptyY = 3;

        for (int i = 0; i < groupView.getChildCount() - 1; i++) {
            buttons[i/4][i%4].setText(String.valueOf(alphabetChar[i]));
            buttons[i/4][i%4].setBackgroundResource(R.drawable.ic_box);
        }

        buttons[emptyX][emptyY].setText("");
        buttons[emptyX][emptyY].setBackgroundColor(ContextCompat.getColor(this, R.color.freeButton));
    }

    private void generateAlphabet() {

        int n = 15;
        Random random = new Random();
        while (n > 1) {

            int randomChar = random.nextInt(n--);
            char temp = alphabetChar[randomChar];
//            Log.d("tes","random char " + alphabetChar[n]);
            alphabetChar[randomChar] = alphabetChar[n];
            alphabetChar[n] = temp;
        }

        if (!isSolvable())
            generateAlphabet();

    }

    private boolean isSolvable() {
        int countInversion = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < i; j++) {
                if (alphabetChar[j] > alphabetChar[i])
                    countInversion++;
            }
        }

        return countInversion % 2 == 0;
    }

    private void loadAlphabet() {
        alphabetChar = new char[16];
        char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O'};
        for (int i = 0; i < groupView.getChildCount() - 1; i++) {
            alphabetChar[i] = alphabet[i];
//            Log.d("tes", "tess " + alphabet[i]);
        }
    }

    private void loadViews() {
         groupView = findViewById(R.id.group);
         totalStep = findViewById(R.id.valueStep);
         buttons = new AppCompatButton[4][4];

         for (int i = 0; i < groupView.getChildCount(); i++) {
            buttons[i/4][i%4] = (AppCompatButton) groupView.getChildAt(i);
         }
    }

    @SuppressLint("SetTextI18n")
    public void buttonClick(View view) {
        AppCompatButton button = (AppCompatButton) view;

        int x = button.getTag().toString().charAt(0) - '0';
        int y = button.getTag().toString().charAt(1) - '0';

        if ((Math.abs(emptyX-x) == 1 && emptyY == y) || (Math.abs(emptyY-y) == 1 && emptyX == x)) {
            buttons[emptyX][emptyY].setText(button.getText().toString());
            buttons[emptyX][emptyY].setBackgroundResource(R.drawable.ic_box);
            button.setText("");
            button.setBackgroundColor(ContextCompat.getColor(this, R.color.freeButton));
            emptyX = x;
            emptyY = y;
            stepCount++;
            totalStep.setText(Integer.toString(stepCount));
            checkWin();
        }
    }

    public void showPopUp() {

        ImageView btnContinue;

        popUp.setContentView(R.layout.winner_popup);
        btnContinue = popUp.findViewById(R.id.buttonContinue);
        btnContinue.setOnClickListener(view -> popUp.dismiss());
        popUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popUp.show();
    }

    private void checkWin() {
        boolean isWin = false;
        char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O'};
        if (emptyX == 3 && emptyY == 3) {
            for (int i = 0; i < groupView.getChildCount() - 1; i++) {
                if (buttons[i/4][i%4].getText().toString().equals(String.valueOf(alphabet[i]))) {
                    isWin = true;
                } else {
                    isWin = false;
                    break;
                }
            }
        }
        if (isWin) {
//            Toast.makeText(this, "You Wint It", Toast.LENGTH_SHORT).show();
            showPopUp();
            for (int i = 0; i < groupView.getChildCount(); i++) {
                buttons[i/4][i%4].setClickable(false);
            }
        }
    }
}