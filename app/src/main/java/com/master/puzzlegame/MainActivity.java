package com.master.puzzlegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.buttonPlay);
        btnStart.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(MainActivity.this, GameActivity.class));
    }
}

